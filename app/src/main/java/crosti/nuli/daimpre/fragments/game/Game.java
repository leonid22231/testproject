package crosti.nuli.daimpre.fragments.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import crosti.nuli.daimpre.MainActivity;
import crosti.nuli.daimpre.R;
import crosti.nuli.daimpre.fragments.game.models.Level;
import crosti.nuli.daimpre.fragments.game.ui.Question;
import crosti.nuli.daimpre.fragments.game.utils.GridAdapter;

import java.util.List;

public class Game extends Fragment {
    private GridView levels;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        levels = view.findViewById(R.id.levels);
        List<Level> levelList = MainActivity.getLevels();
        GridAdapter adapter = new GridAdapter(levelList, getContext());
        levels.setAdapter(adapter);
        levels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack("levels").add(R.id.fragmentView,new Question(levelList.get(i))).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_game, container, false);
    }
}