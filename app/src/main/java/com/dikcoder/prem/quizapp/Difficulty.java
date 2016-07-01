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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Difficulty.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Difficulty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Difficulty extends Fragment {

    private ListView listView;
    static String ARG_POSITION = "PositionArgs";
    static int fieldPosition;
    private OnFragmentInteractionListener mListener;
/*

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Difficulty() {
        // Required empty public constructor
    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Difficulty.
     *//*

    // TODO: Rename and change types and number of parameters
    public static Difficulty newInstance(String param1, String param2) {
        Difficulty fragment = new Difficulty();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
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

        Bundle args;
        args = this.getArguments();
        fieldPosition = (int) args.get(ARG_POSITION);

        listView = (ListView) rootView.findViewById(R.id.listView1);
        listView.setAdapter(difficulty);
        listView.setLayoutAnimation(
                new LayoutAnimationController(
                        AnimationUtils.loadAnimation(getContext(), R.anim.back_entrance),
                        0.2F
                )
        );
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

    @Override
    public void onPause() {
        listView.setLayoutAnimation(
                new LayoutAnimationController(
                        AnimationUtils.loadAnimation(getContext(), R.anim.front_exit),
                        0.2F
                )
        );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onFragmentInteraction(123456, 123456);
            }
        }, 200);
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int selection,int pos);
    }
}
