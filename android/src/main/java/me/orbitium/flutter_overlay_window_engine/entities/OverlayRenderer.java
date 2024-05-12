package me.orbitium.flutter_overlay_window_engine.entities;

import android.content.Context;

import org.jetbrains.annotations.Nullable;

import io.flutter.FlutterInjector;
import io.flutter.embedding.android.FlutterSurfaceView;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import me.orbitium.flutter_overlay_window_engine.FlutterOverlayWindowEnginePlugin;

public class OverlayRenderer {
    String engineID;
    boolean isTransparent;
    FlutterView cachedView;

    public OverlayRenderer(String engineID, String entryPoint, boolean isTransparent) {
        this.engineID = engineID;
        this.isTransparent = isTransparent;

        if (doesEngineExist())
            dispose();

        DartExecutor.DartEntrypoint dartEntrypoint = new DartExecutor.DartEntrypoint(
                FlutterInjector.instance().flutterLoader().findAppBundlePath(),
                entryPoint);

        Context context = FlutterOverlayWindowEnginePlugin.getContext();
        FlutterEngine engine = FlutterOverlayWindowEnginePlugin.getEngineGroup()
                .createAndRunEngine(context, dartEntrypoint);


        FlutterEngineCache.getInstance().put(engineID, engine);
    }

    @Nullable
    public FlutterView createAndCacheFlutterView() {
        if (engineID == null)
            return null;

        Context context = FlutterOverlayWindowEnginePlugin.getContext();

        FlutterSurfaceView surfaceView = new FlutterSurfaceView(context, isTransparent);

        FlutterEngine engine = FlutterEngineCache.getInstance().get(engineID);
        engine.getLifecycleChannel().appIsResumed();

        FlutterView flutterView = new FlutterView(context, surfaceView);
        flutterView.attachToFlutterEngine(engine);
//        flutterView.setFitsSystemWindows(true);

        cachedView = flutterView;
        return flutterView;
    }

    public void clearCache() {
        cachedView = null;
    }

    public FlutterView getCachedView() {
        return cachedView;
    }

    public boolean doesEngineExist() {
        FlutterEngine engine = FlutterEngineCache.getInstance().get(engineID);
        return engine != null;
    }

    public void dispose() {
        FlutterEngine engine = FlutterEngineCache.getInstance().get(engineID);
        if (engine == null)
            return;

//        engine.getLifecycleChannel().appIsDetached();
        engine.destroy();

        FlutterEngineCache.getInstance().remove(engineID);

        if (cachedView != null)
            cachedView.destroyDrawingCache();

        engineID = null;
        cachedView = null;
    }
}
