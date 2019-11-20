package idris.com.yiling_plugin.handler;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;

public class YiLingResponseHandler {
    private static MethodChannel channel = null;

    /**
     *
     * @param channel
     */
    public static void setMethodChannel(MethodChannel channel) {
        YiLingResponseHandler.channel = channel;
    }

    /**
     * 扫描结果
     * @param result
     */
    public static void sendScanResult(HashMap<String,Object> result){
        channel.invokeMethod("sendScanResult",result);
    }

    /**
     * 电量
     * @param result
     */
    public static void sendBtResult(double result){
        channel.invokeMethod("sendBtResult",result);
    }

    /**
     * 存储空间
     * @param result
     */
    public static void sendTFResult(int result){
        channel.invokeMethod("sendTFResult",result);
    }

    /**
     * RTC
     * @param result
     */
    public static void sendRTCResult(String result){
        channel.invokeMethod("sendRTCResult",result);
    }

    /**
     * 心电结果
     * @param result
     */
    public static void startXindian(HashMap<String,Object> result){
        channel.invokeMethod("startXindian",result);
    }


    /**
     * 存卡结果
     * @param result
     */
    public static void cunkaResult(String result){
        channel.invokeMethod("cunkaResult",result);
    }
}
