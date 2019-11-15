import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:yiling_plugin/yiling_plugin.dart' as yl;

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    yl.responseFromScan.listen((data){
      print("responseFromScan=====1"+data.toString());
    });
  }

  void startScan(){
    yl.startScan().then((result){
      print("startScanResult===="+result.toString());
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: GestureDetector(
            onTap: startScan,
            child: Container(
              width: 150,
              height: 75,
              child: Center(
                child: Text(
                  "开始扫描",
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
