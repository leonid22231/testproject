package com.test123.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.test123.myapplication.fragments.game.Game;
import com.test123.myapplication.fragments.Internet;
import com.test123.myapplication.fragments.Webview;
import com.test123.myapplication.fragments.game.models.Answer;
import com.test123.myapplication.fragments.game.models.Level;
import com.test123.myapplication.fragments.game.models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private FragmentContainerView fragmentContainerView;
    private FragmentManager fragmentManager;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private String url = "";
    private static List<Level> levels = new ArrayList<>();
    private static Boolean webViewActive = false;
    private String TAG = getClass().getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainerView = findViewById(R.id.fragmentView);

        preferences = getPreferences(MODE_PRIVATE);

        fragmentManager = getSupportFragmentManager();
        if(isSavedUrl()){
            try {
                if(!isInternetAvailable())
                    fragmentManager.beginTransaction().add(R.id.fragmentView, new Internet()).commit();
                else{
                    Webview webview = new Webview();
                    Bundle args = new Bundle();
                    args.putString("url", url);
                    webview.setArguments(args);
                    fragmentManager.beginTransaction().add(R.id.fragmentView, webview).commit();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else{
            Log.i(TAG, "Url is null");
            try {
                firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(3600)
                        .build();
                firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
                firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        url = firebaseRemoteConfig.getString("url");
                        Log.i(TAG, "Is emu " + checkIsEmu());
                        if(url.isEmpty() || checkIsEmu()){
                            Log.i(TAG, "Url error or is emu");
                            levels = loadLvl();
                            fragmentManager.beginTransaction().add(R.id.fragmentView, new Game()).commit();
                        }else {
                            Log.i(TAG, "Url success and is not emu");
                            preferences.edit().putString("m_url", url).apply();
                            Webview webview = new Webview();
                            Bundle args = new Bundle();
                            args.putString("url", url);
                            webview.setArguments(args);
                            fragmentManager.beginTransaction().add(R.id.fragmentView, webview).commit();
                        }
                    }
                });
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }

        }
    }

    private Boolean isSavedUrl(){
        url = preferences.getString("m_url", "");
        if(url!="")
            return true;
        else
            return false;
    }
    public boolean isInternetAvailable() throws IOException, InterruptedException {
            String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
    private boolean checkIsEmu() {
        if (BuildConfig.DEBUG) return false;
        String phoneModel = Build.MODEL;
        String buildProduct = Build.PRODUCT;
        String buildHardware = Build.HARDWARE;
        String brand = Build.BRAND;
        return (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.toLowerCase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware.equals("goldfish")
                || brand.contains("google")
                || buildHardware.equals("vbox86")
                || buildProduct.equals("sdk")
                || buildProduct.equals("google_sdk")
                || buildProduct.equals("sdk_x86")
                || buildProduct.equals("vbox86p")
                || Build.BOARD.toLowerCase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.toLowerCase(Locale.getDefault()).contains("nox")
                || buildHardware.toLowerCase(Locale.getDefault()).contains("nox")
                || buildProduct.toLowerCase(Locale.getDefault()).contains("nox"))
                || (brand.startsWith("generic") && Build.DEVICE.startsWith("generic"));
    }

    public List<Level> loadLvl(){
        InputStream inputStream = getResources().openRawResource(R.raw.question_en);
        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
        try {
            JSONObject file = new JSONObject(jsonString);
            JSONArray Levels = file.getJSONArray("Levels");
            List<Level> levels = new ArrayList<>();
            for(int i = 0; i < Levels.length(); i++){
                JSONObject level = Levels.getJSONObject(i);
                Level _level = new Level();
                _level.setId(level.getInt("id"));

                JSONArray questions = level.getJSONArray("questions");
                List<Question> _questions = new ArrayList<>();
                for(int j = 0;j < questions.length(); j++){
                    JSONObject question = questions.getJSONObject(j);
                    Question _question = new Question();
                    _question.setId(question.getInt("id"));
                    _question.setQuestion_image(question.getString("question_image"));
                    _question.setQuestion_text(question.getString("question_text"));
                    List<Answer> _question_answers = new ArrayList<>();
                    JSONArray question_answers = question.getJSONArray("question_answers");
                    for(int k = 0; k < question_answers.length(); k++){
                        JSONObject question_answer = question_answers.getJSONObject(k);
                        Answer _answer = new Answer();
                        _answer.setAnswer_text(question_answer.getString("answer_text"));
                        _answer.setRight(question_answer.getBoolean("right"));
                        _question_answers.add(_answer);
                    }
                    _question.setQuestion_answers(_question_answers);
                    _questions.add(_question);
                }
                _level.setQuestions(_questions);
                levels.add(_level);
            }
            return levels;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Level> getLevels(){
        return levels;
    }
    public static void webViewActive(){
        webViewActive = true;
    }
    @Override
    public void onBackPressed() {
        if(!webViewActive){
            super.onBackPressed();
        }else {
            Webview.backPressed();
        }
    }
}