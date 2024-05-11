import 'dart:ui';

import 'package:flutter/material.dart';

class FloatingDotExample extends StatelessWidget {
  const FloatingDotExample({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: CustomPaint(
        size: Size(125, 125),
        painter: CircleWithDotPainter(),
      ),
    );
  }
}

// Generated by ChatGPT-3.5
class CircleWithDotPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final Paint circlePaint = Paint()
      ..color = Colors.blue
      ..strokeWidth = 3.0
      ..style = PaintingStyle.stroke;

    final Paint dotPaint = Paint()
      ..color = Colors.red
      ..strokeWidth = 8.0;

    final double centerX = size.width / 2;
    final double centerY = size.height / 2;
    final double radius = size.width / 2.25; // Adjust the radius as needed
    final double dotRadius = 4.5;

    // Draw the circle
    canvas.drawCircle(Offset(centerX, centerY), radius, circlePaint);

    // Draw the dot in the center
    canvas.drawCircle(Offset(centerX, centerY), dotRadius, dotPaint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return false;
  }
}
