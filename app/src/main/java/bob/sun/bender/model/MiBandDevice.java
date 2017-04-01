package bob.sun.bender.model;

import android.bluetooth.BluetoothDevice;

import com.huami.mibandscan.MiBandScanResult;

/**
 * Created by bmy001 on 西暦17/04/01.
 */

public class MiBandDevice {

    String bandMac;
    int rssi;
    BluetoothDevice device;
    MiBandScanResult result;

    public MiBandScanResult getResult() {
        return result;
    }

    public void setResult(MiBandScanResult result) {
        this.result = result;
    }

    public String getBandMac() {
        return bandMac;
    }

    public void setBandMac(String bandMac) {
        this.bandMac = bandMac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
