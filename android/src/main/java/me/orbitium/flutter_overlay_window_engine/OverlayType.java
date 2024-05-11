package me.orbitium.flutter_overlay_window_engine;

public enum OverlayType {
    // If API version is lower than 26, plugin will use TYPE_PHONE
    // no need to manual version check for API level
    TYPE_APPLICATION_OVERLAY,

    // Uses accessibility service, android manifest update required
    // Requires API level >= 22, in lower APIs turns to [TYPE_APPLICATION_OVERLAY]
    TYPE_ACCESSIBILITY_OVERLAY;

    public static OverlayType findByName(String name) {
        for (OverlayType value : values()) {
            if (value.name().equals(name))
                return value;
        }

        return null;
    }
}