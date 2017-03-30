package com.huami.mibandscan.FormBean;

import android.util.Log;

import java.util.List;

public class opEventsBean {

    /**
     * data : [{"advertiser":[{"mac":"880f10eaf71b","timestamp":1702215104},{"mac":"e9d4205c391e","timestamp":1702215104},{"mac":"880f10eb1f00","timestamp":1702215104}],"scanner":"afd47591c94f4bb6b9ef06f4a32d57be"}]
     * eventId : 635b1826-c535-44af-b4a2-a5cb20acc61b
     * type : swipe
     */

    private String eventId;
    private String type;
    /**
     * advertiser : [{"mac":"880f10eaf71b","timestamp":1702215104},{"mac":"e9d4205c391e","timestamp":1702215104},{"mac":"880f10eb1f00","timestamp":1702215104}]
     * scanner : afd47591c94f4bb6b9ef06f4a32d57be
     */

    private List<DataBean> data;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String scanner;
        /**
         * mac : 880f10eaf71b
         * timestamp : 1702215104
         */

        private List<AdvertiserBean> advertiser;

        public String getScanner() {
            return scanner;
        }

        public void setScanner(String scanner) {
            this.scanner = scanner;
        }

        public List<AdvertiserBean> getAdvertiser() {
            return advertiser;
        }

        public void setAdvertiser(List<AdvertiserBean> advertiser) {
            this.advertiser = advertiser;
        }

        public static class AdvertiserBean {
            private String mac;
            private Long timestamp;

            public AdvertiserBean() {
                setTimestamp();
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public Long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp() {
                Log.d("TimeStamp", Long.toString(System.currentTimeMillis()));
                this.timestamp = (System.currentTimeMillis() / 1000);
            }
        }
    }
}
