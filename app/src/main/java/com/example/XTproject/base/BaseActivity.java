package com.example.XTproject.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
//import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.XTproject.R;
import com.example.XTproject.activity.HomeActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 *作者：created by Administrator on 2020/3/3 22:11
 *邮箱：1723928492@qq.com
 */public abstract class BaseActivity extends AppCompatActivity {
     protected String TAG;
     private String data;
     protected Toolbar mToolBar;
     Context mContext = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId){//泛型 任何类型
        return (T)findViewById(resId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void setSupportActionBar(){
        mToolBar = bindViewId(R.id.toolbar);
        if(mToolBar != null){
            setActionBar(mToolBar);
        }
    }
    //字体页面有箭头
   protected void setSupportArrowActionBar(boolean isSupport){
        getSupportActionBar().setDisplayHomeAsUpEnabled(isSupport);
        getSupportActionBar().setHomeButtonEnabled(isSupport);

   }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void setActionBarIcon(int resId){
        if(mToolBar != null){
            mToolBar.setNavigationIcon(resId);//minApi换成需要的21
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(this instanceof HomeActivity){
            return false;
        }
        //Toolbar的事件---返回
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
