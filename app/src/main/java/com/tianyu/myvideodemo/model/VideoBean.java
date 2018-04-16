package com.tianyu.myvideodemo.model;

import java.io.Serializable;

/**
 * 视频videoinfo
 * @author shisheng.zhao
 *
 */
public class VideoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public VideoBean(String url) {
		super();
		this.url = url;
	}
}
