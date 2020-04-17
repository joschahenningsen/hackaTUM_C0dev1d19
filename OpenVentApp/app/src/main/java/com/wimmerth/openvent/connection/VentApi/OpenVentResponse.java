
package com.wimmerth.openvent.connection.VentApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenVentResponse {

    @SerializedName("device_id")
    @Expose
    private Integer deviceId;
    @SerializedName("processed")
    @Expose
    private Processed processed;
    @SerializedName("raw")
    @Expose
    private Raw raw;
    @SerializedName("time")
    @Expose
    private Integer time;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Processed getProcessed() {
        return processed;
    }

    public void setProcessed(Processed processed) {
        this.processed = processed;
    }

    public Raw getRaw() {
        return raw;
    }

    public void setRaw(Raw raw) {
        this.raw = raw;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
