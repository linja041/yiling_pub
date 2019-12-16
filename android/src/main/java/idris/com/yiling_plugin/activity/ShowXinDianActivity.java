package idris.com.yiling_plugin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecg.ecgalgorithm.ecglib;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import idris.com.yiling_plugin.R;
import idris.com.yiling_plugin.handler.YiLingResponseHandler;
import idris.com.yiling_plugin.wty.nrdemo.DevManager;
import idris.com.yiling_plugin.wty.nrdemo.EcgSurfaceView;
import idris.com.yiling_plugin.wty.nrdemo.EcgWaveData;
import idris.com.yiling_plugin.wty.nrdemo.model.CKSucc;
import idris.com.yiling_plugin.wty.nrdemo.model.CunkRes;
import idris.com.yiling_plugin.wty.nrdemo.model.DataAynResult;
import idris.com.yiling_plugin.wty.nrdemo.model.DataEvent;
import idris.com.yiling_plugin.wty.nrdemo.model.DeviceConnState;
import idris.com.yiling_plugin.wty.nrdemo.util.ByteUtils;
import idris.com.yiling_plugin.wty.nrdemo.util.FileSave;

public class ShowXinDianActivity extends AppCompatActivity {

    private Button button2;

    private TextView tvTuo;
    private TextView tvHr;

    private TextView tvTuo1;
    private TextView tvXY1;
    private TextView tvXY2;
    private Paint p1;

    private TextView hvti;
    private TextView hvf;
    private ecglib ndkLibTool;
    private Spinner button1;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private ImageView btnBackUp;
    private Button mBtnConnectDoc;
    private RelativeLayout mRlTips;

    private TextView mTvDivName;
    private TextView mTvDocName;
    private ImageView mIvAva;

//    Timer timer;
    int count = 10;
    private EcgSurfaceView ecgView;

    //设置透明
    protected void SetTranslanteBar(){
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_xin_dian);

        SetTranslanteBar();
        EventBus.getDefault().register(this);
        ndkLibTool = new ecglib();

        mBtnConnectDoc = findViewById(R.id.btn_connect_doc);
        Drawable drawable= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.ic_contact, null);
        }
        drawable.setBounds(0,0,50,50);
        mBtnConnectDoc.setCompoundDrawables(drawable,null,null,null);
        mBtnConnectDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                YiLingResponseHandler.LXYSOrder("gotoLXYS");
            }
        });

        button1 = (Spinner) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        btnBackUp = findViewById(R.id.icon_goback);
        btnBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTuo = (TextView) findViewById(R.id.tvTuo);
        tvHr = (TextView) findViewById(R.id.tvHr);


        mRlTips = findViewById(R.id.rl_tips);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);


        tvTuo1 = (TextView) findViewById(R.id.tvTuo1);
        tvXY1 = (TextView) findViewById(R.id.tvXY1);
        tvXY2 = (TextView) findViewById(R.id.tvXY2);


        hvti = (TextView) findViewById(R.id.hvti);
        hvf = (TextView) findViewById(R.id.hvf);

        ecgView = (EcgSurfaceView) findViewById(R.id.sf);

        mTvDivName = findViewById(R.id.tv_div_name);
        mTvDocName = findViewById(R.id.tv_doc_name);

        Intent i = getIntent();
        docName = i.getStringExtra("docName");
        divName = i.getStringExtra("divName");
        ava = i.getStringExtra("ava");
        mTvDocName.setText("医生:"+docName);
        mTvDivName.setText("设备名称:"+divName);

        mIvAva = findViewById(R.id.iv_ava);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this)
                .load(ava)
                .apply(options)
                .into(mIvAva);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("开始检测".equals(button2.getText().toString())) {
//                    button1.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    tv6.setVisibility(View.VISIBLE);
                    button2.setText("停止检测");


                    Intent i = getIntent();

                    fileName = i.getStringExtra("fileName");
                    name = i.getStringExtra("name");
                    sex = i.getByteExtra("sex",(byte)0);
                    age = i.getByteExtra("age",(byte)18);

                    Log.e("cunka", fileName + ":" + name + ":" + sex + ":" + age);
//                    FileSave.saveFileNameList(SingleEcgActivity.this, name + "_" +  sex + "_" + age,fileName);
                    byte[] data = DevManager.getInstance().startCK(fileName, name, sex, age, (byte) 0);
                    Log.e("cunka", ByteUtils.toHexString(data, " "));
                    DevManager.getInstance().writeEMS(data);

                    DevManager.getInstance().writeEMS(DevManager.getInstance().startXinDian());

//                    p.setColor(Color.parseColor("#b3f442"));
                    ecgView.color= Color.parseColor("#eb9591");
//                    button.setEnabled(false);

                    button2.setText("停止检测");

                } else if("停止检测".equals(button2.getText().toString())){
//                    button1.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    tv6.setVisibility(View.VISIBLE);
                    button2.setText("开始检测");
//                    button1.setVisibility(View.INVISIBLE);
                    DevManager.getInstance().writeEMS(DevManager.getInstance().stopXinDian());
                    DevManager.getInstance().writeEMS(DevManager.getInstance().stopCK());
                    ecgView.color=Color.parseColor("#eb9591");
                }
            }
        });

        button1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                synchronized (holder.getSurface()) {
                lead = position;
                if (lead == 1) {
                    tv1.setText("V1");
                    tv2.setText("V2");
                    tv3.setText("V3");
                    tv4.setText("V4");
                    tv5.setText("V5");
                    tv6.setText("V6");
                    data.clear();
                    data1.clear();
                    data2.clear();
                    data3.clear();
                    data4.clear();
                    data5.clear();

                        /*x = 0;
                        x1 = 0;
                        x2 = 0;
                        x3 = 0;
                        x4 = 0;
                        x5 = 0;
                        Canvas canvas = holder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        height = canvas.getHeight();
                        holder.unlockCanvasAndPost(canvas);*/


                } else {

                    tv1.setText("I");
                    tv2.setText("II");
                    tv3.setText("III");
                    tv4.setText("aVR");
                    tv5.setText("aVL");
                    tv6.setText("aVF");
                    data.clear();
                    data1.clear();
                    data2.clear();
                    data3.clear();
                    data4.clear();
                    data5.clear();
                       /* x = 0;
                        x1 = 0;
                        x2 = 0;
                        x3 = 0;
                        x4 = 0;
                        x5 = 0;
                        Canvas canvas = holder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        height = canvas.getHeight();
                        holder.unlockCanvasAndPost(canvas);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ndkLibTool.nativeSetNhlFilter(mFilter, (short) 250);

        DevManager.getInstance().writeEMS(DevManager.getInstance().getCunKState());


    }

    String fileName;
    String name;
    String docName;
    String divName;
    String ava;

    byte sex;
    byte age;


    ArrayList<Integer> data = new ArrayList<>();
    ArrayList<Integer> data1 = new ArrayList<>();
    ArrayList<Integer> data2 = new ArrayList<>();
    ArrayList<Integer> data3 = new ArrayList<>();
    ArrayList<Integer> data4 = new ArrayList<>();
    ArrayList<Integer> data5 = new ArrayList<>();
    int width;
    int height;
    Paint p;
    SurfaceView sf;
    SurfaceHolder holder;
    boolean iscreated;
    float x = 0;
    float x1 = 0;
    float x2 = 0;
    float x3 = 0;
    float x4 = 0;
    float x5 = 0;
    int lead = 0;

    short[] mFilter = {1, 0, 0};
    short[] isHeart = {0};
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCKSucc(CKSucc event) {

        if (event.code == 0) {
            if (!"开始检测".equals(button2.getText().toString())) {

                if (fileName != null && name != null)
                {
                    FileSave.saveFileNameList(this, name + "_" + sex + "_" + age, fileName);
                }
                Toast.makeText(ShowXinDianActivity.this, "存卡命令成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShowXinDianActivity.this, "停止存卡命令成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ShowXinDianActivity.this, "文件名已存在或命令响应错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceConnState(DeviceConnState event) {

        if (event.connState == 2) {

        }
        if (event.connState == 1) {
            Log.d("debug", "连接失败: ");

            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDataEvent(final DataEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tvHr.setText("心率：" + event.hr);
                tvXY1.setText("电压：" + event.dy/100.0f+"伏");*/
                if (event.isTuo) {
                    tvTuo.setText("导联脱落");
                    mRlTips.setBackgroundColor(Color.parseColor("#FB4C43"));
                } else {
//                    tvTuo.setText("导联未脱落");
                }

                if (event.isTuo) {
                    tvTuo.setText("导联脱落");
                    mRlTips.setBackgroundColor(Color.parseColor("#FB4C43"));
                } else {
//                    tvTuo.setText("导联未脱落");
                }
                if (event.isNormal) {
//                    tvTuo1.setText("心室大类诊断正常");
                } else {
                    tvTuo1.setText("心室大类诊断异常");
                    mRlTips.setBackgroundColor(Color.parseColor("#FB4C43"));
                }
                tvHr.setText("心率："+event.hr+"");

                /*if (event.isCharging == 0) {
                    tvTuo1.setText("未插入适配器未充电");
                } else if (event.isCharging == 1) {
                    tvTuo1.setText("充电中");
                } else {
                    tvTuo1.setText("充电完成");
                }*/

            }
        });
//        synchronized (holder.getSurface()) {
//            if (iscreated) {
        for (int i = 0; i < event.data1.length; i++) {
            int[] input = new int[9];
            int[] Output = new int[12];
            short[] isHeartBeat = {0};

            input[0] = event.data1[i];
            input[1] = event.data2[i];
            input[2] = (input[1] - input[0]);
            input[3] = event.data11[i];
            input[4] = event.data12[i];
            input[5] = event.data13[i];
            input[6] = event.data14[i];
            input[7] = event.data15[i];
            input[8] = event.data16[i];
//                    Log.d("debug", "input: "+ Arrays.toString(input));
            ndkLibTool.ecg_ProcessDataLead(input, Output, (short) 0,
                    isHeartBeat);
//                    Log.d("debug", "onClick: "+ Arrays.toString(Output));

            if (lead == 0) {
                data.add(Output[0]);
                data1.add(Output[1]);
                data2.add(Output[2]);
                data3.add(Output[3]);
                data4.add(Output[4]);
                data5.add(Output[5]);
            } else {
                data.add(Output[6]);
                data1.add(Output[7]);
                data2.add(Output[8]);
                data3.add(Output[9]);
                data4.add(Output[10]);
                data5.add(Output[11]);
            }
//                    Log.d("dd", "onDataEvent: "+event.data1[i]);

            if (data.size() == 10) {


                Map<Integer, ArrayList<Float>> maps = new LinkedHashMap<>();
                int leadCount = 12;//
                for (int i1 = 0; i1 < leadCount; i1++) {
                    maps.put(i1, new ArrayList<Float>());
                }
                for (int jjj = 0; jjj < data.size(); jjj++) {
                    maps.get(1).add(data.get(jjj)*1.0f);
                    maps.get(2).add(data1.get(jjj)*1.0f);
                    maps.get(3).add(data2.get(jjj)*1.0f);
                    maps.get(4).add(data3.get(jjj)*1.0f);
                    maps.get(5).add(data4.get(jjj)*1.0f);
                    maps.get(6).add(data5.get(jjj)*1.0f);
                }

                ArrayList<EcgWaveData> datas = new ArrayList<>();
                for (int lead : maps.keySet()) {
                    for (Float f : maps.get(lead)) {
                        datas.add(new EcgWaveData(lead, f));
                    }
                }
                ecgView.notifyData(datas);
                data.clear();
                data1.clear();
                data2.clear();
                data3.clear();
                data4.clear();
                data5.clear();




            }


//                }
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataAynResult(DataAynResult event) {

        if (event.HvrTime != null && event.HrvFre != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("HvrTime[0-6]:" + event.HvrTime[0]);
            sb.append(", " + event.HvrTime[1]);
            sb.append(", " + event.HvrTime[2]);
            sb.append(", " + event.HvrTime[3]);
            sb.append(", " + event.HvrTime[4]);
            sb.append(", " + event.HvrTime[5]);
            sb.append(", " + event.HvrTime[6]);

            sb.append("\nHrvFre[0-6]:" + event.HrvFre[0]);
            sb.append(", " + event.HrvFre[1]);
            sb.append(", " + event.HrvFre[2]);
            sb.append(", " + event.HrvFre[3]);
            sb.append(", " + event.HrvFre[4]);
            sb.append(", " + event.HrvFre[5]);
            sb.append(", " + event.HrvFre[6]);


            hvti.setText(sb.toString());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCunkRes(CunkRes event) {

        if(event.state==1){
            button2.setText("停止检测");
        }else{
            button2.setText("开始检测");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DevManager.getInstance().writeEMS(DevManager.getInstance().stopXinDian());
        EventBus.getDefault().unregister(this);
//        if (timer != null) {
//            timer.cancel();
//        }
    }
}
