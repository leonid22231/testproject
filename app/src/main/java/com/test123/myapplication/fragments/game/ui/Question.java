package com.test123.myapplication.fragments.game.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test123.myapplication.R;
import com.test123.myapplication.fragments.game.models.Answer;
import com.test123.myapplication.fragments.game.models.Level;
import com.test123.myapplication.fragments.game.utils.ListAdapter;
import com.test123.myapplication.fragments.game.utils.RadioButtonCenter;

import java.util.List;

public class Question extends Fragment {

    Level level;
    TextView _t_q, _t_t, _t_l;
    ImageView _i_i;
    Button _b_next;
    ListView _q_l;
    int position = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _t_q = view.findViewById(R.id._t_q);
        _t_t = view.findViewById(R.id._t_t);
        _b_next = view.findViewById(R.id._b_next);
        _i_i = view.findViewById(R.id._i_i);
        _t_l = view.findViewById(R.id._t_l);
        _q_l = view.findViewById(R.id._q_l);
        int size = level.getQuestions().size();
        update();

        _b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position!=size-1){
                    position++;
                    update();
                }else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null).add(R.id.fragmentView, new Question_res(level)).commit();
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }


    public Question(Level level){
        this.level = level;
    }

    public void update(){
        String t = _t_q.getText().toString().split(" ")[0] + " " + level.getQuestions().get(position).getId();
        String l = _t_l.getText().toString().split(" ")[0] + " " + (level.getId()+1);
        _t_l.setText(l);
        _t_q.setText(t);
        _t_t.setText(level.getQuestions().get(position).getQuestion_text());
        List<Answer> answers = level.getQuestions().get(position).getQuestion_answers();
        ListAdapter adapter = new ListAdapter(answers, getContext());
        _q_l.setAdapter(adapter);
        _q_l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButtonCenter b = view.findViewById(R.id._b_r);
                b.setChecked(!b.isChecked());
                answers.get(position).setChecked(true);
                for(int i = 0;i < answers.size(); i++){
                    if(i!=position){
                        answers.get(i).setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        String uri = "@drawable/"+level.getQuestions().get(position).getQuestion_image();
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        _i_i.setImageDrawable(res);
        Log.e("Q",""+level.getQuestions().get(position).getId());
    }
}