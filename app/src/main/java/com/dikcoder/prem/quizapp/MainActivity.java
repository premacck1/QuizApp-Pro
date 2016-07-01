package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements Field.OnFragmentInteractionListener, Difficulty.OnFragmentInteractionListener {

    ListView listView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            return;
        }

        Field mField = new Field();
        mField.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, mField).commit();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(View v, int pos) {
/*        Difficulty difficulty = (Difficulty) getSupportFragmentManager().findFragmentById(R.id.difficulty_fragment);
        */
        listView = (ListView) findViewById(android.R.id.list);
        listView.setLayoutAnimation(
                new LayoutAnimationController(
                        AnimationUtils.loadAnimation(this, R.anim.front_exit),
                        0.2F
                )
        );
        final int position = pos;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Difficulty mdifficulty = new Difficulty();
                Bundle args = new Bundle();
                args.putInt(Difficulty.ARG_POSITION, position);
                mdifficulty.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.main_fragment_container, mdifficulty);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }, 200);
    }

    @Override
    public void onFragmentInteraction(int selection, int pos) {
        if(selection == 123456 && pos == 123456){
            getSupportFragmentManager().popBackStack();
        }
        else {
            final int [] args = {selection, pos};

            listView = (ListView) findViewById(R.id.listView1);
            listView.setLayoutAnimation(
                    new LayoutAnimationController(
                            AnimationUtils.loadAnimation(this, R.anim.front_exit),
                            0.2F
                    )
            );
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(MainActivity.this, Questions.class)
                            .putExtra(Questions.ARGS, args));
                    MainActivity.this.finish();
                }
            }, 200);
        }
    }
    /*
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {

                super.onBackPressed();
            }
            else if(count==2){
                listView = (ListView) findViewById(R.id.listView1);
                listView.setLayoutAnimation(
                        new LayoutAnimationController(
                                AnimationUtils.loadAnimation(this, R.anim.back_exit),
                                0.2F
                        )
                );
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 200);
                listView = (ListView) findViewById(android.R.id.list);
                listView.setLayoutAnimation(
                        new LayoutAnimationController(
                                AnimationUtils.loadAnimation(this, R.anim.back_entrance),
                                0.2F
                        )
                );
            }
            else{
                super.onBackPressed();
            }
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/
}