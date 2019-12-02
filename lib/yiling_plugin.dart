import 'dart:async';

import 'package:flutter/services.dart';
import 'package:yiling_plugin/model/scan_result.dart';

import 'model/xindian_result.dart';

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

///获取电量
Future getBt() async {
  String result = await _channel.invokeMethod("getBt");
  return result;
}

///获取剩余存储空间
Future getTF() async {
  String result = await _channel.invokeMethod("getTF");
  return result;
}

///同步rtc
Future syncRTC() async {
  String result = await _channel.invokeMethod("syncRTC");
  return result;
}

///单导展示，开始检测
Future startXinDian() async {
  String result = await _channel.invokeMethod("startXinDian");
  return result;
}

///单导展示，停止检测
Future stopXinDian() async {
  String result = await _channel.invokeMethod("stopXinDian");
  return result;
}

///启动WiFi模块
Future startWiFi() async {
  String result = await _channel.invokeMethod("startWiFi");
  return result;
}

///停止WiFi模块
Future stopWiFi() async {
  String result = await _channel.invokeMethod("stopWiFi");
  return result;
}

///存卡
Future startCunKa({String fileName,String name,int sex,int age,int mode}) async {
  String result = await _channel.invokeMethod("startCunKa",{
    "fileName" : fileName,
    "name" : name,
    "sex" : sex,
    "age" : age,
    "mode" : mode,
  });
  return result;
}

///停止存卡
Future stopCunKa() async {
  String result = await _channel.invokeMethod("stopCunKa");
  return result;
}

///读卡
Future duKa() async {
  String result = await _channel.invokeMethod("duKa");
  return result;
}

///启动配网模式
Future startPeiwang() async {
  String result = await _channel.invokeMethod("startPeiwang");
  return result;
}

///去配网
Future goPeiwang() async {
  String result = await _channel.invokeMethod("goPeiwang");
  return result;
}

///检查蓝牙权限
Future<bool> checkBlePermissionWay() async {
  bool result = await _channel.invokeMethod("checkBlePermissionWay", {});
  return result;
}

///打开蓝牙权限
Future requestBlePermissionWay() async {
  String result = await _channel.invokeMethod("requestBlePermissionWay", {});
  return result;
}


///扫描结果
StreamController<ScanResult> _scanResultController = new StreamController.broadcast();

Stream<ScanResult> get responseFromScan => _scanResultController.stream;

///电量
StreamController<double> _btResultController = new StreamController.broadcast();

Stream<double> get responseFromBt => _btResultController.stream;

///储存空间
StreamController<String> _tfResultController = new StreamController.broadcast();

Stream<String> get responseFromTF => _tfResultController.stream;

///RTC
StreamController<String> _rtcResultController = new StreamController.broadcast();

Stream<String> get responseFromRTC => _rtcResultController.stream;

///心电扫描结果
StreamController<XindianResult> _xindianResultController = new StreamController.broadcast();

Stream<XindianResult> get responseFromXindian => _xindianResultController.stream;

///WiFi配置结果
StreamController<String> _WiFiResultController = new StreamController.broadcast();

Stream<String> get responseFromWiFi => _WiFiResultController.stream;

///cunka
StreamController<String> _cunkaResultController = new StreamController.broadcast();

Stream<String> get responseFromCunka => _cunkaResultController.stream;

///duka
StreamController<List<String>> _dukaResultController = new StreamController.broadcast();

Stream<List<String>> get responseFromDuka => _dukaResultController.stream;

Future<dynamic> _handler(MethodCall methodCall) {
  if ("sendScanResult" == methodCall.method) {
    _scanResultController
        .add(ScanResult.formMap(methodCall.arguments));
  }else if ("sendBtResult" == methodCall.method) {
    _btResultController
        .add(methodCall.arguments);
  }else if ("sendTFResult" == methodCall.method) {
    _tfResultController
        .add(methodCall.arguments.toString());
  }else if ("sendRTCResult" == methodCall.method) {
    _rtcResultController
        .add(methodCall.arguments.toString());
  }else if ("startXinDian" == methodCall.method) {
    _xindianResultController
        .add(XindianResult.formMap(methodCall.arguments));
  }else if ("wifiResult" == methodCall.method) {
    _WiFiResultController
        .add(methodCall.arguments);
  }else if ("cunkaResult" == methodCall.method) {
    _cunkaResultController
        .add(methodCall.arguments);
  }else if ("kaResult" == methodCall.method) {
    print(methodCall.arguments.toString());
    _dukaResultController
        .add(methodCall.arguments);
  }
  return Future.value(true);

}