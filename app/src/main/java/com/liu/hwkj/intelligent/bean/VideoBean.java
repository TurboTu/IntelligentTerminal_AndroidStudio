package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;

public class VideoBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String video_title; //视频标题
	private String video_url; //播放地址
	public String getVideo_title() {
		return video_title;
	}
	public void setVideo_title(String video_title) {
		this.video_title = video_title;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
}
