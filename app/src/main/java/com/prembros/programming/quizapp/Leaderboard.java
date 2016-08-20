package com.prembros.programming.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard extends Fragment {

    public static boolean isFragmentActive = false;
    private OnFragmentInteractionListener mListener;
    public static View rootView;
    private GridView la_listView;

    public Leaderboard() {
        // Required empty public constructor
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Leaderboard.
     */
    public static Leaderboard newInstance() {
        return new Leaderboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        la_listView = (GridView) rootView.findViewById(R.id.la_ListView);
        rootView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fragment_anim_in));
        isFragmentActive = true;
        rootView.findViewById(R.id.leaderboard_list_item_container);

        listItemPopulate();

        la_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = la_listView.getPositionForView(view);
                mListener.onFragmentInteraction(position);
            }
        });
        return rootView;
    }

    public void listItemPopulate(){
        String[] fields = {"iOS", "iOS", "iOS", "iOS",
                "Java", "Java", "Java", "Java",
                "HTML", "HTML", "HTML", "HTML",
                "Javascript", "Javascript", "Javascript", "Javascript"};
        String[] difficulty = {"Rookie", "Apprentice", "Pro", "Hitman",
                "Rookie", "Apprentice", "Pro", "Hitman",
                "Rookie", "Apprentice", "Pro", "Hitman",
                "Rookie", "Apprentice", "Pro", "Hitman"};
//        Integer[] score = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 9, 8, 7, 6, 5};

        List<String> headerText = new ArrayList<>();
        List<String> childText = new ArrayList<>();
//        List<Integer> scoreText = new ArrayList<>();
        Collections.addAll(headerText, fields);
        Collections.addAll(childText, difficulty);
//        Collections.addAll(scoreText, score);

        ListAdapter listAdapter = new LeaderboardAdapter(getContext(), headerText, childText);

        la_listView.setAdapter(listAdapter);
        la_listView.setLayoutAnimation(new LayoutAnimationController(
                AnimationUtils.loadAnimation(getContext(), R.anim.fade_in), 0.1f));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int resultCode);
    }
}
