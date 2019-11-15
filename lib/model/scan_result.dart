
/// 2019/11/15
/// @author Idris
class ScanResult {
  //蓝牙地址
  final String address;
  //信号
  final String rssi;
  //设备名称？
  final String name;

  ScanResult.formMap(Map map)
    : address = map["address"],
        rssi = map["rssi"],
        name = map["name"];
}