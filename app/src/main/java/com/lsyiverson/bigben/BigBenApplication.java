package com.lsyiverson.bigben;

import android.app.Application;

public class BigBenApplication extends Application {
    private static BigBenApplication instance;

    public static final String HTTP_PREFIX = "http://";

    private String hostIp = "127.0.0.1";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BigBenApplication getInstance() {
        return instance;
    }

    public String getUrl() {
        return HTTP_PREFIX + hostIp;
    }

    public void setHostIp(String ip) {
        hostIp = ip;
    }
}
