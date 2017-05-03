package bob.sun.bender.model;

import com.huami.mibandscan.MiBandScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmy001 on 西暦17/04/01.
 */

public class MiBandScanRepo {
    private List<MiBandDevice> bandList;
    private Object LOCK;

    MiBandScanRepo(Object LOCK) {
        this.LOCK = LOCK;
        bandList = new ArrayList<>();
    }

    public MiBandDevice addDevice(MiBandScanResult result) {
        MiBandDevice device = new MiBandDevice();
        device.setBandMac(result.getBandMac());
        device.setRssi(result.getRssi());
        device.setResult(result);
        synchronized (LOCK) {
            bandList.add(device);
        }
        return device;
    }

    public void cleanAll() {
        synchronized (LOCK) {
            bandList.clear();
        }
    }

    public void removeDevice(String bandMac) {
        synchronized (LOCK) {
            for (MiBandDevice item : bandList) {
                if (item.getBandMac().equals(bandMac)) {
                    bandList.remove(item);
                }
            }
        }
    }
}
