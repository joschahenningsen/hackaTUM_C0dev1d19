package com.wimmerth.openvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.wimmerth.openvent.data.Measurement;
import com.wimmerth.openvent.data.Patient;

import java.util.List;

public class PatientDetailsActiviy extends AppCompatActivity {
    LineGraphSeries<DataPoint> dynSeries;

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

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        dynSeries = new LineGraphSeries<>();
        graph.addSeries(dynSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
    }


    public void addData(Measurement m) {
        Log.d("joscha", m.toString());
        if (dynSeries!=null) // wait for initialisation
            dynSeries.appendData(new DataPoint(m.getTime(), m.getVolumePerMovement()), true, 40);
    }
}
