package me.orbitium.flutter_overlay_window_engine.entities;

import java.util.Map;

import me.orbitium.flutter_overlay_window_engine.overlay.NotificationVisibility;

public class OverlayNotification {
    private final String title;
    private final String content;
    private final NotificationVisibility notificationVisibility;

    public OverlayNotification(String title, String content, NotificationVisibility notificationVisibility) {
        this.title = title;
        this.content = content;
        this.notificationVisibility = notificationVisibility;
    }

    public static final OverlayNotification noNotification = new OverlayNotification("", "", NotificationVisibility.none);

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NotificationVisibility getNotificationVisibility() {
        return notificationVisibility;
    }

    public static OverlayNotification fromJson(Map<String, Object> json) {
        String title = (String) json.get("title");
        String content = (String) json.get("content");
        String visibilityIndex = (String) json.get("notificationVisibility");
        NotificationVisibility notificationVisibility = NotificationVisibility.valueOf(visibilityIndex);

        return new OverlayNotification(title, content, notificationVisibility);
    }
}

