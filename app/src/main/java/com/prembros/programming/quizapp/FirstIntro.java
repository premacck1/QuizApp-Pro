package com.prembros.programming.quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

/*
 * Created by Prem $ on 8/29/2016.
 */
public class FirstIntro extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         add colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(rgb(77,182,172));
        colors.add(rgb(92,107,192));
        colors.add(rgb(7, 107, 112));
        colors.add(rgb(12, 152, 149));
        setAnimationColors(colors);

        setFadeAnimation();

//        Add slides
        addSlide(AppIntroFragment.newInstance("Hey There!", "Welcome to QuizApp,\nYou're about to start\nQuizApp - Programming",
                R.drawable.app_icon_big, rgb(255,255,255), Color.BLACK, Color.DKGRAY));

        addSlide(AppIntroFragment.newInstance("QuizApp", "QuizApp isn't just about the name." +
                "\n\nIt is Integrated with Google so that you can keep track of your high scores and compete with your friends",
                R.drawable.game_controller, rgb(92,107,192)));

        addSlide(AppIntroFragment.newInstance("Bookmarks", "You can have Bookmarks in QuizApp!." +
                        "\n\nYou can review them later in the Bookmarks section.",
                R.drawable.bookmarks_see, rgb(7, 107, 112)));

        addSlide(AppIntroFragment.newInstance("All caught up!", "So now that you're familiar with the features, let's get started!",
                R.drawable.ic_action_start_big, rgb(12, 152, 149)));
    }

    public void loadMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstIntro.this);
        builder.setMessage("Are you sure you want to skip the Introduction?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadMainActivity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        loadMainActivity();
    }

    /*
    * Custom Transformer (for animations when switching slides)
    */
//    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
//        private static final float MIN_SCALE = 0.85f;
//        private static final float MIN_ALPHA = 0.5f;
//
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//            int pageHeight = view.getHeight();
//
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                view.setAlpha(0);
//
//            } else if (position <= 1) { // [-1,1]
//                // Modify the default slide transition to shrink the page as well
//                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//                float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
//                float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
//                if (position < 0) {
//                    view.setTranslationX(horizontalMargin - verticalMargin / 2);
//                } else {
//                    view.setTranslationX(-horizontalMargin + verticalMargin / 2);
//                }
//
//                // Scale the page down (between MIN_SCALE and 1)
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);
//
//                // Fade the page relative to its size.
//                view.setAlpha(MIN_ALPHA +
//                        (scaleFactor - MIN_SCALE) /
//                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//
//            } else { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                view.setAlpha(0);
//            }
//        }
//    }
}
