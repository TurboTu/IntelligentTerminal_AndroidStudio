package com.liu.hwkj.intelligent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liu.hwkj.baidu.trackshow.BaiduTraceActivity;
import com.liu.hwkj.intelligent.bean.UserBean;
import com.liu.hwkj.intelligentterminal.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * 智能巡检
 *
 * @author Administrator
 *
 */
public class RoutingActivity extends BaseActivity {

	private GridView gview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routing);

		super.initOnCreate();

		user_info_layout.setVisibility(View.VISIBLE);
		user_info_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		int[] icon = { R.drawable.routing_emap, R.drawable.routing_task, R.drawable.routing_record,
				R.drawable.routing_fault, R.drawable.routing_video, R.drawable.routing_alarm, R.drawable.routing_upload,
				R.drawable.routing_user, R.drawable.routing_setting, R.drawable.routing_eclock,
				R.drawable.routing_help };
		String[] iconName = { getResources().getString(R.string.routing_emap),
				getResources().getString(R.string.routing_task), getResources().getString(R.string.routing_record),
				getResources().getString(R.string.routing_fault), getResources().getString(R.string.routing_video),
				getResources().getString(R.string.routing_alarm), getResources().getString(R.string.routing_upload),
				getResources().getString(R.string.routing_user), getResources().getString(R.string.routing_setting),
				getResources().getString(R.string.routing_eclock), getResources().getString(R.string.routing_help) };

		data_list = new ArrayList<Map<String, Object>>();
		// cion和iconName的长度是相同的，这里任选其一都可以
		for (int i = 0; i < icon.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			data_list.add(map);
		}

		gview = (GridView) this.findViewById(R.id.square_grid);
		gview.setSelector(new ColorDrawable(Color.argb(150, 33, 155, 241)));

		String[] from = { "image", "text" };
		int[] to = { R.id.image, R.id.title };
		sim_adapter = new SimpleAdapter(this, data_list, R.layout.squaregrid_item, from, to);
		// 配置适配器
		gview.setAdapter(sim_adapter);

		ItemClickListener itemClickListener = new ItemClickListener(this);
		itemClickListener.setUserBean(userBean);
		gview.setOnItemClickListener(itemClickListener);
	}

	@Override
	protected void back() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		RoutingActivity.this.finish();
	}
}

class ItemClickListener implements OnItemClickListener {

	private Context context;
	private UserBean userBean;

	public ItemClickListener(Context context) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (arg2) {
			case 0: //电子地图
				intent.setClass(context.getApplicationContext(), EMapActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 1: //巡检任务
				intent.setClass(context.getApplicationContext(), RoutingTaskListActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 2://巡检记录
			intent.setClass(context.getApplicationContext(), RoutingRecordActivity.class);
//				intent.setClass(context.getApplicationContext(), BaiduTraceActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 3://故障处理
				intent.setClass(context.getApplicationContext(), FaultDealListActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 4://视频回放
				intent.setClass(context.getApplicationContext(), VideoPlaybackListActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 5://系统报警
				intent.setClass(context.getApplicationContext(), SystemAlarmListActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 6://文件上传
				intent.setClass(context.getApplicationContext(), FileUploadActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 7://用户信息
				intent.setClass(context.getApplicationContext(), UserInfoActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 8://系统设置
				intent.setClass(context.getApplicationContext(), SettingActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 9://电子时钟
				intent.setClass(context.getApplicationContext(), SyncTimeActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			case 10://使用帮助
				intent.setClass(context.getApplicationContext(), HelpActivity.class);
				intent.putExtra("userBean", userBean);
				context.startActivity(intent);
				break;
			default:
				break;
		}
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
}
