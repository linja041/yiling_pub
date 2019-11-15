package idris.com.yiling_plugin;

import com.inuker.bluetooth.library.BluetoothClientImpl;

import idris.com.yiling_plugin.handler.YiLingHandler;
import idris.com.yiling_plugin.handler.YiLingRequestHandler;
import idris.com.yiling_plugin.handler.YiLingResponseHandler;
import idris.com.yiling_plugin.wty.nrdemo.MyApplication;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** YilingPlugin */
public class YilingPlugin implements MethodCallHandler {
  /** Plugin registration. */

  Registrar registrar;
  MethodChannel channel;

  private YilingPlugin(Registrar registrar, MethodChannel channel){
    this.registrar = registrar;
    this.channel = channel;
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "yiling_plugin");
    YiLingHandler.setRegistrar(registrar);
    YiLingRequestHandler.setRegistrar(registrar);
    YiLingResponseHandler.setMethodChannel(channel);
    MyApplication.setInstance(registrar);
    channel.setMethodCallHandler(new YilingPlugin(registrar, channel));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }  else if (call.method.equals("startScan")) {
      YiLingHandler.startScan(call,result);
    }else {
      result.notImplemented();
    }
  }
}
