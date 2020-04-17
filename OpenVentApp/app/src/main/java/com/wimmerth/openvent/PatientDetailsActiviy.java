package com.wimmerth.openvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.wimmerth.openvent.data.Measurement;
import com.wimmerth.openvent.data.Patient;

import java.util.List;

public class PatientDetailsActiviy extends AppCompatActivity {
    LineGraphSeries<DataPoint> dynSeries;
    private TextView rrTextView;
    private TextView o2TextView;
    private TextView co2TextView;

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

        }
    }
}
