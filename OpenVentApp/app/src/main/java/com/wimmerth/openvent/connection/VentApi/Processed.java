
package com.wimmerth.openvent.connection.VentApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Processed {

    @SerializedName("ExpiredCO2")
    @Expose
    private Integer expiredCO2;
    @SerializedName("ExpiredO2")
    @Expose
    private Integer expiredO2;
    @SerializedName("MVe")
    @Expose
    private Integer mVe;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("triggerSettings")
    @Expose
    private com.wimmerth.openvent.connection.VentApi.TriggerSettings triggerSettings;
    @SerializedName("ventilationMode")
    @Expose
    private String ventilationMode;
    @SerializedName("volumePerMinute")
    @Expose
    private Integer volumePerMinute;
    @SerializedName("volumePerMovement")
    @Expose
    private Integer volumePerMovement;

    public Integer getExpiredCO2() {
        return expiredCO2;
    }

    public void setExpiredCO2(Integer expiredCO2) {
        this.expiredCO2 = expiredCO2;
    }

    public Integer getExpiredO2() {
        return expiredO2;
    }

    public void setExpiredO2(Integer expiredO2) {
        this.expiredO2 = expiredO2;
    }

    public Integer getMVe() {
        return mVe;
    }

    public void setMVe(Integer mVe) {
        this.mVe = mVe;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public TriggerSettings getTriggerSettings() {
        return triggerSettings;
    }

    public void setTriggerSettings(TriggerSettings triggerSettings) {
        this.triggerSettings = triggerSettings;
    }

    public String getVentilationMode() {
        return ventilationMode;
    }

    public void setVentilationMode(String ventilationMode) {
        this.ventilationMode = ventilationMode;
    }

    public Integer getVolumePerMinute() {
        return volumePerMinute;
    }

    public void setVolumePerMinute(Integer volumePerMinute) {
        this.volumePerMinute = volumePerMinute;
    }

    public Integer getVolumePerMovement() {
        return volumePerMovement;
    }

    public void setVolumePerMovement(Integer volumePerMovement) {
        this.volumePerMovement = volumePerMovement;
    }

}
