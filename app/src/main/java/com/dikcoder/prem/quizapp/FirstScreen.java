package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FirstScreen extends AppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        findViewById(R.id.imageView).startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_in_from_above));
        findViewById(R.id.imageView2).startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_in_from_below));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirstScreen.this.startActivity(new Intent(FirstScreen.this, MainActivity.class));
                FirstScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }



}
