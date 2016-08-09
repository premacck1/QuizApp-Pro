package com.prembros.programming.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class Results extends AppCompatActivity implements OnChartValueSelectedListener, ResultsInDetail.OnFragmentInteractionListener {

    private PieChart mChart;
    boolean doubleBackToExitPressedOnce = false;
    private DatabaseHolder dbHandler;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Difficulty.BACK_FROM_RESULTS = 2;
        dbHandler = new DatabaseHolder(getApplicationContext());

//        CREATE THE RESULT PIE CHART
        mChart = (PieChart) findViewById(R.id.resultPieChart);
        createPieChart();
        mChart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_in_from_above));
    }

    public void createPieChart(){
//        sets the description of the chart in the bottom right corner:
        mChart.setDescription("Total questions: " + Questions.QUESTION_COUNT);

        mChart.setExtraOffsets(2, 2, 2, 2);
        mChart.setDragDecelerationFrictionCoef(0.98f);

//        CENTER HOLE SETTINGS
        mChart.setTransparentCircleAlpha(0);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(20f);
        mChart.setHoleColor(Color.argb(0, 0, 0, 0));

        mChart.setRotationAngle(0);
//        enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

//        Legend l = mChart.getLegend();
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguil.ttf"));
        mChart.setEntryLabelTextSize(14f);

//        CODE FOR DISPLAYING CENTER TEXT(commented out):

//        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguisl.ttf"));
//        mChart.setCenterText(generateCenterSpannableText());

//        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleColor(Color.WHITE);

//        mChart.setHoleRadius(58f);
//        mChart.setTransparentCircleRadius(61f);

//        mChart.setDrawCenterText(true);
    }

    private void setData() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        int SKIPPED_ANSWERS = (Questions.QUESTION_COUNT - (Questions.CORRECT_ANSWERS + Questions.INCORRECT_ANSWERS));

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of the chart.
        if (Questions.CORRECT_ANSWERS != 0)
            entries.add(new PieEntry(Questions.CORRECT_ANSWERS, "Correct"));
        if (Questions.INCORRECT_ANSWERS != 0)
            entries.add(new PieEntry(Questions.INCORRECT_ANSWERS, "Incorrect"));
        if (SKIPPED_ANSWERS != 0)
            entries.add(new PieEntry(SKIPPED_ANSWERS, "Skipped"));

        PieDataSet dataSet = new PieDataSet(entries, "Test Results");
        dataSet.setSliceSpace(20f);
        dataSet.setSelectionShift(15f);

//         add colors
        ArrayList<Integer> colors = new ArrayList<>();
//         order: correct, incorrect, skipped
        colors.add(Color.argb(255, 156, 204, 101));        //argb(255, 139,195,74);
        colors.add(Color.argb(255, 239, 83, 80));       //argb(255, 0,188,212);
        colors.add(Color.argb(255, 18, 209, 205));     //argb(255, 244,67,54);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguisl.ttf"));
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    public void resetFlags() {
        Questions.QUESTION_COUNT = 0;
        Questions.CORRECT_ANSWERS = 0;
        Questions.INCORRECT_ANSWERS = 0;
        dbHandler.open();
        dbHandler.resetTables();
        dbHandler.close();
    }

/*

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Your result");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
*/

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED", "Value: " + e.getY()
                + ", index: " + h.getX()
                + ", DataSet index: " + h.getDataSetIndex());
        ResultsInDetail resultsInDetail = new ResultsInDetail();
        Bundle args = new Bundle();
        args.putInt(ResultsInDetail.ARG_ENTRY, (int) h.getX());
        resultsInDetail.setArguments(args);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //noinspection ConstantConditions
                getSupportActionBar().hide();
                mChart.highlightValues(null);
            }
        }, 200);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detailed_result_container, resultsInDetail, "resultsInDetail").commit();
        // undo all highlights
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    protected void onDestroy() {
        resetFlags();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_account:
                startActivity(new Intent(this, LoginActivity.class));
                break;
//            case android.R.id.home:
//                resetFlags();
//                this.finish();
//                break;
            case R.id.action_bookmark:
                startActivity(new Intent(this, Bookmarks.class));
                break;
            case R.id.action_about:
                if(About.isFragmentActive){
                    About.isFragmentActive = false;
                    getSupportFragmentManager().beginTransaction().remove(
                            getSupportFragmentManager().findFragmentByTag("about")).commit();
                }
                getSupportFragmentManager().beginTransaction().add(R.id.detailed_result_container, new About(), "about").commit();
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
                getSupportFragmentManager().beginTransaction().add(R.id.detailed_result_container, new Help(), "help").commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //noinspection ConstantConditions
                        getSupportActionBar().hide();
                    }
                }, 400);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Difficulty.BACK_FROM_RESULTS = 2;
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
            //noinspection ConstantConditions
            getSupportActionBar().show();
            About.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("about")).commit();
            return;
        }
        if(ResultsInDetail.isFragmentActive){
            onFragmentInteraction();
        }
        else {
            ResultsInDetail.isFragmentActive = false;
            if (doubleBackToExitPressedOnce) {
                resetFlags();
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Hit back again to goto home", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onFragmentInteraction() {
        if (ResultsInDetail.isFragmentActive) {
            ResultsInDetail.isFragmentActive = false;
            ResultsInDetail.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
            //noinspection ConstantConditions
            getSupportActionBar().show();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("resultsInDetail")).commit();
        }
        if (Help.isFragmentActive){
            Help.isFragmentActive = false;
            Help.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
            //noinspection ConstantConditions
            getSupportActionBar().show();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("help")).commit();
        }
    }
}
