package me.orbitium.flutter_overlay_window_engine.service;

import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;

public interface OverlayService {
    void addOverlayWindow(OverlayWindow overlayWindow);
    void removeOverlayWindow(OverlayWindow overlayWindow);
    void updateOverlayWindow(OverlayWindow overlayWindow);
}
