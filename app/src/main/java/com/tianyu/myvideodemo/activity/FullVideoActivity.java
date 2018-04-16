package com.tianyu.myvideodemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tianyu.myvideodemo.R;
import com.tianyu.myvideodemo.model.VideoBean;
import com.tianyu.myvideodemo.widget.MediaHelp;
import com.tianyu.myvideodemo.widget.VideoMediaController;
import com.tianyu.myvideodemo.widget.VideoSuperPlayer;

/**
 * 全屏播放
 *
 * @author shisheng.zhao
 */
public class FullVideoActivity extends Activity {
    private VideoSuperPlayer mVideo;
    private String videoUrl;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_full);
        videoUrl = getIntent().getExtras().getString("videoUrl");
        position = getIntent().getExtras().getInt("position");
        initView();
        initData();
    }

    private void initView() {
        mVideo = (VideoSuperPlayer) findViewById(R.id.videoSuperPlayer);
    }

    private void initData() {
        mVideo.loadAndPlay(MediaHelp.getInstance(), videoUrl, position, true);
        mVideo.setPageType(VideoMediaController.PageType.EXPAND);
        mVideo.setVideoPlayCallback(new VideoSuperPlayer.VideoPlayCallbackImpl() {
            @Override
            public void onSwitchPageType() {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    finish();
                }
            }

            @Override
            public void onPlayFinish() {
                finish();
            }

            @Override
            public void onCloseVideo() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("position", mVideo.getCurrentPosition());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaHelp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaHelp.resume();
    }
}
