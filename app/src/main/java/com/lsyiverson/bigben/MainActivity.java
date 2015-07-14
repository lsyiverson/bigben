package com.lsyiverson.bigben;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.lsyiverson.bigben.service.BeaconService;

import butterknife.Bind;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    @Bind(R.id.host_ip)
    EditText hostIp;

    @Bind(R.id.service_button)
    Button serviceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isServiceRunning(BeaconService.class)) {
            serviceButton.setText(R.string.stop_service);
        } else {
            serviceButton.setText(R.string.start_service);
        }
    }

    @OnClick(R.id.service_button)
    void onServiceButtonClicked() {
        if (isServiceRunning(BeaconService.class)) {
            stopService(new Intent(this, BeaconService.class));
            serviceButton.setText(R.string.start_service);
        } else {
            startService(new Intent(this, BeaconService.class));
            serviceButton.setText(R.string.stop_service);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
