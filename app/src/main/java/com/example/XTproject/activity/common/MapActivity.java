package com.example.XTproject.activity.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;

public class MapActivity extends BaseActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;


    @Override
    protected int getLayoutId() {
        return R.layout.map_test;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        setTitle("设备定位");
        mMapView = bindViewId(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        LatLng point = new LatLng(30.26, 120.19);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        //地图中心设置
        MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.setMapStatus(status);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

}