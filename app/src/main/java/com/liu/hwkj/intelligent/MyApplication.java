package com.liu.hwkj.intelligent;

import com.baidu.mapapi.SDKInitializer;
import com.liu.hwkj.intelligent.utils.Settings;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class MyApplication extends Application {

	private Settings settings;
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		// 百度地图
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		context = getApplicationContext();
	}

	public static Context getContext() {
		return context;
	}

	public static void showMessage(String message) {
		Looper.prepare();
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		Looper.loop();
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
