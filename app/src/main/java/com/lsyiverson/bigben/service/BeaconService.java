package com.lsyiverson.bigben.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.UUID;

public class BeaconService extends Service implements BeaconConsumer {
    public static final String TAG = BeaconService.class.getSimpleName();
    private BeaconManager beaconManager;

    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    public static final String FILTER_UUID_1 = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0";
    public static final String FILTER_UUID_2 = "F2C56DB5-DFFB-48D2-B060-D0F5A71096D8";

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.e(TAG, "uuid of region:" + region);
            }

            @Override
            public void didExitRegion(Region region) {
                Log.e(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.e(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            Identifier id1 = Identifier.fromUuid(UUID.fromString(FILTER_UUID_1));
            Identifier id2 = Identifier.fromUuid(UUID.fromString(FILTER_UUID_2));
            Log.d(TAG, id1.toUuid().toString());
            Log.d(TAG, id2.toUuid().toString());
            beaconManager.startMonitoringBeaconsInRegion(new Region("beacon1", id1, null, null));
            beaconManager.startMonitoringBeaconsInRegion(new Region("beacon2", id2, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
