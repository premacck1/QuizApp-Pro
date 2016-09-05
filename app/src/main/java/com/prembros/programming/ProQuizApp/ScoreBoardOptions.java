package com.prembros.programming.ProQuizApp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

/*
 * Created by Prem $ on 9/5/2016.
 */
@SuppressWarnings("ConstantConditions")
public class ScoreBoardOptions extends Fragment{

    public static boolean isFragmentActive = false;
    public static View rootView;

    public ScoreBoardOptions(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.score_board_options, container, false);
        rootView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fragment_anim_in));

        isFragmentActive = true;

        CustomTextViewSemiLight option1 = (CustomTextViewSemiLight) rootView.findViewById(R.id.scoreBoard_iOS);
        CustomTextViewSemiLight option2 = (CustomTextViewSemiLight) rootView.findViewById(R.id.scoreBoard_Java);
        CustomTextViewSemiLight option3 = (CustomTextViewSemiLight) rootView.findViewById(R.id.scoreBoard_HTML);
        CustomTextViewSemiLight option4 = (CustomTextViewSemiLight) rootView.findViewById(R.id.scoreBoard_JavaScript);

        option1.setText(R.string.field_ios);
        option2.setText(R.string.field_java);
        option3.setText(R.string.field_html);
        option4.setText(R.string.field_javascript);

        final FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_anim_in, android.R.anim.fade_out,
                R.anim.slide_in_right, R.anim.slide_out_left);
        final ScoreBoard scoreBoard = new ScoreBoard();
        final String field = "field";
        final Bundle args = new Bundle();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                args.putString(field, "iOS");
                scoreBoard.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container, scoreBoard, "iOS");
                fragmentTransaction.addToBackStack("iOS_Scores");
                fragmentTransaction.commit();
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                args.putString(field, "Java");
                scoreBoard.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container, scoreBoard, "Java");
                fragmentTransaction.addToBackStack("Java_Scores");
                fragmentTransaction.commit();
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                args.putString(field, "HTML");
                scoreBoard.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container, scoreBoard, "HTML");
                fragmentTransaction.addToBackStack("HTML_Scores");
                fragmentTransaction.commit();
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                args.putString(field, "JavaScript");
                scoreBoard.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container, scoreBoard, "JavaScript");
                fragmentTransaction.addToBackStack("JavaScript_Scores");
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        isFragmentActive = false;
        super.onDestroyView();
    }
}
