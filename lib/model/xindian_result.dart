/// 2019/11/15
/// @author Idris

class XindianResult {
   List data1 = new List(10);
   List data2 = new List(10);
   List data11 = new List(10);
   List data12 = new List(10);
   List data13 = new List(10);
   List data14 = new List(10);
   List data15 = new List(10);
   List data16 = new List(10);
   //心率
   int hr;
   int xy;
   int ml;
   int xyWave;
   int dy;
   //true:脱落，false:未脱落
   bool isTuo;
   //true:心室大类诊断正常，false:心室大类诊断异常
   bool isNormal;
   //0"未插入适配器未充电"
   //1"充电中"
   //else"充电完成"
   int isCharging;

   XindianResult.formMap(Map map)
       : data1 = map["data1"],
          data2 = map["data2"],
          data11 = map["data11"],
          data12 = map["data12"],
          data13 = map["data13"],
          data14 = map["data14"],
          data15 = map["data15"],
          data16 = map["data16"],
          hr = map["hr"],
          xy = map["xy"],
          ml = map["ml"],
          xyWave = map["xyWave"],
          dy = map["dy"],
          isTuo = map["isTuo"],
          isNormal = map["isNormal"],
          isCharging = map["isCharging"];
}


