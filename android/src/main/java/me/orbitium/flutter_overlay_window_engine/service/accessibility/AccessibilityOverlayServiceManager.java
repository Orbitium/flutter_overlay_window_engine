package me.orbitium.flutter_overlay_window_engine.service.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import me.orbitium.flutter_overlay_window_engine.FlutterOverlayWindowEnginePlugin;
import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayWindowManager;
import me.orbitium.flutter_overlay_window_engine.service.OverlayServiceManager;
import me.orbitium.flutter_overlay_window_engine.service.application.ApplicationOverlayService;

public class AccessibilityOverlayServiceManager implements OverlayServiceManager {
    private AccessibilityOverlayService accessibilityService;
    private List<String> overlayWindowsAttachedToService;
    private final List<String> queue;

    public AccessibilityOverlayServiceManager() {
        queue = new ArrayList<>();
    }

    @Override
    public void startService(Context context) {
        // Can't start an accessibility service, android itself starts the service
        // once all permissions granted
    }

    @Override
    public void stopService(Context context) {
        // Can't stop service an accessibility service either due android sided stuff
    }

//    public void onAccessibilityServiceConnected(AccessibilityOverlayService service) {
//        for (OverlayWindow overlayWindow : queue) {
//            attachNewOverlayWindowToAccessibilityService(service, overlayWindow);
//        }
//
//        // DON'T delete queue, when service is determinated; queue back the windows
//        // TODO: Users should be able to select if they want to queue the windows on service disconnect
//        queue.clear();
//    }


    @Override
    public void onServiceStarted(IBinder serviceBinder) {
        overlayWindowsAttachedToService = new ArrayList<>();

        AccessibilityOverlayService.AccessibilityOverlayServiceBinder binder = (AccessibilityOverlayService.AccessibilityOverlayServiceBinder) serviceBinder;
        accessibilityService = binder.getService();

        for (String overlayWindowID : queue) {
            OverlayWindow overlayWindow = OverlayWindowManager.getInstance().getOverlayWindow(overlayWindowID);
            attachOverlayWindowToService(overlayWindow);
        }

        queue.clear();
    }

    @Override
    public void onServiceKilled() {
        for (String overlayWindowID : overlayWindowsAttachedToService) {
            OverlayWindow overlayWindow = OverlayWindowManager.getInstance().getOverlayWindow(overlayWindowID);
            detachOverlayWindowFromService(overlayWindow);
        }

        accessibilityService = null;
    }


    public void attachOverlayWindowToService(OverlayWindow overlayWindow) {
        if (accessibilityService == null) {
            queueOverlayWindow(overlayWindow);
            return;
        }

        if (overlayWindowsAttachedToService.contains(overlayWindow.getWindowID()))
            return;

        accessibilityService.addOverlayWindow(overlayWindow);
        overlayWindowsAttachedToService.add(overlayWindow.getWindowID());
    }


    public void detachOverlayWindowFromService(OverlayWindow overlayWindow) {
        if (accessibilityService == null) {
            dequeueOverlayWindow(overlayWindow);
            return;
        }

        if (!overlayWindowsAttachedToService.contains(overlayWindow.getWindowID()))
            return;

        accessibilityService.removeOverlayWindow(overlayWindow);
        overlayWindowsAttachedToService.remove(overlayWindow.getWindowID());
    }

    public void queueOverlayWindow(OverlayWindow overlayWindow) {
        if (queue.contains(overlayWindow.getWindowID()))
            return;

        queue.add(overlayWindow.getWindowID());
    }

    public void dequeueOverlayWindow(OverlayWindow overlayWindow) {
        if (!queue.contains(overlayWindow.getWindowID()))
            return;

        queue.remove(overlayWindow.getWindowID());
    }
}
