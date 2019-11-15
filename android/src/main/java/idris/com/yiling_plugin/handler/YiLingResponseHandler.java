package idris.com.yiling_plugin.handler;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;

public class YiLingResponseHandler {
    private static MethodChannel channel = null;

    public static void setMethodChannel(MethodChannel channel) {
        YiLingResponseHandler.channel = channel;
    }

    public static void sendScanResult(HashMap<String,Object> result){
        channel.invokeMethod("sendScanResult",result);
    }
}
