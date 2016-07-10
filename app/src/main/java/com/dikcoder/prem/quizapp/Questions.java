package com.dikcoder.prem.quizapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Stack;

public class Questions extends AppCompatActivity{

    private int correctAnswers = 0, incorrectAnswers = 0, questionCount = 0;
    private boolean isBookmarked = false;
    private String selectedOption, answer, field, difficulty;
    private Stack<QuestionBean> previousQuestion = new Stack<>();
    private Stack<String> previousAnswer = new Stack<>();
    private DatabaseHolder dbHandler;
    private ArrayList<QuestionBean> questionList;
    private QuestionBean questionBean;
    static String FIELD_ARG = "fieldSelection";
    static String DIFFICULTY_ARG = "difficultySelection";
    TextView question;
    ImageButton fabPrevious, fabSkip, fabNext;
    ToggleButton addBookmark;
    CheckedTextView option1, option2, option3, option4, temp;
    CheckedTextView[] allCheckedTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Difficulty.BACK_FROM_RESULTS = 0;

//        INSTANTIATE ALL THE VIEWS IN THIS ACTIVITY.
        instantiate();

//        GET THE SELECTED FIELD AND DIFFICULTY.
        Bundle b = getIntent().getExtras();
        String[] selections = {b.get(FIELD_ARG).toString(), b.get(DIFFICULTY_ARG).toString()};

//        SET UP ACTION BAR
        android.support.v7.app.ActionBar ab = this.getSupportActionBar();
        if (ab != null) ab.setSubtitle(selections[0] + " : " + selections[1]);
        assert ab != null;
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

//        GET THE QUESTIONS CORRESPONDING TO THE SELECTED FIELD AND DIFFICULTY
        questionList = b.getParcelableArrayList("Question");
        questionBean = questionList.get(questionCount);
        populate();
        answer = questionBean.getAnswer();

        allCheckedTextViews = new CheckedTextView[]{option1, option2, option3, option4};

//        ON CLICK LISTENERS FOR THE 4 OPTIONS (CheckedTextViews)
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

//        ON CLICK LISTENERS FOR NAVIGATION BUTTONS
        addBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkAction(addBookmark);
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOption != null){
                    if (selectedOption.equalsIgnoreCase(answer)){
                        correctAnswers++;
                    }
                    else incorrectAnswers++;

                    showNextQuestion();
                }
                else Toast.makeText(Questions.this, "Select an answer first.", Toast.LENGTH_SHORT).show();
            }
        });

        fabSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
                builder.setMessage("Are you sure to skip this question?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showNextQuestion();
                        getSelectedAnswer(selectedOption);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousQuestion.size()>0) {
                    questionBean = previousQuestion.pop();
                    populate();
                    answer = questionBean.getAnswer();
                    selectedOption = previousAnswer.pop();
                    getSelectedAnswer(selectedOption);
                    questionCount--;
                }
                else{
                    Toast.makeText(Questions.this, "This is the first Question", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        HELPER TOASTS IF USER LONG PRESSES ON THE NAVIGATION BUTTONS
        addBookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Questions.this, "Add / Remove bookmark", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        fabNext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Questions.this, "Next question", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        fabSkip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Questions.this, "Skip this question", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        fabPrevious.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Questions.this, "Previous question", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void getSelectedAnswer(String selectedOption){
        if (selectedOption != null) {
            if (selectedOption.equals(option1.getText().toString())) {
                clickAction(option1);
            } else if (selectedOption.equals(option2.getText().toString())) {
                clickAction(option2);
            } else if (selectedOption.equals(option3.getText().toString())) {
                clickAction(option3);
            } else if (selectedOption.equals(option4.getText().toString())) {
                clickAction(option4);
            }
        }
    }

    public void instantiate(){
        question = (TextView) findViewById(R.id.question_textView);
        option1 = (CheckedTextView) findViewById(R.id.checked_choice_button1);
        option2 = (CheckedTextView) findViewById(R.id.checked_choice_button2);
        option3 = (CheckedTextView) findViewById(R.id.checked_choice_button3);
        option4 = (CheckedTextView) findViewById(R.id.checked_choice_button4);
        fabPrevious = (ImageButton) findViewById(R.id.fabPrevious);
        fabNext = (ImageButton) findViewById(R.id.fabNext);
        fabSkip = (ImageButton) findViewById(R.id.fabSkip);
        addBookmark = (ToggleButton) findViewById(R.id.addBookmark);
    }

    public void populate(){
        question.setText(questionBean.getQuestion().trim());
        option1.setText(questionBean.getOption1().trim());
        option1.setChecked(false);
        option1.setTextColor(Color.parseColor("#000000"));
        option2.setText(questionBean.getOption2().trim());
        option2.setChecked(false);
        option2.setTextColor(Color.parseColor("#000000"));
        option3.setText(questionBean.getOption3().trim());
        option3.setChecked(false);
        option3.setTextColor(Color.parseColor("#000000"));
        option4.setText(questionBean.getOption4().trim());
        option4.setChecked(false);
        option4.setTextColor(Color.parseColor("#000000"));
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
                selectedOption = temp.getText().toString();
                temp.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_selected));
            } else {
                temp.setChecked(false);
                temp.setTextColor(Color.parseColor("#000000"));
                selectedOption = null;
                temp.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_deselected));
            }
        }
    }

    public void bookmarkAction(ToggleButton t) {
        if(t !=null) {
//            IF THE QUESTION IS BOOKMARKED
            if (!t.isChecked()) {
                isBookmarked = false;
                t.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_deselected));
                Toast.makeText(Questions.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }
//            IF THE QUESTION IS NOT BOOKMARKED
            else {
                isBookmarked = true;
                t.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_selected));
                Toast.makeText(Questions.this, "Bookmark added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showNextQuestion(){
        previousQuestion.push(questionBean);
        previousAnswer.push(selectedOption);
        questionCount++;

        if (questionCount < questionList.size()){
            isBookmarked = false;
            selectedOption = null;
            addBookmark.setChecked(false);
//                        fabPrevious.setEnabled(true);
            questionBean = questionList.get(questionCount);
            populate();
            answer = questionBean.getAnswer();
        }
        else{
            onCompletion();
        }
    }

//    ALERTS

    public void onCompletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
        builder.setMessage("You have completed the quiz");
        builder.setCancelable(true);
        builder.setPositiveButton("Results", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Questions.this, Results.class));
                Questions.this.finish();
            }
        });
        builder.setNegativeButton("Take another quiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Difficulty.BACK_FROM_RESULTS = 2;
                onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void changeDifficulty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
        builder.setTitle("Too hard?");
        builder.setMessage("Go back and change difficulty?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes please", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Difficulty.BACK_FROM_RESULTS = 1;
                onBackPressed();
            }
        });
        builder.setNegativeButton("I can take it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Difficulty.BACK_FROM_RESULTS = 0;
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void gotoHome(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
        builder.setTitle("Leaving already?");
        builder.setMessage("Sure to exit the current quiz?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yep", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Difficulty.BACK_FROM_RESULTS = 2;
                onBackPressed();
            }
        });
        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Difficulty.BACK_FROM_RESULTS = 0;
                dialog.cancel();
            }
        });
        builder.show();
    }

//    OPTIONS MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Difficulty.BACK_FROM_RESULTS = 3;
                onBackPressed();
                break;
            case R.id.action_change_difficulty:
                this.openContextMenu(question);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_bookmark:
                startActivity(new Intent(this, Bookmarks.class));
                break;
            case R.id.action_about:
                Dialog d = new Dialog(this);
                d.setContentView(R.layout.about);
                d.setTitle("About us");
                d.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch (Difficulty.BACK_FROM_RESULTS){
//            CALLED TO CHANGE THE DIFFICULTY (BACK BUTTON PRESSED)
            case 0:
                changeDifficulty();
                break;
//            CALLED IF "TAKE ANOTHER QUIZ" SELECTED FROM onCompletion() // OR CONFIRMED EXIT FROM gotoHome()
            case 2:
                super.onBackPressed();
                break;
//            CALLED ON ACTION BAR UP BUTTON PRESS
            case 3:
                gotoHome();
                break;
//            DEFAULT EXIT CALL, GENERALLY WHEN Difficulty.BACK_FROM_RESULTS == 1
            default:
                Difficulty.BACK_FROM_RESULTS = 0;
                super.onBackPressed();
                break;
        }
    }
}