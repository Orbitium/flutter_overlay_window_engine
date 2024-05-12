import 'package:flutter/material.dart';

class CallerExample extends StatelessWidget {
  const CallerExample({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: Container(
        decoration: BoxDecoration(
          color: Colors.grey,
          borderRadius: BorderRadius.circular(28),
        ),
        child: Padding(
          padding: EdgeInsets.all(8),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    width: 50,
                    height: 50,
                    decoration: BoxDecoration(
                      color: Colors.green,
                      shape: BoxShape.circle,
                    ),
                    child: Icon(Icons.call),
                  ),
                ],
              ),
              SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "+123 456 78 90 - Calling",
                      style: TextStyle(
                        fontSize: 20,
                      ),
                    ),
                    Text(
                      "Incoming call",
                      style: TextStyle(
                        fontSize: 20,
                      ),
                    ),
                    Spacer(),
                    Row(
                      children: [
                        ElevatedButton(
                          onPressed: () {},
                          style: ButtonStyle(
                            backgroundColor: MaterialStatePropertyAll(Colors.green),
                            foregroundColor: MaterialStatePropertyAll(Colors.white),
                          ),
                          child: Text("Accept"),
                        ),
                        SizedBox(width: 8),
                        ElevatedButton(
                          onPressed: () {},
                          style: ButtonStyle(
                            backgroundColor: MaterialStatePropertyAll(Colors.red),
                            foregroundColor: MaterialStatePropertyAll(Colors.white),
                          ),
                          child: Text("Decline"),
                        ),
                      ],
                    )
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
