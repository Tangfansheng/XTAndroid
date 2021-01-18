package com.example.XTproject.activity.common;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;
import com.example.XTproject.services.ClientSocket;
import java.io.IOException;



public class RemoteControllerActivity extends BaseActivity {
    private Button _switch;
    private Button change_time;
    private Button _stop_now;
    private TextView status;
    private EditText water_time;
    private TextView remain_time;

    private Context context = this;

    private boolean working;
    private ClientSocket socket;

    private Context mContext;
    private int set_time;

    private static final int On = 999;
    private static final int Off = 666;

    private int millisInFuture = 60*1000, countDownInterval = 1000;
    private CountDownTimer timer = new CountDownTimer(millisInFuture, countDownInterval) {
        @Override
        public void onTick(long l) {
            remain_time.setText((l / 1000) + " s");
            Message msg = Message.obtain();
            msg.what = On;
            msg.obj = l;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onFinish() {
            remain_time.setText("done!");
            stopWork();
        }
    };


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case On:
                    //do sth;
                    if(working){
                        return;
                    }
                    new Thread(){
                        public void run(){
                            startWork();
                        }
                    }.start();
                    Toast.makeText(context, "already send start command...", Toast.LENGTH_SHORT).show();
                    working = true;
                    break;
                case Off:
                    new Thread(){
                        public void run(){
                            stopWork();
                        }
                    }.start();
                    Toast.makeText(context, "already send stop command...", Toast.LENGTH_SHORT).show();
                    working = false;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.remote_controller;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle("设备养生");
        _switch = bindViewId(R.id.remote_starter);
        _stop_now = bindViewId(R.id.stop_now);
        change_time = bindViewId(R.id.change_interval);
        status = bindViewId(R.id.status);
        water_time = bindViewId(R.id.water_time);
        remain_time = bindViewId(R.id.remaining_time);
        _switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.start();
            }
        });

        _stop_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    public void run(){
                        stopWork();
                        timer.cancel();
                        remain_time.setText("0");
                    }
                }.start();
                Toast.makeText(context, "already send stop command...", Toast.LENGTH_SHORT).show();
            }
        });



        change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resoled_time = water_time.getText().toString();
                set_time = resoled_time != null && resoled_time !="" ? Integer.parseInt(resoled_time) : millisInFuture;
                Toast.makeText(mContext, "设置时间为" + set_time + "s", Toast.LENGTH_SHORT).show();
                timer = new CountDownTimer(set_time*1000, countDownInterval) {
                    @Override
                    public void onTick(long l) {
                        remain_time.setText((l / 1000) + " s");
                        Message msg = Message.obtain();
                        msg.what = On;
                        msg.obj = l;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFinish() {
                        remain_time.setText("done!");
                        Message msg = Message.obtain();
                        msg.what = Off;
                        mHandler.sendMessage(msg);
                    }
                };
            }
        });

    }



    private void shiftStatus() {
        try {
            socket.connectServer();
        } catch (IOException e) {

        }

        if (working) {
            status.setText("关闭");
            working = false;
            try {
                socket.sendSinge("0");
            } catch (IOException e) {

            }

        } else {
            status.setText("开启");
            working = true;
            try {
                socket.sendSinge("1");
            } catch (IOException e) {

            }
        }
    }


    private void startWork() {
        try {
            socket = new ClientSocket("120.26.187.166", 7100);
            socket.connectServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status.setText("开启");
        working = false;
        try {
            socket.sendSinge("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //主动关闭养生
    private void stopWork() {
        try {
            socket = new ClientSocket("120.26.187.166", 7100);
            socket.connectServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status.setText("关闭");
        working = false;
        try {
            socket.sendSinge("0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        working = false;
        mContext = this;
    }
}
