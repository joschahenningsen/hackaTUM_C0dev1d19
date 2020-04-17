package com.wimmerth.openvent.data;

import java.util.ArrayList;
import java.util.List;

public class Vitals {
    //TODO
    private ArrayList<Measurement> meassurements;

    public Vitals() {
        this.meassurements = new ArrayList<>();
        meassurements.add(new Measurement(0, 110));
        meassurements.add(new Measurement(1, 102));
        meassurements.add(new Measurement(2, 98));
        meassurements.add(new Measurement(3, 104));
        meassurements.add(new Measurement(4, 95));
        meassurements.add(new Measurement(5, 113));
        meassurements.add(new Measurement(6, 98));
        meassurements.add(new Measurement(7, 104));
        meassurements.add(new Measurement(8, 95));
        meassurements.add(new Measurement(9, 113));
    }

    public List<Measurement> getMeassurements() {
        return meassurements;
    }
}
