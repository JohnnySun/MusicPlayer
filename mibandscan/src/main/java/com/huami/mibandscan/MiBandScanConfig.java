package com.huami.mibandscan;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 此类用于配置小米手环扫描器的一些过滤参数
 */
class MiBandScanConfig {

    public static final int ACCESS_MAC = 1;
    public static final int ACCESS_OPENID = 2;
    private String hboxId = "";
    private String userName = "";
    private String password = "";
    private String[] tempAccessOpenId = null;
    private List<HashMap<Integer, String>> accessList;

    MiBandScanConfig(String[] openId) {
        //this.tempAccessOpenId = openId;
        accessList = new ArrayList<>();
        for(String itemMac : openId) {
            @SuppressLint("UseSparseArrays")
            HashMap<Integer, String> item = new HashMap<>();
            item.put(MiBandScanConfig.ACCESS_MAC, itemMac);
            item.put(MiBandScanConfig.ACCESS_OPENID, itemMac);
            accessList.add(item);
        }
    }

    List<HashMap<Integer, String>> getAccessList() {
        return accessList;
    }

    void setAccessList(List<HashMap<Integer, String>> list) {
        this.accessList = list;
    }


    void setTempAccessOpenId(String[] accessOpenId) {
        this.tempAccessOpenId = accessOpenId;
    }

    String[] getTempAccessOpenId() {
        return tempAccessOpenId;
    }


    @SuppressWarnings("all")
    String getHboxId() {
        return hboxId;
    }

    @SuppressWarnings("all")
    String getUserName() {
        return userName;
    }

    @SuppressWarnings("all")
    String getPassword() {
        return password;
    }

}
