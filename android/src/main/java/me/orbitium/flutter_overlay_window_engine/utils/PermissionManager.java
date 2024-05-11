package me.orbitium.flutter_overlay_window_engine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class PermissionManager {
    public static PermissionManager instance;

    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 1;
    private static final int REQUEST_ACCESSIBILITY = 2;

    public PermissionManager() {
        instance = this;
    }

    public boolean canShowOverlayWindow(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }

        return true;
    }

    // TODO: Create a Stream to notify permission is granted/not granted
    // it' not possible to say it permission is given or not without waiting for
    // activity to end (see -> activity.startActivityForResult(intent, REQUEST_SYSTEM_ALERT_WINDOW))
    public void requestOverlayPermission(Context context, Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            activity.startActivity(intent);
        }
    }

    public void requestAccessibilityPermission(Context context, Activity activity) {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivity(intent);
    }

    public boolean isAccessServiceEnabled(Context context, Class accessibilityServiceClass) {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        return prefString != null && prefString.contains(context.getPackageName() + "/" + accessibilityServiceClass.getName());
    }
}
