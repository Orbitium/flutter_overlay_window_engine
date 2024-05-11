package me.orbitium.flutter_overlay_window_engine.service;

import android.content.Context;
import android.os.IBinder;

import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;

public interface OverlayServiceManager {

    void startService(Context context);

    void stopService(Context context);

    void onServiceStarted(IBinder serviceBinder);

    void onServiceKilled();

    void queueOverlayWindow(OverlayWindow overlayWindow);

    void dequeueOverlayWindow(OverlayWindow overlayWindow);

    void attachOverlayWindowToService(OverlayWindow overlayWindow);

    void detachOverlayWindowFromService(OverlayWindow overlayWindow);

}
