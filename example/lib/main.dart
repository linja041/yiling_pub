import 'package:flutter/material.dart';
import 'dart:async';

import 'package:oktoast/oktoast.dart';
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
    return OKToast(
      child:MaterialApp(
        home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Test(),
        ),
      )
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

  String mac = "";
  String ele = "";
  String tf = "";
  String rtc = "";
  String wifiResult = "";
  bool xinDian = false;
  bool wifi = false;

  @override
  void initState() {
    super.initState();
    yl.responseFromScan.listen((data){
      print("responseFromScan=====>"+data.address);
      setState(() {
        mac = data.address;
      });
    });
    yl.responseFromBt.listen((data){
      print("getBt=====>"+data.toStringAsPrecision(3));
      setState(() {
        ele = data.toStringAsPrecision(3)+"%";
      });
    });
    yl.responseFromTF.listen((data){
      print("getTF=====>"+data.toString());
      setState(() {
        tf = data+"字节";
      });
    });
    yl.responseFromXindian.listen((data){
      print("responseFromXindian=====>" + data.data1.toString());
    });

    yl.responseFromRTC.listen((data){
      setState(() {
        rtc = data;
      });
    });
    yl.responseFromWiFi.listen((data){
      setState(() {
        wifiResult = data;
      });
    });
  }

  void startScan(){
    yl.startScan().then((result){
      showToast("startScan");
      print("startScanResult====>"+result.toString());
    });
  }

  void getBt(){
    yl.getBt().then((result){
      showToast("getBt");
      print("getBt====>"+result.toString());
    });
  }

  void getTF(){
    yl.getTF().then((result){
      showToast("getTF");
      print("stopXinDian====>"+result.toString());
    });
  }

  void syncRTC(){
    yl.syncRTC().then((result){
      showToast("syncRTC");
      print("syncRTC====>"+result.toString());
    });
  }

  void startXinDian(){
    yl.startXinDian().then((result){
      showToast("startXinDian");
      print("startXinDian====>"+result.toString());
      setState(() {
        xinDian = true;
      });
    });
  }

  void stopXinDian(){
    yl.stopXinDian().then((result){
      showToast("stopXinDian");
      print("stopXinDian====>"+result.toString());
      setState(() {
        xinDian = false;
      });
    });
  }

  void startWiFi(){
    setState(() {
      wifi = true;
      wifiResult = "wifi连接中...";
    });
    yl.startWiFi().then((result){
      showToast("startWiFi");
      print("startWiFi====>"+result.toString());

    });
  }

  void goPeiwang(){
    yl.goPeiwang().then((result){

    });
  }

  void stopWiFi(){
    yl.stopWiFi().then((result){
      setState(() {
        wifi = false;
      });
      showToast("stopWiFi");
      print("stopWiFi====>"+result.toString());

    });
  }

  void startPeiwang(){
    yl.startPeiwang().then((result){

    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Container(
      child:Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Expanded(
            flex: 1,
            child: Column(
              children: <Widget>[
                Center(
                  child: Text(
                    "已找到设备：" + mac
                  ),
                ),
                Center(
                  child: Text(
                      "设备电量：" + ele
                  ),
                ),
                Center(
                  child: Text(
                      "储存空间：" + tf
                  ),
                ),
                Center(
                  child: Text(
                      "RTC: " + rtc??"",
                  ),
                ),
                Center(
                  child: Text(
                    "wifi: " + wifiResult??"",
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            flex: 1,
            child: Column(
              children: <Widget>[
                Container(
                  width: 150,
                  height: 45,
                  margin: EdgeInsets.only(bottom: 5.0),
                  decoration: new BoxDecoration(
                    border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                    borderRadius: new BorderRadius.circular((5.0)), // 圆角
                  ),
                  child: Center(
                    child: GestureDetector(
                      onTap: startScan,
                      child: Text("开始扫描"),
                    ),
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Container(
                      height: 45,
                      margin: EdgeInsets.only(bottom: 5.0,right: 10.0),
                      decoration: new BoxDecoration(
                        border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                        borderRadius: new BorderRadius.circular((5.0)), // 圆角
                      ),
                      child: Center(
                        child: GestureDetector(
                          onTap: getBt,
                          child: Text("获取电量"),
                        ),
                      ),
                    ),
                    Container(
                      height: 45,
                      margin: EdgeInsets.only(bottom: 5.0,right: 10.0),
                      decoration: new BoxDecoration(
                        border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                        borderRadius: new BorderRadius.circular((5.0)), // 圆角
                      ),
                      child: Center(
                        child: GestureDetector(
                          onTap: getTF,
                          child: Text("可用存储空间"),
                        ),
                      ),
                    ),
                    Container(
                      height: 45,
                      margin: EdgeInsets.only(bottom: 5.0,right: 10.0),
                      decoration: new BoxDecoration(
                        border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                        borderRadius: new BorderRadius.circular((5.0)), // 圆角
                      ),
                      child: Center(
                        child: GestureDetector(
                          onTap: syncRTC,
                          child: Text("同步RTC"),
                        ),
                      ),
                    ),
                  ],
                ),

                Container(
                  width: 150,
                  height: 45,
                  margin: EdgeInsets.only(bottom: 5.0),
                  decoration: new BoxDecoration(
                    border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                    borderRadius: new BorderRadius.circular((5.0)), // 圆角
                  ),
                  child: Center(
                    child: GestureDetector(
                      onTap: xinDian?stopXinDian:startXinDian,
                      child: xinDian?Text("停止检测"):Text("开始检测（单导展示）"),
                    ),
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Container(
                      width: 150,
                      height: 45,
                      margin: EdgeInsets.only(bottom: 5.0),
                      decoration: new BoxDecoration(
                        border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                        borderRadius: new BorderRadius.circular((5.0)), // 圆角
                      ),
                      child: Center(
                        child: GestureDetector(
                          onTap: wifi?stopWiFi:startWiFi,
                          child: wifi?Text("关闭WiFi模块"):Text("开启WiFi模块"),
                        ),
                      ),
                    ),
                    Container(
                      width: 150,
                      height: 45,
                      margin: EdgeInsets.only(bottom: 5.0),
                      decoration: new BoxDecoration(
                        border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                        borderRadius: new BorderRadius.circular((5.0)), // 圆角
                      ),
                      child: Center(
                        child: GestureDetector(
                          onTap: startPeiwang,
                          child: Text("设置配网模式"),
                        ),
                      ),
                    ),
                  ],
                ),

                Container(
                  height: 45,
                  margin: EdgeInsets.only(bottom: 5.0,right: 10.0),
                  decoration: new BoxDecoration(
                    border: new Border.all(color: Color(0xFFFF0000), width: 2.5), // 边色与边宽度
                    borderRadius: new BorderRadius.circular((5.0)), // 圆角
                  ),
                  child: Center(
                    child: GestureDetector(
                      onTap: goPeiwang,
                      child: Text("去配网"),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      )
    );
  }

}