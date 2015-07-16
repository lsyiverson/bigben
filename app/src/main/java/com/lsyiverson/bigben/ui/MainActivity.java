package com.lsyiverson.bigben.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lsyiverson.bigben.BigBenApplication;
import com.lsyiverson.bigben.R;
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
        if (!"127.0.0.1".equals(BigBenApplication.getInstance().getHostIp())) {
            hostIp.setText(BigBenApplication.getInstance().getHostIp());
        }
    }

    @OnClick(R.id.service_button)
    void onServiceButtonClicked() {
        if (isServiceRunning(BeaconService.class)) {
            stopService(new Intent(this, BeaconService.class));
            serviceButton.setText(R.string.start_service);
        } else {
            if (TextUtils.isEmpty(hostIp.getText())) {
                Toast.makeText(this, R.string.host_empty_tips, Toast.LENGTH_SHORT).show();
            } else {
                BigBenApplication.getInstance().setHostIp(hostIp.getText().toString());
                startService(new Intent(this, BeaconService.class));
                serviceButton.setText(R.string.stop_service);
            }
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
