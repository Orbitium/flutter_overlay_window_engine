package me.orbitium.flutter_overlay_window_engine.overlay;

import android.util.DisplayMetrics;

import java.util.Map;

public class OverlayPosition {
    public static int screenWidth;
    public static int screenHeight;

    private int x;
    private int y;

    public OverlayPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static OverlayPosition parsePosition(int x, int y, OverlaySize size) {
        // x -> Horizontal || -1 -> left & -3 -> right
        // y -> Linear || -1 -> top & -3 -> bottom
        switch (x) {
            case -1:
                switch (y) {
                    case -1:
                        return new OverlayPosition(0, 0);
                    case -2:
                        return new OverlayPosition(0, (screenHeight / 2) - (size.getHeight() / 2));
                    case -3:
                        return new OverlayPosition(0, screenHeight);
                }
                break;

            case -2:
                switch (y) {
                    case -1:
                        return new OverlayPosition(screenWidth / 2, 0);
                    case -2:
                        return new OverlayPosition((screenWidth / 2) - (size.getWidth() / 2), (screenHeight / 2) - (size.getHeight() / 2));
                    case -3:
                        return new OverlayPosition(screenWidth / 2, screenHeight);
                }
                break;

            case -3:
                switch (y) {
                    case -1:
                        return new OverlayPosition(screenWidth, 0);
                    case -2:
                        return new OverlayPosition(screenWidth, (screenHeight / 2) - (size.getHeight() / 2));
                    case -3:
                        return new OverlayPosition(screenWidth, screenHeight);
                }
                break;

            default:
                return null;
        }

        return null;
    }

    // Deserialize from JSON
    public static OverlayPosition fromJson(Map<String, Object> json) {
        int x = (int) json.get("x");
        int y = (int) json.get("y");
        return new OverlayPosition(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

