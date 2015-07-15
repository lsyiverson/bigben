package com.lsyiverson.bigben.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.lsyiverson.bigben.BigBenApplication;
import com.lsyiverson.bigben.R;
import com.lsyiverson.bigben.model.BeaconInfo;
import com.lsyiverson.bigben.ui.AdContentActivity;
import com.lsyiverson.bigben.utils.Constants;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
                UUID uuid = region.getId1().toUuid();
                Log.e(TAG, "uuid of region:" + region);
                BigBenApplication.getInstance().getRestClient().getBeaconDataService().getBeaconInfo(uuid.toString(), new Callback<BeaconInfo>() {
                    @Override
                    public void success(BeaconInfo beaconInfo, Response response) {
                        sendNotification(beaconInfo);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, error.toString());
                    }
                });
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

    private void sendNotification(BeaconInfo beaconInfo) {
        Intent adIntent = new Intent(this, AdContentActivity.class);
        adIntent.putExtra(Constants.BEACON_INFO, beaconInfo);
        adIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(beaconInfo.getTitle())
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(PendingIntent.getActivity(this, 0, adIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
