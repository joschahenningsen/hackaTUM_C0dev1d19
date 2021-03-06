package com.wimmerth.openvent.connection;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wimmerth.openvent.MainActivity;
import com.wimmerth.openvent.R;
import com.wimmerth.openvent.data.Change;
import com.wimmerth.openvent.data.Changes;
import com.wimmerth.openvent.data.Patient;
import com.wimmerth.openvent.ui.changeMonitor.GalleryFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class AlarmServerConnectionService extends IntentService {

    static final int port = 5010;
    static final String server = "openvent.joschas.page";
    List<Patient> patients;
    static PrintWriter writer;
    static BufferedReader reader;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmServerConnectionService(String name) {
        super(name);
    }

    public AlarmServerConnectionService() {
        super("AlarmServerConnectionService");
    }

    public static void sendSignal(final String sig) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (writer != null) {
                    Log.d("joscha", sig + ":" + MainActivity.doctorId);
                    writer.print(sig + ":" + MainActivity.doctorId);
                    writer.flush();
                }
            }
        }).start();
    }

    public void run() throws IOException {
        Socket socket = new Socket(server, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        patients = Patient.fromString(getBaseContext().getSharedPreferences("patients", Context.MODE_PRIVATE).getString("list", ""));
        StringBuilder requestedPatientIds = new StringBuilder("start:");
        for (int i = 0; i < patients.size(); i++) {
            requestedPatientIds.append(patients.get(i).getId());
            if (i < patients.size() - 1) {
                requestedPatientIds.append(",");
            }
        }
        writer.println(requestedPatientIds);
        writer.flush();
        String line;
        while ((line = reader.readLine()) != null) {
            sendNotification(line);
        }
    }

    private void sendNotification(String line) {
        int id;
        try {
            id = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            Log.d("thomas", line);
            String json = "{\"data\":" + line + "}";
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Changes changes = gson.fromJson(json, Changes.class);
            List<Change> changeList = changes.getChanges();
            GalleryFragment.changes.clear();
            GalleryFragment.changes.addAll(changeList);
            return;
        }
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("BEDID" + line,
                    "BEDID" + line,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("MEDICAL_ALERT");
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }
        String patientName = "";
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                patientName = patient.getName();
            }
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "BEDID" + line)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("OpenVent Emergency!") // title for notification
                .setContentText("Alarm at bed " + id + "\nPatient: " + patientName)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (true) {
            try {
                run();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
