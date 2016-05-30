package com.liu.hwkj.intelligent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.liu.hwkj.cameracapture.CameraMonitorActivity;
import com.liu.hwkj.cameracapture.SrsEncoder;
import com.liu.hwkj.cameracapture.rtmp.RtmpPublisher;
import com.liu.hwkj.intelligent.update.UpdateManager;
import com.liu.hwkj.intelligent.utils.CommonUtil;
import com.liu.hwkj.intelligent.utils.Settings;
import com.liu.hwkj.intelligentterminal.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends Activity implements UpdateManager.Listener {

	public static MainActivity instance = null;

	private static final int SOCKET_CONNECT_SUCCESS = 0; //socket连接成功
	private static final int SOCKET_CONNECT_ERROR = 1; //socket连接失败
	private static final int SOCKET_CONNECT_TIMEOUT = 2; //socket连接超时
	private static final int SOCKET_DISCONNECT = 3; //socket断开连接
	private static final int SOCKET_RECEIVE_REQUST = 4; //收到请求

	private String deviceID = "";

	private UpdateManager updateMan;

	private LinearLayout main_routing_ll;
	private LinearLayout main_temperature_ll;
	private LinearLayout main_talkback_ll;

	private ImageView main_routing_dot;
	private ImageView main_temperature_dot;
	private ImageView main_talkback_dot;

	private ImageView main_routing_icon;
	private ImageView main_temperature_icon;
	private ImageView main_talkback_icon;

	private TextView main_routing_text;
	private TextView main_temperature_text;
	private TextView main_talkback_text;

	private Socket socket = null;
	private String socketURL = "58.57.131.90:8001";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		instance = this;
		deviceID = CommonUtil.getDevicedID(getApplicationContext());
		// 初始化组件
		initComponent();
		// 初始化事件
		initEventListner();

		socketURL = instance.getString(R.string.SOCKET_URL);

		InitSocket();
		loginIn();

		Settings settings = Settings.getSettingsFromPerf(getApplicationContext());
		((MyApplication)getApplication()).setSettings(settings);

		updateMan = new UpdateManager(MainActivity.this);
		updateMan.registerListener(this);
		new Thread() {
			@Override
			public void run() {
				if (updateMan.isUpdate()) {
					updateMan.checkUpdate();
				}
			}
		}.start();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		main_routing_ll = (LinearLayout) findViewById(R.id.main_routing_ll);
		main_temperature_ll = (LinearLayout) findViewById(R.id.main_temperature_ll);
		main_talkback_ll = (LinearLayout) findViewById(R.id.main_talkback_ll);

		main_routing_dot = (ImageView) findViewById(R.id.main_routing_dot);
		main_temperature_dot = (ImageView) findViewById(R.id.main_temperature_dot);
		main_talkback_dot = (ImageView) findViewById(R.id.main_talkback_dot);

		main_routing_icon = (ImageView) findViewById(R.id.main_routing_icon);
		main_temperature_icon = (ImageView) findViewById(R.id.main_temperature_icon);
		main_talkback_icon = (ImageView) findViewById(R.id.main_talkback_icon);

		main_routing_text = (TextView) findViewById(R.id.main_routing_text);
		main_temperature_text = (TextView) findViewById(R.id.main_temperature_text);
		main_talkback_text = (TextView) findViewById(R.id.main_talkback_text);
	}

	/**
	 * 初始化事件
	 */
	private void initEventListner() {
		// 智能巡检
		main_routing_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetAllButtonState();

				main_routing_dot.setVisibility(View.VISIBLE);
				main_routing_icon.setBackgroundResource(R.drawable.main_routing_select);
				main_routing_text.setTextColor(getResources().getColor(R.color.main_button_text_gray));

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}
		});

		// 测温测气
		main_temperature_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetAllButtonState();

				main_temperature_dot.setVisibility(View.VISIBLE);
				main_temperature_icon.setBackgroundResource(R.drawable.main_temperature_select);
				main_temperature_text.setTextColor(getResources().getColor(R.color.main_button_text_gray));

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MeasureTemperatureActivity.class);
				startActivity(intent);
			}
		});

		// 智能对讲
		main_talkback_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetAllButtonState();

				main_talkback_dot.setVisibility(View.VISIBLE);
				main_talkback_icon.setBackgroundResource(R.drawable.main_talkback_select);
				main_talkback_text.setTextColor(getResources().getColor(R.color.main_button_text_gray));

				startOrInstallApk("com.airtalkee", "talkback.apk");
			}
		});
	}

	private void InitSocket() {
		if (socket == null) {
			try {
				socket = IO.socket(socketURL);
				socket.on(Socket.EVENT_CONNECT, socketConnectListener)
						.on(Socket.EVENT_CONNECT_ERROR, socketEventConnectError)
						.on(Socket.EVENT_CONNECT_TIMEOUT, socketEventConnectTimeOut)
						.on("require_video", requireEventListener)
						.on(Socket.EVENT_DISCONNECT, socketDisconnectListener);
				socket.connect();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loginIn() {
		if (socket == null) {
			Toast.makeText(getApplicationContext(), "连接失败！", Toast.LENGTH_LONG).show();
		}
		JSONObject jsonObj = new JSONObject();

		try {
			jsonObj.put("userID", deviceID);
			jsonObj.put("userType", "1");
			jsonObj.put("userName", "cs");
			socket.emit("login", jsonObj);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Emitter.Listener socketConnectListener = new Emitter.Listener() {
		@Override
		public void call(Object... args) {
			Message msg = mHandler.obtainMessage(SOCKET_CONNECT_SUCCESS);
			mHandler.sendMessage(msg);
		}
	};

	private Emitter.Listener requireEventListener = new Emitter.Listener() {
		@Override
		public void call(Object... args) {
			Message msg = mHandler.obtainMessage(SOCKET_RECEIVE_REQUST);
			mHandler.sendMessage(msg);
		}
	};

	private Emitter.Listener socketDisconnectListener = new Emitter.Listener() {
		@Override
		public void call(Object... args) {
			Message msg = mHandler.obtainMessage(SOCKET_DISCONNECT);
			mHandler.sendMessage(msg);
		}
	};

	private Emitter.Listener socketEventConnectError = new Emitter.Listener() {
		@Override
		public void call(Object... args) {
			Message msg = mHandler.obtainMessage(SOCKET_CONNECT_ERROR);
			mHandler.sendMessage(msg);
		}
	};

	private Emitter.Listener socketEventConnectTimeOut = new Emitter.Listener() {
		@Override
		public void call(Object... args) {
			Message msg = mHandler.obtainMessage(SOCKET_CONNECT_TIMEOUT);
			mHandler.sendMessage(msg);
		}
	};

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case SOCKET_CONNECT_SUCCESS:
					Toast.makeText(getApplicationContext(), "socket连接成功", Toast.LENGTH_LONG).show();
					break;
				case SOCKET_CONNECT_ERROR:
					Toast.makeText(getApplicationContext(), "socket连接失败", Toast.LENGTH_LONG).show();
					break;
				case SOCKET_CONNECT_TIMEOUT:
					Toast.makeText(getApplicationContext(), "socket连接超时", Toast.LENGTH_LONG).show();
					break;
				case SOCKET_DISCONNECT:
					Toast.makeText(getApplicationContext(), "socket断开连接", Toast.LENGTH_LONG).show();
					break;
				case SOCKET_RECEIVE_REQUST:
					DealRequireDialog();
					break;
			}
		}
	};

	//收到调用摄像头请求后弹出是否接收对话框
	private void DealRequireDialog(){

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("是否接受打开摄像头请求？？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				JSONObject jsonObj = new JSONObject();

				try {
					jsonObj.put("userID", deviceID);
					jsonObj.put("userName", "cs");
					socket.emit("agree_play", jsonObj);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), CameraMonitorActivity.class);
				startActivity(intent);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	/**
	 * 清除按钮状态
	 */
	private void resetAllButtonState() {

		main_routing_dot.setVisibility(View.INVISIBLE);
		main_temperature_dot.setVisibility(View.INVISIBLE);
		main_talkback_dot.setVisibility(View.INVISIBLE);

		main_routing_icon.setBackgroundResource(R.drawable.main_routing_unselect);
		main_temperature_icon.setBackgroundResource(R.drawable.main_temperature_unselect);
		main_talkback_icon.setBackgroundResource(R.drawable.main_talkback_unselect);

		main_routing_text.setTextColor(getResources().getColor(R.color.main_button_text_blue));
		main_temperature_text.setTextColor(getResources().getColor(R.color.main_button_text_blue));
		main_talkback_text.setTextColor(getResources().getColor(R.color.main_button_text_blue));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}


	/**
	 * 安装或启动apk应用
	 *
	 * @param packageName
	 * @param apkName
	 */
	private void startOrInstallApk(String packageName, String apkName) {
		if (isAvilible(this, packageName)) {
			try {
				Intent intent = new Intent();
				intent = getPackageManager().getLaunchIntentForPackage(packageName);

				if (intent != null) {
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					startActivity(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// 未安装，下载该程序并安装
		else {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			String type = "application/vnd.android.package-archive";
			try {
				// 从assets读取文件流
				InputStream is = getClass().getClassLoader().getResourceAsStream("assets/" + apkName);
				// 将该文件流写入到本应用程序的私有数据区this.getFilesDir().getPath();
				FileOutputStream fos = openFileOutput(apkName, Context.MODE_PRIVATE + Context.MODE_WORLD_READABLE);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();
				is.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			File f = new File(getFilesDir().getPath() + "/" + apkName);

			intent.setDataAndType(Uri.fromFile(f), type);
			startActivity(intent);
		}
	}

	/**
	 * 判断是否已安装应用程序
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	private boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				return true;
			default:
				return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onClickCancel(Boolean bool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickConfirm(Boolean bool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadFinished(Boolean bool) {
		// TODO Auto-generated method stub

	}

}
