import 'package:flutter/services.dart';
import 'package:flutter_overlay_window_engine/entities/overlay_notification.dart';

class OverlayWindow {
  String windowID;
  String entryPoint;
  OverlayPosition initialPosition;
  OverlayType overlayType;
  OverlaySize overlaySize;
  OverlayNotification notification;
  bool isClickable;
  bool isDraggable;
  bool isTransparent;

  static const MethodChannel eventChannel = MethodChannel("flutter_overlay_window_engine/test");

  OverlayWindow({
    required this.windowID,
    required this.entryPoint,
    this.initialPosition = OverlayPosition.midCenter,
    this.overlayType = OverlayType.TYPE_APPLICATION_OVERLAY,
    this.overlaySize = OverlaySize.defaultSize,
    this.notification = OverlayNotification.noNotification,
    this.isClickable = true,
    this.isDraggable = false,
    this.isTransparent = false,
  });

  // Method to serialize OverlayWindow object to JSON
  Map<String, dynamic> toJson() {
    return {
      'windowID': windowID,
      'entryPoint': entryPoint,
      'initialPosition': initialPosition.toJson(),
      'overlayType': overlayType.toString().split(".").last,
      'size': overlaySize.toJson(),
      'notification': notification.toJson(),
      'isClickable': isClickable,
      'isDraggable': isDraggable,
      'isTransparent': isTransparent,
    };
  }

  void onDrag(OverlayPosition newPosition) {}
}

class OverlaySize {
  final int width;
  final int height;

  const OverlaySize(this.width, this.height);

  // Serialization
  Map<String, dynamic> toJson() {
    return {
      'width': width,
      'height': height,
    };
  }

  /// 1:1 horizontal and 1:2 vertical
  static const OverlaySize defaultSize = OverlaySize(-1, -1);
}

class OverlayPosition {
  final int x;
  final int y;

  const OverlayPosition(this.x, this.y);

  // ** Ready-to-use Positions ** \\
  static const OverlayPosition topLeft = OverlayPosition(-1, -1);
  static const OverlayPosition topCenter = OverlayPosition(-2, -1);
  static const OverlayPosition topRight = OverlayPosition(-3, -1);

  static const OverlayPosition midLeft = OverlayPosition(-1, -2);
  static const OverlayPosition midCenter = OverlayPosition(-2, -2);
  static const OverlayPosition midRight = OverlayPosition(-3, -2);

  static const OverlayPosition bottomLeft = OverlayPosition(-1, -3);
  static const OverlayPosition bottomCenter = OverlayPosition(-2, -3);
  static const OverlayPosition bottomRight = OverlayPosition(-3, -3);

  // Serialization
  Map<String, dynamic> toJson() {
    return {
      'x': x,
      'y': y,
    };
  }

  // Deserialization
  OverlayPosition.fromJson(Map<String, dynamic> json)
      : x = json['x'],
        y = json['y'];
}

enum OverlayType {
  // If API version is lower than 26, plugin will use TYPE_PHONE
  // no need to manual version check for API level
  TYPE_APPLICATION_OVERLAY,

  // Uses accessibility service, android manifest update required
  // Requires API level >= 22, in lower APIs turns to [TYPE_APPLICATION_OVERLAY]
  TYPE_ACCESSIBILITY_OVERLAY
}
