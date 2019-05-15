package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

public class WOD {

    private String metcon;
    private String metconStatus;
    private String gymnastics;
    private String gymnasticsStatus;
    private String oly;
    private String olyStatus;
    private String power;
    private String powerStatus;
    private String running;
    private String runningStatus;

    public WOD(String metcon, String metconStatus, String gymnastics, String gymnasticsStatus,String oly,String olyStatus, String power,String powerStatus,String running, String runningStatus) {
        this.metcon = metcon;
        this.metconStatus = metconStatus;
        this.gymnastics = gymnastics;
        this.gymnasticsStatus = gymnasticsStatus;
        this.oly = oly;
        this.olyStatus = olyStatus;
        this.power = power;
        this.powerStatus = powerStatus;
        this.running = running;
        this.runningStatus = runningStatus;
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
    public String getMetconStatus() {
        return metconStatus;
    }

    public void setMetconStatus(String metconStatus) {
        this.metconStatus = metconStatus;
    }

    public String getGymnasticsStatus() {
        return gymnasticsStatus;
    }

    public void setGymnasticsStatus(String gymnasticsStatus) {
        this.gymnasticsStatus = gymnasticsStatus;
    }

    public String getOlyStatus() {
        return olyStatus;
    }

    public void setOlyStatus(String olyStatus) {
        this.olyStatus = olyStatus;
    }

    public String getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(String powerStatus) {
        this.powerStatus = powerStatus;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }
}
