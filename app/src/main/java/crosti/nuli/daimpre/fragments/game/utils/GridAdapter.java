package crosti.nuli.daimpre.fragments.game.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import crosti.nuli.daimpre.R;
import crosti.nuli.daimpre.fragments.game.models.Level;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    List<Level> levels;
    LayoutInflater inflater;
    TextView textView;
    @Override
    public int getCount() {
        return levels.size();
    }

    @Override
    public Object getItem(int position) {
        return levels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.level, parent, false);
        }
        textView = convertView.findViewById(R.id.lvl_n);
        textView.setText(textView.getText().toString().split(" ")[0]+ " " + (levels.get(position).getId()+1));
        return convertView;
    }

    public GridAdapter(List<Level> levels, Context context){
        this.levels = levels;
        if(context!=null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

}
