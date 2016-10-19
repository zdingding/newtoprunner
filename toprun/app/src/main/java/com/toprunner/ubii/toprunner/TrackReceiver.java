package com.toprunner.ubii.toprunner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.toprunner.ubii.toprunner.fragment.RoadFragment;

public class TrackReceiver extends BroadcastReceiver {

    @SuppressLint("Wakelock")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            System.out.println("screen off,acquire wake lock!");
            if (null != RoadFragment.wakeLock && !(RoadFragment.wakeLock.isHeld())) {
                RoadFragment.wakeLock.acquire();
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            System.out.println("screen on,release wake lock!");
            if (null != RoadFragment.wakeLock && RoadFragment.wakeLock.isHeld()) {
                RoadFragment.wakeLock.release();
            }
        } else if ("com.baidu.trace.action.GPS_STATUS".equals(action)) {
            int statusCode = intent.getIntExtra("statusCode", 0);
            System.out.println("GPS状态码 : " + statusCode);
            String statusMessage = intent.getStringExtra("statusMessage");
            Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show();
        }
    }

}
