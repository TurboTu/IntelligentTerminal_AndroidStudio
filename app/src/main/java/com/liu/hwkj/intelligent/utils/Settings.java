package com.liu.hwkj.intelligent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

	private boolean isAllowNet = true;
	private boolean isAllowLoacation = true;


	//从配置文件中读取设置信息
	public static Settings getSettingsFromPerf(Context context){
		Settings settings = new Settings();

		SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_APPEND);

		settings.setAllowNet(sp.getBoolean("isAllowNet", true));
		settings.setAllowLoacation(sp.getBoolean("isAllowLoacation", true));

		return settings;
	}

	//将设置信息保存到配置文件中
	public boolean savaToPref(Context context){
		SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.putBoolean("isAllowNet", this.isAllowNet).commit();
		editor.putBoolean("isAllowLoacation", this.isAllowLoacation).commit();
		return true;
	}

	public boolean isAllowNet() {
		return isAllowNet;
	}
	public void setAllowNet(boolean isAllowNet) {
		this.isAllowNet = isAllowNet;
	}
	public boolean isAllowLoacation() {
		return isAllowLoacation;
	}
	public void setAllowLoacation(boolean isAllowLoacation) {
		this.isAllowLoacation = isAllowLoacation;
	}


}
