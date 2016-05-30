package com.liu.hwkj.intelligent;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.liu.hwkj.baidu.trackshow.EMapGeofence;
import com.liu.hwkj.baidu.trackshow.TrackApplication;
import com.liu.hwkj.intelligentterminal.R;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 智能巡检
 *
 * @author Administrator
 *
 */
public class EMapActivity extends BaseActivity {

	public static Trace trace = null; //轨迹服务
	public static String entityName = null; //entity标识
	public static long serviceId = 116072; //鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
	public int traceType = 2; //轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）

	public static LBSTraceClient client = null; //轨迹服务客户端
	public static OnEntityListener entityListener = null; //Entity监听器
	public static Context mContext = null;

	private TextView routing_person_content;
	private TextView department_content;
	private TextView routing_task_content;
	private TextView routing_starttime_content;

	public static MapView mMapView = null; // 百度地图view
	public static BaiduMap mBaiduMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emap);

		mContext = getApplicationContext();

		initOnCreate();
		initComponent();
		initEventListener();
		// 初始化OnEntityListener
		initOnEntityListener();
		EMapGeofence.addEntity();

		title.setText(getResources().getString(R.string.routing_emap));
	}

	private void initComponent() {
		routing_person_content = (TextView)findViewById(R.id.routing_person_content);
		routing_person_content.setText(userBean.getUSER_NAME());

		department_content = (TextView)findViewById(R.id.department_content);
		routing_task_content = (TextView)findViewById(R.id.routing_task_content);
		routing_starttime_content = (TextView)findViewById(R.id.routing_starttime_content);

		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mMapView.showZoomControls(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);

		// 初始化轨迹服务客户端
		client = new LBSTraceClient(mContext);
		// 设置定位模式
		client.setLocationMode(LocationMode.High_Accuracy);
		// 初始化entity标识
		entityName = "myTrace";
		// 初始化轨迹服务
		trace = new Trace(getApplicationContext(), serviceId, entityName, traceType);

	}

	private void initEventListener() {

	}

	/**
	 * 初始化OnEntityListener
	 */
	private void initOnEntityListener() {
		entityListener = new OnEntityListener() {

			// 请求失败回调接口
			@Override
			public void onRequestFailedCallback(String arg0) {
				// TODO Auto-generated method stub
				// TrackApplication.showMessage("entity请求失败回调接口消息 : " + arg0);
				System.out.println("entity请求失败回调接口消息 : " + arg0);
			}

			// 添加entity回调接口
			public void onAddEntityCallback(String arg0) {
				// TODO Auto-generated method stub
				TrackApplication.showMessage("添加entity回调接口消息 : " + arg0);
			}

			// 查询entity列表回调接口
			@Override
			public void onQueryEntityListCallback(String message) {
				// TODO Auto-generated method stub
				System.out.println("entityList回调消息 : " + message);
			}

			@Override
			public void onReceiveLocation(TraceLocation location) {
				// TODO Auto-generated method stub
//                if (mTrackUploadFragment != null) {
//                    mTrackUploadFragment.showRealtimeTrack(location);
//                    // System.out.println("获取到实时位置:" + location.toString());
//                }
			}

		};
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		mMapView = null;
		client.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		// bdDeYangHuaGong.recycle();
	}
}
