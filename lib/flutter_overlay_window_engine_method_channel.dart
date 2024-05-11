import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'entities/overlay_window.dart';

class MethodChannelFlutterOverlayWindowEngine {
  /// The method channel used to interact with the native platform.
  static const MethodChannel _methodChannel = MethodChannel('flutter_overlay_window_engine/engine');

  // TODO: Implement better error-handling, maybe using Either<Failure, Success>?
  Future<String?> getPlatformVersion() async {
    final version = await _methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future<void> test(String id) {
    return _methodChannel.invokeMethod("test", id);
  }

  Future<void> initializeEngine() async {
    await _methodChannel.invokeMethod("initialize_engine");
  }

  Future<void> startService() async {
    await _methodChannel.invokeMethod("start_service");
  }

  Future<void> stopService() async {
    await _methodChannel.invokeMethod("stop_service");
  }

  Future<bool> isSystemAlertWindowPermissionGranted() async {
    return await _methodChannel.invokeMethod("check_system_alert_window_permission");
  }

  Future<void> requestSystemAlertWindowPermission() async {
    return await _methodChannel.invokeMethod("request_system_alert_window_permission");
  }

  Future<bool> isAccessibilityPermissionGranted() async {
    return await _methodChannel.invokeMethod("check_accessibility_permission");
  }

  Future<void> requestAccessibilityPermission() async {
    return await _methodChannel.invokeMethod("request_accessibility_permission");
  }

  /// Returns true if OverlayWindow has created and cached successfully
  Future<void> createAndCacheOverlayWindow(OverlayWindow overlayWindow) async {
    var encodedOverlayWindow = overlayWindow.toJson();
    await _methodChannel.invokeMethod('create_and_cache_overlay_window', encodedOverlayWindow) ?? false;
  }

  Future<void> deleteCachedOverlayWindow(String id) async {
    await _methodChannel.invokeMethod('delete_cached_overlay_window', id) ?? false;
  }

  Future<void> activateOverlayWindow(String id) async {
    await _methodChannel.invokeMethod('activate_overlay_window', id) ?? false;
  }

  Future<void> disableOverlayWindow(String id) async {
    await _methodChannel.invokeMethod('disable_overlay_window', id) ?? false;
  }
}
