package com.example.echo.balloonbuddy;

import android.os.CountDownTimer;
import android.util.Log;

public class GameTimer extends CountDownTimer {
    private int timeRemaining;

    public GameTimer(int totalSeconds, int interval) {
        super(totalSeconds, interval);
    }

    @Override
    public void onFinish() {
        Log.d("TICKTOCK", "DIT IS DE FINISH");
    }

    @Override
    public void onTick(long l) {
        Log.d("TICKTOCK", "AANTAL MILLIS OVER: " + l);
        timeRemaining = (int) l;
    }

    public int returnTimeRmaining() {
        Log.d("TICKTOCK", "DIT IS TIMEREMAIING: " + timeRemaining);
        return timeRemaining;
    }
}
