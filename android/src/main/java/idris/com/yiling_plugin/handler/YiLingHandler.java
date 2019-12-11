package idris.com.yiling_plugin.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import idris.com.yiling_plugin.activity.EsptouchDemoActivity;
import idris.com.yiling_plugin.activity.PeiwangActivity;
import idris.com.yiling_plugin.activity.ShowXinDianActivity;
import idris.com.yiling_plugin.activity.ViewDKAct;
import idris.com.yiling_plugin.wty.nrdemo.DevManager;
import idris.com.yiling_plugin.wty.nrdemo.util.ECGReportAdapter;
import idris.com.yiling_plugin.wty.nrdemo.util.FileSave;
import idris.com.yiling_plugin.wty.nrdemo.util.UUID8;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class YiLingHandler {

    static String fileName;
    static String name;
    static String docName;
    static String divName;
    static String ava;
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
        result.success("success");
    }

    /**
     * 开始检测（去Android显示）
     * @param call
     * @param result
     */
    public static void goXinDian(MethodCall call, MethodChannel.Result result) {
        if (call != null) {
            fileName = call.argument("fileName");
            name = call.argument("name");
            docName = call.argument("docName");
            divName = call.argument("divName");
            ava = call.argument("ava");
            if(fileName == null){
                fileName = UUID8.generateShortUuid1();
            }
            if(name == null){
                name = UUID8.getAccountIdByUUId();
            }
            int s = (int) call.argument("sex");
            int a = (int) call.argument("age");
            int m = (int) call.argument("mode");
            sex = (byte) s;
            age  = (byte) a;
            mode = (byte) m;
        }
        Intent intent = new Intent(registrar.activity(), ShowXinDianActivity.class);
        intent.putExtra("fileName",fileName);
        intent.putExtra("docName",docName);
        intent.putExtra("divName",divName);
        intent.putExtra("ava",ava);
        intent.putExtra("name",name);
        intent.putExtra("sex",sex);
        intent.putExtra("age",age);
        intent.putExtra("mode",mode);
        registrar.activity().startActivity(intent);
        result.success("success");
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
     * sex : 1 男 : 0 女 不传默认0
     * age : 年龄 不传默认18
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
            int s = (int) call.argument("sex");
            int a = (int) call.argument("age");
            int m = (int) call.argument("mode");
            sex = (byte) s;
            age  = (byte) a;
            mode = (byte) m;
            System.out.print("-------------------->存卡信息：fileName = " + fileName + " name = " + name + " sex = " + sex + " age = " + age + " mode: " + mode+"<--------------------");
            byte[] data = DevManager.getInstance().startCK(fileName, name, sex, age, (byte) 0);
            DevManager.getInstance().writeEMS(data);

            FileSave.saveFileNameList(registrar.context(),  name + "_" +sex + "_" + age, fileName);
            result.success("success");
        }
    }

    /**
     * 停止存卡
     * @param call
     * @param result
     */
    public static void stopCunKa(MethodCall call, MethodChannel.Result result){
        DevManager.getInstance().writeEMS(DevManager.getInstance().stopCK());
        result.success("success");
    }

    /**
     * 启动WiFi模块
     * @param call
     * @param result
     */
    public static void startWiFi(MethodCall call, MethodChannel.Result result){
        //启动WiFi模块
        System.out.println("------------------->startWiFi on Handler startWiFi on Handler startWiFi on Handler<--------------------");
        DevManager.getInstance().writeEMS(DevManager.getInstance().startWifi((byte) 0));
        //设置配网模式
        result.success("success");
    }

    /**
     * 设置配网
     * @param call
     * @param result
     */
    public static void startPeiwang(MethodCall call, MethodChannel.Result result){
        //设置配网模式
        DevManager.getInstance().writeEMS(DevManager.getInstance().setWifiMode());
        result.success("success");
    }

    /**
     * 停止WiFi模块
     * @param call
     * @param result
     */
    public static void stopWiFi(MethodCall call, MethodChannel.Result result){
        DevManager.getInstance().writeEMS(DevManager.getInstance().startWifi((byte) 1));
        result.success("success");
    }

    /**
     * 读卡
     * @param call
     * @param result
     */
    public static void duKa(MethodCall call, MethodChannel.Result result){
        final String[] daa = FileSave.getFileNameList(registrar.context());
        List<String> beans = new ArrayList<>();
        if (daa != null) {
            List<String> tem = Arrays.asList(daa);
//            beans =  new ArrayList<String> (daa);
            System.out.print(tem.get(0));
            for(int i = 0; i < tem.size() ; i++){
                beans.add(tem.get(i));
            }
            System.out.print(beans.toString());
            ECGReportAdapter reportAdapter = new ECGReportAdapter(registrar.context(), beans);
        }
        System.out.println("-------------------->读卡信息 :" + daa + "<--------------------");

        YiLingResponseHandler.kaResult(beans);
    }

    /**
     * 读并跳转
     * @param call
     * @param result
     */
    public static void duKaAndIntent(MethodCall call, MethodChannel.Result result){
        final String[] daa = FileSave.getFileNameList(registrar.context());
        List<String> beans = new ArrayList<>();
        if (daa != null) {
            List<String> tem = Arrays.asList(daa);
//            beans =  new ArrayList<String> (daa);
            System.out.print(tem.get(0));
            for(int i = 0; i < tem.size() ; i++){
                beans.add(tem.get(i));
            }
            System.out.print(beans.toString());
            ECGReportAdapter reportAdapter = new ECGReportAdapter(registrar.context(), beans);
        }
        int position = call.argument("position");
        Intent intent = new Intent(registrar.activity(), ViewDKAct.class);
        intent.putExtra("data", beans.get(position));
        registrar.activity().startActivity(intent);
    }

    /**
     * 读并跳转
     * @param call
     * @param result
     */
    public static void duKaAndIntent2(MethodCall call, MethodChannel.Result result){
        int position = call.argument("position");
        final String[] daa = FileSave.getFileNameList(registrar.context());
        List<String> beans = new ArrayList<>();
        if (daa != null) {
            List<String> tem = Arrays.asList(daa);
//            beans =  new ArrayList<String> (daa);
            System.out.print(tem.get(0));
            for(int i = 0; i < tem.size() ; i++){
                beans.add(tem.get(i));
            }
            System.out.print(beans.toString());
            ECGReportAdapter reportAdapter = new ECGReportAdapter(registrar.context(), beans);
        }
        Intent intent = new Intent(registrar.activity(), EsptouchDemoActivity.class);
        intent.putExtra("data", beans.get(position));
        registrar.activity().startActivity(intent);
    }

    /**
     * 设置WiFi名称
     * @param call
     * @param result
     */
    public static void setWiFiName(MethodCall call, MethodChannel.Result result) {
        String wifiName = call.argument("wifiName");
        if(wifiName != null){
            DevManager.getInstance().writeEMS(DevManager.getInstance().setWifiName(wifiName));
            result.success(wifiName+" = set success");
        }else{
            result.success("wifiName was null");
        }
    }

    /**
     * 设置WiFi密码
     * @param call
     * @param result
     */
    public static void setWiFiPSW(MethodCall call, MethodChannel.Result result) {
        String wifiPSW = call.argument("wifiPSW");
        if(wifiPSW != null){
            DevManager.getInstance().writeEMS(DevManager.getInstance().setWifiPSW(wifiPSW));
            result.success(wifiPSW+" = set success");
        }else{
            result.success("wifiPSW was null");
        }
    }

    /**
     * 连接WiFi
     * @param call
     * @param result
     */
    public static void connWifi(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().connWifi());
        result.success("success");
    }

    /**
     * 查询上电
     * @param call
     * @param result
     */
    public static void wiFiEle(MethodCall call, MethodChannel.Result result) {
        DevManager.getInstance().writeEMS(DevManager.getInstance().wifiStatus());
        result.success("success");
    }

    /**
     * 配置服务器地址和端口
     * @param call
     * @param result
     */
    public static void setIp(MethodCall call, MethodChannel.Result result) {
//        DevManager.getInstance().writeEMS(DevManager.getInstance().setIpPort());
        result.success("success");
    }

    //蓝牙权限申请
    public static void checkBlePermissionWay(MethodCall call, MethodChannel.Result result) {
        boolean isPermission = checkBlePermission(registrar.context());
        result.success(isPermission);
    }

    public static void requestBlePermissionWay(MethodCall call, final MethodChannel.Result result) {
        requestBlePermission(registrar.activity());
        registrar.addRequestPermissionsResultListener(new PluginRegistry.RequestPermissionsResultListener() {
            @Override
            public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
                if (i == 1) {
                    registrar.activity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.success("success");
                        }
                    });
                } else {
                    result.success("error");
                }
                return false;
            }
        });
    }

    public static boolean checkBlePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean bPermission = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0;
            if (bPermission) {
                ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
            }

            return bPermission;
        } else {
            return true;
        }
    }

    public static void requestBlePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean bPermission = checkBlePermission(activity.getApplicationContext());
            if (!bPermission) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{"android.permission.ACCESS_COARSE_LOCATION",
                                "android.permission.WRITE_EXTERNAL_STORAGE",
                                "android.permission.READ_EXTERNAL_STORAGE"}, 1);
            }
        }

    }

    //写入权限申请
    public static void checkWritePermissionWay(MethodCall call, MethodChannel.Result result) {
        boolean isPermission = checkWritePermission(registrar.context());
        result.success(isPermission);
    }

    public static void requestWritePermissionWay(MethodCall call, final MethodChannel.Result result) {
        requestWritePermission(registrar.activity());
        registrar.addRequestPermissionsResultListener(new PluginRegistry.RequestPermissionsResultListener() {
            @Override
            public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
                if (i == 1) {
                    registrar.activity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.success("success");
                        }
                    });
                } else {
                    result.success("error");
                }
                return false;
            }
        });
    }

    public static boolean checkWritePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean bPermission = ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
            if (bPermission) {
                ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
            }

            return bPermission;
        } else {
            return true;
        }
    }

    public static void requestWritePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean bPermission = checkBlePermission(activity.getApplicationContext());
            if (!bPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }
        }

    }

    //读权限申请
    public static void checkReadPermissionWay(MethodCall call, MethodChannel.Result result) {
        boolean isPermission = checkReadPermission(registrar.context());
        result.success(isPermission);
    }

    public static void requestReadPermissionWay(MethodCall call, final MethodChannel.Result result) {
        requestReadPermission(registrar.activity());
        registrar.addRequestPermissionsResultListener(new PluginRegistry.RequestPermissionsResultListener() {
            @Override
            public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
                if (i == 1) {
                    registrar.activity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.success("success");
                        }
                    });
                } else {
                    result.success("error");
                }
                return false;
            }
        });
    }

    public static boolean checkReadPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean bPermission = ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0;
            if (bPermission) {
                ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE");
            }

            return bPermission;
        } else {
            return true;
        }
    }

    public static void requestReadPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean bPermission = checkBlePermission(activity.getApplicationContext());
            if (!bPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
            }
        }

    }

}
