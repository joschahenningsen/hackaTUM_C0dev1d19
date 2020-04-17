package com.wimmerth.openvent.data;

public class Measurement implements Comparable{
    private final double co2;
    private final double o2;
    private final int rr;
    private int time;
    private int volumePerMovement;

    public Measurement(int time, int volumePerMovement, int expiredCO2, int expiredO2, int rr) {
        this.time = time;
        this.volumePerMovement = volumePerMovement;
        this.co2 = expiredCO2;
        this.o2 = expiredO2;
        this.rr = rr;
    }

    public Measurement(int time, int volumePerMovement, double expiredO2, double expiredCO2, int rr) {
        this.time = time;
        this.volumePerMovement = volumePerMovement;
        this.co2 = expiredCO2;
        this.o2 = expiredO2;
        this.rr = rr;
    }

    public int getTime() {
        return time;
    }

    public int getVolumePerMovement() {
        return volumePerMovement;
    }

    public double getCo2() {
        return co2;
    }

    public double getO2() {
        return o2;
    }

    public int getRr() {
        return rr;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(time, ((Measurement)o ).getTime());
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "time=" + time +
                ", volumePerMovement=" + volumePerMovement +
                '}';
    }
}
