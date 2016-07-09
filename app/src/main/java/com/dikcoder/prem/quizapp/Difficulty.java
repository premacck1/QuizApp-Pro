package com.dikcoder.prem.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Difficulty extends Fragment {

    private ListView listView;
    private TextView difficultyText;
    static String ARG_POSITION = "PositionArgs";
    static int fieldPosition;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args;
            args = this.getArguments();
            fieldPosition = (int) args.get(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_difficulty, container, false);

        String [] choices = {
                "Rookie",
                "Apprentice",
                "Pro",
                "Hitman"
        };

        List<String> allFields = new ArrayList<>(Arrays.asList(choices));

        ArrayAdapter<String> difficulty = new ArrayAdapter<>(
                getContext(),
                R.layout.choice,
                R.id.choice_button,
                allFields);

        listView = (ListView) rootView.findViewById(R.id.listView1);
        difficultyText = (TextView) rootView.findViewById(R.id.difficulty_textView);
        listView.setAdapter(difficulty);
        listView.setLayoutAnimation(
                new LayoutAnimationController(
                        AnimationUtils.loadAnimation(getContext(), R.anim.back_entrance),
                        0.2F
                )
        );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                difficultyText.setText(R.string.intro_difficulty);
                difficultyText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.float_in_from_above));
            }
        }, 200);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onFragmentInteraction(fieldPosition, position);
            }
        });

        return rootView;
//        return inflater.inflate(R.layout.fragment_difficulty, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int selection,int pos);
    }
}