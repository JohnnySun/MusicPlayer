package com.huami.mibandscan.FormBean;


public class BindingsBean {

    /**
     * applicationId : string
     * createdTime : 0
     * deviceId : string
     * deviceSource : 0
     * deviceType : 0
     * macAddress : string
     * openId : string
     */

    private String applicationId;
    private int createdTime;
    private String deviceId;
    private int deviceSource;
    private int deviceType;
    private String macAddress;
    private String openId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public int getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(int createdTime) {
        this.createdTime = createdTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceSource() {
        return deviceSource;
    }

    public void setDeviceSource(int deviceSource) {
        this.deviceSource = deviceSource;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
