package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class FirstScreen extends AppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    TextView tv1, tv2;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        tv1 = (TextView) findViewById(R.id.textView2);
        tv2 = (TextView) findViewById(R.id.textView3);
        imageButton = (ImageButton) findViewById(R.id.imageButtonForward);

        tv1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguibl.ttf"));
        tv1.setShadowLayer(10, 4, 4, getResources().getColor(R.color.welcome_page_text));
        tv2.setShadowLayer(10, 4, 4, getResources().getColor(R.color.welcome_page_text));

        tv1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_in_from_above));
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.float_in_from_below);
        anim.setStartOffset(800);
        tv2.startAnimation(anim);

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageButton.startAnimation(AnimationUtils.loadAnimation(FirstScreen.this, R.anim.float_in_from_below));
            }
        }, SPLASH_DISPLAY_LENGTH);*/
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreen.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                // For calling GC
                FirstScreen.this.finish();
            }
        });
    }
}
