package com.prembros.programming.quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

    public static int CORRECT_ANSWERS = 0,
            INCORRECT_ANSWERS = 0,
            QUESTION_COUNT = 0;
    static String FIELD_ARG = "fieldSelection";
    static String DIFFICULTY_ARG = "difficultySelection";

    private String selectedOption, answer;
    private Stack<QuestionBean> previousQuestion = new Stack<>();
    private Stack<String> previousAnswer = new Stack<>();
    private DatabaseHolder dbHandler;
    private ArrayList<QuestionBean> questionList;
    private QuestionBean questionBean;
    private String[] selections;
    private TextView question;
    private ImageButton fabPrevious, fabSkip, fabNext;
    private ToggleButton addBookmark;
    private CheckedTextView option1;
    private CheckedTextView option2;
    private CheckedTextView option3;
    private CheckedTextView option4;
    private CheckedTextView[] allCheckedTextViews;

    public Questions() {
    }

    @Override
    protected void onRestart() {
        resumeBookmarkAction(addBookmark);
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        GET THE SELECTED FIELD AND DIFFICULTY.
        Bundle b = getIntent().getExtras();
        selections = new String[]{String.valueOf(b.get(FIELD_ARG)), String.valueOf(b.get(DIFFICULTY_ARG))};

//        GET THE QUESTIONS CORRESPONDING TO THE SELECTED FIELD AND DIFFICULTY
        questionList = b.getParcelableArrayList("Question");
        if (questionList != null) {
            setContentView(R.layout.activity_questions);

            Difficulty.BACK_FROM_RESULTS = 0;
            dbHandler = new DatabaseHolder(getApplicationContext());

//        INSTANTIATE ALL THE VIEWS IN THIS ACTIVITY.
            instantiate();

//        SET UP ACTION BAR
            android.support.v7.app.ActionBar ab = this.getSupportActionBar();
            if (ab != null) ab.setSubtitle(selections[0] + " : " + selections[1]);
            assert ab != null;
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);

            questionBean = questionList.get(QUESTION_COUNT);
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

                        dbHandler.open();
                        if (selectedOption.equalsIgnoreCase(answer)){
                            dbHandler.insertCorrectAnswer(question.getText().toString(), selectedOption);
                        }
                        else {
                            dbHandler.insertIncorrectAnswer(question.getText().toString(), selectedOption, answer);
                        }
                        dbHandler.close();

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

                            dbHandler.open();
                            dbHandler.insertSkippedAnswer(question.getText().toString(), answer);
                            dbHandler.close();

                            showNextQuestion();
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
                        String currentQuestion = question.getText().toString();

                        dbHandler.open();
                        int isQuestionPresentInAnswersTable = dbHandler.isQuestionPresentInAnswersTable(currentQuestion);
                        if (isQuestionPresentInAnswersTable < 0)
                            dbHandler.deleteQuestion(-1, currentQuestion);
                        else if (isQuestionPresentInAnswersTable > 0)
                            dbHandler.deleteQuestion(1, currentQuestion);
                        else dbHandler.deleteQuestion(0, currentQuestion);
                        dbHandler.close();
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
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
            builder.setMessage("Sorry, but the questions couldn't be loaded.");
            builder.setCancelable(false);
            builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Questions.this.finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void resetFlags() {
        QUESTION_COUNT = 0;
        CORRECT_ANSWERS = 0;
        INCORRECT_ANSWERS = 0;
        dbHandler.open();
        dbHandler.resetTables();
        dbHandler.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        addBookmark.setChecked(false);
        isBookmarked();
    }

    public void clickAction(View v) {
        CheckedTextView temp = (CheckedTextView) v;
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

    public boolean isBookmarked(){
        dbHandler.open();
        boolean areQuestionsEqual = false;
        Cursor questionToBeCompared = dbHandler.returnQuestion();
        questionToBeCompared.moveToNext();
        if (!questionToBeCompared.isAfterLast()) {
            while (!questionToBeCompared.isAfterLast()) {
                String questionInDb = questionToBeCompared.getString(questionToBeCompared.getColumnIndex("question"));
                String questionOnScreen = question.getText().toString();

//            REPLACE THE SINGLE QUOTES (THAT RESULT IN SQLiteException)
/*          if (questionInDb.contains("'"))
                questionInDb = questionInDb.replace("'", "\'");
            if(questionOnScreen.contains("'"))
                questionOnScreen = questionOnScreen.replace("'", "\'");
*/

//            CHECK IF THE QUESTION IS ALREADY BOOKMARKED
                areQuestionsEqual = questionOnScreen.equalsIgnoreCase(questionInDb);
                if (areQuestionsEqual) {
                    questionToBeCompared.moveToLast();
                    questionToBeCompared.moveToNext();
                }
                questionToBeCompared.moveToNext();
            }
        }
        if (areQuestionsEqual) {
            addBookmark.setChecked(true);
            dbHandler.close();
            return true;
        } else {
            addBookmark.setChecked(false);
            dbHandler.close();
            return false;
        }
    }

    public void resumeBookmarkAction(ToggleButton t) {
        if(isBookmarked()) {
            t.setChecked(true);
        }
        else t.setChecked(false);
    }

    public void bookmarkAction(ToggleButton t) {
        if(t !=null) {
//            IF THE QUESTION IS BOOKMARKED
            if (!t.isChecked()) {
                t.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_deselected));
                dbHandler.open();
                dbHandler.deleteData(question.getText().toString());
                dbHandler.close();
                Toast.makeText(Questions.this, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
            }
//            IF THE QUESTION IS NOT BOOKMARKED
            else {
                t.startAnimation(AnimationUtils.loadAnimation(Questions.this, R.anim.anim_selected));
                dbHandler.open();
                dbHandler.insertData(selections[0], selections[1],
                        question.getText().toString(),
                        option1.getText().toString(),
                        option2.getText().toString(),
                        option3.getText().toString(),
                        option4.getText().toString(),
                        answer);

                dbHandler.close();
                Toast.makeText(Questions.this, "Added to bookmarks", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showNextQuestion(){
        previousQuestion.push(questionBean);
        previousAnswer.push(selectedOption);
//        QUESTION_COUNT++;

        if (QUESTION_COUNT < questionList.size()){
            selectedOption = null;
            addBookmark.setChecked(false);
            questionBean = questionList.get(QUESTION_COUNT);
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
        builder.setCancelable(false);
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
                resetFlags();
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
                resetFlags();
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
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Difficulty.BACK_FROM_RESULTS = 2;
                resetFlags();
                onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
            case R.id.action_account:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case android.R.id.home:
                Difficulty.BACK_FROM_RESULTS = 3;
                onBackPressed();
                break;
            case R.id.action_bookmark:
                startActivity(new Intent(this, Bookmarks.class));
                break;
            case R.id.action_about:
                if(About.isFragmentActive){
                    About.isFragmentActive = false;
                    getSupportFragmentManager().beginTransaction().remove(
                            getSupportFragmentManager().findFragmentByTag("about")).commit();
                }
                getSupportFragmentManager().beginTransaction().add(R.id.help_container, new About(), "about").commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //noinspection ConstantConditions
                        getSupportActionBar().hide();
                    }
                }, 400);
                break;
            case R.id.action_help:
                if(Help.isFragmentActive){
                    Help.isFragmentActive = false;
                    getSupportFragmentManager().beginTransaction().remove(
                            getSupportFragmentManager().findFragmentByTag("help")).commit();
                }
                getSupportFragmentManager().beginTransaction().add(R.id.help_container, new Help(), "help").commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //noinspection ConstantConditions
                        getSupportActionBar().hide();
                    }
                }, 400);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Help.isFragmentActive){
            Help.isFragmentActive = false;
            Help.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
            //noinspection ConstantConditions
            getSupportActionBar().show();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("help")).commit();
            return;
        }
        if (About.isFragmentActive){
            About.isFragmentActive = false;
            About.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
            //noinspection ConstantConditions
            getSupportActionBar().show();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("about")).commit();
            return;
        }
        switch (Difficulty.BACK_FROM_RESULTS){
//            CALLED TO CONFIRM EXIT (BACK BUTTON PRESS)
            case 0:
                gotoHome();
                break;
//            CALLED IF "TAKE ANOTHER QUIZ" SELECTED FROM onCompletion() // OR CONFIRMED EXIT FROM gotoHome()
            case 2:
                super.onBackPressed();
                break;
//            CALLED ON ACTION BAR UP BUTTON PRESS (CHANGE THE DIFFICULTY)
            case 3:
                changeDifficulty();
                break;
//            DEFAULT EXIT CALL, GENERALLY WHEN Difficulty.BACK_FROM_RESULTS == 1
            default:
                Difficulty.BACK_FROM_RESULTS = 0;
                super.onBackPressed();
                this.finish();
                break;
        }
    }
}