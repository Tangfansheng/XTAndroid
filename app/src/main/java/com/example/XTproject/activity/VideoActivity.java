package com.example.XTproject.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;
import com.example.XTproject.base.MyAdapter;
import com.example.XTproject.model.Icon;

import java.util.ArrayList;

public class VideoActivity extends BaseActivity {
    private GridView gridView;
    private Context mContext;
    private BaseAdapter mAdapter ;
    private ArrayList<Icon> icons;

    private String[] urls = {
            "rtmp://58.200.131.2:1935/livetv/cctv13",
            "rtmp://58.200.131.2:1935/livetv/cctv12",
            "rtmp://58.200.131.2:1935/livetv/cctv5"};
    private String[] titles = {"监控1", "监控2","监控3"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.video_list_grid;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        mContext = this;
        gridView = bindViewId(R.id.video_grid);
        mToolBar = bindViewId(R.id.toolbar);
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("远程监控");

    }

    @Override
    protected void initData() {
        icons= new ArrayList<>();
        icons.add(new Icon(R.drawable.ic_camera, "挂篮全局监控"));
        icons.add(new Icon(R.drawable.ic_camera, "挂篮侧模监控"));
        icons.add(new Icon(R.drawable.ic_camera, "挂篮底篮监控"));
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
//                Toast.makeText(mContext, "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("url", urls[position]);
                intent.putExtra("title", titles[position]);
                startActivity(intent);
            }
        });
    }
}