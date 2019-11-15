package idris.com.yiling_plugin.handler;


import idris.com.yiling_plugin.wty.nrdemo.DevManager;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class YiLingHandler {

    private static PluginRegistry.Registrar registrar = null;

    private static YiLingHandler bluetoothHandler;

    public static void setRegistrar(PluginRegistry.Registrar registrar) {
        YiLingHandler.registrar = registrar;
        bluetoothHandler = new YiLingHandler();
    }

    public static void startScan(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().startXinDian());
        DevManager.getInstance().startScan();
        result.success("startScan success");
    }

}
