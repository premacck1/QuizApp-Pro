package com.dikcoder.prem.quizapp;

/**
 * Created by Prem $ on 6/30/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class CustomAdapter extends BaseAdapter {
    String[] names;
    Context context;
    LayoutInflater inflter;
    String value;
    CheckedTextView simpleCheckedTextView;

    public CustomAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
        inflter = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        /*view = inflter.inflate(R.layout.checked, null);
        simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.checked_button1);
        simpleCheckedTextView.setText(names[position]);
        simpleCheckedTextView.setBackgroundResource(R.drawable.checked_button);
// perform on Click Event Listener on CheckedTextView
        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleCheckedTextView.isChecked()) {
// set cheek mark drawable and set checked property to false
                    value = "unChecked";
//                    simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checked_button_checked);
                    simpleCheckedTextView.setBackgroundResource(R.drawable.checked_button_default);
                    simpleCheckedTextView.setChecked(false);
                    simpleCheckedTextView.setTextColor(R.color.bg_text);
                } else {
// set cheek mark drawable and set checked property to true
                    value = "Checked";
                    simpleCheckedTextView.setChecked(true);
                    simpleCheckedTextView.setBackgroundResource(R.drawable.checked_button_checked);
                    simpleCheckedTextView.setTextColor(R.color.bg_text_checked);
                }
                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
    }
}