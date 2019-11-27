package idris.com.yiling_plugin.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import idris.com.yiling_plugin.R;
import idris.com.yiling_plugin.espressif.EspUtils;
import idris.com.yiling_plugin.espressif.iot.esptouch.EsptouchTask;
import idris.com.yiling_plugin.espressif.iot.esptouch.IEsptouchListener;
import idris.com.yiling_plugin.espressif.iot.esptouch.IEsptouchResult;
import idris.com.yiling_plugin.espressif.iot.esptouch.IEsptouchTask;
import idris.com.yiling_plugin.espressif.iot.esptouch.task.__IEsptouchTask;
import idris.com.yiling_plugin.espressif.iot.esptouch.util.ByteUtil;
import idris.com.yiling_plugin.espressif.iot.esptouch.util.EspNetUtil;
import idris.com.yiling_plugin.wty.nrdemo.ClientThread;
import idris.com.yiling_plugin.wty.nrdemo.DevManager;
import idris.com.yiling_plugin.wty.nrdemo.util.ACache;

public class PeiwangActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PeiwangActivity";

    private static final int REQUEST_PERMISSION = 0x01;

    private TextView mApSsidTV;
    private TextView mApBssidTV;
    private EditText mApPasswordET;
    private EditText mDeviceCountET;
    private RadioGroup mPackageModeGroup;
    private static TextView mMessageTV;
    private static Button mConfirmBtn;
    private static ClientThread cli;
    static long count = 0;
    static long tempCount = 0;

    //识别头4个字节需要四个变量
    Integer head1Byte = null;
    Integer head2Byte = null;


    int headNub = 2;//识别头字节数

    String mBssid;
    final int HEAD_1 = 0xfd;//识别头值
    final int HEAD_2 = 0xa7;

    int tempDataCount = 0;//临时拷贝统计
    int allDataCount = 0;//除了识别头总共的数据位个数
    byte[] onePack = null;

    ExecutorService executorService1 = Executors.newSingleThreadExecutor();
    /* private Handler handler = new Handler() {
         @Override
         public void handleMessage( Message msg) {
             super.handleMessage(msg);
             if (msg.what == 0) {
                 Log.d("debug", "socket连接成功: ");
                 tv.append("\n连接成功");
                 count = 0;
                 handler.sendEmptyMessageDelayed(99, 2500);
             }
             if (msg.what == 3) {
                 final byte[] bytes = (byte[]) msg.obj;
                 count += bytes.length;
                 tv.setText("读取" + count + "字节");

                 executorService1.execute(new Runnable() {
                     @Override
                     public void run() {


                         for (int i = 0; i < bytes.length; i++) {

                             if (head1Byte != null && head2Byte != null) {
                                 if (tempDataCount == 0) {
                                     allDataCount = 1038;
                                     onePack = new byte[headNub + allDataCount];
                                     onePack[0] = (byte) HEAD_1;
                                     onePack[1] = (byte) HEAD_2;

                                 }

                                 int resumeCount = allDataCount - tempDataCount;
                                 if (bytes.length - i < resumeCount) {
                                     System.arraycopy(bytes, i, onePack, headNub + tempDataCount, bytes.length - i);
                                     tempDataCount += bytes.length - i;
                                     break;
                                 } else {

                                     System.arraycopy(bytes, i, onePack, headNub + tempDataCount, resumeCount);
                                     i += (resumeCount - 1);
                                     tempDataCount += resumeCount;

                                     if (tempDataCount == allDataCount) {
                                         tempDataCount = 0;
                                         allDataCount = 0;
                                         head1Byte = null;
                                         head2Byte = null;

                                         //组包完成，可以把onepack抛出去
                                         Log.d("debug", onePack.length + "package: " + ByteUtils.toHexString(onePack, " "));
                                         int size=1024;
                                         byte[] ecgD = new byte[size];
                                         System.arraycopy(onePack, 15, ecgD, 0, size);
                                         FileSave.saveEcgData(PeiwangActivity.this, dataY,ecgD );
                                     }
                                     continue;
                                 }


                             }
                             if (head1Byte == null && (bytes[i] & 0xff) == HEAD_1) {
                                 head1Byte = HEAD_1;
                                 continue;
                             }

                             if (head2Byte == null && head1Byte != null && (bytes[i] & 0xff) != HEAD_2) {
                                 head1Byte = null;
                                 continue;
                             }

                             if (head2Byte == null && head1Byte != null && (bytes[i] & 0xff) == HEAD_2) {
                                 head2Byte = HEAD_2;
                                 continue;
                             }


                         }



                         */
    /*int size=1024;
                        byte[] ecgD = new byte[size];
                        System.arraycopy(onePack, 15, ecgD, 0, size);
                        FileSave.saveEcgData(PeiwangActivity.this, dataY,ecgD );*/
    /*
                    }
                });



            }
            if (msg.what == 99) {
                tv.append("\n正在读取，请稍后...");
                String fileName = "ABCDEFGH";
                byte[] xinDian_end = new byte[23];
                xinDian_end[0] = (byte) 0xFD;
                xinDian_end[1] = (byte) 0xA6;
                for (int i = 0; i < 8; i++) {
//                    xinDian_end[2 + i] = data1[4].getBytes()[i];
                    xinDian_end[2 + i] = fileName.getBytes()[i];
                }
                xinDian_end[10] = 0;
                xinDian_end[11] = 0;
                xinDian_end[12] = 0;
                xinDian_end[13] = 0;

                xinDian_end[22] = DevManager.Crc7Chksum(xinDian_end, 23);
//                byte[] data = {(byte) 0xFD, (byte) 0xA6, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 0x43};
                Log.d("debug", "write: " + ByteUtils.toHexString(xinDian_end, " "));
                cli.write(xinDian_end);

                if (timer != null){
                    timer.cancel();
                }
                timer=new Timer();
                timer.schedule(timerTask, 10 * 1000, 1000 * 10);


            }

            if (msg.what == 1 || msg.what == 2) {
                tv.append("\n连接失败");

                Log.d("debug", "连接失败: ");
            }

        }
    };*/
   /* Timer timer;
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (count == tempCount) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        tv.append("\n读取结束");
                        FileSave.deleteFileNameList(PeiwangActivity.this, dataY);
                    }
                });

            } else {
                tempCount = count;

            }
        }
    };*/
    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    private EsptouchAsyncTask4 mTask;

    private boolean mReceiverRegistered = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    WifiInfo wifiInfo;
                    if (intent.hasExtra(WifiManager.EXTRA_WIFI_INFO)) {
                        wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    } else {
                        wifiInfo = wifiManager.getConnectionInfo();
                    }
                    onWifiChanged(wifiInfo);
                    break;
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(wifiManager.getConnectionInfo());
                    onLocationChanged();
                    break;
            }
        }
    };

    private boolean mDestroyed = false;
    private static TextView tv;
    //    static String[] data1;
//    String dataY;
    private Button confirmBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peiwang);
       /* dataY = getIntent().getStringExtra("data");
        data1 = dataY.split("_");*/

        mApSsidTV = (TextView) findViewById(R.id.ap_ssid_text);
//        mApBssidTV = (TextView) findViewById(R.id.ap_bssid_text);
        mApPasswordET = (EditText) findViewById(R.id.ap_password_edit);
//        mDeviceCountET = (EditText) findViewById(R.id.device_count_edit);
//        mDeviceCountET.setText("1");
        mPackageModeGroup = (RadioGroup) findViewById(R.id.package_mode_group);
        mMessageTV = (TextView) findViewById(R.id.message);
        mConfirmBtn = (Button) findViewById(R.id.confirm_btn);
        mConfirmBtn.setEnabled(false);
        mConfirmBtn.setOnClickListener(this);



        confirmBtn1 = (Button) findViewById(R.id.confirm_btn1);
        confirmBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cli!=null){
                    cli.write(DevManager.getInstance().testIpConn());
                }
            }
        });

        tv = (TextView) findViewById(R.id.tv);

     /*   TextView versionTV = (TextView) findViewById(R.id.version_tv);
        versionTV.setText(IEsptouchTask.ESPTOUCH_VERSION);*/

        if (isSDKAtLeastP()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
            } else {
                registerBroadcastReceiver();
            }

        } else {
            registerBroadcastReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!mDestroyed) {
                        registerBroadcastReceiver();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cli != null) {
            cli.close();
        }
       /* if (timer != null)
            timer.cancel();*/
        mDestroyed = true;
        if (mReceiverRegistered) {
            unregisterReceiver(mReceiver);
        }
    }

    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        registerReceiver(mReceiver, filter);
        mReceiverRegistered = true;
    }

    private void onWifiChanged(WifiInfo info) {
        if (info == null) {
            mApSsidTV.setText("");
            mApSsidTV.setTag(null);
            mApBssidTV.setTag("");
            mMessageTV.setText("");
            mConfirmBtn.setEnabled(false);

            if (mTask != null) {
                mTask.cancelEsptouch();
                mTask = null;
                new AlertDialog.Builder(PeiwangActivity.this)
                        .setMessage("Wifi disconnected or changed")
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            mApSsidTV.setText(ssid);
            mApSsidTV.setTag(ByteUtil.getBytesByString(ssid));
            byte[] ssidOriginalData = EspUtils.getOriginalSsidBytes(info);
            mApSsidTV.setTag(ssidOriginalData);

            mBssid = info.getBSSID();
//            mApBssidTV.setText(bssid);

            mConfirmBtn.setEnabled(true);
            mMessageTV.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequence = info.getFrequency();
                if (frequence > 4900 && frequence < 5900) {
                    // Connected 5G wifi. Device does not support 5G
                    mMessageTV.setText("设备不支持5GWiFi");
                }
            }
        }
    }

    private void onLocationChanged() {
        boolean enable;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            enable = false;
        } else {
            boolean locationGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean locationNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            enable = locationGPS || locationNetwork;
        }

        if (!enable) {
            mMessageTV.setText("请打开定位权限");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmBtn) {
            byte[] ssid = mApSsidTV.getTag() == null ? ByteUtil.getBytesByString(mApSsidTV.getText().toString())
                    : (byte[]) mApSsidTV.getTag();
            byte[] password = ByteUtil.getBytesByString(mApPasswordET.getText().toString());
            Log.i("dsbdsbdsbdsbdsb===",mApPasswordET.getText().toString());
            byte[] bssid = EspNetUtil.parseBssid2bytes(mBssid);
            byte[] deviceCount = "1".getBytes();
            byte[] broadcast = {(byte) (1)};

            if (mTask != null) {
                mTask.cancelEsptouch();
            }
            mTask = new EsptouchAsyncTask4(this);
            mTask.execute(ssid, bssid, password, deviceCount, broadcast);
        }
    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
                Toast.makeText(PeiwangActivity.this, text,
                        Toast.LENGTH_LONG).show();

                Log.i("",text);
            }

        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class EsptouchAsyncTask4 extends AsyncTask<byte[], Void, List<IEsptouchResult>> {
        private WeakReference<PeiwangActivity> mActivity;

        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();
        private ProgressDialog mProgressDialog;
        private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;

        EsptouchAsyncTask4(PeiwangActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        void cancelEsptouch() {
            cancel(true);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (mResultDialog != null) {
                mResultDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {
            Activity activity = mActivity.get();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("WIFI配置中，请稍作等候...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i(TAG, "progress dialog back pressed canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getText(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            synchronized (mLock) {
                                if (__IEsptouchTask.DEBUG) {
                                    Log.i(TAG, "progress dialog cancel button canceled");
                                }
                                if (mEsptouchTask != null) {
                                    mEsptouchTask.interrupt();
                                }
                            }
                        }
                    });
            mProgressDialog.show();
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            PeiwangActivity activity = mActivity.get();
            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                byte[] broadcastData = params[4];
                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
                Context context = activity.getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(activity.myListener);
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        String ip = "";

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            PeiwangActivity activity = mActivity.get();
            mProgressDialog.dismiss();
            mResultDialog = new AlertDialog.Builder(activity)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
            mResultDialog.setCanceledOnTouchOutside(false);
            if (result == null) {
                mResultDialog.setMessage("Create Esptouch task failed, the esptouch port could be used by other thread");
                mResultDialog.show();
                return;
            }

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();

                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = ")
                                .append(resultInList.getBssid())
                                .append(", InetAddress = ")
                                .append(resultInList.getInetAddress().getHostAddress())
                                .append("\n");
                        ip = resultInList.getInetAddress().getHostAddress();
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's ")
                                .append(result.size() - count)
                                .append(" more result(s) without showing\n");
                    }
                    mResultDialog.setMessage(sb.toString());
                    mConfirmBtn.setEnabled(false);
//                    confirmBtn1.setVisibility(View.VISIBLE);
                    tv.setText("配网成功，ip:" + ip);
                    ACache.get(PeiwangActivity.this).put("ip",ip+":"+4321);
//                    tv.append("\n正在连接,请稍后...");
                  /*  handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cli = new ClientThread(handler, ip);
                            new Thread(cli).start();
                        }
                    }, 5500);*/


                } else {
                    mResultDialog.setMessage("Esptouch fail");
                    mResultDialog.show();
                }


            }

            activity.mTask = null;
        }
    }
}
