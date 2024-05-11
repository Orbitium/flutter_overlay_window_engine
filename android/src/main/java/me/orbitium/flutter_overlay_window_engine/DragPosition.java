package me.orbitium.flutter_overlay_window_engine;

public class DragPosition {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    public DragPosition(int initialX, int initialY, float initialTouchX, float initialTouchY) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialTouchX = initialTouchX;
        this.initialTouchY = initialTouchY;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public float getInitialTouchX() {
        return initialTouchX;
    }

    public float getInitialTouchY() {
        return initialTouchY;
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    public void setInitialTouchX(float initialTouchX) {
        this.initialTouchX = initialTouchX;
    }

    public void setInitialTouchY(float initialTouchY) {
        this.initialTouchY = initialTouchY;
    }
}
