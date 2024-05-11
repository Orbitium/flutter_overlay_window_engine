class OverlayNotification {
  final String title;
  final String content;
  final NotificationVisibility notificationVisibility;

  const OverlayNotification(this.title, this.content, this.notificationVisibility);

  static const noNotification = OverlayNotification("", "", NotificationVisibility.none);

  // Method to serialize OverlayNotification object to JSON
  Map<String, dynamic> toJson() {
    return {
      'title': title,
      'content': content,
      'notificationVisibility': notificationVisibility.name,
    };
  }

  // Factory method to deserialize JSON to OverlayNotification object
  factory OverlayNotification.fromJson(Map<String, dynamic> json) {
    return OverlayNotification(
      json['title'] as String,
      json['content'] as String,
      _getNotificationVisibility(json['notificationVisibility']),
    );
  }

  // Helper method to convert string to NotificationVisibility enum
  static NotificationVisibility _getNotificationVisibility(String value) {
    switch (value) {
      case 'visibilityPublic':
        return NotificationVisibility.visibilityPublic;
      case 'visibilitySecret':
        return NotificationVisibility.visibilitySecret;
      case 'visibilityPrivate':
        return NotificationVisibility.visibilityPrivate;
      case 'none':
        return NotificationVisibility.none;
      default:
        return NotificationVisibility.none; // Default to none if invalid value
    }
  }
}

enum NotificationVisibility {
  /// Show this notification in its entirety on all lockscreens.
  visibilityPublic,

  /// Do not reveal any part of this notification on a secure lockscreen.
  visibilitySecret,

  /// Show this notification on all lockscreens, but conceal sensitive or private information on secure lockscreens.
  visibilityPrivate,
  none,
}
