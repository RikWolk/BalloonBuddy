package com.example.echo.balloonbuddy;

import android.os.CountDownTimer;

public class GameTimer extends CountDownTimer {

    // Variabelen aanmaken
    private int timeRemaining;
    private ChangeListener listener;

    // Publieke klasse van Gametimer, hiermee kun je een game timer aanmaken
    // Heeft de duur van de timer nodig en het interval waarop de onTick aangeroepen wordt
    public GameTimer(int totalSeconds, int interval) {
        super(totalSeconds, interval);
    }

    // Als de timer is afgelopen wordt deze methode aangeroepen
    @Override
    public void onFinish() {
        // Maak een verandering aan de listener zodat van buitenaf opgemerkt kan worden dat de timer voorbij is
        listener.onChange();
    }

    // Iedere tick wordt deze methode aangeroepen
    @Override
    public void onTick(long l) {
        // Zet het aantal milisecondes dat over is gelijk aan de variabele
        timeRemaining = (int) l;
    }

    // Geeft terug hoeveel tijd de huidige timer nog te gaan heeft
    public int returnTimeRmaining() {
        return timeRemaining;
    }

    // Listeren aanmaken
    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    // Onchange voor listener maken
    public interface ChangeListener {
        void onChange();
    }
}
