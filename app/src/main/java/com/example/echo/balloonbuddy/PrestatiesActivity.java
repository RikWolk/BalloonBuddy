package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;

public class  PrestatiesActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    private LineChart nChart;

    DataBaseHelper mDatabaseHelper;

    private ListView mListView;

    public static ArrayList<String> x = new ArrayList<String>();
    public static ArrayList<String> y = new ArrayList<String>();

    ImageView vlucht1image;
    ImageView vlucht100image;
    ImageView vlucht250image;
    ImageView login7image;
    ImageView login14image;
    ImageView login28image;
    ImageView xp1000image;
    ImageView xp2500image;
    ImageView xp5000image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // INITIATE ACHIEVEMENT VIEW
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);

        // GET DB AND MAKE LIST
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DataBaseHelper(this);

        // ADD BUTTONS
        ImageButton settingsButton;
        ImageButton homeButton;

        // LINK STUFF TO XML
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

        // ADD SOME MORE DATA
        mDatabaseHelper.insertScore(200, 5);
        mDatabaseHelper.insertScore(220, 3);
        mDatabaseHelper.insertScore(250, 8);

        // GET ALL THE DATA FROM TABLE SCORES AND ACHIEVEMENTS
        Cursor scoresData = mDatabaseHelper.getAllData("scores");
        Cursor achievementsData = mDatabaseHelper.getAllData("achievements");

        // CLEAR ANY LOCAL OLD TABLE DATA
        x.clear();
        y.clear();

        // ITERATE THROUGH TABLE DATA AND ADD IT TO X and Y ARRAYS
        while(scoresData.moveToNext()) {
            x.add(scoresData.getString(0));
            y.add(scoresData.getString(1));
        }

        graphPlotter();

        // DELETE DATA FROM TABLE SCORES
//        mDatabaseHelper.deleteAllData("scores");

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PrestatiesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        achievementShower(achievementsData);
    }

    public void graphPlotter() {
        //Test
        GraphView graph;
        LineGraphSeries<DataPoint> series;
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(data());
        series.setColor(Color.rgb(216, 57, 73));
        series.setThickness(6);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(60,95, 226, 156));
        series.setDrawDataPoints(true);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.addSeries(series);
    }

    public DataPoint[] data() {
        int n = x.size();     //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i = 0; i < n; i++) {
            DataPoint v = new DataPoint(Double.parseDouble(x.get(i)), Double.parseDouble(y.get(i)));
            values[i] = v;
        }
        return values;
    }

    public void achievementShower(Cursor achievementsData) {
        while(achievementsData.moveToNext()) {
            int number = achievementsData.getInt(4);
            int unlocked = achievementsData.getInt(1);

            Log.d("Table Data", "Achievement number: " + number);
//            Log.d("Table Data", "Unlocked status: " + unlocked);

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