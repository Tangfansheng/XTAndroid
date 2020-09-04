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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BasketBottomActivity extends BaseActivity {
    static final String TAG = "BasketMonitor";
    private Context mContext;
    private Button button;
    private ListView listView;
    private int mount = 4; // 前吊杆
    List<Map<String, Object>> listItems;
    String data;
    String url = "http://120.26.187.166:8080/XTBridge_war/basket/recent";


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
                doGet(url);
                boolean refreshed = refreshData(data);
                if(refreshed){
                    Toast.makeText(mContext, "数据刷新成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "数据刷新失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.basket_bottom_monitor_list;
    }

    @Override
    protected void initData() {
        doGet(url);
        refreshData(data);

    }
    //刷新页面数据
    protected boolean refreshData(String jsonData){
        /**
         * Todo
         * 完成数据的请求：吊篮-锚杆-底篮等数据
         * 完成数据填充
         */
        boolean refreshed = false;
        List<Float> list = JSON.parseArray(jsonData, Float.class);
        listItems= new ArrayList<>();
        if(list!=null && list.size() == mount){
            for(int i = 0; i<mount; i++){
                Map<String, Object> item = new HashMap<>();
                item.put("header", i+1);
                item.put("second", list.get(i));
                listItems.add(item);
            }
            refreshed = true;
        }else{
            for(int i = 0; i<mount; i++){
                Map<String, Object> item = new HashMap<>();
                item.put("header", i+1);
                item.put("second", 0);
                listItems.add(item);
            }
        }
        SimpleAdapter listAdapter = new SimpleAdapter(this, listItems, R.layout.data_list_item, new String[]{"header","second"},new int[]{R.id.tvF,R.id.tvS});
        listView.setAdapter(listAdapter);
        return refreshed;
     }

    private void doGet(String url){
        OkHttpClient client= new OkHttpClient.Builder().build();
        final Request request =new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                data = result;
            }
        });

    }


}
