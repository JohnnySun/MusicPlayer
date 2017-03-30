package com.huami.mibandscan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import com.huami.mibandscan.AuthService;
import com.huami.mibandscan.MiBandScanCallBack;
import com.huami.mibandscan.MiBandScanConfig;
import com.huami.mibandscan.MiBandScanResult;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * 此类用于小米手环的搜索，发现，过滤等功能。
 */
class MiBandAdapter implements BluetoothAdapter.LeScanCallback {

    private MiBandScanCallBack callback;
    private MiBandScanConfig miBandScanConfig;
    //private AuthService authService;
    private int onDataCount = 0;
    private ScanCallback newLeScanCallback = null;

    /**
     * 用于初始化MibandAdapter，实现蓝牙扫描的消息回调
     * @param callback MiBandScanResult， 用于将扫描到的信息发送到用户回调
     */
    public MiBandAdapter(MiBandScanConfig miBandScanConfig,
                         MiBandScanCallBack callback,
                         AuthService authService) {
        this.miBandScanConfig = miBandScanConfig;
        this.callback = callback;
        //this.authService = authService;
    }

    // Return true if Manufacturer ID = 0x0157
    private boolean isHuamiDevice(byte[] devicesScanRecord) {
        return devicesScanRecord[5] == 0x57 && devicesScanRecord[6] == 0x01;
    }

    /**
     * 返回当前sdk是否提供服务的状态
     */
    private boolean isOnService() {
        return true;
    }

    @Override
    public void onLeScan(final BluetoothDevice device,
                         final int rssi,
                         final byte[] scanRecord) {
        // 每3600次发现设备发送一次心跳包
        /*if (onDataCount >= 3600) {
            authService.auth();
            onDataCount = 0;
        }*/

        if (isHuamiDevice(scanRecord) && isOnService()) {
            if (checkAccessBands(device)) {
                // noinspection ResourceType
                if (device.getName().contains("MI")) {
                    MiBandScanResult miBandScanResult = new MiBandScanResult(device.getAddress(),
                            findIdByMac(device.getAddress()),
                            rssi,
                            getStep(getScanResponse(scanRecord)),
                            getUserSleepStatus(getRegAdvData(scanRecord)),
                            System.currentTimeMillis());
                    callback.onData(miBandScanResult);
                    //onDataCount++;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    ScanCallback getScanCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 如果没有初始化过callback， 则初始化，如果已经初始化了，直接返回即可
            if (newLeScanCallback == null) {
                newLeScanCallback = new ScanCallback() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        try {
                            BluetoothDevice device = result.getDevice();
                            byte[] scanRecord = result.getScanRecord().getBytes();
                            if (isHuamiDevice(scanRecord) && isOnService()) {
                                if (checkAccessBands(device)) {
                                    // noinspection ResourceType
                                    if (device.getName().contains("MI")) {
                                        MiBandScanResult miBandScanResult =
                                                new MiBandScanResult(device.getAddress(),
                                                        findIdByMac(device.getAddress()),
                                                        result.getRssi(),
                                                        getStep(getScanResponse(scanRecord)),
                                                        getUserSleepStatus(getRegAdvData(scanRecord)),
                                                        System.currentTimeMillis());
                                        callback.onData(miBandScanResult);
                                        //onDataCount++;
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        }
        return newLeScanCallback;
    }

    // 检查是否在白名单中
    private boolean checkAccessBands(BluetoothDevice device) {
        if (miBandScanConfig.getAccessList() != null) {
            for (HashMap<Integer, String> item : miBandScanConfig.getAccessList()) {
                String mac = item.get(MiBandScanConfig.ACCESS_MAC);
                String address = convertAddress(device.getAddress());
                if (mac.equals(address)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String findIdByMac(String address) {
        if (miBandScanConfig.getAccessList() != null) {
            for (HashMap<Integer, String> item : miBandScanConfig.getAccessList()) {
                String mac = item.get(MiBandScanConfig.ACCESS_MAC);
                if (mac.equals(convertAddress(address))) {
                    return item.get(MiBandScanConfig.ACCESS_OPENID);
                }
            }
        }
        return "";
    }

    // 将mac中的帽号去掉，并转换为小写
    private String convertAddress(String address) {
        String newAddress = address.replace(":", "");
        return newAddress.toLowerCase();
    }

    private byte[] getRegAdvData(byte[] scanRecord) {
        byte[] regAdvData;
        if (scanRecord[2] == 0x06 || scanRecord[2] ==  0x05) {
            regAdvData = new byte[31];
            System.arraycopy(scanRecord, 0, regAdvData, 0, 31);
        } else if (scanRecord[2] == 0x04) {
            regAdvData = new byte[31];
            System.arraycopy(scanRecord, 0, regAdvData, 0, 25);
        } else {
            regAdvData = scanRecord;
        }
        return regAdvData;
    }

    private byte[] getScanResponse(byte[] scanRecord) {
        Log.d("ScanRecord", bytesToHex(scanRecord));
        byte[] scanResponse;
        int offset = 0;
        byte[] len = new byte[2];
        len[0] = 0x00;
        len[1] = scanRecord[offset];
        short lenght = ByteBuffer.wrap(len).getShort();
        offset += lenght + 1;
        len[0] = 0x00;
        len[1] = scanRecord[offset];
        lenght = ByteBuffer.wrap(len).getShort();
        offset += lenght + 1;
        scanResponse = new byte[62 - offset];

        // 以上部分都是为了偏移掉adv data，得到scanResponse
        System.arraycopy(scanRecord, offset, scanResponse, 0, 62 - offset);

        return scanResponse;
    }

    private int getUserSleepStatus(byte[] regAdvRecord) {
        byte[] byteSleepStatus = new byte[2];
        byteSleepStatus[0] = 0x00;
        byteSleepStatus[1] = regAdvRecord[24];
        short intSleepStatus = ByteBuffer.wrap(byteSleepStatus).getShort();
        switch (intSleepStatus) {
            case 0x02:
                return  0;
            case 0x01:
                return  1;
            default:
                return  -1;
        }
    }

    // 获取用户步数
    private int getStep(byte[] scanResponse) {
        try {
            int offset = 0;
            byte[] len = new byte[2];
            len[0] = 0x00;
            len[1] = scanResponse[offset];
            short lenght = ByteBuffer.wrap(len).getShort();
            offset += lenght + 1;
            len[0] = 0x00;
            len[1] = scanResponse[offset];
            lenght = ByteBuffer.wrap(len).getShort();
            len[0] = 0x00;
            offset += lenght + 1;
            len[1] = scanResponse[offset];
            lenght = ByteBuffer.wrap(len).getShort();
            offset += 4;
            int stepsLenght = lenght - 3;
            byte[] arr  = new byte[stepsLenght];
            for (int i = stepsLenght - 1; i >= 0; i--) {
                arr[i] = scanResponse[offset + stepsLenght - 1 - i];
            }

            return ByteBuffer.wrap(arr).getInt();
        } catch (Exception e) {
            e.printStackTrace();

            return  -1;
        }
    }

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    @SuppressWarnings("unused")
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
