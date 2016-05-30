package com.liu.hwkj.intelligent;

import com.liu.hwkj.intelligentterminal.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

/**
 * 出入库管理
 *
 * @author Administrator
 *
 */
public class HelpActivity extends BaseActivity {

	public static final String ACTION_FLAG = "actionFlag";

	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		context = this;

		initOnCreate();
		initComponent();
		initEventListener();

		new Thread() {
			@Override
			public void run() {
				// WebServiceManager webServiceManager = new
				// WebServiceManager();
				// userInfo =
				// webServiceManager.getLoginInfor(userInfo.getUserAccount());
				// Message msg = new Message();
				// Bundle b = new Bundle();
				// b.putString(ACTION_FLAG, "get_userinfo_complete");
				// msg.setData(b);
				// handler.sendMessage(msg);
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String actionFlag = b.getString(ACTION_FLAG);

		}
	};

	@Override
	public void onStart() {
		super.onStart();
	}

	private void initComponent() {

		title.setText(context.getResources().getString(R.string.routing_help));

	}

	private void initEventListener() {

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
}
