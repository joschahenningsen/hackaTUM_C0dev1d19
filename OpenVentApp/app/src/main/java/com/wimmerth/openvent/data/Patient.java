package com.wimmerth.openvent.data;

import android.os.Debug;
import android.util.Log;

import com.google.gson.Gson;
import com.wimmerth.openvent.PatientDetailsActiviy;
import com.wimmerth.openvent.connection.Caller;
import com.wimmerth.openvent.connection.CallerMeassurement;
import com.wimmerth.openvent.connection.ServerConnection;
import com.wimmerth.openvent.connection.VentApi.OpenVentResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Patient implements Caller {
    private ArrayList<CallerMeassurement> callbacks = new ArrayList<>();
    private String name;
    private int id;
    private Gson g = new Gson();
    private ServerConnection sc;
    private OpenVentResponse apiData;

    public Patient(String name, int id) {
        this.name = name;
        this.id = id;
        sc = new ServerConnection(this, id);
        sc.start();

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void addCallback(CallerMeassurement c) {
        callbacks.add(c);
    }

    private void notifyCallers() {
        for (CallerMeassurement callback : callbacks) {
            callback.addData(this.apiData, id);
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public static String toString(List<Patient> patients) {
        StringBuilder ret = new StringBuilder();
        for (Patient p : patients) {
            ret.append(p.name).append("%").append(p.id).append("§");
        }
        System.out.println(ret.toString());
        return ret.toString();
    }

    public static List<Patient> fromString(String string) {
        List<Patient> ret = new ArrayList<>();
        if (string == null || string.length() == 0) return ret;
        String[] strings = string.split("§");
        System.out.println(Arrays.toString(strings));
        for (String s : strings) {
            if (s.length() >= 2) {
                String[] strings1 = s.split("%");
                System.out.println(Arrays.toString(strings1));
                ret.add(new Patient(strings1[0], Integer.parseInt(strings1[1])));
            }
        }
        return ret;
    }


    @Override
    public void onResponse(String line) {
        Log.d("joscha", "new Data reveiced for " + this.id);
        apiData = g.fromJson(line, OpenVentResponse.class);

        notifyCallers();
    }

    public void close() {
        //sc.close(); DONT, otherwise it will not receive data anywhere
    }

    public OpenVentResponse getApiData() {
        return apiData;
    }
}
