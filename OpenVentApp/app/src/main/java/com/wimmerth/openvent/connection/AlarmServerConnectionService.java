package com.wimmerth.openvent.connection;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.wimmerth.openvent.MainActivity;
import com.wimmerth.openvent.R;
import com.wimmerth.openvent.data.Patient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class AlarmServerConnectionService extends IntentService {

    private Context context;
    private Socket socket;
    static final int port = 5010;
    static final String server = "openvent.joschas.page";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmServerConnectionService(String name) {
        super(name);
        context = getApplicationContext();
    }

    public AlarmServerConnectionService(){
        super("AlarmServerConnectionService");
    }

    public void run() {
        try {
            socket = new Socket(server, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            List<Patient> patients = Patient.fromString(getBaseContext().getSharedPreferences("patients", Context.MODE_PRIVATE).getString("list", ""));
            for (int i = 0; i < patients.size(); i++) {
                writer.print(patients.get(i).getId());
                if (i < patients.size() - 1) {
                    writer.print(",");
                }
                writer.flush();
            }
            String line;
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                            "YOUR_CHANNEL_NAME",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "YOUR_CHANNEL_ID")
                        .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                        .setContentTitle("Emergency") // title for notification
                        .setContentText("Alarm at " + id)// message for notification
                        .setAutoCancel(true); // clear notification after click
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                assert mNotificationManager != null;
                mNotificationManager.notify(0, mBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        run();
    }
}
