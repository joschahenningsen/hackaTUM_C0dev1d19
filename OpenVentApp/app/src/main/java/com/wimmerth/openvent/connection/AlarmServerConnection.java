package com.wimmerth.openvent.connection;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.wimmerth.openvent.MainActivity;
import com.wimmerth.openvent.R;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class AlarmServerConnection extends Thread {

    Context context;
    Socket socket;
    static final int port = 5005;//TODO
    static final String server = "openvent.joschas.page";

    public AlarmServerConnection(Context c) {
        context = c;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(server, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                        .setContentText("Alarm at "+id)// message for notification
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
}
