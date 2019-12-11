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
  String name;
  String pas;
  bool xinDian = false;
  bool wifi = false;
  bool cunka = false;
  String cunkaResult = "";
  List<String> ka = new List();

  @override
  void initState() {
    super.initState();
    initBluetooth();
//    initWrite();
//    initRead();
    yl.responseFromScan.listen((data){
      print("responseFromScan=====>"+data.address);
      setState(() {
        mac = data.name;
      });
    });

    yl.responseFromBt.listen((data){
      print("getBt=====>"+data.toStringAsPrecision(3));
      setState(() {
        ele = (data*10).toStringAsPrecision(3)+"%";
      });
    });

    yl.responseFromTF.listen((data){
      print("getTF=====>"+data.toString());
      setState(() {
        tf = data+"字节";
      });
    });

    //心电数据
    yl.responseFromXindian.listen((data){
      print("responseFromXindian=====>" + data.data1.toString());
    });
    //跳转到联系医生监听
    yl.responseFromGoLXYS.listen((data){
      if(data == "gotoLXYS"){
        showToast(data);
      }
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

    yl.responseFromCunka.listen((data){
      setState(() {
        cunkaResult = data;
      });
    });

    yl.responseFromDuka.listen((data){
      setState(() {
        ka = data.ka;
      });
    });

//设置WiFi名称监听
    yl.responseFromSetWiFiName.listen((data){
      setState(() {
        name = data;
      });
    });

//设置WiFi密码监听
    yl.responseFromSetWiFiPSW.listen((data){
      setState(() {
        pas = data;
      });
    });

//连接WiFi监听
    yl.responseFromConnWifi.listen((data){
      setState(() {
        ele = data;
      });
    });

//连接WiFi监听
    yl.responseFromWifiStatus.listen((data){
      setState(() {
        ele = data;
      });
    });

  }

  Future<void> initBluetooth() async {
    try {
      bool result = await checkBle();
      if(result == false) {
        var res = await yl.requestBlePermissionWay();
        result = await checkBle();
      }
    } on PlatformException {
    }
  }

  Future<bool> checkBle() async{
    bool result = await yl.checkBlePermissionWay();
    return result;
  }

  Future<void> initWrite() async {
    try {
      bool result = await checkWrite();
      if(result == false) {
        var res = await yl.requestWritePermissionWay();
        result = await checkWrite();
      }
    } on PlatformException {
    }
  }

  Future<bool> checkWrite() async{
    bool result = await yl.checkWritePermissionWay();
    return result;
  }

  Future<void> initRead() async {
    try {
      bool result = await checkRead();
      if(result == false) {
        var res = await yl.requestReadPermissionWay();
        result = await checkRead();
      }
    } on PlatformException {
    }
  }

  Future<bool> checkRead() async{
    bool result = await yl.checkReadPermissionWay();
    return result;
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

  void goXinDian({String filename,String name,int sex,int age,int mode,String docName,String divName,String ava}){
    yl.goXinDian(fileName: filename ,
        name: name ,
        sex: sex ,
        age: age ,
        mode: mode ,
        docName: docName ,
        divName: divName,
        ava: ava,
    ).then((result){
    });
  }

  //1、WiFi模块上电
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
  //1、设置配网模式
  void startPeiwang(){
    yl.startPeiwang().then((result){

    });
  }
  //2、检查WiFi模块上电状态
  void wifiStatus(){
    yl.wiFiEle().then((result){

      print("wiFiEle"+result.toString());
    });
  }
  //3、配置WiFi名称
  void wifiname(){
    yl.setWifiName(wifiName:"TP-LINK_9EB6").then((result){

      print("wifiname"+result.toString());
    });
  }
  //4、配置WiFi密码
  void wifiPassword(){
    yl.setWifiPSW(wifiPSW:"zjhlwmj01").then((result){

      print("wifiPassword"+result.toString());
    });
  }

  //5、设备连网
  void connWifi(){
    yl.connWiFi();
  }

  //关闭WiFi模块
  void stopWiFi(){
    yl.stopWiFi().then((result){
      setState(() {
        wifi = false;
      });
      showToast("stopWiFi");
      print("stopWiFi====>"+result.toString());

    });
  }

  void stopCunka(){
    yl.stopCunKa().then((result){
      setState(() {
        cunka = false;
      });
      showToast("stopCunka");
    });
  }

  int fileName = 20191203;
  void startCunka(String filename,String name,int sex,int age,int mode)async{
    setState(() {
      cunka = true;
      fileName++;
    });
    await yl.startCunKa(fileName: filename , name: name , sex: sex , age: age , mode: mode);
    showToast("startCunka");
  }

  void duka() {
    yl.duKa().then((value){
    });
    showToast("duka");
  }

  void duKaAndIntent() {
    yl.duKaAndIntent(position: 1).then((value){
    });
    showToast("duka");
  }


  void duKaAndIntent2() {
    yl.duKaAndIntent2(position: 6).then((value){
    });
    showToast("duka");
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.maxFinite,
      height: double.maxFinite,
      child:  Column(
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
                Center(
                  child: Text(
                    "cunka: " + cunkaResult??"",
                  ),
                ),
                Center(
                  child: Text(
                    "卡信息: " + ka.toString()??"",
                  ),
                ),
                Center(
                  child: Text(
                    "设置名称: " + name.toString()??"",
                  ),
                ),
                Center(
                  child: Text(
                    "设置密码: " + pas.toString()??"",
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            flex: 1,
            child: ListView(
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
                          onTap: (){
                              goXinDian(
                                  filename:"20191222",
                                  name:"林骏雄",
                                  sex:1,
                                  age:20,
                                  mode:0,
                                  docName:"林医生",
                                  divName:"8848",
                                  ava:"http://47.112.202.101/upload/image/201912/8ac4c536-6e47-4bd6-b2db-ee0fa8abb1c0.jpg");
                            },
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
                          onTap: wifiStatus,
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
                      onTap:(){
                        if(cunka){
                          stopCunka();
                        }else{
                          startCunka("20191256","林骏雄",1,20,0);
                        }
                      },
                      child: cunka?Text("停止存卡"):Text("开始存卡"),
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
                      onTap:duKaAndIntent2,
                      child: Text("读卡"),
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

               Row(
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
                         onTap: wifiname,
                         child: Text("name"),
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
                         onTap: wifiPassword,
                         child: Text("password"),
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
                         onTap: connWifi,
                         child: Text("conn"),
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
                      onTap: duKaAndIntent,
                      child: Text("quduka"),
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