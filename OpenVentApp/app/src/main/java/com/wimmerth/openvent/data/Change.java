package com.wimmerth.openvent.data;

import java.util.ArrayList;
import java.util.List;

public class Change {
    private int bedNr;
    private String oldState;
    private String newState;
    private String descr;

    public Change(int bedNr, String descr,  String oldState, String newState) {
        this.bedNr = bedNr;
        this.descr = descr;
        this.oldState = oldState;
        this.newState = newState;

    }

    public String getDescr() {
        return descr;
    }

    public int getBed() {
        return bedNr;
    }

    public String getOld() {
        return oldState;
    }

    public String getNew() {
        return newState;
    }

    @Override
    public String toString() {
        return "Change{" +
                "bedNr=" + bedNr +
                ", oldState='" + oldState + '\'' +
                ", newState='" + newState + '\'' +
                '}';
    }

    public static String toString(List<Change> changes){
        StringBuilder s = new StringBuilder();
        for(Change c: changes){
            s.append(c.bedNr).append("%").append(c.descr).append("%").append(c.oldState).append("%").append(c.newState).append("§");
        }
        return s.toString();
    }

    public static List<Change> fromString(String string){
        String[] strings = string.split("§");
        List<Change> ret = new ArrayList<>();
        for(String s:strings){
            if(s.length()>2) {
                String[] strings1 = s.split("%");
                Change c = new Change(Integer.parseInt(strings1[0]),strings1[1],strings1[2],strings1[3]);
                ret.add(c);
            }
        }
        return ret;
    }
}
