package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.database.Cursor;
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

public class PrestatiesActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    private LineChart nChart;

    DataBaseHelper mDatabaseHelper;

    private ListView mListView;

    public static ArrayList<String> x = new ArrayList<String>();
    public static ArrayList<String> y = new ArrayList<String>();

    boolean vlucht1unlocked = true;
    boolean vlucht100unlocked = false;
    boolean vlucht250unlocked = true;

    ImageView vlucht1image;
    ImageView vlucht100image;
    ImageView vlucht250image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);

        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DataBaseHelper(this);

        // ADD SOME MORE DATA
        mDatabaseHelper.insertScore(200, 5);
        mDatabaseHelper.insertScore(220, 3);
        mDatabaseHelper.insertScore(250, 8);

        // GET ALL THE DATA FROM TABLE SCORES
        Cursor data = mDatabaseHelper.getAllData("scores");

        // CLEAR ANY LOCAL OLD TABLE DATA
        x.clear();
        y.clear();

        // ITERATE THROUGH TABLE DATA AND ADD IT TO X and Y ARRAYS
        while(data.moveToNext()) {
            x.add(data.getString(0));
            y.add(data.getString(1));
        }

        // CHECK THE AMOUNT OF DATA
        Log.d("Table Data", "X values: " + Integer.toString(x.size()));
        Log.d("Table Data", "Y values: " + Integer.toString(y.size()));

        // DELETE DATA FROM TABLE SCORES
//        mDatabaseHelper.deleteAllData("scores");

        // PLOT THE GRAPH
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

        ImageButton settingsButton;
        ImageButton homeButton;

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);

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

        vlucht1image = (ImageView) findViewById(R.id.vlucht1image);
        vlucht100image = (ImageView) findViewById(R.id.vlucht100image);
        vlucht250image = (ImageView) findViewById(R.id.vlucht250image);

        if(vlucht1unlocked) {
            vlucht1image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(vlucht100unlocked) {
            vlucht100image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(vlucht250unlocked) {
            vlucht250image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

    }

    public DataPoint[] data(){
        int n = x.size();     //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i = 0; i < n; i++){
            DataPoint v = new DataPoint(Double.parseDouble(x.get(i)),Double.parseDouble(y.get(i)));
            values[i] = v;
        }
        return values;
    }
}

