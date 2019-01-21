package com.example.echo.balloonbuddy;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class BalloonMover {

    private static Animation balloonAnimation;

    // Deze functie wordt gebruikt om de ballon omhoog te laten gaan
    // Het heeft de foto van de ballon nodig, de context van de Game Activity en de huidige stand van de ballon
    public static void up(ImageView balloon, Context gameActivity, int state) {
        // De switch kijkt naar de huidige staat van de ballon en laat deze stijgen aan de hand daarvan
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

        // Start de animatie
        balloon.startAnimation(balloonAnimation);
    }

    // Deze functie wordt gebruikt om de ballon omlaag te laten gaan
    // Het heeft de foto van de ballon nodig, de context van de Game Activity en de huidige stand van de ballon
    public static void down(ImageView balloon, Context gameActivity, int state) {
        // De switch kijkt naar de huidige staat van de ballon en laat deze dalen aan de hand daarvan
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

        // Start de animatie
        balloon.startAnimation(balloonAnimation);
    }
}
