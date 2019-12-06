package idris.com.yiling_plugin.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import idris.com.yiling_plugin.R;
import idris.com.yiling_plugin.wty.nrdemo.util.ECGReportAdapter;
import idris.com.yiling_plugin.wty.nrdemo.util.FileSave;

public class DKListActivity extends Activity {

    private ListView listView;
    List<String> beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dklist);


        listView = (ListView) findViewById(R.id.listView);


        final String[] daa = FileSave.getViewDKFileNameList(this);

        if (daa != null) {
            beans = Arrays.asList(daa);
            ECGReportAdapter reportAdapter = new ECGReportAdapter(this, beans);
            listView.setAdapter(reportAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DKListActivity.this, ViewDKAct.class);
                intent.putExtra("data", beans.get(position));
                startActivity(intent);
            }
        });

    }

}
