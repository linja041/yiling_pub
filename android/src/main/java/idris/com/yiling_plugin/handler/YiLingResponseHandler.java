package idris.com.yiling_plugin.handler;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class YiLingResponseHandler {
    private static MethodChannel channel = null;

    public static void setMethodChannel(MethodChannel channel) {
        YiLingResponseHandler.channel = channel;
    }

    public static void sendScanResult(Map<String,Object> result){
        channel.invokeMethod("sendScanResult",result);
    }
}
