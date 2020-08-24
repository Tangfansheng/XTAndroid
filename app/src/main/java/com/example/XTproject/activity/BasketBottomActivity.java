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

public class BasketBottomActivity extends BaseActivity {
    static final String TAG = "BasketMonitor";
    private Context mContext;
    private Button button;
    private ListView listView;
    private int mount = 4; // 前吊杆
    List<Map<String, Object>> listItems;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("挂篮底篮下放吊杆力监测");
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
        return R.layout.basket_bottom_monitor_list;
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
        for(int i = 0; i<mount; i++){
            Map<String, Object> item = new HashMap<>();
            item.put("header", i+1);
            item.put("second", 100);
            listItems.add(item);
        }
        SimpleAdapter listAdapter = new SimpleAdapter(this, listItems, R.layout.data_list_item, new String[]{"header","second"},new int[]{R.id.tvF,R.id.tvS});
        listView.setAdapter(listAdapter);
     }

}
