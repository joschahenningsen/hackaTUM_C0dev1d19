package com.wimmerth.openvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.wimmerth.openvent.data.Measurement;
import com.wimmerth.openvent.data.Patient;

import java.util.List;

public class PatientDetailsActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details_activiy);
        Intent i = getIntent();
        Patient p = new Patient(i.getStringExtra("name"),
                i.getIntExtra("int", 0));

        TextView patientNameTextView = findViewById(R.id.patientName);
        TextView bedNumberTextView = findViewById(R.id.bedNumber);
        patientNameTextView.setText(p.getName());
        bedNumberTextView.setText("Bett Nr.: " + p.getId());

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        LineGraphSeries<DataPoint> dynSeries = getSeries(p.getMeassurements());
        graph.addSeries(dynSeries);
    }

    private LineGraphSeries<DataPoint> getSeries(List<Measurement> measurements) {
        DataPoint[] dataPoints = new DataPoint[measurements.size()];
        for (int i = 0; i < measurements.size(); i++) {
            dataPoints[i] = new DataPoint(measurements.get(i).getTime(), measurements.get(i).getVolumePerMovement());
        }
        return new LineGraphSeries<>(dataPoints);
    }
}
