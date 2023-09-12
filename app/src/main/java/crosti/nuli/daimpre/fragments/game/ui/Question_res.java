package crosti.nuli.daimpre.fragments.game.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import crosti.nuli.daimpre.R;
import crosti.nuli.daimpre.fragments.game.models.Answer;
import crosti.nuli.daimpre.fragments.game.models.Question;
import crosti.nuli.daimpre.fragments.game.models.Level;

import java.util.ArrayList;
import java.util.List;


public class Question_res extends Fragment {

    private Level level;
    private TextView message, res;
    private Button back;
    public Question_res(Level level) {
        this.level = level;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message = view.findViewById(R.id._t_message);
        res = view.findViewById(R.id._t_res);
        back = view.findViewById(R.id.b_back);
        List<Boolean> _res = new ArrayList<>();
        check:for(Question q : level.getQuestions()){
           for(Answer a : q.getQuestion_answers()){
               if(a.getChecked() && a.getRight()){
                   _res.add(true);
                   continue check;
               }
           }
            _res.add(false);
        }
        int right = 0;
        for(Boolean b : _res){
            if(b)right++;
        }
        String res_ = right+"/"+ _res.size();
        res.setText(res_);
        if(right==_res.size())
            message.setText("You win !");
        else message.setText("You lose !");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack("levels", 1);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_res, container, false);
    }
}