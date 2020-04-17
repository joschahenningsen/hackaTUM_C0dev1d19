package com.wimmerth.openvent.data;

import com.wimmerth.openvent.PatientDetailsActiviy;

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
}
