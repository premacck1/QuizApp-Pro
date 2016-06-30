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
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {
    String[] names;
    Context context;
    LayoutInflater inflter;
    String value;

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
        view = inflter.inflate(R.layout.checked_choice, null);
        final CheckedTextView simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.checked_choice_button);
        simpleCheckedTextView.setText(names[position]);
        simpleCheckedTextView.setBackgroundResource(R.drawable.button_theme_selected);
// perform on Click Event Listener on CheckedTextView
        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleCheckedTextView.isChecked()) {
// set cheek mark drawable and set checked property to false
                    value = "un-Checked";
                    simpleCheckedTextView.setCheckMarkDrawable(0);
                    simpleCheckedTextView.setBackgroundResource(R.drawable.button_theme_selected);
                    simpleCheckedTextView.setChecked(false);
                } else {
// set cheek mark drawable and set checked property to true
                    value = "Checked";
                    simpleCheckedTextView.setBackgroundResource(R.drawable.button_theme_checked);
                    simpleCheckedTextView.setChecked(true);
                }
                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}