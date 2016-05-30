package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
//报警信息
public class AlarmBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String alarm_title; //报警标题
	private String alarm_time; //报警时间
	public String getAlarm_title() {
		return alarm_title;
	}
	public void setAlarm_title(String alarm_title) {
		this.alarm_title = alarm_title;
	}
	public String getAlarm_time() {
		return alarm_time;
	}
	public void setAlarm_time(String alarm_time) {
		this.alarm_time = alarm_time;
	}

}
