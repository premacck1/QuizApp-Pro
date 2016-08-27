package com.prembros.programming.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field extends ListFragment{

    private ListView listView;
    private TextView introText;
    private OnFragmentInteractionListener mListener;
    private boolean mAlreadyLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_field, container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        listView.setVisibility(View.INVISIBLE);
        introText = (TextView) rootView.findViewById(R.id.intro_textView);

        // IF THE FRAGMENT IS STARTING FOR THE FIRST TIME
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            listItemPopulate();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.setLayoutAnimation(
                            new LayoutAnimationController(
                                    AnimationUtils.loadAnimation(getContext(), R.anim.fragment_anim_in),
                                    0.3F
                            )
                    );
                    listView.setVisibility(View.VISIBLE);
                    introText.setText(R.string.intro);
                    introText.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                }
            }, 250);
        }

        //IF THE FRAGMENT HAS ALREADY STARTED BEFORE AND USER IS JUST RETURNING BACK TO THE PAGE
        else{
            listItemPopulate();
            listView.setLayoutAnimation(
                    new LayoutAnimationController(
                            AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left),
                            0.1F
                    )
            );
            listView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    introText.setText(R.string.intro);
                    introText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right));
                }
            }, 200);
        }

        ImageView sign_in = (ImageView) rootView.findViewById(R.id.sign_in);
        if (sign_in != null)
            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        return rootView;
    }

    public void listItemPopulate(){
        String[] choices = {
                "iOS",
                "Java",
                "HTML",
                "JavaScript"
        };

        List<String> allFields = new ArrayList<>(Arrays.asList(choices));

        ArrayAdapter<String> field = new ArrayAdapter<>(
                getContext(),
                R.layout.choice,
                R.id.choice_button,
                allFields);

        listView.setAdapter(field);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onFragmentInteraction(v, position);
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
        void onFragmentInteraction(View v, int pos);
    }
}