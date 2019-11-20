package idris.com.yiling_plugin.handler;

import java.util.Timer;

import idris.com.yiling_plugin.wty.nrdemo.DevManager;
import idris.com.yiling_plugin.wty.nrdemo.util.FileSave;
import idris.com.yiling_plugin.wty.nrdemo.util.UUID8;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class YiLingHandler {

    static String fileName;
    static String name;
    static byte sex;
    static byte age;
    static byte mode;
    private static Timer timer;
    static int count = 10;

    private static PluginRegistry.Registrar registrar = null;

    private static YiLingHandler bluetoothHandler;

    public static void setRegistrar(PluginRegistry.Registrar registrar) {
        YiLingHandler.registrar = registrar;
        bluetoothHandler = new YiLingHandler();
    }

    public static void startScan(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().startScan();

        result.success("startScan success");
    }

    /**
     * 获取电量
     * @param call
     * @param result
     */
    public static void getBt(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().getBt());
        result.success("getBt success");
    }

    /**
     * TF卡剩余空间（字节）
     * @param call
     * @param result
     */
    public static void getTF(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().getTF());
        result.success("getTF success");
    }

    /**
     * 同步RTC
     * @param call
     * @param result
     */
    public static void syncRTC(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().syncRTC());
        result.success("syncRTC success");
    }

    /**
     * 开始检测
     * @param call
     * @param result
     */
    public static void startXinDian(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().startXinDian());
        result.success("getBt startXinDian");
    }

    /**
     * 停止检测
     * @param call
     * @param result
     */
    public static void stopXinDian(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().stopXinDian());
        result.success("getBt stopXinDian");
    }

    /**
     * 存卡 需要传入性别年龄姓名文件名mode
     * name : 姓名长度小于16，默认系统随机
     * fileName : 文件名长度小于8，默认系统随机
     * sex : 1 男 : 0 女 必传
     * age : 年龄 必传
     * mode : 0 250hz16bit : 1 125hz16bit : 2 250hz8bit : 3 123hz8bit 建议传0
     */
    public static void startCunKa(MethodCall call, MethodChannel.Result result) {
        if (call != null) {
            fileName = call.argument("fileName");
            name = call.argument("name");
            if(fileName == null){
                fileName = UUID8.generateShortUuid1();
            }
            if(name == null){
                name = UUID8.getAccountIdByUUId();
            }
            sex = (byte) call.argument("sex");
            age  = (byte) call.argument("age");
            mode = (byte) call.argument("mode");
            System.out.print("-------------------->存卡信息：fileName = " + fileName + " name = " + name + " sex = " + age + " age = " + name + " mode: " + mode+"<--------------------");
            byte[] data = DevManager.getInstance().startCK(fileName, name, sex, age, (byte) 0);
            DevManager.getInstance().writeEMS(data);

            FileSave.saveFileNameList(registrar.context(), name + "_" + sex + "_" + age, fileName);
            result.success("success");
        }
    }

    /**
     * 停止存卡
     * @param call
     * @param result
     */
    public static void stopCunKa(MethodCall call, MethodChannel.Result result){
        DevManager.getInstance().writeEMS(DevManager.getInstance().stopXinDian());
    }

    /**
     * 读卡
     * @param call
     * @param result
     */
    public static void duKa(MethodCall call, MethodChannel.Result result){
        final String[] daa = FileSave.getFileNameList(registrar.context());
        System.out.print("-------------------->读卡信息 :" + daa + "<--------------------");
    }

}
