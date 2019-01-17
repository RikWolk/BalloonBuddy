package com.example.echo.balloonbuddy;

import android.widget.ProgressBar;

public class ProgressbarChanger {

    static int progress;

    public static void up(ProgressBar progressBar) {
        progress = progressBar.getProgress();
        if(progress < 99) {
            progress++;
            progressBar.setProgress(progress);
        }
    }

    public static void down(ProgressBar progressBar) {
        if(progress > 0) {
            progress--;
            progressBar.setProgress(progress);
        }
    }
}
