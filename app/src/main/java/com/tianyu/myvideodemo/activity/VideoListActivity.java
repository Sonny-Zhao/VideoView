package com.tianyu.myvideodemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.tianyu.myvideodemo.R;
import com.tianyu.myvideodemo.adapter.VideoListAdapter;
import com.tianyu.myvideodemo.model.VideoBean;
import com.tianyu.myvideodemo.widget.MediaHelp;
import com.tianyu.myvideodemo.widget.VideoSuperPlayer;

/**
 * 列表播放视频activity
 *
 * @author shisheng.zhao
 */
public class VideoListActivity extends Activity {
    private String url = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    private List<VideoBean> videoList = new ArrayList<VideoBean>();
    private ListView mListView;
    private VideoListAdapter adapter;
    public int firstVisible = 0, visibleCount = 0, totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initView();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            videoList.add(new VideoBean(url));
        }
        adapter = new VideoListAdapter(this, videoList);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        Log.e("videoTest", "SCROLL_STATE_FLING");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.e("videoTest", "SCROLL_STATE_IDLE");
                       /* if ((adapter.getIndexPostion() > mListView.getFirstVisiblePosition() ||
                                (adapter.getIndexPostion() < mListView.getLastVisiblePosition()) && !adapter.getIsPlaying())) {
                            VideoSuperPlayer mSuperVideoPlayer = (VideoSuperPlayer) mListView.getChildAt(firstVisible).findViewById(R.id.video);
                            MediaHelp.release();
                            adapter.setIndexPostion(firstVisible);
                            adapter.setIsPlaying(true);
                            mSuperVideoPlayer.setVisibility(View.VISIBLE);
                            mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), videoList.get(1).getUrl(), 0, false);
//                            adapter.notifyDataSetChanged();
                        }*/
                        /*autoPlayVideo(view);*/
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        Log.e("videoTest", "SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // firstVisibleItem   当前第一个可见的item
                // visibleItemCount   当前可见的item个数
                Log.e("videoTest", "firstVisiblePos  =  " + firstVisible + "visibleItemCount =  " + visibleCount + "totalCount =" + totalCount);
                if (firstVisible == firstVisibleItem) {
                    return;
                }
                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;
                totalCount = totalItemCount;
                if ((adapter.getIndexPostion() < mListView.getFirstVisiblePosition()
                        || adapter.getIndexPostion() > mListView.getLastVisiblePosition()) && adapter.getIsPlaying()) {
                    adapter.setIsPlaying(false);
                    adapter.setIndexPostion(-1);
                    adapter.notifyDataSetChanged();
                    MediaHelp.release();
                }
            }
        });
    }

    void autoPlayVideo(AbsListView view) {
        Log.e("videoTest", "firstVisiblePos  =  " + firstVisible + "visibleItemCount =  " + visibleCount + "totalCount =" + totalCount);
        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.video) != null) {
                VideoSuperPlayer mSuperVideoPlayer = (VideoSuperPlayer) view.getChildAt(i).findViewById(R.id.video);
                ImageView mPlayBtnView = (ImageView) view.getChildAt(i).findViewById(R.id.play_btn);
                Rect rect = new Rect();
                mSuperVideoPlayer.getLocalVisibleRect(rect);
                int videoheight3 = mSuperVideoPlayer.getHeight();
                Log.e("videoTest", "i=" + i + "===" + "videoheight3:" + videoheight3 + "===" + "rect.top:" + rect.top + "===" + "rect.bottom:" + rect.bottom);
                if (rect.top == 0 && rect.bottom == videoheight3) {
                    MediaHelp.release();
                    adapter.setIndexPostion(i);
                    adapter.setIsPlaying(true);
                    mSuperVideoPlayer.setVisibility(View.VISIBLE);
                    mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), videoList.get(i).getUrl(), 0, false);
                    mSuperVideoPlayer.setVideoPlayCallback(new VideoPlayCallback(mPlayBtnView, mSuperVideoPlayer, videoList.get(i)));
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }


    /**
     * 视频播放回调
     *
     * @author shisheng.zhao
     */
     class VideoPlayCallback implements VideoSuperPlayer.VideoPlayCallbackImpl {
        ImageView mPlayBtnView;
        VideoSuperPlayer mSuperVideoPlayer;
        VideoBean info;

        public VideoPlayCallback(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer, VideoBean info) {
            this.mPlayBtnView = mPlayBtnView;
            this.info = info;
            this.mSuperVideoPlayer = mSuperVideoPlayer;
        }

        @Override
        public void onCloseVideo() {
            closeVideo();
        }

        @Override
        public void onSwitchPageType() {
            if (VideoListActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                // 全屏播放
                Intent intent = new Intent(new Intent(VideoListActivity.this, FullVideoActivity.class));
                intent.putExtra("video", info);
                intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
                startActivityForResult(intent, 1);
            }
        }

        @Override
        public void onPlayFinish() {
            closeVideo();
        }

        private void closeVideo() {
            adapter.setIsPlaying(false);
            adapter.setIndexPostion(-1);
            mSuperVideoPlayer.close();
            MediaHelp.release();
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaHelp.getInstance().seekTo(data.getIntExtra("position", 0));
    }

    @Override
    protected void onDestroy() {
        MediaHelp.release();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        MediaHelp.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        MediaHelp.pause();
        super.onPause();
    }
}
