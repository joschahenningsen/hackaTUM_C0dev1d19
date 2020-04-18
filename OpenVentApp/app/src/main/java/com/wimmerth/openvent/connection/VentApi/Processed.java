
package com.wimmerth.openvent.connection.VentApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Processed {

    @SerializedName("ExpiredCO2")
    @Expose
    private Double expiredCO2;
    @SerializedName("ExpiredO2")
    @Expose
    private Double expiredO2;
    @SerializedName("FiO2")
    @Expose
    private Double fiO2;
    @SerializedName("IE")
    @Expose
    private Double iE;
    @SerializedName("MVe")
    @Expose
    private Integer mVe;
    @SerializedName("PEEP")
    @Expose
    private Integer pEEP;
    @SerializedName("RR")
    @Expose
    private Integer rR;
    @SerializedName("VT")
    @Expose
    private Integer vT;
    @SerializedName("flowrate")
    @Expose
    private Integer flowrate;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("triggerSettings")
    @Expose
    private TriggerSettings triggerSettings;
    @SerializedName("ventilationMode")
    @Expose
    private String ventilationMode;
    @SerializedName("volumePerMinute")
    @Expose
    private Integer volumePerMinute;
    @SerializedName("volumePerMovement")
    @Expose
    private Integer volumePerMovement;

    public Double getExpiredCO2() {
        return expiredCO2;
    }

    public void setExpiredCO2(Double expiredCO2) {
        this.expiredCO2 = expiredCO2;
    }

    public Double getExpiredO2() {
        return expiredO2;
    }

    public void setExpiredO2(Double expiredO2) {
        this.expiredO2 = expiredO2;
    }

    public Double getFiO2() {
        return fiO2;
    }

    public void setFiO2(Double fiO2) {
        this.fiO2 = fiO2;
    }

    public Double getIE() {
        return iE;
    }

    public void setIE(Double iE) {
        this.iE = iE;
    }

    public Integer getMVe() {
        return mVe;
    }

    public void setMVe(Integer mVe) {
        this.mVe = mVe;
    }

    public Integer getPEEP() {
        return pEEP;
    }

    public void setPEEP(Integer pEEP) {
        this.pEEP = pEEP;
    }

    public Integer getRR() {
        return rR;
    }

    public void setRR(Integer rR) {
        this.rR = rR;
    }

    public Integer getVT() {
        return vT;
    }

    public void setVT(Integer vT) {
        this.vT = vT;
    }

    public Integer getFlowrate() {
        return flowrate;
    }

    public void setFlowrate(Integer flowrate) {
        this.flowrate = flowrate;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
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
