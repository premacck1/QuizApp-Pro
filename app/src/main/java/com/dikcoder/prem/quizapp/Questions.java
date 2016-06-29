package com.dikcoder.prem.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Questions extends AppCompatActivity {

    static String ARGS = "questionArgs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        /*
        ListView optionList = (ListView) this.findViewById(R.id.optionsList);

        */

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
    }

}
