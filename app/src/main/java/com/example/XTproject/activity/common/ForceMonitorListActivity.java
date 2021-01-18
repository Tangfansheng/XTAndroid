package com.example.XTproject.activity.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.XTproject.R;
import com.example.XTproject.activity.sensor.FrontDerrickActivity;
import com.example.XTproject.activity.sensor.RearAnchorActivity;
import com.example.XTproject.base.BaseActivity;
import com.example.XTproject.base.MyAdapter;
import com.example.XTproject.model.Icon;

import java.util.ArrayList;

public class ForceMonitorListActivity extends BaseActivity {
    private GridView gridView;
    private Context mContext;
    private BaseAdapter mAdapter ;
    private ArrayList<Icon> icons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_force_monitor_list;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        mContext = this;
        gridView = bindViewId(R.id.force_monitor_list);
        mToolBar = bindViewId(R.id.toolbar);
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle("力的监控");

    }

    @Override
    protected void initData() {
        icons= new ArrayList<>();
        icons.add(new Icon(R.drawable.ic_force, "前吊杆力监控"));
        icons.add(new Icon(R.drawable.ic_force, "后锚杆力监控"));
        mAdapter = new MyAdapter<Icon>(icons, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, Icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getIconId());//绑定Icon图标
                holder.setText(R.id.txt_icon, obj.getIconName());//绑定Icon的text
            }
        };
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(mContext, FrontDerrickActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(mContext, RearAnchorActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;

                }
            }
        });
    }
}