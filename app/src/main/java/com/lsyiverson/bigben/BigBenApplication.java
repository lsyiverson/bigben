package com.lsyiverson.bigben;

import android.app.Application;

import com.lsyiverson.bigben.rest.RestClient;

public class BigBenApplication extends Application {
    private static BigBenApplication instance;

    public static final String HTTP_PREFIX = "http://";

    private String hostIp = "127.0.0.1";

    public static final int PORT = 3000;

    private RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        restClient = new RestClient();
    }

    public static BigBenApplication getInstance() {
        return instance;
    }

    public String getUrl() {
        return HTTP_PREFIX + hostIp + ":" + PORT;
    }

    public void setHostIp(String ip) {
        hostIp = ip;
        resetRestClient();
    }

    public String getHostIp() {
        return hostIp;
    }

    public RestClient getRestClient() {
        return restClient;
    }

    private void resetRestClient() {
        restClient = new RestClient();
    }
}
