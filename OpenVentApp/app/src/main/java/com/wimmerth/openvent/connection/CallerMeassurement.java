package com.wimmerth.openvent.connection;

import com.wimmerth.openvent.connection.VentApi.OpenVentResponse;
import com.wimmerth.openvent.data.Measurement;

public interface CallerMeassurement {
    void addData(OpenVentResponse apiData, int p);
}
