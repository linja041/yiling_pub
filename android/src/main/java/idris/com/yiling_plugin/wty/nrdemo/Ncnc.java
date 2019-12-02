package idris.com.yiling_plugin.wty.nrdemo;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import io.flutter.app.FlutterActivity;

public class Ncnc extends FlutterActivity {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private void openBuleTooth(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Andorid M Permission check
            if (this.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

}
