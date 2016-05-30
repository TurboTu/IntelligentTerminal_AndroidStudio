package com.liu.hwkj.intelligent;

import java.util.ArrayList;
import java.util.List;

import com.liu.hwkj.intelligent.bean.AlarmBean;
import com.liu.hwkj.intelligent.bean.RoutingTaskBean;
import com.liu.hwkj.intelligent.net.WebServiceManager;
import com.liu.hwkj.intelligentterminal.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 巡检任务列表，每个巡检任务包含多个巡检点
 *
 * @author Administrator
 *
 */
public class SystemAlarmListActivity extends BaseActivity {

	private LinearLayout alarm_fault_layout;
	private LinearLayout alarm_routing_layout;
	private LinearLayout alarm_temperature_layout;
	private LinearLayout alarm_cross_layout;

	private TextView alarm_fault_textview;
	private TextView alarm_routing_textview;
	private TextView alarm_temperature_textview;
	private TextView alarm_cross_textview;

	private List<AlarmBean> alarmInfos = new ArrayList<AlarmBean>();
	private ListView alarm_listview;
	private SystemAlarmListViewAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_alarm);

		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_alarm));
		AlarmBean data1 = new AlarmBean();
		data1.setAlarm_title("报警标题1");
		data1.setAlarm_time("2016-03-27 10:00:00");

		alarmInfos.add(data1);

		new Thread() {
			@Override
			public void run() {
				WebServiceManager webServiceManager = new WebServiceManager(getApplicationContext());

				// routingTaskInfos =
				// webServiceManager.getDayLiuLiangListForJson(gatherPointInfo.getId(),
				// startTimeQuery, endTimeQuery);

				// Message msg = new Message();
				// Bundle b = new Bundle();
				// b.putString(ACTION_FLAG, "updatelist");
				// msg.setData(b);
				// handler.sendMessage(msg);
			}
		}.start();
	}

	private void initComponent() {

		alarm_fault_layout = (LinearLayout) findViewById(R.id.alarm_fault_layout);
		alarm_routing_layout = (LinearLayout) findViewById(R.id.alarm_routing_layout);
		alarm_temperature_layout = (LinearLayout) findViewById(R.id.alarm_temperature_layout);
		alarm_cross_layout = (LinearLayout) findViewById(R.id.alarm_cross_layout);

		alarm_fault_textview = (TextView) findViewById(R.id.alarm_fault_textview);
		alarm_routing_textview = (TextView) findViewById(R.id.alarm_routing_textview);
		alarm_temperature_textview = (TextView) findViewById(R.id.alarm_temperature_textview);
		alarm_cross_textview = (TextView) findViewById(R.id.alarm_cross_textview);

		alarm_listview = (ListView) findViewById(R.id.alarm_listview);

		alarm_listview.setSelector(new ColorDrawable(Color.argb(150, 33, 155, 241)));
		adapter = new SystemAlarmListViewAdapter(this);
		alarm_listview.setAdapter(adapter);

	}

	private void initEventListener() {

		// 故障报警
		alarm_fault_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				alarm_fault_layout.setBackgroundResource(R.drawable.round_white);
				alarm_fault_textview.setTextColor(getResources().getColor(R.color.main_blue));
			}
		});

		// 巡检报警
		alarm_routing_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				alarm_routing_layout.setBackgroundResource(R.drawable.round_white);
				alarm_routing_textview.setTextColor(getResources().getColor(R.color.main_blue));
			}
		});

		// 测温测气报警
		alarm_temperature_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				alarm_temperature_layout.setBackgroundResource(R.drawable.round_white);
				alarm_temperature_textview.setTextColor(getResources().getColor(R.color.main_blue));
			}
		});

		// 越域报警
		alarm_cross_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				alarm_cross_layout.setBackgroundResource(R.drawable.round_white);
				alarm_cross_textview.setTextColor(getResources().getColor(R.color.main_blue));
			}
		});
	}

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String actionFlag = b.getString(ACTION_FLAG);
			if ("updatelist".equals(actionFlag)) {
				adapter.notifyDataSetChanged();
			} else if ("clickItem".equals(actionFlag)) {
//				int position = b.getInt("position");
//				AlarmInfo task = alarmInfos.get(position);
//				Intent intent = new Intent();
//				intent.putExtra("RoutingTask", task);
//				intent.setClass(getApplicationContext(), RoutingPointListActivity.class);
//				startActivity(intent);
			}
		}
	};

	/**
	 *
	 * @author Administrator
	 *
	 */
	public class SystemAlarmListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public SystemAlarmListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return alarmInfos.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return alarmInfos.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.system_alarm_listview_item, null);

				holder.alarm_title = (TextView) convertView.findViewById(R.id.alarm_title);
				holder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AlarmBean alarmInfo = alarmInfos.get(position);

			holder.alarm_title.setText(alarmInfo.getAlarm_title());
			holder.alarm_time.setText(alarmInfo.getAlarm_time());

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString(ACTION_FLAG, "clickItem");
					b.putInt("position", position);
					msg.setData(b);
					handler.sendMessage(msg);
				}
			});
			return convertView;
		}
	}

	public final class ViewHolder {

		public TextView alarm_title;
		public TextView alarm_time;
	}

	// 清除底部栏选中状态
	private void clearButtonStates() {

		alarm_fault_layout.setBackgroundResource(R.color.main_blue);
		alarm_routing_layout.setBackgroundResource(R.color.main_blue);
		alarm_temperature_layout.setBackgroundResource(R.color.main_blue);
		alarm_cross_layout.setBackgroundResource(R.color.main_blue);

		alarm_fault_textview.setTextColor(getResources().getColor(R.color.white));
		alarm_routing_textview.setTextColor(getResources().getColor(R.color.white));
		alarm_temperature_textview.setTextColor(getResources().getColor(R.color.white));
		alarm_cross_textview.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
