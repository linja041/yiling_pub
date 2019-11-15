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
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Test(),
      ),
    );
  }
}

class Test extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return TestState();
  }

}

class TestState extends State<Test>{

  @override
  void initState() {
    super.initState();
    yl.responseFromScan.listen((data){
      print("responseFromScan=====1"+data.address);
    });
  }

  void startScan(){
    yl.startScan().then((result){
      print("startScanResult===="+result.toString());
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Container(
      child: Center(
        child: GestureDetector(
          onTap: startScan,
          child: Text("开始扫描"),
        ),
      ),
    );
  }

}