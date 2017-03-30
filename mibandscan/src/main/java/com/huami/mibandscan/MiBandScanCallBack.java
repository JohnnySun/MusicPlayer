package com.huami.mibandscan;

public interface MiBandScanCallBack {

    void onData(MiBandScanResult miBandScanResult);

    void onStatus(MiBandScanStatus miBandScanStatus);

}
