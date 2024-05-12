package me.orbitium.flutter_overlay_window_engine;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.Map;

import io.flutter.embedding.engine.FlutterEngineGroup;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayPosition;
import me.orbitium.flutter_overlay_window_engine.overlay.OverlayWindowManager;
import me.orbitium.flutter_overlay_window_engine.service.ServiceManager;
import me.orbitium.flutter_overlay_window_engine.service.accessibility.AccessibilityOverlayService;
import me.orbitium.flutter_overlay_window_engine.utils.PermissionManager;

/**
 * FlutterOverlayWindowEnginePlugin
 */
public class FlutterOverlayWindowEnginePlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

    private static FlutterEngineGroup engineGroup;
    private MethodChannel channel;
    private static FlutterPluginBinding binding;
    private Context context;
    private Activity activity;

    @Override
    public void onAttachedToEngine(FlutterPluginBinding flutterPluginBinding) {
        binding = flutterPluginBinding;
        context = binding.getApplicationContext();

        engineGroup = new FlutterEngineGroup(context);
        new PermissionManager();

        loadDisplayMetrics();

        channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_overlay_window_engine/engine");
        channel.setMethodCallHandler(this);
    }

    public void loadDisplayMetrics() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        OverlayPosition.screenHeight = metrics.heightPixels;
        OverlayPosition.screenWidth = metrics.widthPixels;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        System.out.println("Method: " + call.method);
        EngineMethod engineMethod = EngineMethod.getByMethodID(call.method);
        if (engineMethod == null) {
            result.notImplemented();
            return;
        }

        switch (engineMethod) {
            case INITIALIZE:
                OverlayWindowManager.getInstance().clearCache();
                break;

            case START_SERVICE:
                ServiceManager.getInstance().startApplicationOverlayService(context);
                result.success(true);
                break;

            case STOP_SERVICE:
                ServiceManager.getInstance().stopApplicationService(context);
                result.success(true);
                break;

            //! Permissions
            case CHECK_SYSTEM_ALERT_WINDOW_PERMISSION:
                result.success(PermissionManager.instance.canShowOverlayWindow(context));
                break;

            case REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION:
                PermissionManager.instance.requestOverlayPermission(context, activity);
                result.success(null);
                break;

            case CHECK_ACCESSIBILITY_PERMISSION:
                result.success(PermissionManager.instance.isAccessServiceEnabled(context, AccessibilityOverlayService.class));
                break;

            case REQUEST_ACCESSIBILITY_PERMISSION:
                PermissionManager.instance.requestAccessibilityPermission(context, activity);
                result.success(null);
                break;

            case CREATE_AND_CACHE_OVERLAY_WINDOW:
                OverlayWindow overlayWindow = OverlayWindow.fromJson((Map<String, Object>) call.arguments);

                if (overlayWindow == null)
                    return;

                if (OverlayWindowManager.getInstance().contains(overlayWindow.getWindowID()))
                    throw new RuntimeException("Duplicate overlay window ID! Overlay window id must be unique");

                OverlayWindowManager.getInstance().cacheOverlayWindow(overlayWindow);
                result.success(null);
                break;

            case DELETE_CACHED_OVERLAY_WINDOW:
                String windowID = (String) call.arguments;
                OverlayWindow targetWindow = OverlayWindowManager.getInstance().getOverlayWindow(windowID);
                if (targetWindow == null)
                    throw new NullPointerException("Overlay window with overlay window id " + windowID + " does not exists!");

                if (targetWindow.isAttachedToService())
                    throw new IllegalStateException("Overlay window " + windowID + " has attached to a service, can't be deleted!");

//                ServiceManager.getInstance().detachOverlayWindow(windowID);
                OverlayWindowManager.getInstance().deleteCachedOverlayWindow(windowID);
                result.success(null);
                break;

            case ACTIVATE_OVERLAY_WINDOW:
                ServiceManager.getInstance().attachOverlayWindow((String) call.arguments);
                result.success(null);
                break;

            case DISABLE_OVERLAY_WINDOW:
                ServiceManager.getInstance().detachOverlayWindow((String) call.arguments);
                result.success(null);
                break;

            default:
                result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding flutterPluginBinding) {
        channel.setMethodCallHandler(null);
//        OverlayServiceManager.getInstance().dispose(binding.getApplicationContext());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
        activity = activityPluginBinding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    public static FlutterEngineGroup getEngineGroup() {
        return engineGroup;
    }

    public static Context getContext() {
        return binding.getApplicationContext();
    }


    @Override
    public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
        activity = activityPluginBinding.getActivity();
    }


}

enum EngineMethod {
    INITIALIZE("initialize_engine"),
    START_SERVICE("start_service"),
    STOP_SERVICE("stop_service"),
    CHECK_SYSTEM_ALERT_WINDOW_PERMISSION("check_system_alert_window_permission"),
    REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION("request_system_alert_window_permission"),
    CHECK_ACCESSIBILITY_PERMISSION("check_accessibility_permission"),
    REQUEST_ACCESSIBILITY_PERMISSION("request_accessibility_permission"),
    CREATE_AND_CACHE_OVERLAY_WINDOW("create_and_cache_overlay_window"),
    DELETE_CACHED_OVERLAY_WINDOW("delete_cached_overlay_window"),
    ACTIVATE_OVERLAY_WINDOW("activate_overlay_window"),
    DISABLE_OVERLAY_WINDOW("disable_overlay_window"),
    MOVE_OVERLAY_WINDOW("move_overlay_window"),
    RESIZE_OVERLAY_WINDOW("resize_overlay_window");

    String methodID;

    EngineMethod(String methodID) {
        this.methodID = methodID;
    }

    public static EngineMethod getByMethodID(String methodID) {
        for (EngineMethod value : values()) {
            if (value.methodID.equals(methodID))
                return value;
        }

        return null;
    }
}
