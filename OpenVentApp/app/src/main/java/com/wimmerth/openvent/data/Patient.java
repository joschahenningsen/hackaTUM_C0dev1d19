package com.wimmerth.openvent.data;

import java.util.List;

public class Patient {
    private String name;
    private int id;
    private Vitals vitals;

    public Patient(String name, int id) {
        this.name = name;
        this.id = id;
        vitals = new Vitals();
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
