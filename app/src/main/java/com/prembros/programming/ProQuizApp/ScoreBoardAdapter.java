package com.prembros.programming.ProQuizApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/*
 * Created by Prem $ on 9/5/2016.
 */
class ScoreBoardAdapter extends BaseAdapter {

    private Context _context;
    private List<String> scoreField;
    private List<String> scoreDifficulty;
    private List<Integer> scoreValue;

    ScoreBoardAdapter(Context context, List<String> field, List<String> difficulty, List<Integer> score) {
        this._context = context;
        scoreField = field;
        scoreDifficulty = difficulty;
        scoreValue = score;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getCount() {
        if (scoreField.size() == 0 || scoreDifficulty.size() == 0 || scoreValue.size() == 0)
            return 1;
        else return scoreField.size() & scoreDifficulty.size() & scoreValue.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        String headerText = null;
        String childText = null;
        int scoreText = 0;

        if (scoreField.size() > 0 || scoreDifficulty.size() > 0 || scoreValue.size() > 0) {
            headerText = scoreField.get(position);
            childText = scoreDifficulty.get(position);
            scoreText = scoreValue.get(position);
        }

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.score_board_list_item, null);
        }

        if (scoreField.size() == 0 && scoreDifficulty.size() == 0 && scoreValue.size() == 0){
            CustomTextViewSemiLight score_field = (CustomTextViewSemiLight) convertView.findViewById(R.id.score_field);
            if (score_field != null) {
                score_field.setText(R.string.score_empty_notification);
            }
        } else {
            CustomTextViewSemiLight score_field = (CustomTextViewSemiLight) convertView.findViewById(R.id.score_field);
            if (score_field != null) {
                score_field.setText(headerText);
            }

            CustomTextViewLight score_difficulty = (CustomTextViewLight) convertView.findViewById(R.id.score_difficulty);
            if (score_difficulty != null) {
                score_difficulty.setText(childText);
            }

            CustomTextViewSemiLight score_value = (CustomTextViewSemiLight) convertView.findViewById(R.id.score_value);
            if (score_value != null) {
                score_value.setText(String.valueOf(scoreText));
            }
        }
        return convertView;
    }
}
