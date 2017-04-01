package bob.sun.bender.controller;

import com.huami.mibandscan.MiBandScanResult;
import com.huami.mibandscan.MiBandScanStatus;

import bob.sun.bender.model.MiBandDevice;

/**
 * Created by bmy001 on 西暦17/04/01.
 */

public interface OnBandFoundListener {
    void onData(MiBandDevice device);
    void onStatus(MiBandScanStatus scanStatus);
}
