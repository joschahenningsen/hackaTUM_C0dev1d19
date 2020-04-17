
package com.wimmerth.openvent.connection.VentApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Raw {

    @SerializedName("CO2")
    @Expose
    private Double cO2;
    @SerializedName("O2")
    @Expose
    private Double o2;
    @SerializedName("angleSensor")
    @Expose
    private Integer angleSensor;
    @SerializedName("current")
    @Expose
    private Integer current;
    @SerializedName("motorRPM")
    @Expose
    private Integer motorRPM;
    @SerializedName("pressure1")
    @Expose
    private Integer pressure1;
    @SerializedName("pressure2")
    @Expose
    private Integer pressure2;
    @SerializedName("temperature1")
    @Expose
    private Double temperature1;
    @SerializedName("temperature2")
    @Expose
    private Double temperature2;

    public Double getCO2() {
        return cO2;
    }

    public void setCO2(Double cO2) {
        this.cO2 = cO2;
    }

    public Double getO2() {
        return o2;
    }

    public void setO2(Double o2) {
        this.o2 = o2;
    }

    public Integer getAngleSensor() {
        return angleSensor;
    }

    public void setAngleSensor(Integer angleSensor) {
        this.angleSensor = angleSensor;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getMotorRPM() {
        return motorRPM;
    }

    public void setMotorRPM(Integer motorRPM) {
        this.motorRPM = motorRPM;
    }

    public Integer getPressure1() {
        return pressure1;
    }

    public void setPressure1(Integer pressure1) {
        this.pressure1 = pressure1;
    }

    public Integer getPressure2() {
        return pressure2;
    }

    public void setPressure2(Integer pressure2) {
        this.pressure2 = pressure2;
    }

    public Double getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(Double temperature1) {
        this.temperature1 = temperature1;
    }

    public Double getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(Double temperature2) {
        this.temperature2 = temperature2;
    }

}
