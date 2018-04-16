package com.tianyu.myvideodemo.adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tianyu.myvideodemo.R;
import com.tianyu.myvideodemo.activity.FullVideoActivity;
import com.tianyu.myvideodemo.model.VideoBean;
import com.tianyu.myvideodemo.widget.MediaHelp;
import com.tianyu.myvideodemo.widget.VideoSuperPlayer;

/**
 * 列表播放视频adapter
 * @author shisheng.zhao
 *
 */
public class VideoListAdapter extends BaseAdapter {
	private Context context;
	private List<VideoBean> videoList;
	LayoutInflater inflater;
	public boolean isPlaying;
	public int indexPostion = -1;

	public VideoListAdapter(Context context, List<VideoBean> videoList) {
		this.context = context;
		this.videoList = videoList;
		inflater = LayoutInflater.from(context);
	}

	public VideoListAdapter(Context context){
this.context = context;
	}

	@Override
	public VideoBean getItem(int position) {
		return videoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return videoList.size();
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		VideoViewHolder holder = null;
		if (v == null) {
			holder = new VideoViewHolder();
			v = inflater.inflate(R.layout.list_video_item, parent, false);
			holder.mVideoViewLayout = (VideoSuperPlayer) v.findViewById(R.id.video);
			holder.mPlayBtnView = (ImageView) v.findViewById(R.id.play_btn);
			v.setTag(holder);
		} else {
			holder = (VideoViewHolder) v.getTag();
		}
		holder.mPlayBtnView.setOnClickListener(new MyPlayBtnOnclick(holder.mPlayBtnView, holder.mVideoViewLayout, position));
		if (indexPostion == position) {
			holder.mVideoViewLayout.setVisibility(View.VISIBLE);
		} else {
			holder.mVideoViewLayout.setVisibility(View.GONE);
			holder.mVideoViewLayout.close();
		}
		return v;
	}

	/**
	 * 点击播放视频监听
	 * @author shisheng.zhao
	 *
	 */
	class MyPlayBtnOnclick implements OnClickListener {
		VideoSuperPlayer mSuperVideoPlayer;
		ImageView mPlayBtnView;
		int position;

		public MyPlayBtnOnclick(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer, int position) {
			this.position = position;
			this.mSuperVideoPlayer = mSuperVideoPlayer;
			this.mPlayBtnView = mPlayBtnView;
		}

		@Override
		public void onClick(View v) {
			MediaHelp.release();
			indexPostion = position;
			isPlaying = true;
			mSuperVideoPlayer.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), videoList.get(position).getUrl(), 0, false);
			mSuperVideoPlayer.setVideoPlayCallback(
					new MyVideoPlayCallback(mPlayBtnView, mSuperVideoPlayer, videoList.get(position)));
			notifyDataSetChanged();
		}
	}

	/**
	 * 视频播放回调
	 * @author shisheng.zhao
	 *
	 */
	public class MyVideoPlayCallback implements VideoSuperPlayer.VideoPlayCallbackImpl {
		ImageView mPlayBtnView;
		VideoSuperPlayer mSuperVideoPlayer;
		VideoBean info;

		public MyVideoPlayCallback(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer, VideoBean info) {
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
			if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				// 全屏播放
				Intent intent = new Intent(new Intent(context, FullVideoActivity.class));
				intent.putExtra("videoUrl", info.getUrl());
				intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
				((Activity) context).startActivityForResult(intent, 1);
			}
		}

		@Override
		public void onPlayFinish() {
			closeVideo();
		}

		private void closeVideo() {
			isPlaying = false;
			indexPostion = -1;
			mSuperVideoPlayer.close();
			MediaHelp.release();
			mPlayBtnView.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.setVisibility(View.GONE);
		}

	}

	class VideoViewHolder {
		private VideoSuperPlayer mVideoViewLayout;
		private ImageView mPlayBtnView;
	}

	public void setIsPlaying(boolean isPlaying){
		this.isPlaying = isPlaying;
	}
	
	public boolean getIsPlaying(){
		return isPlaying;
	}
	
	public void setIndexPostion(int indexPostion){
		this.indexPostion = indexPostion;
	}
	
	public int getIndexPostion(){
		return indexPostion;
	}
}
