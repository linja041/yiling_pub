package idris.com.yiling_plugin.wty.nrdemo;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import idris.com.yiling_plugin.handler.YiLingResponseHandler;
import idris.com.yiling_plugin.wty.nrdemo.model.AuSucc;
import idris.com.yiling_plugin.wty.nrdemo.model.CKSucc;
import idris.com.yiling_plugin.wty.nrdemo.model.CKSucc1;
import idris.com.yiling_plugin.wty.nrdemo.model.CKSucc2;
import idris.com.yiling_plugin.wty.nrdemo.model.CunkRes;
import idris.com.yiling_plugin.wty.nrdemo.model.CunkRes1;
import idris.com.yiling_plugin.wty.nrdemo.model.DataEvent;
import idris.com.yiling_plugin.wty.nrdemo.model.DeviceConnState;
import idris.com.yiling_plugin.wty.nrdemo.model.DeviceScannState;
import idris.com.yiling_plugin.wty.nrdemo.model.DeviceSwitchState;
import idris.com.yiling_plugin.wty.nrdemo.model.Dianliang;
import idris.com.yiling_plugin.wty.nrdemo.model.Dianliang1;
import idris.com.yiling_plugin.wty.nrdemo.model.WifiRes;
import idris.com.yiling_plugin.wty.nrdemo.model.WifiRes1;
import idris.com.yiling_plugin.wty.nrdemo.model.WifiRes2;
import idris.com.yiling_plugin.wty.nrdemo.model.WifiRes3;
import idris.com.yiling_plugin.wty.nrdemo.model.WifiRes4;
import idris.com.yiling_plugin.wty.nrdemo.util.ByteUtils;
import idris.com.yiling_plugin.wty.nrdemo.util.ClientManager;
import idris.com.yiling_plugin.wty.nrdemo.util.DataTreater;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

/**
 * Created by Benefm on 2017/6/7 0007.
 */

public class DevManager_save1 {

    private static DevManager_save1 devManager;
    public List<SearchResult> mSearchDevices = new ArrayList<>();
//    private  SearchResponse

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private HashMap<String, Boolean> connState = new HashMap<>();
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac1, int status) {
            synchronized (connState) {
                DeviceConnState deviceConnState = new DeviceConnState();
                deviceConnState.mac = mac1;
                if (status == STATUS_CONNECTED) {
                    connState.put(mac1, true);
                    deviceConnState.connState = 2;
                } else {
                    connState.put(mac1, false);
                    deviceConnState.connState = 1;
                }
                EventBus.getDefault().post(deviceConnState);
            }


        }
    };


    private DevManager_save1() {

    }


    public static DevManager_save1 getInstance() {
        if (devManager == null) {
            synchronized (DevManager_save1.class) {
                if (devManager == null) {
                    devManager = new DevManager_save1();

                }
            }

            ClientManager.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
                @Override
                public void onBluetoothStateChanged(boolean openOrClosed) {

                    DeviceSwitchState deviceConnState = new DeviceSwitchState();
                    if (openOrClosed) {
                        deviceConnState.state = true;
                    } else {
                        deviceConnState.state = false;
                    }

                    EventBus.getDefault().post(deviceConnState);

                }
            });


        }
        return devManager;
    }


    public synchronized void startScan() {


        mSearchDevices.clear();
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(15 * 1000, 1).build();

        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {

                if (mSearchDevices.size() == 0 && device != null && !TextUtils.isEmpty(device.getName()) && device.getName().startsWith("LR")) {//ECG
                    mSearchDevices.add(device);
                    stopScan();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("address", device.getAddress());
                    map.put("rssi",device.rssi);
                    map.put("name",device.getName());

//                    DeviceScannState deviceScannState = new DeviceScannState();
//                    deviceScannState.mac = device.getAddress();
//                    bindMac = device.getAddress();
//                    deviceScannState.scannState = 2;
//                    EventBus.getDefault().post(deviceScannState);

                    if (DevManager_save1.getInstance().mSearchDevices != null && DevManager_save1.getInstance().mSearchDevices.size() > 0) {
                        System.out.println("--------------------->找到设备[" + device.getAddress() + "],正在尝试连接...<---------------------");
                        DevManager_save1.getInstance().connectDeviceWithReg(device.getAddress());
                    }

                    YiLingResponseHandler.sendScanResult(map);

                }


            }

            @Override
            public void onSearchStopped() {
                if (mSearchDevices != null && mSearchDevices.size() == 0) {
                    DeviceScannState deviceConnState = new DeviceScannState();
                    deviceConnState.scannState = 1;
                    EventBus.getDefault().post(deviceConnState);
                }


            }

            @Override
            public void onSearchCanceled() {
                /*if (mSearchDevices != null && mSearchDevices.size() == 0) {
                    DeviceScannState deviceConnState = new DeviceScannState();
                    deviceConnState.scannState = 1;
                    deviceConnState.mac = bindMac;
                    EventBus.getDefault().post(deviceConnState);
                }*/

            }
        });

    }


    public synchronized void stopScan() {

        ClientManager.getClient().stopSearch();

    }

    Integer head1Byte = null;
    Integer head2Byte = null;


    int headNub = 2;


    final int HEAD_1 = 0xfd;
    final int HEAD_2 = 0xb1;

    int tempDataCount = 0;
    int allDataCount = 0;
    byte[] onePack = null;
    ExecutorService executorService1 = Executors.newSingleThreadExecutor();

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }

    public void connectDeviceWithReg(final String mac) {
        if (TextUtils.isEmpty(mac)) {
            return;
        }

        ClientManager.getClient().registerConnectStatusListener(mac, mConnectStatusListener);
        ClientManager.getClient().connect(mac, new BleConnectResponse() {
            @Override
            public void onResponse(final int code, BleGattProfile profile) {
                if (code == Code.REQUEST_SUCCESS) {
                    ClientManager.getClient().notify(mac, UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"), UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e"), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID service, UUID character, final byte[] value) {

                            executorService1.execute(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < value.length; i++) {

                                        if (head1Byte != null && head2Byte != null) {
                                            if (tempDataCount == 0) {
                                                allDataCount = getPackLen(head2Byte);
                                                onePack = new byte[headNub + allDataCount];
                                                onePack[0] = head1Byte.byteValue();
                                                onePack[1] = head2Byte.byteValue();

                                            }

                                            int resumeCount = allDataCount - tempDataCount;
                                            if (value.length - i < resumeCount) {
                                                System.arraycopy(value, i, onePack, headNub + tempDataCount, value.length - i);
                                                tempDataCount += value.length - i;
                                                break;
                                            } else {

                                                System.arraycopy(value, i, onePack, headNub + tempDataCount, resumeCount);
                                                i += (resumeCount - 1);
                                                tempDataCount += resumeCount;

                                                if (tempDataCount == allDataCount) {
                                                    tempDataCount = 0;
                                                    allDataCount = 0;
                                                    head1Byte = null;
                                                    head2Byte = null;


                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xf1) {
                                                        DevManager_save1.getInstance().writeEMS(sendAuthBT(onePack));
                                                    }
                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xf3) {
                                                        EventBus.getDefault().post(new AuSucc());
                                                    }
                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xd6) {
                                                        Log.e("cunka", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        CKSucc2 ckSucc = new CKSucc2();
                                                        ckSucc.code = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xd8) {
                                                        Log.e("cunka", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        CKSucc1 ckSucc = new CKSucc1();
                                                        ckSucc.size =bytesToInt(onePack,2);
                                                        EventBus.getDefault().post(ckSucc);

                                                    }
                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xd2) {
                                                        Log.e("cunka", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        CKSucc ckSucc = new CKSucc();
                                                        ckSucc.code = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xc3) {
                                                        Log.e("dianliang", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        Dianliang ckSucc = new Dianliang();
                                                        float a = DataTreater.byteToShort(onePack[4], onePack[3]) / 100.0f;
                                                        ckSucc.code = (float) (Math.round(a * 100) / 100.0);
                                                        System.out.println("------------------>电量：" + ckSucc.code + "<------------------");
                                                        EventBus.getDefault().post(ckSucc.code);
                                                        YiLingResponseHandler.sendBtResult(ckSucc.code);

                                                    }
                                                    if (onePack.length > 2 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xc1) {
                                                        Log.e("dianliang", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        Dianliang1 ckSucc = new Dianliang1();
                                                        ckSucc.y = (onePack[3] & 0xFF) + 2000;
                                                        ckSucc.mm = onePack[4] & 0xFF;
                                                        ckSucc.d = onePack[5] & 0xFF;
                                                        ckSucc.h = onePack[6] & 0xFF;
                                                        ckSucc.m = onePack[7] & 0xFF;
                                                        ckSucc.s = onePack[8] & 0xFF;
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xa7) {
                                                        Log.e("cun", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        WifiRes3 ckSucc = new WifiRes3();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xa9) {
                                                        Log.e("cun", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        WifiRes4 ckSucc = new WifiRes4();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }
                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xaf) {
                                                        Log.e("cun", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        CunkRes1 ckSucc = new CunkRes1();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xd4) {
                                                        Log.e("cun", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        CunkRes ckSucc = new CunkRes();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xa1) {
                                                        Log.e("wifi", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        WifiRes ckSucc = new WifiRes();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }
                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xa3) {
                                                        Log.e("wifi", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        WifiRes1 ckSucc = new WifiRes1();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 3 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xa5) {
                                                        Log.e("wifi", "onNotify: " + ByteUtils.toHexString(onePack, " "));
                                                        WifiRes2 ckSucc = new WifiRes2();
                                                        ckSucc.state = onePack[2];
                                                        EventBus.getDefault().post(ckSucc);

                                                    }

                                                    if (onePack.length > 20 && onePack[0] == (byte) 0xfd && onePack[1] == (byte) 0xe2) {
                                                        DataEvent dataEvent = new DataEvent();
                                                        byte[] xd_wave = new byte[160];
                                                        for (int ii = 0; ii < xd_wave.length; ii++) {
                                                            xd_wave[ii] = onePack[ii + 8];
                                                        }

                                                        byte[] xd_w = new byte[2];
                                                        byte[] xd_w1 = new byte[2];
                                                        byte[] xd_w11 = new byte[2];
                                                        byte[] xd_w12 = new byte[2];
                                                        byte[] xd_w13 = new byte[2];
                                                        byte[] xd_w14 = new byte[2];
                                                        byte[] xd_w15 = new byte[2];
                                                        byte[] xd_w16 = new byte[2];

                                                        for (int ii = 0, j = 0; ii < xd_wave.length; j++) {
                                                            xd_w[0] = xd_wave[ii + 1];
                                                            xd_w[1] = xd_wave[ii];

                                                            xd_w1[0] = xd_wave[ii + 3];
                                                            xd_w1[1] = xd_wave[ii + 2];

                                                            xd_w11[0] = xd_wave[ii + 5];
                                                            xd_w11[1] = xd_wave[ii + 4];

                                                            xd_w12[0] = xd_wave[ii + 7];
                                                            xd_w12[1] = xd_wave[ii + 6];

                                                            xd_w13[0] = xd_wave[ii + 9];
                                                            xd_w13[1] = xd_wave[ii + 8];

                                                            xd_w14[0] = xd_wave[ii + 11];
                                                            xd_w14[1] = xd_wave[ii + 10];

                                                            xd_w15[0] = xd_wave[ii + 13];
                                                            xd_w15[1] = xd_wave[ii + 12];

                                                            xd_w16[0] = xd_wave[ii + 15];
                                                            xd_w16[1] = xd_wave[ii + 14];

                                                            dataEvent.data1[j] = DataTreater.byteToShort(xd_w[0], xd_w[1]);
                                                            dataEvent.data2[j] = DataTreater.byteToShort(xd_w1[0], xd_w1[1]);
                                                            dataEvent.data11[j] = DataTreater.byteToShort(xd_w11[0], xd_w11[1]);
                                                            dataEvent.data12[j] = DataTreater.byteToShort(xd_w12[0], xd_w12[1]);
                                                            dataEvent.data13[j] = DataTreater.byteToShort(xd_w13[0], xd_w13[1]);
                                                            dataEvent.data14[j] = DataTreater.byteToShort(xd_w14[0], xd_w14[1]);
                                                            dataEvent.data15[j] = DataTreater.byteToShort(xd_w15[0], xd_w15[1]);
                                                            dataEvent.data16[j] = DataTreater.byteToShort(xd_w16[0], xd_w16[1]);

                                                            ii += 16;

                                                        }

                                                        short hr = combind(onePack[169], onePack[168]);
                                                        dataEvent.hr=hr;
                                                        if (onePack[170] != 0) {
                                                            dataEvent.isTuo = true;
                                                        } else {
                                                            dataEvent.isTuo = false;
                                                        }
                                                        if (onePack[172] != 0) {
                                                            dataEvent.isNormal = false;
                                                        } else {
                                                            dataEvent.isNormal = true;
                                                        }


                                                        EventBus.getDefault().post(dataEvent);
                                                    }


                                                }
                                                continue;
                                            }


                                        }
                                        if (head1Byte == null && (value[i] & 0xff) == HEAD_1) {
                                            head1Byte = HEAD_1;
                                            continue;
                                        }

                                        if (head2Byte == null && head1Byte != null && !isHead(value[i] & 0xff)) {
                                            head1Byte = null;
                                            continue;
                                        }

                                        if (head2Byte == null && head1Byte != null && isHead(value[i] & 0xff)) {
                                            head2Byte = (value[i] & 0xff);
                                            continue;
                                        }


                                    }
                                }
                            });


                           /* executorService1.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("ecgdata", "onNotify: " + ByteUtils.toHexString(value, " "));



                                }
                            });*/


                        }

                        @Override
                        public void onResponse(int code) {
                            if (code == Code.REQUEST_SUCCESS) {

                            }
                        }
                    });
                    System.out.println("--------------->Request success<--------------");
                }

            }
        });
    }

    static short combind(byte high, byte low) {

        return (short) (((high << 8) & 0xFF00) | (low & 0xFF));
    }

    public boolean isHead(int head) {
        return head == 0xf1 || head == 0xf3 || head == 0xe2 || head == 0xe3 || head == 0xd2 || head == 0xc1 || head == 0xc3 || head == 0xa1 || head == 0xa3 || head == 0xa5|| head == 0xd4|| head == 0xa7|| head == 0xa9|| head == 0xaf|| head == 0xd8|| head == 0xd6;
    }

    public int getPackLen(int head) {
        if (head == 0xf1) {
            return 9;
        }
        if (head == 0xf3) {
            return 2;
        }

        if (head == 0xe2) {
            return 180;
        }

        if (head == 0xe3) {
            return 2;
        }
        if (head == 0xd2) {
            return 2;
        }
        if (head == 0xc1) {
            return 8;
        }
        if (head == 0xc3) {
            return 5;
        }
        if (head == 0xa1) {
            return 2;
        }
        if (head == 0xa3) {
            return 2;
        }
        if (head == 0xa5) {
            return 2;
        }
        if (head == 0xd4) {
            return 2;
        }
        if (head == 0xa7) {
            return 2;
        }
        if (head == 0xa9) {
            return 2;
        }
        if (head == 0xaf) {
            return 2;
        }
        if (head == 0xd8) {
            return 5;
        }
        if (head == 0xd6) {
            return 2;
        }

        return 0;
    }

    public byte[] sendAuthBT(byte[] result) {
        byte[] Rng = new byte[8];
        for (int i = 0; i < 8; i++) {
            Rng[i] = result[i + 2];
        }

        byte[] encryptResult = new byte[8];

        encryptResult[0] = (byte) (((Rng[1] & 0xFF) >> 1) + ((Rng[4] & 0xFF) >> 1));
        encryptResult[1] = (byte) (((Rng[2] & 0xFF) >> 1) + ((Rng[3] & 0xFF) >> 1));
        encryptResult[2] = (byte) (((Rng[2] & 0xFF) >> 1) + ((Rng[3] & 0xFF) >> 1));
        encryptResult[3] = (byte) (((Rng[1] & 0xFF) >> 1) + ((Rng[7] & 0xFF) >> 1));
        encryptResult[4] = (byte) (((Rng[0] & 0xFF) >> 1) + ((Rng[6] & 0xFF) >> 1));
        encryptResult[5] = (byte) (((Rng[0] & 0xFF) >> 1) + ((Rng[5] & 0xFF) >> 1));
        encryptResult[6] = (byte) (((Rng[5] & 0xFF) >> 1) + ((Rng[7] & 0xFF) >> 1));
        encryptResult[7] = (byte) (((Rng[5] & 0xFF) >> 1) + ((Rng[4] & 0xFF) >> 1));

        byte[] result_cmd = new byte[11];
        result_cmd[0] = (byte) 0xFD;
        result_cmd[1] = (byte) 0xF2;
        for (int i = 0; i < encryptResult.length; i++) {
            result_cmd[i + 2] = encryptResult[i];
        }

        result_cmd[10] = (byte) 0x00;
        result_cmd[10] = Crc7Chksum(result_cmd, 11);
        return result_cmd;
    }

    public byte[] getCunKState() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xD3;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public byte[] startXinDian() {
        byte[] xinDian_start = new byte[4];
        xinDian_start[0] = (byte) 0xFD;
        xinDian_start[1] = (byte) 0xE0;
        xinDian_start[2] = (byte) 0x00;
        xinDian_start[3] = (byte) 0x26;

        return xinDian_start;
    }

    public byte[] stopXinDian() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xE1;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = (byte) 0x67;

        return xinDian_end;
    }

    public byte[] stopCK() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xD1;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public byte[] getBt() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xC2;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        System.out.println("getBtgetBtgetBtgetBtgetBtgetBtgetBtgetBtgetBtgetBtgetBt");
        return xinDian_end;
    }

    public byte[] getTF() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xD7;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public byte[] getRTC() {
        byte[] xinDian_end = new byte[10];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xC0;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[9] = Crc7Chksum(xinDian_end, 10);

        return xinDian_end;
    }

    public byte[] startWifi(byte trigger) {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xa0;
        xinDian_end[2] = (byte) trigger;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    public byte[] setIpPort(byte ip1, byte ip2, byte ip3, byte ip4, short port) {
        byte[] xinDian_end = new byte[13];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xa6;
        xinDian_end[2] = (byte) ip1;
        xinDian_end[3] = (byte) ip2;
        xinDian_end[4] = (byte) ip3;
        xinDian_end[5] = (byte) ip4;
        byte[] temp = shortToByteArray(port);
        xinDian_end[6] = (byte) temp[1];
        xinDian_end[7] = (byte) temp[0];
        xinDian_end[8] = (byte) 0;
        xinDian_end[9] = (byte) 0;
        xinDian_end[10] = (byte) 0;
        xinDian_end[11] = (byte) 0;
        xinDian_end[12] = Crc7Chksum(xinDian_end, 13);

        return xinDian_end;
    }

    public byte[] quesyWifi() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xa2;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public byte[] testIpConn() {
        byte[] xinDian_end = new byte[11];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xac;
        xinDian_end[2] = (byte) 0x01;
        xinDian_end[3] = (byte) 0x02;
        xinDian_end[4] = (byte) 0x03;
        xinDian_end[5] = (byte) 0x04;

        xinDian_end[10] = Crc7Chksum(xinDian_end, 11);

        return xinDian_end;
    }

    public byte[] quesyIpConn() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xa8;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public byte[] setWifiMode() {
        byte[] xinDian_end = new byte[4];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xa4;
        xinDian_end[2] = (byte) 0x00;
        xinDian_end[3] = Crc7Chksum(xinDian_end, 4);

        return xinDian_end;
    }

    public byte[] syncRTC() {
        byte[] xinDian_end = new byte[10];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xC0;
        xinDian_end[2] = (byte) 0x01;

        Calendar calendar = Calendar.getInstance();
        xinDian_end[3] = (byte) (calendar.get(Calendar.YEAR) - 2000);
        xinDian_end[4] = (byte) (calendar.get(Calendar.MONTH) + 1);
        xinDian_end[5] = (byte) (calendar.get(Calendar.DAY_OF_MONTH));
        xinDian_end[6] = (byte) (calendar.get(Calendar.HOUR_OF_DAY));
        xinDian_end[7] = (byte) (calendar.get(Calendar.MINUTE));
        xinDian_end[8] = (byte) (calendar.get(Calendar.SECOND));
        xinDian_end[9] = Crc7Chksum(xinDian_end, 10);

        return xinDian_end;
    }

    public byte[] startCK(String fileName, String name, byte sex, byte age, byte mode) {
        byte[] xinDian_end = new byte[30];
        xinDian_end[0] = (byte) 0xFD;
        xinDian_end[1] = (byte) 0xD0;
        for (int i = 0; i < 8; i++) {
            xinDian_end[2 + i] = fileName.getBytes()[i];
        }
        for (int i = 0; i < 16; i++) {
            xinDian_end[10 + i] = name.getBytes()[i];
        }

        xinDian_end[26] = sex;
        xinDian_end[27] = age;
        xinDian_end[28] = mode;
        xinDian_end[29] = Crc7Chksum(xinDian_end, 30);

        return xinDian_end;
    }


    public static byte Crc7Chksum(byte[] DataBuf, int Len) {
        byte CRCValue, TempChar;
        int ii;
        CRCValue = 0;
        for (ii = 0; ii < Len - 2; ii++) {
            TempChar = (byte) (CRCValue ^ (DataBuf[ii + 1]));
            CRCValue = crc7_table[TempChar & 0xFF];
        }

        return CRCValue;
    }

    public static byte crc7_table[] = {0, 9, 18, 27, 36, 45, 54, 63, 72, 65, 90, 83, 108, 101, 126, 119, 25, 16, 11, 2, 61, 52, 47, 38, 81, 88, 67, 74, 117, 124, 103, 110, 50, 59, 32, 41, 22, 31, 4, 13, 122, 115, 104, 97, 94, 87, 76, 69, 43, 34, 57, 48, 15, 6, 29, 20, 99, 106, 113, 120, 71, 78, 85, 92, 100, 109, 118, 127, 64, 73, 82, 91, 44, 37, 62, 55, 8, 1, 26, 19, 125, 116, 111, 102, 89, 80, 75, 66, 53, 60, 39, 46, 17, 24, 3, 10, 86, 95, 68, 77, 114, 123, 96, 105, 30, 23, 12, 5, 58, 51, 40, 33, 79, 70, 93, 84, 107, 98, 121, 112, 7, 14, 21, 28, 35, 42, 49, 56, 65, 72, 83, 90, 101, 108, 119, 126, 9, 0, 27, 18, 45, 36, 63, 54, 88, 81, 74, 67, 124, 117, 110, 103, 16, 25, 2, 11, 52, 61, 38, 47, 115, 122, 97, 104, 87, 94, 69, 76, 59, 50, 41, 32, 31, 22, 13, 4, 106, 99, 120, 113, 78, 71, 92, 85, 34, 43, 48, 57, 6, 15, 20, 29, 37, 44, 55, 62, 1, 8, 19, 26, 109, 100, 127, 118, 73, 64, 91, 82, 60, 53, 46, 39, 24, 17, 10, 3, 116, 125, 102, 111, 80, 89, 66, 75, 23, 30, 5, 12, 51, 58, 33, 40, 95, 86, 77, 68, 123, 114, 105, 96, 14, 7, 28, 21, 42, 35, 56, 49, 70, 79, 84, 93, 98, 107, 112, 121};

    public void readRssi(String mac) {
        ClientManager.getClient().readRssi(mac, new BleReadRssiResponse() {
            @Override
            public void onResponse(int code, Integer rssi) {
                if (code == Code.REQUEST_SUCCESS) {
                }
            }
        });
    }

    String bindMac;

    public void close() {
        if (TextUtils.isEmpty(bindMac)) {
            return;
        }
        closeDevice(bindMac);

    }

    public void closeDevice(String mac) {
        connState.put(mac, false);
        Log.d("debug", "closeDevice: " + mac);
        ClientManager.getClient().unregisterConnectStatusListener(mac, mConnectStatusListener);
        ClientManager.getClient().disconnect(mac);

        DeviceConnState deviceConnState = new DeviceConnState();
        deviceConnState.mac = mac;
        deviceConnState.connState = 1;
        EventBus.getDefault().post(deviceConnState);
    }

    public boolean getConnStateByMac() {

        synchronized (connState) {

            if (connState.get(bindMac) != null && connState.get(bindMac)) {
                return true;
            } else {
                return false;
            }
        }
    }


    public void writeEMS(byte[] data) {
        if (TextUtils.isEmpty(bindMac)) {
            return;
        }
        int one = 200;
        if (data.length <= one) {
            DevManager_save1.getInstance().write(bindMac, data);
        } else {
            int count = data.length / one;
            int resume = data.length % one;
            for (int i = 0; i < count; i++) {
                byte[] head = new byte[one];
                System.arraycopy(data, i * one, head, 0, one);
                DevManager_save1.getInstance().write(bindMac, head);
                SystemClock.sleep(100);
            }
            if (resume != 0) {
                byte[] head = new byte[resume];
                System.arraycopy(data, count * one, head, 0, resume);
                DevManager_save1.getInstance().write(bindMac, head);
            }
        }
    }


    public void write(String mac, byte[] bytes) {
        Log.e("ecgdata", "write: " + ByteUtils.toHexString(bytes, " "));
        ClientManager.getClient().write(mac, UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"), UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e"), bytes, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code == Code.REQUEST_SUCCESS) {

                }
            }
        });

    }


}
