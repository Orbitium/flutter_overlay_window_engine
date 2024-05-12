import 'dart:async';

import 'package:flutter_overlay_window_engine/entities/overlay_window.dart';

import 'flutter_overlay_window_engine_method_channel.dart';

class FlutterOverlayWindowEngine {
  static final MethodChannelFlutterOverlayWindowEngine _methodChannel = MethodChannelFlutterOverlayWindowEngine();

  // static final ServiceEventStream _serviceEventStream = ServiceEventStream();
  static late FlutterOverlayWindowEngine instance;

  /* ! Progress
    * Create & cache overlay window at runtime ✔️
    * Activate & disable window ✔️
    * Start & Stop service ✔️
    *
    * Implementing service-state management is not worth the time for now ❌
    *
    * Service streams to manage states of service (might need slight modifications) ❌
    * onServiceStarted/onServiceStopped ❌
    *
    * Draggable overlay windows ✔️
    * Transparent overlay windows ✔️
    * Accessibility overlay windows (requires min API 22) ✔️
    * Overlay window events (only onDrag for now) ❌ MethodChannel problems
    *
    * Create/Update notification ❌ (won't be available in 0.1.0 version)
    *
    * Better error-handling ❌ (won't be available in 0.1.0 version)
    *
    * Code cleanup
    * Create an example project ✔️
    * Upload to pub lib ✔️
    *
   */

  static void initialize() async {
    instance = FlutterOverlayWindowEngine();
    await _methodChannel.initializeEngine();
    // _serviceEventStream.initStream();
  }

  Future<void> startService() async {
    return _methodChannel.startService();
  }

  Future<void> stopService() async {
    return _methodChannel.stopService();
  }

  //
  // Stream<ServiceState> getServiceStream() {
  //   return _serviceEventStream.getStream();
  // }

  Future<bool> isSystemAlertWindowPermissionGranted() {
    return _methodChannel.isSystemAlertWindowPermissionGranted();
  }

  Future<void> requestSystemAlertWindowPermission() {
    return _methodChannel.requestSystemAlertWindowPermission();
  }

  Future<bool> isAccessibilityPermissionGranted() async {
    return await _methodChannel.isAccessibilityPermissionGranted();
  }

  Future<void> requestAccessibilityPermission() async {
    return await _methodChannel.requestAccessibilityPermission();
  }

  Future<void> createAndCacheOverlayWindow(OverlayWindow overlayWindow) async {
    await _methodChannel.createAndCacheOverlayWindow(overlayWindow);
  }

  Future<void> deleteCachedOverlayWindow(String id) async {
    await _methodChannel.deleteCachedOverlayWindow(id);
  }

  Future<void> activateOverlayWindow(String id) async {
    await _methodChannel.activateOverlayWindow(id);
  }

  Future<void> deactivateOverlayWindow(String id) async {
    await _methodChannel.disableOverlayWindow(id);
  }
}
