package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class Questions extends AppCompatActivity{

    static String ARGS = "questionArgs";
    static int selectedAnswerId;
    TextView question;
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

        ((TextView) findViewById(R.id.question_textView))
                .setText("This is a sample question: Ishq ka rang kya hai?");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_q_change_difficulty:
                this.openContextMenu(question);
                break;
            case R.id.action_q_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_q_bookmark:
                startActivity(new Intent(this, Bookmarks.class));

        }

        return super.onOptionsItemSelected(item);
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
//        startActivity(new Intent(Questions.this, MainActivity.class));
        this.finish();
        super.onBackPressed();
    }

    public void clickAction(View v) {
        temp = (CheckedTextView) v;
        if(temp !=null) {
            if (!temp.isChecked()) {
                for (CheckedTextView item : allCheckedTextViews) {
                    item.setChecked(false);
                    item.setTextColor(Color.parseColor("#000000"));
                }
                temp.setChecked(true);
                temp.setTextColor(Color.parseColor("#FFFFFF"));
                selectedAnswerId = temp.getId();
                temp.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_selected));
            } else {
                temp.setChecked(false);
                temp.setTextColor(Color.parseColor("#000000"));
                selectedAnswerId = 0;
                temp.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_deselected));
            }
            System.out.println("Selected Answer Id: " + selectedAnswerId);
        }
    }
}