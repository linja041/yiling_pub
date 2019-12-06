package idris.com.yiling_plugin.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ecg.ecgalgorithm.ecglib;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import idris.com.yiling_plugin.R;
import idris.com.yiling_plugin.wty.nrdemo.util.DataTreater;
import idris.com.yiling_plugin.wty.nrdemo.util.FileSave;

public class ViewDKAct extends AppCompatActivity {
    private ecglib ndkLibTool;
    private Spinner button1;

    ArrayList<Integer> data = new ArrayList<>();
    ArrayList<Integer> data1 = new ArrayList<>();
    ArrayList<Integer> data2 = new ArrayList<>();
    ArrayList<Integer> data3 = new ArrayList<>();
    ArrayList<Integer> data4 = new ArrayList<>();
    ArrayList<Integer> data5 = new ArrayList<>();

    ArrayList<Integer> datav1 = new ArrayList<>();
    ArrayList<Integer> datav2 = new ArrayList<>();
    ArrayList<Integer> datav3 = new ArrayList<>();
    ArrayList<Integer> datav4 = new ArrayList<>();
    ArrayList<Integer> datav5 = new ArrayList<>();
    ArrayList<Integer> datav6 = new ArrayList<>();
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
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private ImageView mGoBackBtn;

    private Paint p1;
    private EditText et1;
    private TextView tvAll;
    private Button view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dk);
        mGoBackBtn = findViewById(R.id.icon_goback);
        mGoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish 安卓 ， 返回flutter
                finish();
            }
        });
        button1 = (Spinner) findViewById(R.id.button1);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);


        et1 = (EditText) findViewById(R.id.et1);
        tvAll = (TextView) findViewById(R.id.tvAll);
        view = (Button) findViewById(R.id.view);

        final String fileName = getIntent().getStringExtra("data");
        String[] infos = fileName.split("_");

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        sf = (SurfaceView) findViewById(R.id.sf);

        sf.setZOrderOnTop(true);


        p = new Paint();
        p.setColor(Color.parseColor("#09F797"));
        p.setStrokeWidth(3.0F);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);

        p1 = new Paint();
        p1.setColor(Color.parseColor("#FF0033"));
        p1.setStrokeWidth(3.0F);
        p1.setStyle(Paint.Style.STROKE);
        p1.setAntiAlias(true);
        p1.setDither(true);
        p1.setStrokeCap(Paint.Cap.ROUND);
        p1.setStrokeJoin(Paint.Join.ROUND);
        holder = sf.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                synchronized (holder.getSurface()) {
                    iscreated = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPageData(1, fileName);
                        }
                    }, 300);


                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                synchronized (holder.getSurface()) {
                    iscreated = false;
                }

            }
        });


        button1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                synchronized (holder.getSurface()) {
                    lead = position;
                    if (lead == 1) {
                        tv1.setText("V1");
                        tv2.setText("V2");
                        tv3.setText("V3");
                        tv4.setText("V4");
                        tv5.setText("V5");
                        tv6.setText("V6");


                        x = 0;
                        x1 = 0;
                        x2 = 0;
                        x3 = 0;
                        x4 = 0;
                        x5 = 0;
                        Canvas canvas = holder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        height = canvas.getHeight();
                        holder.unlockCanvasAndPost(canvas);


                    } else {

                        tv1.setText("I");
                        tv2.setText("II");
                        tv3.setText("III");
                        tv4.setText("aVR");
                        tv5.setText("aVL");
                        tv6.setText("aVF");

                        x = 0;
                        x1 = 0;
                        x2 = 0;
                        x3 = 0;
                        x4 = 0;
                        x5 = 0;
                        Canvas canvas = holder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        height = canvas.getHeight();
                        holder.unlockCanvasAndPost(canvas);


                    }

                    draw();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ndkLibTool = new ecglib();
        short[] mFilter = {1, 0, 0};
        ndkLibTool.nativeSetNhlFilter(mFilter, (short) 250);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et1.getText().toString())) {
                    int a = Integer.parseInt(et1.getText().toString());
                    int b = Integer.parseInt(tvAll.getText().toString().substring(1, tvAll.getText().toString().length() - 1));
                    if (a > 0 && a <= b) {
                        getPageData(a, fileName);

                    } else {
                        Toast.makeText(ViewDKAct.this, "请输入正确的页码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewDKAct.this, "请输入页码", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    int allPageNum = 0;
    ExecutorService executorService1 = Executors.newSingleThreadExecutor();

    void getPageData(final int num, final String fileName) {

        executorService1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (holder.getSurface()) {
                        data.clear();
                        data1.clear();
                        data2.clear();
                        data3.clear();
                        data4.clear();
                        data5.clear();
                        x = 0;
                        x1 = 0;
                        x2 = 0;
                        x3 = 0;
                        x4 = 0;
                        x5 = 0;
                        Canvas canvas = holder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        height = canvas.getHeight();
                        width = canvas.getWidth();
                        holder.unlockCanvasAndPost(canvas);

                        RandomAccessFile raf = new RandomAccessFile(FileSave.getRootEcgDataPath(ViewDKAct.this) + "/" + fileName, "r");
                        long ecgSize = raf.length() - 64;
                        int onePagesize = width * 16;

                        int realOne = onePagesize;
                        if (ecgSize % onePagesize != 0) {
                            allPageNum = (int) (ecgSize / onePagesize) + 1;
                            if (num == allPageNum) {
                                realOne = (int) (ecgSize % onePagesize);
                            }
                        } else {
                            allPageNum = (int) (ecgSize / onePagesize);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAll.setText("共" + allPageNum + "页");
                            }
                        });
                        raf.seek(64 + (num - 1) * onePagesize);

                        byte[] xd_wave = new byte[realOne];

                        raf.read(xd_wave);
                        byte[] xd_w = new byte[2];
                        byte[] xd_w1 = new byte[2];
                        byte[] xd_w11 = new byte[2];
                        byte[] xd_w12 = new byte[2];
                        byte[] xd_w13 = new byte[2];
                        byte[] xd_w14 = new byte[2];
                        byte[] xd_w15 = new byte[2];
                        byte[] xd_w16 = new byte[2];

                        for (int i = 0, j = 0; i < xd_wave.length; j++) {
                            xd_w[0] = xd_wave[i + 1];
                            xd_w[1] = xd_wave[i];

                            xd_w1[0] = xd_wave[i + 3];
                            xd_w1[1] = xd_wave[i + 2];

                            xd_w11[0] = xd_wave[i + 5];
                            xd_w11[1] = xd_wave[i + 4];

                            xd_w12[0] = xd_wave[i + 7];
                            xd_w12[1] = xd_wave[i + 6];

                            xd_w13[0] = xd_wave[i + 9];
                            xd_w13[1] = xd_wave[i + 8];

                            xd_w14[0] = xd_wave[i + 11];
                            xd_w14[1] = xd_wave[i + 10];

                            xd_w15[0] = xd_wave[i + 13];
                            xd_w15[1] = xd_wave[i + 12];

                            xd_w16[0] = xd_wave[i + 15];
                            xd_w16[1] = xd_wave[i + 14];


                            int[] input = new int[9];
                            int[] Output = new int[12];
                            short[] isHeartBeat = {0};

                            input[0] = (int) DataTreater.byteToShort(xd_w[1], xd_w[0]);
                            input[1] = (int) DataTreater.byteToShort(xd_w1[1], xd_w1[0]);
                            input[2] = (input[1] - input[0]);
                            input[3] = (int) DataTreater.byteToShort(xd_w11[1], xd_w11[0]);
                            input[4] = (int) DataTreater.byteToShort(xd_w12[1], xd_w12[0]);
                            input[5] = (int) DataTreater.byteToShort(xd_w13[1], xd_w13[0]);
                            input[6] = (int) DataTreater.byteToShort(xd_w14[1], xd_w14[0]);
                            input[7] = (int) DataTreater.byteToShort(xd_w15[1], xd_w15[0]);
                            input[8] = (int) DataTreater.byteToShort(xd_w16[1], xd_w16[0]);
//                    Log.d("debug", "input: "+ Arrays.toString(input));
                            ndkLibTool.ecg_ProcessDataLead(input, Output, (short) 0,
                                    isHeartBeat);
                            data.add(Output[0]);
                            data1.add(Output[1]);
                            data2.add(Output[2]);
                            data3.add(Output[3]);
                            data4.add(Output[4]);
                            data5.add(Output[5]);

                            datav1.add(Output[6]);
                            datav2.add(Output[7]);
                            datav3.add(Output[8]);
                            datav4.add(Output[9]);
                            datav5.add(Output[10]);
                            datav6.add(Output[11]);


                            i += 16;

                        }
                    }

                    draw();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    void draw() {
        synchronized (holder.getSurface()) {
            if (iscreated) {

                float step = 1f;
                float scalY = 0.003f;

                long st = System.currentTimeMillis();
//                                Rect rect = new Rect((int) x, 0, (int) (x + space + size * step), height);
                Rect rect = new Rect((int) x, 0, (int) (width), height);
                Canvas ca = holder.lockCanvas(rect);
                if (ca != null) {
                    ca.drawColor(Color.WHITE);
                    for (int ii = 0; ii < data.size() - 1; ii++) {
                        if (lead == 0) {
                            ca.drawLine(x, height / 12 - data.get(ii) * scalY, (x + 1f), height / 12 - data.get(ii + 1) * scalY, p);
                            ca.drawLine(x1, height / 4 - data1.get(ii) * scalY, (x1 + 1f), height / 4 - data1.get(ii + 1) * scalY, p);
                            ca.drawLine(x2, height / 12 * 5 - data2.get(ii) * scalY, (x2 + 1f), height / 12 * 5 - data2.get(ii + 1) * scalY, p);
                            ca.drawLine(x3, height / 12 * 7 - data3.get(ii) * scalY, (x3 + 1f), height / 12 * 7 - data3.get(ii + 1) * scalY, p);
                            ca.drawLine(x4, height / 4 * 3 - data4.get(ii) * scalY, (x4 + 1f), height / 4 * 3 - data4.get(ii + 1) * scalY, p);
                            ca.drawLine(x5, height / 12 * 11 - data5.get(ii) * scalY, (x5 + 1f), height / 12 * 11 - data5.get(ii + 1) * scalY, p);
                        } else {
                            ca.drawLine(x, height / 12 - datav1.get(ii) * scalY, (x + 1f), height / 12 - datav1.get(ii + 1) * scalY, p);
                            ca.drawLine(x1, height / 4 - datav2.get(ii) * scalY, (x1 + 1f), height / 4 - datav2.get(ii + 1) * scalY, p);
                            ca.drawLine(x2, height / 12 * 5 - datav3.get(ii) * scalY, (x2 + 1f), height / 12 * 5 - datav3.get(ii + 1) * scalY, p);
                            ca.drawLine(x3, height / 12 * 7 - datav4.get(ii) * scalY, (x3 + 1f), height / 12 * 7 - datav4.get(ii + 1) * scalY, p);
                            ca.drawLine(x4, height / 4 * 3 - datav5.get(ii) * scalY, (x4 + 1f), height / 4 * 3 - datav5.get(ii + 1) * scalY, p);
                            ca.drawLine(x5, height / 12 * 11 - datav6.get(ii) * scalY, (x5 + 1f), height / 12 * 11 - datav6.get(ii + 1) * scalY, p);
                        }
                        x += step;
                        x1 += step;
                        x2 += step;
                        x3 += step;
                        x4 += step;
                        x5 += step;

                    }

                    holder.unlockCanvasAndPost(ca);


                }
                long end = System.currentTimeMillis();


            }
        }
    }

}



