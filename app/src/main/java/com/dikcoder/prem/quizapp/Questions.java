package com.dikcoder.prem.quizapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
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

        question = (TextView) findViewById(R.id.question_textView);
        question.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguisl.ttf"));
        question.setText("This is a sample question: Ishq ka rang kya hai?");
        question.setTextSize(20);
        registerForContextMenu(question);

        allCheckedTextViews = new CheckedTextView[]{option1, option2, option3, option4};

        option1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguil.ttf"));
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option1);
            }
        });

        option2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguil.ttf"));
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option2);
            }
        });

        option3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguil.ttf"));
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction(option3);
            }
        });

        option4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguil.ttf"));
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
            case R.id.action_change_difficulty:
                this.openContextMenu(question);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_difficulty, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
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