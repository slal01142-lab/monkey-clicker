package com.monkey.clicker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;

public class ClickService extends AccessibilityService {

    public static ClickService instance;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean running = false;
    private float x, y;
    private long interval = 100;

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (!running) return;
            tap();
            handler.postDelayed(this, interval);
        }
    };

    public void start(float x, float y, long intervalMs) {
        this.x = x;
        this.y = y;
        this.interval = Math.max(50, intervalMs);
        if (running) return;
        running = true;
        handler.post(task);
    }

    public void stop() {
        running = false;
        handler.removeCallbacks(task);
    }

    public boolean isRunning() {
        return running;
    }

    private void tap() {
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.StrokeDescription stroke =
                new GestureDescription.StrokeDescription(path, 0, 40);
        GestureDescription gesture =
                new GestureDescription.Builder().addStroke(stroke).build();
        dispatchGesture(gesture, null, null);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop();
        instance = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
