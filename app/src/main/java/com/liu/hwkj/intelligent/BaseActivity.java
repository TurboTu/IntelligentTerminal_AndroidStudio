package com.liu.hwkj.intelligent;

import com.liu.hwkj.intelligent.bean.UserBean;
import com.liu.hwkj.intelligentterminal.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends Activity {

	public static final String ACTION_FLAG = "actionFlag";

	protected Activity instance;
	protected TextView title;
	// 头部按钮
	protected LinearLayout back_button;
	protected LinearLayout user_info_layout;

	protected UserBean userBean;

	public void initOnCreate() {

		Intent intent = getIntent();
		userBean = (UserBean)intent.getSerializableExtra("userBean");

		back_button = (LinearLayout) findViewById(R.id.back_button);
		title = (TextView) findViewById(R.id.title);
		user_info_layout = (LinearLayout) findViewById(R.id.user_info_layout);

		back_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});
	}

	protected void back(){
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
