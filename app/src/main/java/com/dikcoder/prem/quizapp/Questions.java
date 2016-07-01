package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

public class Questions extends AppCompatActivity{

    static String ARGS = "questionArgs";
    CheckedTextView option1, option2, option3, option4, temp;
    CheckedTextView[] allCheckedTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        option1 = (CheckedTextView) findViewById(R.id.checked_choice_button1);
        option2 = (CheckedTextView) findViewById(R.id.checked_choice_button2);
        option3 = (CheckedTextView) findViewById(R.id.checked_choice_button3);
        option4 = (CheckedTextView) findViewById(R.id.checked_choice_button4);

        allCheckedTextViews = new CheckedTextView[]{option1, option2, option3, option4};

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option3);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option4);
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

/*    void setCheckedState(View v, CheckedTextView[] whichCheckedTextViews){
        CheckedTextView
        //Uncheck all others
        for (CheckedTextView item : whichCheckedTextViews)
            item.setChecked(false);
        //Detect which checkedTextView initiated on click event
        switch(v.getId()){
            case R.id.checked_choice_button1:
            case R.id.checked_choice_button2:
            case R.id.checked_choice_button3:
            case R.id.checked_choice_button4:
                temp.setChecked(true);
                break;
            default:
                break;
        }
    }
    */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Questions.this, MainActivity.class));
        this.finish();
        super.onBackPressed();
    }

    public void clickAction(View v) {
        temp = (CheckedTextView) v;
        if(temp !=null) {
            if (!temp.isChecked()) {
                for (CheckedTextView item : allCheckedTextViews)
                    item.setChecked(false);
                temp.setChecked(true);
//                        temp.setBackgroundResource(R.drawable.checked_button_checked);
                Toast.makeText(Questions.this, "Checked", Toast.LENGTH_SHORT).show();
            } else {
                temp.setChecked(false);
//                        temp.setBackgroundResource(R.drawable.checked_button_default);
                Toast.makeText(Questions.this, "unChecked", Toast.LENGTH_SHORT).show();
            }
        }
    }
}