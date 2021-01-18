package com.example.XTproject.activity.video;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;
import com.example.XTproject.utils.DateUtils;
import com.example.XTproject.utils.SysUtils;
import com.example.XTproject.widget.media.IjkVideoView;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayActivity extends BaseActivity {

    private static final String TAG = PlayActivity.class.getSimpleName();
    private static final int CHECK_TIME = 1;
    private static final int CHECK_PROGRESS = 2;
    private String mUrl;
    private int mStreamType;
    private int mCurrentPosition;
    private MediaStore.Video mVideo;
    private IjkVideoView mVideoView;
    private RelativeLayout mLoadingLayout;
    private TextView mLoadingText;
    private FrameLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mBackButton;
    private TextView mVideoNameView;
    private ImageView mBigPauseButton;
    private CheckBox mPlayOrPauseButton;
    private Formatter mFormatter;
    private StringBuilder mFormatterBuilder;

    private TextView mDragHorizontalView;
    private TextView mDragVerticalView;

    private int mCurrentLight;
    private AudioManager mAudioManager;
    private String mLiveTitle;//直播节目标题


    private static final int AUTO_HIDE_TIME = 10000;
    private static final int AFTER_DRAGGLE_HIDE_TIME = 3000;
    private boolean mIsDragging;
    private TextView mSysTimeView;
    private TextView mVideoCurrentTime;
    private TextView mVideoTotalTime;
    private TextView mBitStreamView;
    private EventHandler mEventHandler;
    private boolean mIsPanelShowing = false;
    private int mBatteryLevel;
    private ImageView mBatteryView;
    private boolean mIsMove = false;//是否在屏幕上滑动
    private SeekBar mSeekBar;
    private long mScrollProgress;
    private boolean mIsHorizontalScroll;
    private boolean mIsVerticalScroll;
    private int mMaxLight = 255;
    private int mCurrentVolume;
    private int mMaxVolume = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mBatteryReceiver != null) {
//            unregisterReceiver(mBatteryReceiver);
//            mBatteryReceiver = null;
//        }
        //释放audiofocus
        mAudioManager.abandonAudioFocus(null);
    }

//    /**
//     * 通过广播获取系统电量情况
//     */
//    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            mBatteryLevel = intent.getIntExtra("level", 0);
//            Log.d(TAG, ">> mBatteryReceiver onReceive mBatteryLevel=" + mBatteryLevel);
//        }
//    };


    //用于组合图片及文字
    private void setComposeDrawableAndText(TextView textView, int drawableId, Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        //这四个参数表示把drawable绘制在矩形区域
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //设置图片在文字的上方
        //The Drawables must already have had drawable.setBounds called.
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    //更新垂直方向上滑动时的百分比
    private void updateVerticalText(int current, int total) {
        NumberFormat formater = NumberFormat.getPercentInstance();
        formater.setMaximumFractionDigits(0);//设置整数部分允许最大小数位 66.5%->66%
        String percent = formater.format((double) (current) / (double) total);
        mDragVerticalView.setText(percent);
    }

    //更新水平方向seek的进度, duration表示变化后的duration
    private void updateHorizontalText(long duration) {
        String text = stringForTime((int) duration) + "/" + stringForTime(mVideoView.getDuration());
        mDragHorizontalView.setText(text);
    }


    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_TIME:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSysTimeView.setText(DateUtils.getCurrentTime());
                        }
                    });
                    break;
                case CHECK_PROGRESS:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long duration = mVideoView.getDuration();
                            long nowduration = (mSeekBar.getProgress() * duration) / 1000L;
                            mVideoCurrentTime.setText(stringForTime((int) nowduration));
                        }
                    });
                    break;
            }
        }
    }


    @Override
    protected void initView() {
        //外部传入一个url
        mUrl = getIntent().getStringExtra("url");
        mLiveTitle = getIntent().getStringExtra("title");
        mStreamType = getIntent().getIntExtra("type", 0);
        mCurrentPosition = getIntent().getIntExtra("currentPosition", 0);
        mVideo = getIntent().getParcelableExtra("video");
        Log.d(TAG, ">> ulr " + mUrl + ", mStreamType " + mStreamType + ", mCurrentPosition " + mCurrentPosition);
        Log.d(TAG, ">> video " + mVideo);
        mEventHandler = new EventHandler(Looper.myLooper());
        initAudio();
        initLight();
        initTopAndBottomView();
        initCenterView();
        initListener();
        //init player
        mVideoView = bindViewId(R.id.video_view);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");


        mLoadingLayout = bindViewId(R.id.rl_loading_layout);
        mLoadingText = bindViewId(R.id.tv_loading_info);
        mLoadingText.setText("正在加载中...");

        //视频信息
        mVideoView.setVideoURI(Uri.parse(mUrl));
        //播放状态的回调
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mVideoView.start();
            }
        });


        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //加载信息的转圈
                        mLoadingLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
//        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        toggleTopAndBottomLayout();
    }

    private void initCenterView() {
        mDragHorizontalView = bindViewId(R.id.tv_horiontal_gesture);
        mDragVerticalView = bindViewId(R.id.tv_vertical_gesture);
    }

    private void initAudio() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 10;// 系统声音取值是0-10,*10为了和百分比相关
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 10;
    }

    private void initLight() {
        mCurrentLight = SysUtils.getDefaultBrightness(this);
        if (mCurrentLight == -1) {//获取不到亮度sharedpreferences文件
            mCurrentLight = SysUtils.getBrightness(this);
        }
    }


    private void initTopAndBottomView() {
        mTopLayout = bindViewId(R.id.fl_player_top_container);
        mBottomLayout = bindViewId(R.id.ll_player_bottom_layout);
        mBackButton = bindViewId(R.id.iv_player_close);//返回按钮
        mVideoNameView = bindViewId(R.id.tv_player_video_name);//video标题
        mBigPauseButton = bindViewId(R.id.iv_player_center_pause);//屏幕中央暂停按钮
        mPlayOrPauseButton = bindViewId(R.id.cb_play_pause);//底部播放暂停按钮
        mFormatterBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatterBuilder, Locale.getDefault());
    }

    private void initListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBigPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();
                updatePlayPauseStatus(true);
            }
        });
        mPlayOrPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayPause();
            }
        });
    }


    private void hideTopAndBottomLayout() {
        if (mIsDragging == true) {
            return;
        }
        mIsPanelShowing = false;
        mTopLayout.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
    }

    private void handlePlayPause() {
        //TODO
        if (mVideoView.isPlaying()) {//视频正在播放
            mVideoView.pause();
            updatePlayPauseStatus(false);
        } else {
            mVideoView.start();
            updatePlayPauseStatus(true);
        }
    }

    private void updatePlayPauseStatus(boolean isPlaying) {
        mBigPauseButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        mPlayOrPauseButton.invalidate();
        mPlayOrPauseButton.setChecked(isPlaying);
        mPlayOrPauseButton.refreshDrawableState();
    }


    @Override
    protected void initData() {
        Log.d(TAG, ">> initData mVideo=" + mVideo);
        mVideoNameView.setText(mLiveTitle);
    }


//    private void setCurrentBattery() {
//        Log.d(TAG, ">> setCurrentBattery level " + mBatteryLevel);
//        if (0 < mBatteryLevel && mBatteryLevel <= 10) {
//            mBatteryView.setBackgroundResource(R.drawable.ic_battery_10);
//        } else if (10 < mBatteryLevel && mBatteryLevel <= 20) {
//            mBatteryView.setBackgroundResource(R.drawable.ic_battery_20);
//        } else if (20 < mBatteryLevel && mBatteryLevel <= 50) {
//            mBatteryView.setBackgroundResource(R.drawable.ic_battery_50);
//        } else if (50 < mBatteryLevel && mBatteryLevel <= 80) {
//            mBatteryView.setBackgroundResource(R.drawable.ic_battery_80);
//        } else if (80 < mBatteryLevel && mBatteryLevel <= 100) {
//            mBatteryView.setBackgroundResource(R.drawable.ic_battery_100);
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

//    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
//        // seekbar进度发生变化时回调
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (!fromUser) {
//                return;
//            }
//            long duration = mVideoView.getDuration();//视频时长
//            long nowPosition = (duration * progress) / 1000L;
//            mVideoCurrentTime.setText(stringForTime((int) nowPosition));
//        }
//
//        // seekbar开始拖动时回调
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            mIsDragging = true;
//        }
//
//        // seekbar拖动完成后回调
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            mIsDragging = false;
//            int progress = seekBar.getProgress();//最后拖动停止的进度
//            long duration = mVideoView.getDuration();//视频时长
//            long newPosition = (duration * progress) / 1000L;//当前的进度
//            mVideoView.seekTo((int) newPosition);
//            mEventHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    hideTopAndBottomLayout();
//                }
//            }, AFTER_DRAGGLE_HIDE_TIME);
//        }
//    };

//    private void updateProgress() {
//        int currentPosition = mVideoView.getCurrentPosition();//当前的视频位置
//        int duration = mVideoView.getDuration();//视频时长
//        if (mSeekBar != null) {
//            if (duration > 0) {
//                //转成long型,避免溢出
//                long pos = currentPosition * 1000L / duration;
//                mSeekBar.setProgress((int) pos);
//            }
//            int perent = mVideoView.getBufferPercentage();//已经缓冲的进度
//            mSeekBar.setSecondaryProgress(perent);//设置缓冲进度
//            mVideoCurrentTime.setText(stringForTime(currentPosition));
//            mVideoTotalTime.setText(stringForTime(duration));
//        }
//    }


    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60; //换成秒
        int minutes = (totalSeconds / 60) % 60;
        int hours = (totalSeconds / 3600);
        mFormatterBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //从直播模块跳转过来
    public static void launch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

}
