package com.wimmerth.openvent.connection;

import com.wimmerth.openvent.data.Change;

import java.util.ArrayList;
import java.util.List;

public class BreakConnectionService extends Thread {

    public static void endPause(List<Change> changes) {
        //TODO
        for(int i = 0; i<changes.size();i++){
            changes.remove(0);
        }
        changes.add(new Change(4242,"PEEP:","5 cmH20","6 cmH20"));
    }

    public static void startPause() {
        //TODO
    }
}
