package idris.com.yiling_plugin.wty.nrdemo;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import io.flutter.app.FlutterActivity;

public class Ncnc extends FlutterActivity {

    //初始化
    private BluetoothManager bluetoothManager = null;
    //蓝牙适配器
    private BluetoothAdapter bluetoothAdapter = null;

    //是否支持蓝牙

    private boolean supportBuleTooth(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager=(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothAdapter=bluetoothManager.getAdapter();
        }
        //不支持蓝牙
        if (bluetoothAdapter==null){
            return false;
        }
        return true;
    }

    //打开蓝牙
    private void openBuleTooth(){
        //判断蓝牙是否开启
        Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enabler,1);
    }

    //判断蓝牙是否已经开启
    private boolean disabled(){
        if(bluetoothAdapter.isEnabled()){
            return true;
        }
        return false;
    }

}
