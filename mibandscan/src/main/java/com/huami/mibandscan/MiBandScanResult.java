package com.huami.mibandscan;

public class MiBandScanResult {

    private String bandMac;
    private String openId;
    private int rssi;
    private int steps;
    private int sleep;
    private long timeStamp;

    public MiBandScanResult(String bandMac,
                            String openId,
                            int rssi,
                            int steps,
                            int sleep,
                            long timeStamp) {
        this.bandMac = bandMac;
        this.openId = openId;
        this.rssi = rssi;
        this.steps = steps;
        this.sleep = sleep;
        this.timeStamp = timeStamp;
    }

    public String getBandMac() {
        return bandMac;
    }

    public int getSteps() {
        return steps;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getSleep() {
        return sleep;
    }

    public void setBandMac(String bandMac) {
        this.bandMac = bandMac;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
