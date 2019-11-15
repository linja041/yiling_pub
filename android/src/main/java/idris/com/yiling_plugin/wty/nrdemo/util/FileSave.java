package idris.com.yiling_plugin.wty.nrdemo.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/12/20.
 */

public class FileSave {
    public static final String TempFolderName = "nr_ecg";

    public static String getRootPath(Context ctx) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        return dir + "/" + TempFolderName;
    }


    public static void saveFileNameList(Context context, String info, String filename) {
        try {

            File root = new File(getRootPath(context) + "/list");
            if (!root.exists()) {
                root.mkdirs();
            }

            String _currentDatetime = null;
            _currentDatetime = getCurrentDate();

            String _name = info + "_" + _currentDatetime + "_" + filename;
            File gpxfile = new File(root, _name);

            FileWriter writer = new FileWriter(gpxfile, false);
            writer.append("1");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void deleteFileNameList(Context context, String filename) {

        File root = new File(getRootPath(context) + "/list");
        if (!root.exists()) {
            root.mkdirs();
        }


        File gpxfile = new File(root, filename);
        if (gpxfile.exists()) {
            gpxfile.delete();
        }


    }

    public static boolean isFileNameExist(Context context, String filename) {

        File root = new File(getRootPath(context) + "/list");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, filename);

        return gpxfile.exists();

    }
    public static String getRootEcgDataPath(Context context){
        return getRootPath(context) + "/ecgdata";
    }

    public static void saveEcgData(Context context, String allInfo, byte[] data) {
        try {


            File root = new File(getRootPath(context) + "/ecgdata");
            if (!root.exists()) {
                root.mkdirs();
            }

            FileOutputStream outputStream;
            File gpxfile = new File(root, allInfo);
            if (!gpxfile.exists()) {
                gpxfile.createNewFile();
                outputStream = new FileOutputStream(gpxfile, true);
                String[] infos = allInfo.split("_");
                String name = infos[0];
                String sex = infos[1];
                String age = infos[2];
                String time = infos[3];
                String fileName = infos[4];
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = df.parse(time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                byte[] version = "000000".getBytes();
                byte[] remark = new byte[]{0, 0};
                byte[] timebyte = new byte[6];
                byte leadNum = 8;

                byte fileType = 0;
                byte[] namee = name.getBytes();
                byte sexx = Byte.parseByte(sex);
                byte agee = Byte.parseByte(age);
                byte[] cyl = ByteUtils.stringToBytes("FA00");
                byte[] fileTypef1 = new byte[]{0, 0};
                byte[] fileState = new byte[]{0, 0};
                byte[] fileState1 = new byte[]{0, 0, 0, 0};
                byte[] caijiTime = new byte[]{0, 0, 0, 0};
                byte[] beiy = new byte[16];
                timebyte[0] = (byte) (calendar.get(Calendar.YEAR) - 2000);
                timebyte[1] = (byte) (calendar.get(Calendar.MONTH) + 1);
                timebyte[2] = (byte) (calendar.get(Calendar.DAY_OF_MONTH));
                timebyte[3] = (byte) (calendar.get(Calendar.HOUR_OF_DAY));
                timebyte[4] = (byte) (calendar.get(Calendar.MINUTE));
                timebyte[5] = (byte) (calendar.get(Calendar.SECOND));

                outputStream.write(version);
                outputStream.write(remark);
                outputStream.write(timebyte);
                outputStream.write(leadNum);
                outputStream.write(fileType);
                outputStream.write(namee);
                outputStream.write(sexx);
                outputStream.write(agee);
                outputStream.write(cyl);
                outputStream.write(fileTypef1);
                outputStream.write(fileState);
                outputStream.write(fileState1);
                outputStream.write(caijiTime);
                outputStream.write(beiy);


                outputStream.write(data);
            } else {
                outputStream = new FileOutputStream(gpxfile, true);
                outputStream.write(data);
            }

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String[] getFileNameList(Context context) {

        File root = new File(getRootPath(context) + "/list");
        if (!root.exists()) {
            return null;
        }
        String[] lists = new String[root.listFiles().length];
        for (int i = 0; i < root.listFiles().length; i++) {
            lists[i] = root.listFiles()[i].getName();
        }

        return lists;


    }
    public static String[] getViewDKFileNameList(Context context) {

        File root = new File(getRootPath(context) + "/ecgdata");
        if (!root.exists()) {
            return null;
        }
        String[] lists = new String[root.listFiles().length];
        for (int i = 0; i < root.listFiles().length; i++) {
            lists[i] = root.listFiles()[i].getName();
        }

        return lists;


    }

    private static String getCurrentDate() {

        Date date;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        Date _time = new Date();
        String result = df.format(_time);
        return result;
    }
}
