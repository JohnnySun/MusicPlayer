package com.huami.mibandscan.FormBean;

import java.util.List;

public class opEventsResponseBean {

    /**
     * mac : 880f10eaf71b
     * details : {"deviceId":null,"type":null,"openId":"880f10eaf71b"}
     */

    private List<DevicesBean> devices;

    public List<DevicesBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DevicesBean> devices) {
        this.devices = devices;
    }

    public static class DevicesBean {
        private String mac;
        /**
         * deviceId : null
         * type : null
         * openId : 880f10eaf71b
         */

        private DetailsBean details;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public DetailsBean getDetails() {
            return details;
        }

        public void setDetails(DetailsBean details) {
            this.details = details;
        }

        public static class DetailsBean {
            private Object deviceId;
            private Object type;
            private String openId;

            public Object getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(Object deviceId) {
                this.deviceId = deviceId;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }

            public String getOpenId() {
                return openId;
            }

            public void setOpenId(String openId) {
                this.openId = openId;
            }
        }
    }
}
