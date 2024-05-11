package me.orbitium.flutter_overlay_window_engine.service.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import io.flutter.embedding.android.FlutterView;
import me.orbitium.flutter_overlay_window_engine.DragPosition;
import me.orbitium.flutter_overlay_window_engine.FlutterOverlayWindowEnginePlugin;
import me.orbitium.flutter_overlay_window_engine.entities.OverlayWindow;
import me.orbitium.flutter_overlay_window_engine.service.OverlayService;
import me.orbitium.flutter_overlay_window_engine.service.ServiceManager;
import me.orbitium.flutter_overlay_window_engine.service.application.ApplicationOverlayService;
import me.orbitium.flutter_overlay_window_engine.service.application.ApplicationOverlayServiceManager;


/* ! IMPORTANT
*
* REQUIRES:
* <service android:name=".MyAccessibilityService"
         android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
     <intent-filter>
         <action android:name="android.accessibilityservice.AccessibilityService" />
     </intent-filter>
     . . .
 </service>
*/
public class AccessibilityOverlayService extends AccessibilityService implements OverlayService {

    private final IBinder serviceBinder = new AccessibilityOverlayService.AccessibilityOverlayServiceBinder();

    // Class used for client Binder. You can extend this to add more methods and functionality.
    public class AccessibilityOverlayServiceBinder extends Binder {
        AccessibilityOverlayService getService() {
            // Return this instance of MyBoundService so clients can call public methods
            return AccessibilityOverlayService.this;
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        System.out.println("AccessibilityOverlayService has been started");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        ServiceManager.getInstance().getAccessibilityServiceManager().onServiceStarted(serviceBinder);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    /// *** Service  *** ///
    WindowManager windowManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_NOT_STICKY;
    }

    @Override
    public void addOverlayWindow(OverlayWindow overlayWindow) {
        WindowManager.LayoutParams params = overlayWindow.getParams();
        FlutterView flutterView = overlayWindow.getRenderer().createAndCacheFlutterView();
        if (flutterView == null) {
            System.out.println("FlutterView is null, cannot be added to the service");
            return;
        }

        // TODO: implement dragging & it should notify flutterside that widget has been moved
        if (overlayWindow.isDraggable()) {
            ViewGroup viewGroup = (ViewGroup) flutterView;
            DragPosition effectiveFinalPosition = new DragPosition(0, 0, 0, 0);
            viewGroup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            effectiveFinalPosition.setInitialX(params.x);
                            effectiveFinalPosition.setInitialY(params.y);
                            effectiveFinalPosition.setInitialTouchX(event.getRawX());
                            effectiveFinalPosition.setInitialTouchY(event.getRawY());
                            return false;
                        case MotionEvent.ACTION_MOVE:
                            params.x = effectiveFinalPosition.getInitialX() + (int) (event.getRawX() - effectiveFinalPosition.getInitialTouchX());
                            params.y = effectiveFinalPosition.getInitialY() + (int) (event.getRawY() - effectiveFinalPosition.getInitialTouchY());
                            windowManager.updateViewLayout(view, params);
                            return false;
                        default:
                            return false;
                    }
                }
            });
        }

        System.out.println("View has been added to the window manager");
        windowManager.addView(flutterView, params);
    }

    @Override
    public void removeOverlayWindow(OverlayWindow overlayWindow) {
        System.out.println("View has been removed from the window manager");
        windowManager.removeView(overlayWindow.getRenderer().getCachedView());
    }

    @Override
    public void updateOverlayWindow(OverlayWindow overlayWindow) {

    }

}
