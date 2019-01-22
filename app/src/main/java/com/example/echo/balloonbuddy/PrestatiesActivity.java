package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;

public class  PrestatiesActivity extends AppCompatActivity {

    // Maak een databasehelper aan
    DataBaseHelper mDatabaseHelper;

    // Maak arraylists aan voor de X en Y waardes in de grafiek
    public static ArrayList<String> x = new ArrayList<String>();
    public static ArrayList<String> y = new ArrayList<String>();

    // Variabelen voor foto's
    ImageView vlucht1image;
    ImageView vlucht100image;
    ImageView vlucht250image;
    ImageView login7image;
    ImageView login14image;
    ImageView login28image;
    ImageView xp1000image;
    ImageView xp2500image;
    ImageView xp5000image;

    // Variabelen voor buttons
    ImageButton settingsButton;
    ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Geef de context mee aan de databasehelper
        mDatabaseHelper = new DataBaseHelper(this);

        // Uodate de achievements bij het openen van de activity
        mDatabaseHelper.updateAchievements();

        // Koppel de variabelen aan de UI spullen
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        vlucht1image = (ImageView) findViewById(R.id.vlucht1image);
        vlucht100image = (ImageView) findViewById(R.id.vlucht100image);
        vlucht250image = (ImageView) findViewById(R.id.vlucht250image);
        login7image  = (ImageView) findViewById(R.id.login7image);
        login14image = (ImageView) findViewById(R.id.login14image);
        login28image = (ImageView) findViewById(R.id.login28image);
        xp1000image = (ImageView) findViewById(R.id.xp1000image);
        xp2500image = (ImageView) findViewById(R.id.xp2500image);
        xp5000image = (ImageView) findViewById(R.id.xp5000image);

        // Haal alle data op van de tabellen SCORES en ACHIEVEMENTS
        Cursor scoresData = mDatabaseHelper.getAllData("scores");
        Cursor achievementsData = mDatabaseHelper.getAllData("achievements");

        // Maak enige lokale data leeg
        x.clear();
        y.clear();

        // Loop door de score data heeft en voeg het toe aan de X en Y arrays
        while(scoresData.moveToNext()) {
            x.add(scoresData.getString(0));
            y.add(scoresData.getString(1));
        }

        // Plot de grafiek
        graphPlotter();

        // Maakt een onclick listener aan voor de instellingen knop
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish de huidige activity
                finish();

                // Maak settings activiity aan en start hem
                Intent intent = new Intent(PrestatiesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Maakt een onclick listener aan voor de home knop
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Laat de achievement goed zien met huidige data
        achievementShower(achievementsData);
    }

    // Methode om de grafiek aan te maken
    public void graphPlotter() {
        // Maak een GraphView aan
        GraphView graph;

        // Maak een serie voor data aan
        LineGraphSeries<DataPoint> series;

        // Koppel de UI aan de variabelen
        graph = (GraphView) findViewById(R.id.graph);

        // Variabelen moet een nieuwe LineGraphSeries zijn
        series = new LineGraphSeries<>(data());

        // Zorg dat de data op een bepaalde manier gestyled en weergegeven wordt
        series.setThickness(6);
        series.setDrawBackground(true);
        series.setDrawDataPoints(true);
        series.setColor(Color.rgb(216, 57, 73));
        series.setBackgroundColor(Color.argb(60,95, 226, 156));

        // Zorg dat de graph zelf op een bepaalde manier gestyled en weergegeven wordt
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Score");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHumanRounding(false);

        // Voeg data toe aan de grafiek
        graph.addSeries(series);
    }

    // Data teruggeven in de vorm van de DataPoint[]
    public DataPoint[] data() {
        // Vind het nummer van totaal aantal data punten
        int n = x.size();

        // Maakt een object van het type DataPoint[] met de grootte van 'n'
        DataPoint[] values = new DataPoint[n];

        // Loop de data heen en voeg het toe aan variabele 'values'
        for(int i = 0; i < n; i++) {
            DataPoint v = new DataPoint(Double.parseDouble(x.get(i)), Double.parseDouble(y.get(i)));
            values[i] = v;
        }

        // Geef de data terug
        return values;
    }

    // Methode voor het laten zien van de achievements (welke zijn locked, welke zijn unlocked)
    public void achievementShower(Cursor achievementsData) {
        // Loop door de achievement data heen
        while(achievementsData.moveToNext()) {
            // De achievement die nu in de loop zit
            int number = achievementsData.getInt(4);

            // Of deze achievement unlocked is, of niet
            int unlocked = achievementsData.getInt(1);

            // Laat het juiste plaatje zien als de achievement unlocked is
            if(number == 1 && unlocked == 1) {
                vlucht1image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
            }

            if(number == 2 && unlocked == 1) {
                vlucht100image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
            }

            if(number == 3 && unlocked == 1) {
                vlucht250image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
            }

            if(number == 4 && unlocked == 1) {
                login7image.setImageResource(R.mipmap.icons_award_unlocked_v01);
            }

            if(number == 5 && unlocked == 1) {
                login14image.setImageResource(R.mipmap.icons_award_unlocked_v01);
            }

            if(number == 6 && unlocked == 1) {
                login28image.setImageResource(R.mipmap.icons_award_unlocked_v01);
            }

            if(number == 7 && unlocked == 1) {
                xp1000image.setImageResource(R.mipmap.icons_lintje_unlocked_v01);
            }

            if(number == 8 && unlocked == 1) {
                xp2500image.setImageResource(R.mipmap.icons_lintje_unlocked_v01);
            }

            if(number == 9 && unlocked == 1) {
                xp5000image.setImageResource(R.mipmap.icons_lintje_unlocked_v01);
            }
        }
    }
}