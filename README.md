# Flutter Overlay Window Engine

A Flutter package to create overlay windows at runtime on Android.

## Table of Contents
- [Showcase](#showcase)
- [Warning](#warning)
- [Installation](#installation)
- [Requirements](#requirements)
- [Usage](#usage)
- [Features](#features)
- [Known issues](#known-issues)

## Showcase
<div style="display: flex; justify-content: space-between;">
    <img src="https://github.com/Orbitium/flutter_overlay_window_engine/blob/main/example/assets/Example1.png" alt="Caller example" width="200" height="400">
    <img src="https://github.com/Orbitium/flutter_overlay_window_engine/blob/main/example/assets/Example2.png" alt="Eye care example" width="200" height="400">
    <img src="https://github.com/Orbitium/flutter_overlay_window_engine/blob/main/example/assets/Example3.gif" alt="Floating dot example" width="200" height="400">
    <img src="https://github.com/Orbitium/flutter_overlay_window_engine/blob/main/example/assets/Example4.png" alt="Eye care example" width="200" height="400">
</div>

## Warning
This package is still under development and might lead to crashes/memory leaks. Any kind of help is appreciated.

## Installation

## Requirements

### Overlay Permission

#### Until API version 26
The engine doesn't require any additional permissions.

#### After API version 26
SYSTEM_ALERT_WINDOW permission required. 
```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
```


### Background service

#### For APPLICATION overlay windows
The engine requires a background service to create/manage views. Add
```xml
<service
android:name="me.orbitium.flutter_overlay_window_engine.service.application.ApplicationOverlayService">
</service>
```
to AndroidManifest.xml.

#### For ACCESSIBILITY overlay windows
The engine requires an accessibility service to create/manage views. Add
```xml
<service android:name="me.orbitium.flutter_overlay_window_engine.service.accessibility.AccessibilityOverlayService"
    android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"
    android:exported="false">
    
    <intent-filter>
        <action android:name="android.accessibilityservice.AccessibilityService" />
    </intent-filter>
</service>
```
to AndroidManifest.xml.

## Usage

### Create/Activate an overlay window
1. Initialize the Overlay Engine by `FlutterOverlayWindowEngine.initialize();`.
2. You need to create a dart entry point first. In main.dart, create a function with `@pragma("vm:entry-point")`.
3. Create an `OverlayWindow` entity, then use `FlutterOverlayWindowEngine.instance.createAndCacheOverlayWindow(callerExample);`. This will create and cache your overlay window entity.
4. Activate the cached overlay window by using `await FlutterOverlayWindowEngine.instance.activateOverlayWindow(...)`.

Caching overlay windows will allocate memory; caching multiple overlay windows is not recommended.
Overlay windows can be created/activated at runtime.

### Delete/Deactivate an overlay window
1. Use `await FlutterOverlayWindowEngine.instance.deactivateOverlayWindow(...)` if the window is activated and currently on display.
2. After deactivating the window, you can delete the window by `FlutterOverlayWindowEngine.instance.deleteCachedOverlayWindow(...)`.

Deleting overlay windows will free allocated memory.

## Features

1. Create/Cache/Activate/Deactivate/Delete multiple overlay windows at runtime.
2. Draggable overlay windows.
3. Optional transparent windows. Non-transparent windows are more efficient than transparent ones.
4. Click through overlay windows, meaning the user can click a widget under overlay windows.
5. Use Flutter code to create overlay windows without needing XML knowledge.

#### Planned

- Allow overlay windows to be resizable. Currently, overlay windows will keep their size after being created.
- Allow overlay windows to be re-positioned programmatically. Currently, it's not possible to re-position overlay windows.
- Overlay window events such as `onActivated()`, `onDeactivated()`, `onDragged()` etc.
- Current position variable in overlay windows; currently, it's not possible to know the overlay window's location if it's draggable. Note: it's possible to get the current position of overlay windows on the Android side.
- Service events such as `onServiceStarted()`, `onServiceStopped()` etc.
- Foreground/Stick/Non-sticky services.
- Notifications.
- Better stabilization; currently, the engine can't handle heavy loads or wrong use cases.
- Better error-handling. Currently, the engine does not respond as well as expected; you can't really know if the engine is doing its thing in the background.

## Known issues

- Minor memory leaks. Due to some runtime exceptions thrown by the Dart VM, the engine can leak some memory over time.
- Ghost overlay windows that crash the app on touch.
- Crashes on wrong use cases such as trying to delete an overlay window without detaching it from the service.
