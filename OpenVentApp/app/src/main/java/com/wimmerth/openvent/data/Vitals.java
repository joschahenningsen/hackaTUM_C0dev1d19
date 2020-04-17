package com.wimmerth.openvent.data;

import java.util.ArrayList;
import java.util.List;

public class Vitals {
    //TODO
    private ArrayList<Measurement> meassurements;

    public Vitals() {
        this.meassurements = new ArrayList<>();
    }

    public List<Measurement> getMeassurements() {
        return meassurements;
    }
}
