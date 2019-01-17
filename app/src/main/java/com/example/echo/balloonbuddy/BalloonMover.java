package com.example.echo.balloonbuddy;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class BalloonMover {

    private static Animation balloonAnimation;

    public static void up(ImageView balloon, Context gameActivity, int state) {
        switch(state) {
            case -2:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.up_bottom_to_quarter);
                break;
            case -1:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.up_quarter_to_half);
                break;
            case 0:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.up_half_to_third);
                break;
            case 1:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.up_third_to_max);
                break;
        }

        balloon.startAnimation(balloonAnimation);
    }

    public static void down(ImageView balloon, Context gameActivity, int state) {
        switch(state) {
            case 2:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.down_max_to_third);
                break;
            case 1:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.down_third_to_half);
                break;
            case 0:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.down_half_to_quarter);
                break;
            case -1:
                balloonAnimation = AnimationUtils.loadAnimation(gameActivity, R.anim.down_quarter_to_bottom);
                break;
        }

        balloon.startAnimation(balloonAnimation);
    }
}
