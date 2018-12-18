package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class PrestatiesActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    private LineChart nChart;

    DataBaseHelper mDatabaseHelper;

    private ListView mListView;

    public static ArrayList<String> x = new ArrayList<String>();
    public static ArrayList<String> y = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);

        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DataBaseHelper(this, "scores");

        // ADD SOME MORE DATA
        mDatabaseHelper.addData(Integer.toString(100), "scores");

        Cursor data = mDatabaseHelper.getAllData("scores");
        while(data.moveToNext()) {
            x.add(data.getString(0));
            y.add(data.getString(1));
        }

        // CHECK THE DATA
        Log.d("Table Data", "X values: " + Integer.toString(x.size()));
        Log.d("Table Data", "Y values: " + Integer.toString(y.size()));

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

        settingsButton = (ImageButton) findViewById(R.id.pauseButton);
        homeButton = (ImageButton) findViewById(R.id.pauseButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrestatiesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Dit haalt het huidige scherm van de stack af in plaats van een nieuwe bovenop toevoegen.
            }
        });
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

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}


