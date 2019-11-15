import 'dart:async';

import 'package:flutter/services.dart';
import 'package:yiling_plugin/model/scan_result.dart';

class YilingPlugin {
}
final MethodChannel _channel =
const MethodChannel('yiling_plugin')..setMethodCallHandler(_handler);

Future<String> get platformVersion async {
  final String version = await _channel.invokeMethod('getPlatformVersion');
  return version;
}

///开始扫描
Future startScan() async {
  String result = await _channel.invokeMethod("startScan");
  return result;
}

///扫描结果
StreamController<ScanResult> _scanResultController = new StreamController.broadcast();

Stream<ScanResult> get responseFromScan => _scanResultController.stream;


Future<dynamic> _handler(MethodCall methodCall) {
  if ("sendScanResult" == methodCall.method) {
    print("``````````````````````````````````````");
    _scanResultController
        .add(ScanResult.formMap(methodCall.arguments));
  }
  return Future.value(true);
}