import 'package:flutter/material.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Scaffold(
        appBar: final AppBar(
          title: Text('My First Flutter App')
        ),
        body: Center(
          child: Text('Hello Alu!!!'),
        ),
      ),
    );
  }
}
