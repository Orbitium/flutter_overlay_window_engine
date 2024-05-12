import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_overlay_window_engine/entities/overlay_window.dart';
import 'package:flutter_overlay_window_engine/flutter_overlay_window_engine.dart';
import 'package:flutter_overlay_window_engine_example/example_overlays/caller_example.dart';
import 'package:flutter_overlay_window_engine_example/example_overlays/eye_care_example.dart';
import 'package:flutter_overlay_window_engine_example/example_overlays/floating_dot_example.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  FlutterOverlayWindowEngine.initialize();

  runApp(const MaterialApp(home: MyApp()));
}

@pragma("vm:entry-point")
void callerExample() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: CallerExample(),
  ));
}

@pragma("vm:entry-point")
void eyeCareExample() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: EyeCareExample(),
  ));
}

@pragma("vm:entry-point")
void floatingDotExample() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: FloatingDotExample(),
  ));
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool hasServiceStarted = false;
  bool hasWindowsInitialized = false;

  @override
  void initState() {
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initOverlays() async {
    Size screenSize = WidgetsBinding.instance.platformDispatcher.views.first.physicalSize;

    OverlayWindow callerExample = OverlayWindow(
      windowID: "callerExample",
      entryPoint: "callerExample",
      overlaySize: OverlaySize((screenSize.width * 0.9).toInt(), 400),
      initialPosition: OverlayPosition((screenSize.width * 0.05).toInt(), 100),
      isDraggable: false,
      isClickable: true,
      isTransparent: true,
    );

    OverlayWindow eyeCareExample = OverlayWindow(
      windowID: "eyeCareExample",
      entryPoint: "eyeCareExample",
      overlaySize: OverlaySize(screenSize.width.toInt(), screenSize.height.toInt()),
      isDraggable: false,
      isClickable: false,
      isTransparent: true,
    );

    OverlayWindow floatingDotExample = OverlayWindow(
      windowID: "floatingDotExample",
      entryPoint: "floatingDotExample",
      overlaySize: const OverlaySize(125, 125),
      overlayType: OverlayType.TYPE_ACCESSIBILITY_OVERLAY,
      initialPosition: const OverlayPosition(500, 300),
      isDraggable: true,
      isTransparent: true,
    );

    FlutterOverlayWindowEngine.instance.createAndCacheOverlayWindow(callerExample);
    FlutterOverlayWindowEngine.instance.createAndCacheOverlayWindow(eyeCareExample);
    FlutterOverlayWindowEngine.instance.createAndCacheOverlayWindow(floatingDotExample);
  }

  Future<void> deleteOverlayWindowCache() async {
    FlutterOverlayWindowEngine.instance.deleteCachedOverlayWindow("callerExample");
    FlutterOverlayWindowEngine.instance.deleteCachedOverlayWindow("eyeCareExample");
    FlutterOverlayWindowEngine.instance.deleteCachedOverlayWindow("floatingDotExample");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Overlay window example"),
        centerTitle: true,
        backgroundColor: Colors.cyan,
      ),
      backgroundColor: Colors.cyan,
      body: Center(
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // *** PERMISSIONS *** //
              const Text("Permissions", style: TextStyle(fontSize: 20)),
              ElevatedButton(
                onPressed: () async {
                  bool isPermissionGranted =
                      await FlutterOverlayWindowEngine.instance.isSystemAlertWindowPermissionGranted();

                  showSnackBar("Is overlay service granted: $isPermissionGranted");
                },
                child: const Text("Is overlay permission granted"),
              ),
              ElevatedButton(
                onPressed: () async {
                  await FlutterOverlayWindowEngine.instance.requestSystemAlertWindowPermission();
                },
                child: const Text("Request overlay permission"),
              ),
              ElevatedButton(
                onPressed: () async {
                  await FlutterOverlayWindowEngine.instance.requestAccessibilityPermission();
                },
                child: const Text("Request accessibility permission"),
              ),
              // *** PERMISSIONS *** //

              // *** Service controls *** //
              const SizedBox(height: 20),
              const Text("Service controls", style: TextStyle(fontSize: 20)),
              ElevatedButton(
                onPressed: hasServiceStarted == false
                    ? () async {
                        showSnackBar("Starting overlay service...");
                        FlutterOverlayWindowEngine.instance.startService();
                        setState(() {
                          hasServiceStarted = true;
                        });
                      }
                    : null,
                child: const Text("Start service"),
              ),
              ElevatedButton(
                onPressed: hasServiceStarted == true
                    ? () async {
                        showSnackBar("Stopping overlay service...");
                        FlutterOverlayWindowEngine.instance.stopService();
                        setState(() {
                          hasServiceStarted = false;
                        });
                      }
                    : null,
                child: const Text("Stop Service"),
              ),
              // *** Service controls *** //

              // *** Caching controls *** //
              const SizedBox(height: 20),
              const Text("Caching controls", style: TextStyle(fontSize: 20)),
              ElevatedButton(
                onPressed: hasWindowsInitialized == true
                    ? null
                    : () async {
                        await initOverlays();
                        setState(() {
                          hasWindowsInitialized = true;
                        });
                      },
                child: const Text("Initialize all overlay windows"),
              ),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        await deleteOverlayWindowCache();

                        setState(() {
                          hasWindowsInitialized = false;
                        });
                      },
                child: const Text("Delete all window cache"),
              ),
              // *** Caching controls *** //

              // *** Overlay window controls *** //
              const SizedBox(height: 20),
              const Text("Overlay window controls", style: TextStyle(fontSize: 20)),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        await FlutterOverlayWindowEngine.instance.activateOverlayWindow("callerExample");
                      },
                child: const Text("Activate caller example"),
              ),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        await FlutterOverlayWindowEngine.instance.deactivateOverlayWindow("callerExample");
                      },
                child: const Text("Deactivate caller example"),
              ),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        await FlutterOverlayWindowEngine.instance.activateOverlayWindow("eyeCareExample");
                      },
                child: const Text("Activate eye care example"),
              ),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        await FlutterOverlayWindowEngine.instance.deactivateOverlayWindow("eyeCareExample");
                      },
                child: const Text("Deactivate eye care example"),
              ),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        if (await FlutterOverlayWindowEngine.instance.isAccessibilityPermissionGranted() == false) {
                          showSnackBar("This example requires accessibility permission");
                          return;
                        }
                        await FlutterOverlayWindowEngine.instance.activateOverlayWindow("floatingDotExample");
                      },
                child: const Text("Activate floating dot example"),
              ),
              ElevatedButton(
                onPressed: hasWindowsInitialized == false
                    ? null
                    : () async {
                        await FlutterOverlayWindowEngine.instance.deactivateOverlayWindow("floatingDotExample");
                      },
                child: const Text("Deactivate floating dot example"),
              ),
              // *** Overlay window controls *** //
            ],
          ),
        ),
      ),
    );
  }

  void showSnackBar(String text) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(text),
      ),
    );
  }
}
