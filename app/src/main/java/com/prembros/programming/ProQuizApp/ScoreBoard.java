package com.prembros.programming.ProQuizApp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Prem $ on 9/5/2016.
 */
public class ScoreBoard extends Fragment {

    public static boolean isFragmentActive;
    public static View rootView;
    private String field;
    private ActionBar ab;

    public ScoreBoard() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = this.getArguments();
        field = (String) args.get("field");
        if (field == null){
            Toast.makeText(context, "Error! NULL Field", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }

        ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.hide();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.score_board, container, false);
        rootView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fragment_anim_in));
        isFragmentActive = true;

        ListView listView = (ListView) rootView.findViewById(R.id.scoreBoard_listView);
        List<String> scoreField = new ArrayList<>();
        List<String> scoreDifficulty = new ArrayList<>();
        List<Integer> scoreValue = new ArrayList<>();
        ScoreBoardAdapter scoreBoardAdapter = new ScoreBoardAdapter(getContext(), scoreField, scoreDifficulty, scoreValue);
        listView.setAdapter(scoreBoardAdapter);
        Cursor scoreCursor;
        int location;

        DatabaseHolder db = new DatabaseHolder(getContext());
        db.open();
        scoreCursor = db.getScore(field);
        scoreCursor.moveToFirst();
        location = 0;
        while (!scoreCursor.isAfterLast()){
            scoreField.add(location, scoreCursor.getString(scoreCursor.getColumnIndex("field")));
            scoreDifficulty.add(location, scoreCursor.getString(scoreCursor.getColumnIndex("difficulty")));
            scoreValue.add(location, scoreCursor.getInt(scoreCursor.getColumnIndex("score")));
            scoreCursor.moveToNext();
            location++;
        }
        db.close();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        ab.show();
        isFragmentActive = false;
        super.onDestroyView();
    }
}