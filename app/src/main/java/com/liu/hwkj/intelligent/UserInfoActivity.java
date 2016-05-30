package com.liu.hwkj.intelligent;

import com.liu.hwkj.intelligent.bean.UserBean;
import com.liu.hwkj.intelligent.net.WebServiceManager;
import com.liu.hwkj.intelligentterminal.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 出入库管理
 *
 * @author Administrator
 *
 */
public class UserInfoActivity extends BaseActivity {

	public static final String ACTION_FLAG = "actionFlag";

	private Context context;

	private TextView username;
	private TextView last_login_date;
	private TextView login_times;

	private Button signout_button; // 退出按钮

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
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
			if ("get_userinfo_complete".equals(actionFlag)) {

			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
	}

	private void initComponent() {

		title.setText(context.getResources().getString(R.string.routing_user));

		signout_button = (Button) findViewById(R.id.signout_button);
		username = (TextView) findViewById(R.id.username);
		username.setText(userBean.getUSER_NAME());

		last_login_date = (TextView) findViewById(R.id.last_login_date);
		login_times = (TextView) findViewById(R.id.login_times);
	}

	private void initEventListener() {

		signout_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				UserInfoActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
}
