package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FirstScreen extends AppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 2200;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        tv1 = (TextView) findViewById(R.id.textView2);
        tv2 = (TextView) findViewById(R.id.textView3);

        tv1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguibl.ttf"));
        tv1.setShadowLayer(10, 4, 4, getResources().getColor(R.color.welcome_page_text));
        tv2.setShadowLayer(10, 4, 4, getResources().getColor(R.color.welcome_page_text));

        tv1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_in_from_above));
        tv2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_in_from_below));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirstScreen.this.startActivity(new Intent(FirstScreen.this, MainActivity.class));
                FirstScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    public boolean releaseInstance() {
        return super.releaseInstance();
    }
}
