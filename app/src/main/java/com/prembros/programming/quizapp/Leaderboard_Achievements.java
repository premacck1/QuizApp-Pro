package com.prembros.programming.quizapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Leaderboard_Achievements extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_ACTION = "param1";
    public static boolean isFragmentActive = false;
    public static View rootView;
    private ListView la_listView;
    CustomTextViewSemiLight mHeader;
    private String mAction;

    public Leaderboard_Achievements() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Leaderboard_Achievements.
     */
    public static Leaderboard_Achievements newInstance(String action) {
        Leaderboard_Achievements fragment = new Leaderboard_Achievements();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAction = getArguments().getString(ARG_ACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_leaderboard__achievements, container, false);
        la_listView = (ListView) rootView.findViewById(R.id.la_ListView);
        rootView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fragment_anim_in));
        isFragmentActive = true;
        mHeader = (CustomTextViewSemiLight) rootView.findViewById(R.id.la_header);
        if (mAction.equalsIgnoreCase("leaderboard")){
            mHeader.setText(R.string.leaderboard);
            listItemPopulate("iOS");
        }
        else if (mAction.equalsIgnoreCase("achievements")){
            mHeader.setText(R.string.achievements);
            listItemPopulate("Java");
        }
        return rootView;
    }

    public void listItemPopulate(String field){
        String s1 = field + ": Rookie",
                s2 = field + ": Apprentice",
                s3 = field + ": Pro",
                s4 = field + ": Hitman";
        String[] choices = {s1, s2, s3, s4};

        List<String> allFields = new ArrayList<>(Arrays.asList(choices));

        ArrayAdapter<String> fieldAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.checked_choice,
                R.id.checked_choice_button,
                allFields);

        la_listView.setAdapter(fieldAdapter);
    }
}
