package com.liu.hwkj.intelligent;

import com.liu.component.customprogressdialog.CustomProgressDialog;
import com.liu.hwkj.intelligent.bean.UserBean;
import com.liu.hwkj.intelligent.net.WebServiceManager;
import com.liu.hwkj.intelligent.utils.CommonUtil;
import com.liu.hwkj.intelligentterminal.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	public static int INPUT_PHONE_NUMBER = 0; // 打开手机号输入界面
	private UserBean userBean;

	private String username;
	private String password;
	private Context context;
	private Button loginButton; // 登陆按钮

	private EditText usernameEditText; // 用户名输入框
	private EditText passwordEditText; // 密码输入框
	private TextView tipInfoTextView;
	private CheckBox remeberCheckBox; // 记住密码
	private TextView version;

	private CustomProgressDialog progressDialog = null;

	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		sp = getSharedPreferences("login", MODE_APPEND);

		context = this;
		initComponent();
		initEventListener();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在登录...");

		remeberCheckBox = (CheckBox) findViewById(R.id.recd);
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.pass);

		username = sp.getString("save_username", "");
		password = sp.getString("save_password", "");
		if (!"".equals(username)) {
			usernameEditText.setText(username);
			passwordEditText.setText(password);
			remeberCheckBox.setChecked(true);
		}

		loginButton = (Button) findViewById(R.id.signin_button);
		tipInfoTextView = (TextView) findViewById(R.id.tipinfo);
		version = (TextView) findViewById(R.id.version);
		version.setText("版本：V" + CommonUtil.getVersionName(context));
	}

	/**
	 * 初始化事件
	 */
	private void initEventListener() {

		// 登录
		loginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				progressDialog.show();
				tipInfoTextView.setText("");
				new Thread() {
					@Override
					public void run() {

						username = usernameEditText.getText().toString().trim();
						password = passwordEditText.getText().toString().trim();
						Message msg = new Message();
						Bundle b = new Bundle();
						if ("".equals(username)) {
							b.putString("info", "用户名不能为空！");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
							return;
						}
						if ("".equals(password)) {
							b.putString("info", "密码不能为空！");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
							return;
						}

						WebServiceManager webServiceManager = new WebServiceManager(getApplicationContext());
						String[][] paras = { { "strUserName", username }, { "strPassWord", password } };
                        String test = "{\"Result\":\"0\",\"USER_ID\":\"48f3889c-af8d-401f-ada2-c383031af92d\",\"USER_ACCOUNT\":\"system\",\"USER_PWD\":\"54B53072540EEEB8F8E9343E71F28176\",\"USER_NAME\":\"管理员\",\"DELETEMARK\":\"1\",\"USERGROUP_ID\":\"901659a9-e671-4b2f-b7fa-0d77144fb4c3\"}";
						userBean = (UserBean)webServiceManager.GetRemoteData("getLogin", paras, UserBean.class, test);

						if (userBean == null) {
							b.putString("info", "网络连接错误，请检查网络连接！");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
						} else if ("1".equals(userBean.getResult())) {
							b.putString("info", "账户或者密码有错误！");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
						} else if ("2".equals(userBean.getResult())) {
							b.putString("info", "账户被锁,联系管理员！");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
						} else if ("0".equals(userBean.getResult())) {

							if (remeberCheckBox.isChecked()) {
								Editor edit = sp.edit();
								edit.putString("save_username", username);
								edit.putString("save_password", password);
								edit.commit();

							} else {
								sp.edit().clear();
							}

							b.putString("info", "success");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
						} else {
							b.putString("info", "其他未知错误");
							msg.setData(b);
							TipsHandler.sendMessage(msg);
						}
					}
				}.start();
			}
		});
	}

	private void startMainActivity() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RoutingActivity.class);
		intent.putExtra("userBean", userBean);
		startActivity(intent);
		progressDialog.dismiss();
		LoginActivity.this.finish();
	}

	@SuppressLint("HandlerLeak")
	private Handler TipsHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String info = b.getString("info");
			if ("success".equals(info)) {
				startMainActivity();
			} else {
				tipInfoTextView.setText(info);
				progressDialog.cancel();
			}
		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("Login", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.e("Login", "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e("Login", "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.e("Login", "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.e("Login", "onStop");
		super.onStop();
	}

}
