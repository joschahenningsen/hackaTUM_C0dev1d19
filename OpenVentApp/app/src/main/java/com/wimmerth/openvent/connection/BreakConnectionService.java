package com.wimmerth.openvent.connection;

import com.wimmerth.openvent.data.Change;

import java.util.List;

public class BreakConnectionService extends Thread {

    public static void endPause(List<Change> changes) {
        //TODO
        changes.add(new Change(4242,"PEEP: 5 cmH20","6 cmH20"));
    }

    public static void startPause() {
        //TODO
    }
}
