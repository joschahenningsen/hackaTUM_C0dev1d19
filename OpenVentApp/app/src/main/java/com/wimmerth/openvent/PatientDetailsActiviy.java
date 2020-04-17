package com.wimmerth.openvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.wimmerth.openvent.data.Measurement;
import com.wimmerth.openvent.data.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientDetailsActiviy extends AppCompatActivity {
    LineGraphSeries<DataPoint> dynSeries;
    private TextView rrTextView;
    private TextView o2TextView;
    private TextView co2TextView;
    LineChart l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details_activiy);
        Intent i = getIntent();
        Patient p = new Patient(
                i.getStringExtra("name"),
                i.getIntExtra("id", 0),
                this);

        TextView patientNameTextView = findViewById(R.id.patientName);
        TextView bedNumberTextView = findViewById(R.id.bedNumber);
        patientNameTextView.setText(p.getName());
        bedNumberTextView.setText("Bett Nr.: " + p.getId());

        this.rrTextView = findViewById(R.id.rr);
        this.o2TextView = findViewById(R.id.o2);
        this.co2TextView = findViewById(R.id.co2);

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        dynSeries = new LineGraphSeries<>();
        graph.addSeries(dynSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
        graph.setTitle("Volume per minute (ml)");

        l = findViewById(R.id.chart1);
        setupChart(l, getData(100, 10f), Color.rgb(137, 230, 81));
    }

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setBackgroundColor(color);
        chart.setViewPortOffsets(10, 0, 10, 0);
        chart.setData(data);
        Legend l = chart.getLegend();
        l.setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.animateX(2500);
    }


    private LineData getData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        // create a data object with the data sets
        return new LineData(set1);
    }

    public void addData(final Measurement m) {
        Log.d("joscha", m.toString());
        if (dynSeries!=null) { // wait for initialisation
            dynSeries.appendData(new DataPoint(m.getTime(), m.getVolumePerMovement()), true, 100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rrTextView.setText(""+m.getRr());
                    o2TextView.setText(""+m.getO2());
                    co2TextView.setText(""+m.getCo2());
                }
            });
            if (l.getLineData().getDataSetCount()==100){
                l.getLineData().getDataSetByIndex(0).removeFirst();
            }
            l.getLineData().addEntry(new Entry(m.getTime(), m.getVolumePerMovement()), 0);

        }
    }
}
