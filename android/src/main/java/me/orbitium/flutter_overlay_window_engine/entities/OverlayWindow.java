package me.orbitium.flutter_overlay_window_engine.entities;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import java.util.Map;

import me.orbitium.flutter_overlay_window_engine.OverlayType;
import me.orbitium.flutter_overlay_window_engine.overlay.NotificationVisibility;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayPosition;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlaySize;

// TODO: Window events???? -> onActivate(), onDisable(), onDrag(), onTerminated() (when app loses required permissions)
// TODO: Implement useAccessibilityService;
public class OverlayWindow {
    private String engineID;
    private OverlayRenderer renderer;
    private String windowID;
    private String entryPoint;
    private OverlayPosition currentPosition;
    private OverlayPosition initialPosition;
    private OverlayType overlayType;
    private OverlaySize overlaySize;
    private OverlayNotification notification;
    private boolean isClickable;
    private boolean isDraggable;
    private boolean isTransparent;
    private boolean isAttachedToService = false;

    // Is ONLY available when dragging is enabled
    // private MethodChannel methodChannel;

    public OverlayWindow(String windowID, String entryPoint, OverlayPosition initialPosition,
                         OverlayType overlayType, OverlaySize overlaySize, OverlayNotification notification,
                         boolean isClickable, boolean isDraggable, boolean isTransparent) {
        this.windowID = windowID;
        this.entryPoint = entryPoint;
        this.initialPosition = initialPosition;
        this.overlayType = overlayType;
        this.overlaySize = overlaySize;
        this.notification = notification;
        this.isClickable = isClickable;
        this.isDraggable = isDraggable;
        this.isTransparent = isTransparent;

        currentPosition = initialPosition;

        engineID = windowID + "_engine_renderer";
        renderer = new OverlayRenderer(engineID, entryPoint, isTransparent);

        if (!isDraggable)
            return;
    }


    public static OverlayWindow fromJson(Map<String, Object> json) {
        try {
            String windowID = (String) json.get("windowID");
            String entryPoint = (String) json.get("entryPoint");

            Map<String, Object> initialPositionJson = (Map<String, Object>) json.get("initialPosition");
            int initialPositionX = (int) initialPositionJson.get("x");
            int initialPositionY = (int) initialPositionJson.get("y");
            OverlayPosition initialPosition = new OverlayPosition(initialPositionX, initialPositionY);

            String overlayTypeString = (String) json.get("overlayType");
            OverlayType overlayType = OverlayType.findByName(overlayTypeString);

            Map<String, Object> sizeJson = (Map<String, Object>) json.get("size");
            int sizeWidth = (int) sizeJson.get("width");
            int sizeHeight = (int) sizeJson.get("height");
            OverlaySize size = new OverlaySize(sizeWidth, sizeHeight);

            Map<String, Object> notificationJson = (Map<String, Object>) json.get("notification");
            String title = (String) notificationJson.get("title");
            String content = (String) notificationJson.get("content");
            String visibility = (String) notificationJson.get("notificationVisibility");
            NotificationVisibility notificationVisibility = NotificationVisibility.valueOf(visibility);
            OverlayNotification notification = new OverlayNotification(title, content, notificationVisibility);

            boolean isClickable = (boolean) json.get("isClickable");
            boolean isDraggable = (boolean) json.get("isDraggable");
            boolean isTransparent = (boolean) json.get("isTransparent");
            return new OverlayWindow(windowID, entryPoint, initialPosition, overlayType, size, notification, isClickable, isDraggable, isTransparent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public WindowManager.LayoutParams getParams() {
        int overlayFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        if (overlayType == OverlayType.TYPE_ACCESSIBILITY_OVERLAY)
            overlayFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) ? WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        boolean isTouchable = isClickable || isDraggable;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                overlaySize.getWidth(),
                overlaySize.getHeight(),
                overlayFlag,
                isTouchable ? WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE : WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );



        params.x = currentPosition.getX();
        params.y = currentPosition.getY();
        params.gravity = Gravity.LEFT | Gravity.TOP;
        return params;
    }

    public String getWindowID() {
        return windowID;
    }

    public OverlayNotification getNotification() {
        return notification;
    }

    public OverlayType getOverlayType() {
        return overlayType;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public OverlayRenderer getRenderer() {
        return renderer;
    }

    public OverlayPosition getCurrentPosition() {
        return currentPosition;
    }

    public boolean isAccessibilityWindow() {
        return overlayType == OverlayType.TYPE_ACCESSIBILITY_OVERLAY;
    }

    public void setCurrentPosition(OverlayPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setOverlaySize(OverlaySize overlaySize) {
        this.overlaySize = overlaySize;
    }

    public OverlaySize getOverlaySize() {
        return overlaySize;
    }

    public boolean isAttachedToService() {
        return isAttachedToService;
    }

    public void setAttachedToService(boolean attachedToService) {
        this.isAttachedToService = attachedToService;
    }
}



