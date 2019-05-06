package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

public class WOD {

    private String metcon;
    private String gymnastics;
    private String oly;
    private String power;
    private String running;

    public WOD(String metcon,String gymnastics,String oly,String power,String running) {
        this.metcon = metcon;
        this.gymnastics = gymnastics;
        this.oly = oly;
        this.power = power;
        this.running = running;
    }

    public String getMetcon() {
        return metcon;
    }

    public void setMetcon(String metcon) {
        this.metcon = metcon;
    }

    public String getGymnastics() {
        return gymnastics;
    }

    public void setGymnastics(String gymnastics) {
        this.gymnastics = gymnastics;
    }

    public String getOly() {
        return oly;
    }

    public void setOly(String oly) {
        this.oly = oly;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }
}
