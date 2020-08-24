package com.example.XTproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;
import com.example.XTproject.base.MyAdapter;
import com.example.XTproject.model.Icon;

import java.util.ArrayList;

public class SynVideoActivity extends BaseActivity {
    private GridView gridView;
    private Context mContext;
    private BaseAdapter mAdapter ;
    private ArrayList<Icon> icons;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.syn_video_list_grid;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        mContext = this;
        gridView = bindViewId(R.id.video_grid);
        mToolBar = bindViewId(R.id.toolbar);
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("同步性监控");

    }

    @Override
    protected void initData() {
        icons= new ArrayList<>();
        icons.add(new Icon(R.drawable.ic_syn, "挂篮底篮下放吊杆力监测"));
        icons.add(new Icon(R.drawable.ic_syn, "挂篮行走同步监控"));
        mAdapter = new MyAdapter<Icon>(icons, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, Icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getIconId());
                holder.setText(R.id.txt_icon, obj.getIconName());
            }
        };
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(mContext, BasketBottomActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(mContext, "该功能模块正在设计", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        });
    }
}