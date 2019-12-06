
/// 2019/12/03
/// @author Idris

class kaResult{
  final List<String> ka;
  kaResult.fromList(List list)
      : ka = _getListMessage(list);
}
List<String> _getListMessage(List list) {
  List<String> listMsg = new List();
  list.forEach((value) {
    listMsg.add(value.toString());
  });
  return listMsg;
}