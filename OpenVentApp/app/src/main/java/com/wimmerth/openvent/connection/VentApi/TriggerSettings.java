
package com.wimmerth.openvent.connection.VentApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TriggerSettings {

    @SerializedName("FiO2")
    @Expose
    private Integer fiO2;
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
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("pressure_max")
    @Expose
    private Integer pressureMax;

    public Integer getFiO2() {
        return fiO2;
    }

    public void setFiO2(Integer fiO2) {
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

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(Integer pressureMax) {
        this.pressureMax = pressureMax;
    }

}
