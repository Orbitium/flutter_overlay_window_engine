package me.orbitium.flutter_overlay_window_engine.overlay;

import java.util.Map;

public class OverlaySize {
    private int width;
    private int height;

    public OverlaySize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Deserialize from JSON
    public static OverlaySize fromJson(Map<String, Object> json) {
        int width = (int) json.get("width");
        int height = (int) json.get("height");
        return new OverlaySize(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Getters
    // Implement as needed
}