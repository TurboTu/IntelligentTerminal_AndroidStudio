package com.liu.hwkj.intelligent.gps;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.liu.hwkj.intelligent.net.WebServiceManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GpsService extends Service {

	ArrayList<CellInfo> cellIds = null;
	private String myAppKey;
	private Gps gps = null;
	private boolean threadDisable = false;
	private final static String TAG = GpsService.class.getSimpleName();

	private Timer timer = null;


	@Override
	public void onCreate() {
		super.onCreate();

		gps = new Gps(GpsService.this);
		cellIds = UtilTool.init(GpsService.this);

		new Thread() {
			@Override
			public void run() {
				WebServiceManager webServiceManager = new WebServiceManager(getApplicationContext());

				myAppKey = webServiceManager.getAppKey(getDevicedID());
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("flag", "getDevicedID");
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}.start();

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (!threadDisable) {
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//					if (gps != null) { // 当结束服务时gps为空
//						// 获取经纬度
//						Location location = gps.getLocation();
//						// 如果gps无法获取经纬度，改用基站定位获取
//						if (location == null) {
//							Log.v(TAG, "gps location null");
//							// 2.根据基站信息获取经纬度
//							try {
//								location = UtilTool.callGear(GpsService.this,
//										cellIds);
//							} catch (Exception e) {
//								location = null;
//								e.printStackTrace();
//							}
//							if (location == null) {
//								Log.v(TAG, "cell location null");
//							}
//						}
//
////						// 发送广播
////						Intent intent = new Intent();
////						intent.putExtra("lat",
////								location == null ? "" : location.getLatitude()
////										+ "");
////						intent.putExtra("lon",
////								location == null ? "" : location.getLongitude()
////										+ "");
////						intent.setAction("com.liu.hwkj.easymanager.gps.GpsService");
////						sendBroadcast(intent);
//					}
//
//				}
//			}
//		}).start();

	}

	private TimerTask getLocationTask = new TimerTask() {
		public void run() {

			if (gps != null) { // 当结束服务时gps不为空
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("flag", "getLocationFromBaseStation");
				msg.setData(b);
				handler.sendMessage(msg);

//				// 获取经纬度
//				Location location = gps.getLocation();
//				// 如果gps无法获取经纬度，改用基站定位获取
//				if (location == null) {
//					Log.v(TAG, "gps location null");
//					// 2.根据基站信息获取经纬度
//					try {
//						location = UtilTool.callGear(GpsService.this,
//								cellIds);
//					} catch (Exception e) {
//						location = null;
//						e.printStackTrace();
//					}
//					if (location == null) {
//						Log.v(TAG, "cell location null");
//					}
//				}
//				if(location != null){
//					WebServiceManager webServiceManager = new WebServiceManager();
//					String result = webServiceManager.getMobileLBS("", location.getLongitude() + "", location.getLatitude() + "", myAppKey);
//					Message msg = new Message();
//					Bundle b = new Bundle();
//					b.putString("flag", "addLocation");
//					b.putString("result", result);
//					msg.setData(b);
//					handler.sendMessage(msg);
//				}
			}

		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String actionFlag = b.getString("flag");
			if ("getDevicedID".equals(actionFlag)) {

				if (!UtilTool
						.isGpsEnabled((LocationManager) getSystemService(Context.LOCATION_SERVICE))) {
					Toast.makeText(getApplicationContext(),
							"GSP当前已禁用，请在您的系统设置屏幕启动。", Toast.LENGTH_LONG).show();
					// Intent callGPSSettingIntent = new Intent(
					// android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					// startActivity(callGPSSettingIntent);
					// return;
				}
				//初始化计时器
				timer = new Timer(true);
				timer.schedule(getLocationTask, 1000, 1000);
			}
			else if("getLocationFromBaseStation".equals(actionFlag)){
				// 获取经纬度
				Location location = gps.getLocation();
				// 如果gps无法获取经纬度，改用基站定位获取
				if (location == null) {
					Log.v(TAG, "gps location null");
					// 2.根据基站信息获取经纬度
					try {
						location = UtilTool.callGear(GpsService.this,
								cellIds);
					} catch (Exception e) {
						location = null;
						e.printStackTrace();
					}
					if (location != null) {
						final double longitude =  location.getLongitude();
						final double latitude =  location.getLatitude();

						new Thread() {
							@Override
							public void run() {
								WebServiceManager webServiceManager = new WebServiceManager(getApplicationContext());
								String result = webServiceManager.getMobileLBS("", longitude + "", latitude + "", myAppKey);
								Message msg = new Message();
								Bundle b = new Bundle();
								b.putString("flag", "addLocation");
								b.putString("result", result);
								msg.setData(b);
								handler.sendMessage(msg);
							}
						}.start();
					}
				}
			}
			else if("addLocation".equals(actionFlag)){
				String result = b.getString("result");
//				0成功
//				1;//插入数据库错误
//				2;//没有该车辆信息
//				3;//传递参数错误
//				4;//未授权客户端
				if("1".equals(result)){
					Toast.makeText(getApplicationContext(), "插入数据库错误", Toast.LENGTH_LONG).show();
					timer.cancel();
				}
				else if("1".equals(result)){
					Toast.makeText(getApplicationContext(), "没有该车辆信息", Toast.LENGTH_LONG).show();
					timer.cancel();
				}
				else if("1".equals(result)){
					Toast.makeText(getApplicationContext(), "传递参数错误", Toast.LENGTH_LONG).show();
					timer.cancel();
				}
				else if("1".equals(result)){
					Toast.makeText(getApplicationContext(), "未授权客户端", Toast.LENGTH_LONG).show();
					timer.cancel();
				}
			}
		}
	};

	private String getDevicedID() {
		String androidId = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.ANDROID_ID);
		return androidId;
	}

	@Override
	public void onDestroy() {
		threadDisable = true;
		if (cellIds != null && cellIds.size() > 0) {
			cellIds = null;
		}
		if (gps != null) {
			gps.closeLocation();
			gps = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}