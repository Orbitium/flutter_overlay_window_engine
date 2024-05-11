package me.orbitium.flutter_overlay_window_engine.overlay;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;

public class OverlayWindowManager {

    // ! Do NOT put overlay windows into ANY different list/map, might lead to memory leaks
    private static volatile OverlayWindowManager instance;
    private final Map<String, OverlayWindow> overlayWindowCache;

    public OverlayWindowManager() {
        System.out.println("New instance");
        overlayWindowCache = new HashMap<>();
    }

    public void cacheOverlayWindow(OverlayWindow overlayWindow) {
        overlayWindowCache.put(overlayWindow.getWindowID(), overlayWindow);
    }

    public void deleteCachedOverlayWindow(String id) {
        OverlayWindow overlayWindow = getOverlayWindow(id);
        if (overlayWindow == null)
            return;

        overlayWindow.getRenderer().dispose();
        overlayWindowCache.remove(id);
    }

    public void clearCache() {
        for (Map.Entry<String, OverlayWindow> entry : overlayWindowCache.entrySet()) {
            entry.getValue().getRenderer().dispose();
        }

        overlayWindowCache.clear();
    }

    @Nullable
    public OverlayWindow getOverlayWindow(String id) {
        System.out.println("Requested ID: " + id);
        System.out.println("Available IDs: " + (overlayWindowCache.keySet()));
        return overlayWindowCache.get(id);
    }

    public boolean contains(String id) {
        System.out.println(overlayWindowCache.keySet());
        return overlayWindowCache.containsKey(id);
    }

    public static OverlayWindowManager getInstance() {
        try {
            if (instance == null) {
                instance = new OverlayWindowManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }
}
