package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class Questions extends AppCompatActivity {

    ListView optionList;
    static String ARGS = "questionArgs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        /*optionList = (ListView) this.findViewById(R.id.optionsList);

        String[] choices = {
                "Option 1",
                "Option 2",
                "Option 3",
                "Option 4"
        };

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), choices);
        optionList.setAdapter(customAdapter);*/
/*
        optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
*/

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Questions.this, MainActivity.class));
        this.finish();
        super.onBackPressed();
    }
}
