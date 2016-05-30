package com.liu.hwkj.intelligent;

import java.util.Calendar;

import com.liu.hwkj.intelligent.bean.RoutingPointBean;
import com.liu.hwkj.intelligentterminal.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateFormat;
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
public class SyncTimeActivity extends BaseActivity {

	public static final String ACTION_FLAG = "actionFlag";

	private Context context;

	private TextView service_time_textview;
	private TextView local_time_textview;

	private Button sync_button; // 退出按钮
	private Calendar mCalendar;
	private String mFormat = "yyyy-MM-dd HH:mm:ss";

	private Runnable mTicker;
	private boolean mTickerStopped = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_time);
		context = this;

		initOnCreate();

		initComponent();
		initEventListener();

		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}

		mTicker = new Runnable() {
			public void run() {
				if (mTickerStopped)
					return;
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				// setText(mSimpleDateFormat.format(mCalendar.getTime()));

				long now = SystemClock.uptimeMillis();
				// long next = now + (1000 - now % 1000);
				long next = now + (1000 - System.currentTimeMillis() % 1000);
				// TODO
				mHandler.postAtTime(mTicker, next);
				mHandler.sendEmptyMessage(0);
			}
		};
		mTicker.run();
	}


	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			local_time_textview.setText(DateFormat.format(mFormat, mCalendar));
		}
	};

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initComponent() {

		title.setText(context.getResources().getString(R.string.routing_eclock));

		sync_button = (Button) findViewById(R.id.sync_button);
		service_time_textview = (TextView) findViewById(R.id.service_time_textview);
		local_time_textview = (TextView) findViewById(R.id.local_time_textview);
	}

	private void initEventListener() {

		sync_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
}
