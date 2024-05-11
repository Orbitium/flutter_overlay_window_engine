package me.orbitium.flutter_overlay_window_engine.service;

import android.content.Context;

import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayWindowManager;
import me.orbitium.flutter_overlay_window_engine.service.accessibility.AccessibilityOverlayServiceManager;
import me.orbitium.flutter_overlay_window_engine.service.application.ApplicationOverlayServiceManager;

public class ServiceManager {

    /* TODO: Update new Service system to be compatible with new 2 service classes
     * implement foreground service
     * REMOVE STATIC INSTANCES FROM BOTH SUB-MANAGER SERVICE CLASSES
     * THEY MUST BE *ONLY* ACCESSIBLE TRU THIS CLASS
     */

    private static volatile ServiceManager instance;
    private final ApplicationOverlayServiceManager overlayManager;
    private final AccessibilityOverlayServiceManager accessibilityServiceManager;

    public ServiceManager() {
        overlayManager = new ApplicationOverlayServiceManager();
        accessibilityServiceManager = new AccessibilityOverlayServiceManager();
    }

    public void startApplicationOverlayService(Context context) {
        overlayManager.startService(context);
    }

    public void stopApplicationService(Context context) {
        overlayManager.stopService(context);
    }

    public void attachOverlayWindow(String overlayWindowID) {
        OverlayWindow overlayWindow = OverlayWindowManager.getInstance().getOverlayWindow(overlayWindowID);
        if (overlayWindow == null)
            throw new NullPointerException("Can't attach overlay window to service");

        if (overlayWindow.isAccessibilityWindow())
            accessibilityServiceManager.attachOverlayWindowToService(overlayWindow);
        else
            overlayManager.attachOverlayWindowToService(overlayWindow);

        overlayWindow.setAttachedToService(true);
    }

    public void detachOverlayWindow(String overlayWindowID) {
        OverlayWindow overlayWindow = OverlayWindowManager.getInstance().getOverlayWindow(overlayWindowID);

        if (overlayWindow == null)
            throw new NullPointerException("Can't detach overlay window from service");


        if (overlayWindow.isAccessibilityWindow())
            accessibilityServiceManager.detachOverlayWindowFromService(overlayWindow);
        else
            overlayManager.detachOverlayWindowFromService(overlayWindow);

        overlayWindow.setAttachedToService(false);
    }

    public AccessibilityOverlayServiceManager getAccessibilityServiceManager() {
        return accessibilityServiceManager;
    }

    public static ServiceManager getInstance() {
        try {
            if (instance == null) {
                instance = new ServiceManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

}
