package com.wimmerth.openvent.data;

import com.wimmerth.openvent.PatientDetailsActiviy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Patient {
    private String name;
    private int id;
    private Vitals vitals;
    private PatientDetailsActiviy caller;

    public Patient(String name, int id, final PatientDetailsActiviy caller) {
        this.name = name;
        this.id = id;
        vitals = new Vitals();
        this.caller = caller;
        if (caller!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int time = 0;
                    Random r = new Random();
                    while (true) {
                        caller.addData(new Measurement(time++, 80 + r.nextInt(30)));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
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


}
