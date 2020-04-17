package com.wimmerth.openvent.data;

public class Patient {
    private String name;
    private int id;
    private Vitals vitals;

    public Patient(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Vitals getVitals() {
        return vitals;
    }
}
