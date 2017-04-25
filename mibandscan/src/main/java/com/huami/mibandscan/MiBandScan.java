package com.huami.mibandscan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.bluetooth.le.ScanSettings.MATCH_MODE_STICKY;
import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

/**
 * 此类用于用于初始化小米手环扫描器
 */
public class MiBandScan {

    @SuppressWarnings("unused")
    private static final String TAG = "MiBandScan";
    @SuppressWarnings("FieldCanBeLocal")
    private MiBandScanConfig miBandScanConfig = null;
    private MiBandAdapter mMiBandAdapter = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private MiBandScanCallBack miBandScanCallBack = null;
    private AuthService authService;
    private boolean mScanning = false;

    /**
     * 构造函数，用于初始化小米手环扫描器
     * @param miBandScanCallBack 用户的回调对象
     */
    public MiBandScan(MiBandScanCallBack miBandScanCallBack) {
        this.miBandScanCallBack = miBandScanCallBack;
    }

    public boolean config(String[] accessOpenId) {
        // 检查配置是否有误
        if ( miBandScanCallBack != null) {
            miBandScanCallBack.onStatus(MiBandScanStatus.CONFIG_SUCCESS);
        }

        this.miBandScanConfig = new MiBandScanConfig(accessOpenId);

        authService = new AuthService(miBandScanConfig, miBandScanCallBack);
        this.mMiBandAdapter = new MiBandAdapter(miBandScanConfig, miBandScanCallBack, authService);
        // get the default bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            miBandScanCallBack.onStatus(MiBandScanStatus.NOT_SUPPORT_BLE);
        }

        //authService.auth();
        //authService.checkOpenId();
        return true;
    }

    /**
     * 开始或轻停止扫描手环
     * @param enable 当值为真时开始扫描手环，值为假时停止扫描手环
     */
    public boolean startScan(final boolean enable) {
        if (miBandScanConfig == null || mBluetoothAdapter == null) {
            return false;
        }

        if (isBluetoothEnabled() && enable) {
            mScanning = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.getBluetoothLeScanner().startScan(null, getScanSettings(), mMiBandAdapter.getScanCallback());
                miBandScanCallBack.onStatus(MiBandScanStatus.START_SUCCESS);
                return true;
            } else {
                // noinspection all
                if (mBluetoothAdapter.startLeScan(mMiBandAdapter)) {
                    miBandScanCallBack.onStatus(MiBandScanStatus.START_SUCCESS);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            mScanning = false;
            if (isBluetoothEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(mMiBandAdapter.getScanCallback());
                } else {
                    // noinspection all
                    mBluetoothAdapter.stopLeScan(mMiBandAdapter);
                }
            }
            miBandScanCallBack.onStatus(MiBandScanStatus.STOP_SUCCESS);
            return true;
        }
    }

    public boolean isScanning() {
        return mScanning;
    }

    public String[] getAccessOpenId() {
        List<String> accessOpenIdList = new ArrayList<>();
        for (HashMap<Integer, String> item : miBandScanConfig.getAccessList()) {
            accessOpenIdList.add(item.get(MiBandScanConfig.ACCESS_OPENID));
        }

        String openIdString = TextUtils.join(",", accessOpenIdList);
        return TextUtils.split(openIdString, ",");
    }

    public void setAccessOpenId(String[] accessOpenId) {
        miBandScanConfig.setTempAccessOpenId(accessOpenId);
        authService.checkOpenId();
    }

    @SuppressWarnings("unused")
    public boolean isOnService() {
        return authService != null && authService.isOnService();
    }

    private boolean isBluetoothEnabled() {
        // noinspection ResourceType
        boolean status = mBluetoothAdapter.isEnabled();
        if (!status) {
            miBandScanCallBack.onStatus(MiBandScanStatus.BLUETOOTH_DISABLED);
        }

        return status;
    }

    /**
     * BLE 5.0的新api需要的ScanSetting
     * @return ScanSettings
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings getScanSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ScanSettings.Builder()
                    .setScanMode(SCAN_MODE_LOW_LATENCY)
                    .setMatchMode(MATCH_MODE_STICKY)
                    .build();
        } else {
            return new ScanSettings.Builder()
                    .setScanMode(SCAN_MODE_LOW_LATENCY)
                    .build();
        }
    }

}
