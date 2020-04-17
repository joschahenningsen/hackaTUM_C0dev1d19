package com.wimmerth.openvent.data;

import com.google.gson.Gson;
import com.wimmerth.openvent.PatientDetailsActiviy;
import com.wimmerth.openvent.connection.Caller;
import com.wimmerth.openvent.connection.ServerConnection;
import com.wimmerth.openvent.connection.VentApi.OpenVentResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Patient implements Caller {
    private String name;
    private int id;
    private Vitals vitals;
    private PatientDetailsActiviy caller;
    private Gson g = new Gson();

    public Patient(String name, int id, final PatientDetailsActiviy caller) {
        this.name = name;
        this.id = id;
        vitals = new Vitals();
        this.caller = caller;
        if (caller!=null) {
            ServerConnection sc = new ServerConnection(this);
            sc.start();
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Measurement> getMeassurements() {
        return vitals.getMeassurements();
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
                ret.add(new Patient(strings1[0], Integer.parseInt(strings1[1]),null));
            }
        }
        return ret;
    }


    @Override
    public void onResponse(String line) {
        OpenVentResponse res = g.fromJson(line, OpenVentResponse.class);
        if (caller!=null){
            caller.addData(
                    new Measurement(res.get0().getTime(),
                            res.get0().getProcessed().getVolumePerMinute())
            );
        }
    }
}
