package com.wimmerth.openvent.connection;

import com.wimmerth.openvent.data.Measurement;

public interface CallerMeassurement {
    void addData(final Measurement m, int p);
}
