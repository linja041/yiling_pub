package idris.com.yiling_plugin.wty.nrdemo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import idris.com.yiling_plugin.R;

/**
 * Created by Administrator on 2017/9/18.
 */

public class ECGReportAdapter extends BaseAdapter {


    private Context context;
    private List<String> beans;


    public ECGReportAdapter(Context context, List<String> beans) {
        this.beans = beans;
        this.context = context;

    }


    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public String getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.list_item_ecg_report, null);

            holder = new ViewHolder();

            holder.tvReportTime = (TextView) convertView.findViewById(R.id.tv_report_time);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String bean = beans.get(position);
        String[] data = bean.split("_");
        String sex = "男";
        if (data[1].equals("0")) {
            sex = "女";
        }
        holder.tvReportTime.setText("姓名：" + data[0] + "\n性别：" +sex+"\n年龄：" +data[2]+"\n时间：" +data[3]+"\n文件名：" +data[4]);

        return convertView;
    }

    class ViewHolder {
        private TextView tvReportName;
        private TextView tvReportTime;
        private TextView tvHeartRateStatus;
        private TextView tvHeartRateAverage;


    }


}
