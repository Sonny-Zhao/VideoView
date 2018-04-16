package com.tianyu.myvideodemo.widget;

import android.media.MediaPlayer;

/**
 * 构建一个MediaPlayer单例，对mediaPlayer进行管理
 * @author shisheng.zhao
 *
 */
public class MediaHelp {
	private static MediaPlayer mPlayer;

	public static MediaPlayer getInstance() {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		return mPlayer;
	}

	/**
	 * MediaPlayer resume
	 */
	public static void resume() {
		if (mPlayer != null) {
			mPlayer.start();
		}

	}

	/**
	 * MediaPlayer pause
	 */
	public static void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	/**
	 * MediaPlayer release
	 */
	public static void release() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}
