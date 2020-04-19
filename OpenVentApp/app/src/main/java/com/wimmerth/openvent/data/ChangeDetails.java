package com.wimmerth.openvent.data;

import java.util.ArrayList;
import java.util.List;

class ChangeDetails {
    private float FiO2_Old, IE_Old, IE, MVe_Old, PEEP_Old, PEEP, humidity_Old;
    private int pressure_max_Old, VT_Old, VT, RR_Old, RR;
    private int device_id;
    private String FiO2, MVe, humidity, pressure_max;

    List<Change> getChanges() {
        ArrayList<Change> ret = new ArrayList<>();
        if (Float.compare(FiO2_Old, Float.parseFloat(FiO2)) != 0) {
            ret.add(new Change(device_id, "FiO₂", FiO2_Old + "%", FiO2 + "%"));
        }
        if (Float.compare(IE_Old, IE) != 0) {
            ret.add(new Change(device_id, "IE", IE_Old + "", IE + ""));
        }
        if (Float.compare(MVe_Old, Float.parseFloat(MVe)) != 0) {
            ret.add(new Change(device_id, "MVe", MVe_Old + " mL", MVe + " mL"));
        }
        if (Float.compare(PEEP_Old, PEEP) != 0) {
            ret.add(new Change(device_id, "PEEP", PEEP_Old + " cmH₂O", PEEP + " cmH₂O"));
        }
        if (Float.compare(humidity_Old, Float.parseFloat(humidity)) != 0) {
            ret.add(new Change(device_id, "humidity", humidity_Old + "%", humidity + "%"));
        }
        if (Integer.parseInt(pressure_max) != pressure_max_Old) {
            ret.add(new Change(device_id, "Pmax", pressure_max_Old + " cmH₂O", pressure_max + " cmH₂O"));
        }
        if (VT_Old != VT) {
            ret.add(new Change(device_id, "VT", VT_Old + " mL", VT + " mL"));
        }
        if (RR_Old != RR) {
            ret.add(new Change(device_id, "RR", RR_Old + " 1/m", RR + " 1/m"));
        }
        return ret;
    }
}
