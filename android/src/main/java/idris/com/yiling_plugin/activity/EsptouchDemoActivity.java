package idris.com.yiling_plugin.activity;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import idris.com.yiling_plugin.R;
import idris.com.yiling_plugin.wty.nrdemo.ClientThread;
import idris.com.yiling_plugin.wty.nrdemo.DevManager;
import idris.com.yiling_plugin.wty.nrdemo.util.ACache;
import idris.com.yiling_plugin.wty.nrdemo.util.ByteUtils;
import idris.com.yiling_plugin.wty.nrdemo.util.FileSave;

public class EsptouchDemoActivity extends AppCompatActivity {
    private static final String TAG = "EsptouchDemoActivity";

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


    final int HEAD_1 = 0xfd;//识别头值
    final int HEAD_2 = 0xa7;

    int tempDataCount = 0;//临时拷贝统计
    int allDataCount = 0;//除了识别头总共的数据位个数
    byte[] onePack = null;

    ExecutorService executorService1 = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Log.d("debug", "socket连接成功: ");
                tv.append("\n连接成功");
                count = 0;
                confirmBtn1.setVisibility(View.VISIBLE);
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
                                        int size = 1024;
                                        byte[] ecgD = new byte[size];
                                        System.arraycopy(onePack, 15, ecgD, 0, size);
                                        FileSave.saveEcgData(EsptouchDemoActivity.this, dataY, ecgD);
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



                        /*int size=1024;
                        byte[] ecgD = new byte[size];
                        System.arraycopy(onePack, 15, ecgD, 0, size);
                        FileSave.saveEcgData(EsptouchDemoActivity.this, dataY,ecgD );*/
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
                Log.e("write", "" + ByteUtils.toHexString(xinDian_end, " "));
                cli.write(xinDian_end);

                if (timer != null) {
                    timer.cancel();
                }
                timer = new Timer();
                timer.schedule(timerTask, 10 * 1000, 1000 * 10);


            }

            if (msg.what == 1 || msg.what == 2) {
                tv.append("\n连接失败");

                Log.d("debug", "连接失败: ");
            }

        }
    };
    Timer timer;
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (count == tempCount) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        tv.append("\n读取结束");
                        FileSave.deleteFileNameList(EsptouchDemoActivity.this, dataY);
                    }
                });

            } else {
                tempCount = count;

            }
        }
    };


    private boolean mDestroyed = false;
    private static TextView tv;
    static String[] data1;
    String dataY;
    private Button confirmBtn1;
    private ImageView mGoBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esptouch_demo);
        mGoBackBtn = findViewById(R.id.icon_goback);
        mGoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish 安卓 ， 返回flutter
                finish();
            }
        });

        dataY = getIntent().getStringExtra("data");
        data1 = dataY.split("_");


        confirmBtn1 = (Button) findViewById(R.id.confirm_btn1);
        confirmBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cli != null) {
                    Log.e("write", ByteUtils.toHexString(DevManager.getInstance().testIpConn()," "));
                    cli.write(DevManager.getInstance().testIpConn());
                }
            }
        });

        tv = (TextView) findViewById(R.id.tv);

     /*   TextView versionTV = (TextView) findViewById(R.id.version_tv);
        versionTV.setText(IEsptouchTask.ESPTOUCH_VERSION);*/

       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5500);*/


        String ipp = ACache.get(EsptouchDemoActivity.this).getAsString("ip");
        if (!TextUtils.isEmpty(ipp)) {
            String[] tem = ipp.split(":");
            if (tem != null && tem.length == 2) {
                tv.append("\n正在连接,请稍后...");
                cli = new ClientThread(handler, tem[0], Integer.parseInt(tem[1]));
                new Thread(cli).start();
            } else {
                tv.append("\n请先配网");
            }
        } else {
            tv.append("\n请先配网");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cli != null) {
            cli.close();
        }
        if (timer != null)
            timer.cancel();
        mDestroyed = true;

    }


}
