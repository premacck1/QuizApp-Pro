package com.dikcoder.prem.quizapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
** Created by Prem $ on 7/20/2016.
*/
public class ResultsInDetail extends Fragment {

    public static String ARG_ENTRY = "entry number";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DatabaseHolder dbHandler;
    private int entry_number;
    private OnFragmentInteractionListener mListener;
    public static View rootView;
    public static boolean isFragmentActive;
    private CustomTextViewSemiLight headerText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            Bundle args;
            args = this.getArguments();
            entry_number = (int) args.get(ARG_ENTRY);
        }
        else entry_number = -1;

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.results_in_detail, container, false);
        isFragmentActive = true;

        if (entry_number != -1) {

            rootView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.result_anim_in));

            headerText = (CustomTextViewSemiLight) rootView.findViewById(R.id.detailedResultHeader);
            // get the listview
            expListView = (ExpandableListView) rootView.findViewById(R.id.detailedResultExpandableListView);

            // preparing list data
            prepareListData();

            ImageButton fragmentCloseButton = (ImageButton) rootView.findViewById(R.id.close_detailed_results);
            fragmentCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFragmentInteraction();
                }
            });
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Sorry, but the questions couldn't be loaded.");
            builder.setCancelable(false);
            builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resetFlags();
                    mListener.onFragmentInteraction();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return rootView;
    }
    /*
             * Preparing the list data
             */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        Cursor detailedResult;
        int location;

        dbHandler = new DatabaseHolder(getContext());
        dbHandler.open();
        switch(entry_number){
//            PIE SECTION OF CORRECT ANSWERS
            case 0: {
                setUpListAdapter();
                detailedResult = dbHandler.returnCorrectAnswers();
                detailedResult.moveToNext();
                location = 0;

                headerText.setText(R.string.your_correct_answers);

//            POPULATING THE expandableListView with the correct answers
                while (!detailedResult.isAfterLast()) {
                    // Adding header data
                    listDataHeader.add((location + 1) + ": " + detailedResult.getString(detailedResult.getColumnIndex("question")));

                    // Adding child data
                    List<String> q1 = new ArrayList<>();
                    q1.add("Answer: " + detailedResult.getString(detailedResult.getColumnIndex("answer")));

                    listDataChild.put(listDataHeader.get(location), q1); // Header, Child data
                    location++;
                    detailedResult.moveToNext();
                }
                dbHandler.close();
                break;
            }
//            PIE SECTION OF INCORRECT ANSWERS
            case 1: {
                setUpListAdapter();
                detailedResult = dbHandler.returnIncorrectAnswers();
                detailedResult.moveToNext();
                location = 0;

                headerText.setText(R.string.your_incorrect_answers);

//            POPULATING THE expandableListView with the incorrect answers
                while (!detailedResult.isAfterLast()) {
                    // Adding header data
                    listDataHeader.add((location + 1) + ": " + detailedResult.getString(detailedResult.getColumnIndex("question")));

                    // Adding child data
                    List<String> q1 = new ArrayList<>();
                    q1.add("Given Answer: " + detailedResult.getString(detailedResult.getColumnIndex("givenAnswer")));
                    q1.add("Correct Answer: " + detailedResult.getString(detailedResult.getColumnIndex("correctAnswer")));

                    listDataChild.put(listDataHeader.get(location), q1); // Header, Child data
                    location++;
                    detailedResult.moveToNext();
                }
                dbHandler.close();
                break;
            }
//            PIE SECTION OF SKIPPED ANSWERS
            case 2: {
                setUpListAdapter();
                detailedResult = dbHandler.returnSkippedAnswers();
                detailedResult.moveToNext();
                location = 0;

                headerText.setText(R.string.your_skipped_answers);

//            POPULATING THE expandableListView with the skipped answers
                while (!detailedResult.isAfterLast()) {
                    // Adding header data
                    listDataHeader.add((location + 1) + ": " + detailedResult.getString(detailedResult.getColumnIndex("question")));

                    // Adding child data
                    List<String> q1 = new ArrayList<>();
                    q1.add("Answer: " + detailedResult.getString(detailedResult.getColumnIndex("answer")));

                    listDataChild.put(listDataHeader.get(location), q1); // Header, Child data
                    location++;
                    detailedResult.moveToNext();
                }
                dbHandler.close();
                break;
            }
            default:
                dbHandler.close();
                break;
        }

        dbHandler.close();

/*        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }


    public void resetFlags() {
        Questions.QUESTION_COUNT = 0;
        Questions.CORRECT_ANSWERS = 0;
        Questions.INCORRECT_ANSWERS = 0;
        dbHandler.open();
        dbHandler.resetTables();
        dbHandler.close();
    }

    public void setUpListAdapter(){
        // setting list adapter
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
