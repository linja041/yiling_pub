package idris.com.yiling_plugin.wty.nrdemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Benefm on 2017/9/11 0011.
 * 心跳包
 */

public class ClientThread implements Runnable {
    Handler handler;
    InputStream in = null;
    OutputStream ot = null;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Socket s;
    String ip;
    int port;
    boolean flag=true;

    public ClientThread(Handler handler, String ip, int port) {
        this.handler = handler;
        this.ip = ip;
        this.port = port;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            SystemClock.sleep(1500);
            s = new Socket();
            Log.d("debug", ip);
//            s = new Socket(ip, 4321);//此方法不能设定连接时限
            s.setKeepAlive(true);
            s.connect(new InetSocketAddress(ip, port), 1000 * 60);
            in = s.getInputStream();
            ot = s.getOutputStream();
            // 启动一条子线程来读取服务器相应的数据

            new Thread() {

                @Override
                public void run() {
                    String content = null;
                    // 不断的读取Socket输入流的内容
                    int temp = 0;
                    long cc = 0;
                    byte[] buffer = new byte[1040 * 400];

                    try {
                        handler.sendEmptyMessage(0);

                        while (flag&&(temp = in.read(buffer)) != -1) {
                            cc += temp;
                            Log.d("ss", cc + "");
                            byte[] data = new byte[temp];
                            System.arraycopy(buffer, 0, data, 0, temp);
                            handler.obtainMessage(3, data);
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = data;
                            handler.sendMessage(msg);

                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }

            }.start();


        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(1);
        } catch (IOException io) {
            io.printStackTrace();
            handler.sendEmptyMessage(2);
        } finally {

        }

    }

    public void write(final byte[] data) {
        if (!executorService.isShutdown()) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ot.write(data);
                        ot.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }

    public void close() {
        if (s != null) {
            try {
                flag=false;
                executorService.shutdown();
                s.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
