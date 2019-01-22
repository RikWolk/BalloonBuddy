package com.example.echo.balloonbuddy;

import android.widget.ProgressBar;

public class ProgressbarChanger {

    // Variabele voor huidige progress
    static int progress;

    // Methode om de progressbar omhoog te laten gaan
    public static void up(ProgressBar progressBar) {
        // Haal de huidge waarde op en zet die gelijk aan de variabele
        progress = progressBar.getProgress();

        // Als de huidige waarde lager is dan 99...
        if(progress < 99) {
            // ...mag de progressbar 1 getal omhoog
            progress++;

            // Geef de nieuwe waarde door aan de progressbar
            progressBar.setProgress(progress);
        }
    }

    // Methode om de progressbar omlaag te laten gaan
    public static void down(ProgressBar progressBar) {
        // Haal de huidge waarde op en zet die gelijk aan de variabele
        progress = progressBar.getProgress();

        // Als de huidige waarde groter is dan 0...
        if(progress > 0) {
            // ...mag de progressbar 1 getal omlaag
            progress--;

            // Geef de nieuwe waarde door aan de progressbar
            progressBar.setProgress(progress);
        }
    }
}
