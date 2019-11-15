package idris.com.yiling_plugin.wty.nrdemo;

import android.app.Application;
import android.content.Context;

import com.inuker.bluetooth.library.BluetoothContext;

import io.flutter.plugin.common.PluginRegistry;

/**
 * Created by Administrator on 2018/4/26.
 */

public class MyApplication {

    public static void setInstance(PluginRegistry.Registrar registrar) {
        sInstance = registrar.context();
        BluetoothContext.set(sInstance);
    }

    public static Context sInstance;

    public static Context getInstance() {
        return sInstance;
    }
}
