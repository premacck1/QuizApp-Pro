package com.dikcoder.prem.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class FirstScreen extends AppCompatActivity{

    CustomTextViewBlack tv1;
//    ImageButton imageButton;
    ImageView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        tv1 = (CustomTextViewBlack) findViewById(R.id.textView2);
        tv1.setShadowLayer(10, 10, 10, Color.argb(140, 5, 77, 77));
//        imageButton = (ImageButton) findViewById(R.id.imageButtonForward);
        bg = (ImageView) findViewById(R.id.imageView_bg);

        int SPLASH_DISPLAY_LENGTH = 10000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                imageButton.startAnimation(AnimationUtils.loadAnimation(FirstScreen.this, R.anim.float_in_from_below));
                startActivity(new Intent(FirstScreen.this, MainActivity.class));
                FirstScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
