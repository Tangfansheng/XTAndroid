package com.example.XTproject.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment_Monitor_Activity extends BaseActivity {
    static final String TAG = "StressMonitor";
    private Context mContext;
    private Button button;
    private ListView listView;
    private int mount = 5; // 风力 湿度 温度 噪声 扬尘监控
    List<Map<String, Object>> listItems;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("施工环境监测");
    }

    @Override
    protected void initView() {
        mToolBar = bindViewId(R.id.toolbar);

        listView = bindViewId(R.id.data_list);
        button = bindViewId(R.id.refresh_data);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
                Toast.makeText(mContext, "数据刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.environment_monitor_list;
    }

    @Override
    protected void initData() {
        refreshData();

    }
    //刷新页面数据
    protected void refreshData(){
        /**
         * Todo
         * 完成数据的请求：吊篮-锚杆-底篮等数据
         * 完成数据填充
         */

        listItems= new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("风力", "2");
        dataMap.put("温度","35度");
        dataMap.put("湿度", "65%");
        dataMap.put("噪声","67分贝");
        dataMap.put("扬尘", "20");
        for(String key : dataMap.keySet()){
            Map<String, Object> item = new HashMap<>();
            item.put("header", key);
            item.put("second", dataMap.get(key));
            listItems.add(item);
        }

        SimpleAdapter listAdapter = new SimpleAdapter(this, listItems, R.layout.data_list_item, new String[]{"header","second"},new int[]{R.id.tvF,R.id.tvS});
        listView.setAdapter(listAdapter);
     }

}
