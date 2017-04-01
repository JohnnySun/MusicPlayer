package bob.sun.bender.model;

import android.util.Log;

import com.huami.mibandscan.MiBandScan;
import com.huami.mibandscan.MiBandScanCallBack;
import com.huami.mibandscan.MiBandScanResult;
import com.huami.mibandscan.MiBandScanStatus;

import java.lang.ref.WeakReference;

import bob.sun.bender.controller.OnBandFoundListener;

/**
 *
 * Created by bmy001 on 西暦17/04/01.
 */

public class MIBandSearchInstance {
    private static MIBandSearchInstance INSTANCE;
    private static final Object LOCK = new Object();

    private MiBandScanRepo scanRepo;
    private MiBandScan miBandScan;
    private boolean startScan;
    private WeakReference<OnBandFoundListener> weakBandFoundCallback;


    public static MIBandSearchInstance getInstance() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new MIBandSearchInstance();
            }
            return INSTANCE;
        }
    }

    private MIBandSearchInstance() {
        scanRepo = new MiBandScanRepo(LOCK);
        startScan = false;
        weakBandFoundCallback = new WeakReference<>(null);
        miBandScan = new MiBandScan(new MiBandScanCallBack() {
            @Override
            public void onData(MiBandScanResult miBandScanResult) {
                Log.i("MiBandScan", "BandFound");
                MiBandDevice device = scanRepo.addDevice(miBandScanResult);
                // 弱引用如果不存在则意味着回掉已经被释放，这边应当直接停止搜索
                if (weakBandFoundCallback.get() != null) {
                    weakBandFoundCallback.get().onData(device);
                } else {
                    scanRepo.cleanAll();
                    stopScan();
                }
            }

            @Override
            public void onStatus(MiBandScanStatus miBandScanStatus) {
                if (weakBandFoundCallback.get() != null) {
                    weakBandFoundCallback.get().onStatus(miBandScanStatus);
                }
            }
        });

        String[] bandlist = {};
        miBandScan.config(bandlist);
    }

    public boolean startScan(OnBandFoundListener callback) {
        if (!startScan) {
            Log.i("MiBandScan", "StartScan");
            weakBandFoundCallback = new WeakReference<>(callback);
            startScan = miBandScan.startScan(true);
        }
        return startScan;
    }

    public void stopScan() {
        Log.i("MiBandScan", "StopScan");
        miBandScan.startScan(false);
        startScan = false;
    }

    public boolean isStartScan() {
        return startScan;
    }
}
