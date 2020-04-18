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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    LineChart chart;
    Patient p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details_activiy);
        Intent i = getIntent();
        p = new Patient(
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

        //chart:
        chart = findViewById(R.id.chart1);

        // enable description text
        chart.getDescription().setEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.rgb(250,250,250));

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        data.notifyDataChanged();

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(5.5f);
        leftAxis.setAxisMinimum(4.5f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void addData(final Measurement m) {
        Log.d("joscha", m.toString());
        if (dynSeries!=null) { // wait for initialisation
            dynSeries.appendData(new DataPoint(m.getTime(), m.getCo2()), true, 100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rrTextView.setText(""+m.getRr());
                    o2TextView.setText(""+m.getO2());
                    co2TextView.setText(""+m.getCo2());
                }
            });
        }
        addEntry(m);
    }



    /*COMPLEX CHART:*/


    private void addEntry(Measurement m) {

        LineData data = chart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                LineDataSet ldset = createSet();
                ldset.setDrawFilled(true);
                data.addDataSet(ldset);
                ldset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set = ldset;
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) m.getCo2()), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries

            chart.setVisibleXRangeMinimum(30);
            chart.setVisibleXRangeMaximum(30);
            chart.setAutoScaleMinMaxEnabled(true);

            //chart.setVisibleYRange(30, 30, YAxis.AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        p.close();
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(1f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
}
