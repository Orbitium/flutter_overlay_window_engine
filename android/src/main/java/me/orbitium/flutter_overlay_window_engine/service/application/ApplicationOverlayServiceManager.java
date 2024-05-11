package me.orbitium.flutter_overlay_window_engine.service.application;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import me.orbitium.flutter_overlay_window_engine.FlutterOverlayWindowEnginePlugin;
import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayPosition;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlaySize;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayWindowManager;
import me.orbitium.flutter_overlay_window_engine.service.OverlayServiceManager;

public class ApplicationOverlayServiceManager implements OverlayServiceManager {
    //    private static volatile ApplicationOverlayServiceManager instance;
    private ApplicationOverlayService applicationOverlayService;
    private List<String> overlayWindowsAttachedToService;
    private final List<String> queue;
//    private boolean isServiceRunning = false;


    @Override
    public void startService(Context context) {
        if (isMyServiceRunning(context, ApplicationOverlayService.class))
            return;

        System.out.println("Starting overlay service");
        Intent intent = new Intent(context, ApplicationOverlayService.class);
        // TODO: implement ability to switch between foreground/background service
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void stopService(Context context) {
        // If service is null, return
        if (applicationOverlayService == null)
            return;

        for (String overlayWindowID : overlayWindowsAttachedToService) {
            OverlayWindow overlayWindow = OverlayWindowManager.getInstance().getOverlayWindow(overlayWindowID);
            detachOverlayWindowFromService(overlayWindow);
        }

        context.unbindService(serviceConnection);
        onServiceKilled();
    }

    @Override
    public void onServiceStarted(IBinder serviceBinder) {
        overlayWindowsAttachedToService = new ArrayList<>();

        ApplicationOverlayService.OverlayServiceBinder binder = (ApplicationOverlayService.OverlayServiceBinder) serviceBinder;
        applicationOverlayService = binder.getService();

        for (String overlayWindowID : queue) {
            OverlayWindow overlayWindow = OverlayWindowManager.getInstance().getOverlayWindow(overlayWindowID);
            attachOverlayWindowToService(overlayWindow);
        }

        queue.clear();
    }

    @Override
    public void onServiceKilled() {
        applicationOverlayService = null;
    }


    public ApplicationOverlayServiceManager() {
        queue = new ArrayList<>();
//        instance = this;
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            onServiceStarted(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            onServiceKilled();
        }
    };

    public void attachOverlayWindowToService(OverlayWindow overlayWindow) {
        if (applicationOverlayService == null) {
            queueOverlayWindow(overlayWindow);
            return;
        }

        if (overlayWindowsAttachedToService.contains(overlayWindow.getWindowID()))
            return;

        applicationOverlayService.addOverlayWindow(overlayWindow);
        overlayWindowsAttachedToService.add(overlayWindow.getWindowID());
    }


    public void detachOverlayWindowFromService(OverlayWindow overlayWindow) {
        if (applicationOverlayService == null) {
            dequeueOverlayWindow(overlayWindow);
            return;
        }

        if (!overlayWindowsAttachedToService.contains(overlayWindow.getWindowID()))
            return;

        applicationOverlayService.removeOverlayWindow(overlayWindow);
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

//    public boolean updateOverlayWindowPosition(OverlayWindow window, OverlayPosition position) {
//        OverlayWindow overlayWindow = null;
//
//        for (String attachedWindowID : overlayWindowsAttachedToService) {
//            if (attachedWindow.getWindowID().equals(window.getWindowID())) {
//                overlayWindow = attachedWindow;
//                break;
//            }
//        }
//
//        if (overlayWindow == null)
//            return false;
//
//        overlayWindow.setCurrentPosition(position);
//        if (applicationOverlayService != null)
//            applicationOverlayService.updateOverlayWindow(overlayWindow);
//        return false;
//    }
//
//    public boolean updateOverlayWindowSize(OverlayWindow window, OverlaySize size) {
//        OverlayWindow overlayWindow = null;
//
//        for (OverlayWindow attachedWindow : overlayWindowsAttachedToService) {
//            if (attachedWindow.getWindowID().equals(window.getWindowID())) {
//                overlayWindow = attachedWindow;
//                break;
//            }
//        }
//
//        if (overlayWindow == null)
//            return false;
//
//        overlayWindow.setOverlaySize(size);
//        applicationOverlayService.updateOverlayWindow(overlayWindow);
//        return false;
//    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

//    protected static synchronized ApplicationOverlayServiceManager getInstance() {
//        if (instance == null) {
//            System.out.println("New OverlayServiceManager instance");
//            instance = new ApplicationOverlayServiceManager();
//        }
//
//        return (instance);
//    }
}
