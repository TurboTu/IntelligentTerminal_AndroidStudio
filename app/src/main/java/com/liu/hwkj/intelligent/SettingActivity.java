package com.liu.hwkj.intelligent;

import com.hw.ui.components.SwitchButton;
import com.hw.ui.components.SwitchButton.OnChangeListener;
import com.liu.hwkj.intelligent.utils.CommonUtil;
import com.liu.hwkj.intelligent.utils.Settings;
import com.liu.hwkj.intelligentterminal.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 出入库管理
 *
 * @author Administrator
 *
 */
public class SettingActivity extends BaseActivity {

	public static final String ACTION_FLAG = "actionFlag";

	private Context context;
	private Settings settings;

	private TextView current_version;
	private TextView latest_version;

	private RelativeLayout setting_advanced_clickable_layout;
	private ImageView setting_advanced_arrow;

	private LinearLayout setting_advanced_content_layout;
	private SwitchButton allow_net_switch;
	private SwitchButton sys_location_switch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		context = this;

		settings = ((MyApplication)getApplication()).getSettings();

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

		title.setText(context.getResources().getString(R.string.routing_setting));

		current_version = (TextView) findViewById(R.id.current_version);
		current_version.setText("V" + CommonUtil.getVersionName(context));

		latest_version = (TextView) findViewById(R.id.latest_version);
		latest_version.setText("new");

		setting_advanced_clickable_layout = (RelativeLayout) findViewById(R.id.setting_advanced_clickable_layout);
		setting_advanced_arrow = (ImageView) findViewById(R.id.setting_advanced_arrow);

		setting_advanced_content_layout = (LinearLayout) findViewById(R.id.setting_advanced_content_layout);
		allow_net_switch = (SwitchButton) findViewById(R.id.allow_net_switch);
		allow_net_switch.setSwitch(settings.isAllowNet());

		sys_location_switch = (SwitchButton) findViewById(R.id.sys_location_switch);
		sys_location_switch.setSwitch(settings.isAllowLoacation());
	}

	private void initEventListener() {
		setting_advanced_clickable_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (setting_advanced_content_layout.getVisibility() == View.VISIBLE) {
					setting_advanced_content_layout.setVisibility(View.GONE);
					setting_advanced_arrow.setBackgroundResource(R.drawable.blue_arrow_right);
				} else {
					setting_advanced_content_layout.setVisibility(View.VISIBLE);
					setting_advanced_arrow.setBackgroundResource(R.drawable.blue_arrow_down);
				}
			}
		});

		// 是否允许上网
		allow_net_switch.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChange(SwitchButton sb, boolean switchOff) {
				// TODO Auto-generated method stub
				settings.setAllowNet(!switchOff);
				settings.savaToPref(getApplicationContext());
			}
		});

		// 是否允许系统定位
		sys_location_switch.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChange(SwitchButton sb, boolean switchOff) {
				// TODO Auto-generated method stub
				settings.setAllowLoacation(!switchOff);
				settings.savaToPref(getApplicationContext());
			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
}
