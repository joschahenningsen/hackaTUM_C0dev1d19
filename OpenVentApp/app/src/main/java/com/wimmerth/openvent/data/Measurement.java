package com.wimmerth.openvent.data;

public class Measurement implements Comparable{
    private int time;
    private int volumePerMovement;

    public Measurement(int time, int volumePerMovement) {
        this.time = time;
        this.volumePerMovement = volumePerMovement;
    }

    public int getTime() {
        return time;
    }

    public int getVolumePerMovement() {
        return volumePerMovement;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(time, ((Measurement)o ).getTime());
    }
}
