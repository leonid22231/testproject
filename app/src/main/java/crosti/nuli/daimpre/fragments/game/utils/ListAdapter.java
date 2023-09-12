package crosti.nuli.daimpre.fragments.game.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import crosti.nuli.daimpre.R;
import crosti.nuli.daimpre.fragments.game.models.Answer;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    List<Answer> answers;
    LayoutInflater inflater;
    TextView _t_answer;
    RadioButtonCenter _b_r;
    @Override
    public int getCount() {
        return answers.size();
    }

    @Override
    public Object getItem(int position) {
        return answers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.answer, parent, false);
        }
        _b_r = convertView.findViewById(R.id._b_r);
        _t_answer = convertView.findViewById(R.id._t_answer);
        _t_answer.setText(answers.get(position).getAnswer_text());
        _b_r.setChecked(answers.get(position).getChecked());
        return convertView;
    }

    public ListAdapter(List<Answer> answers, Context context){
        this.answers = answers;
        if(context!=null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

}
