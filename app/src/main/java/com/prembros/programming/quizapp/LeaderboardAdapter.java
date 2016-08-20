package com.prembros.programming.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/*
 * Created by Prem $ on 8/8/2016.
 */
public class LeaderboardAdapter extends BaseAdapter {

    private Context _context;
    private List<String> leaderboardHeader;
    private List<String> leaderboardChild;
//    private List<Integer> leaderboardScore;

    public LeaderboardAdapter(Context context, List<String> header, List<String> child) {
        this._context = context;
        leaderboardHeader = header;
        leaderboardChild = child;
//        leaderboardScore = score;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getCount() {
        return leaderboardHeader.size() & leaderboardChild.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView leaderboard_header;
//        TextView leaderboard_child;

        String headerText = leaderboardHeader.get(position) + ", " + leaderboardChild.get(position);
//        String childText = "Your high score: " + leaderboardScore.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.leaderboard_list_item, null);
        }

        leaderboard_header = (TextView) convertView.findViewById(R.id.leaderboard_header_textView);
        if (leaderboard_header != null) {
            leaderboard_header.setTypeface(MainActivity.fontTypefaceSemiLight);
            leaderboard_header.setText(headerText);
        }

//        leaderboard_child = (TextView) convertView.findViewById(R.id.leaderboard_child_textView);
//        if (leaderboard_child != null) {
//            leaderboard_child.setTypeface(MainActivity.fontTypefaceLight);
//            leaderboard_child.setText(childText);
//        }
        return convertView;
    }
}