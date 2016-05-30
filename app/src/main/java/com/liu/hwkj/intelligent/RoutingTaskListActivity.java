package com.liu.hwkj.intelligent;

import java.util.ArrayList;
import java.util.List;

import com.liu.hwkj.intelligent.bean.RoutingTaskBean;
import com.liu.hwkj.intelligent.bean.RoutingTaskListBean;
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
import android.widget.Button;
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
public class RoutingTaskListActivity extends BaseActivity {

	WebServiceManager webServiceManager = null;

	private LinearLayout routing_wait_layout;
	private LinearLayout routing_complete_layout;
	private LinearLayout routing_outofdate_layout;

	private ImageView routing_task_wait_imageview;
	private ImageView routing_task_complete_imageview;
	private ImageView routing_task_outofdate_imageview;

	private TextView routing_task_wait_textview;
	private TextView routing_task_complete_textview;
	private TextView routing_task_outofdate_textview;

	private List<RoutingTaskBean> routingTaskBeans = new ArrayList<RoutingTaskBean>();
	private ListView task_listview;
	private RoutingTaskListViewAdapter adapter;

    private String currentTag = "getNowTask"; //用于标记当前是处于待巡检（getNowTask）、已巡检（getHasFinishTask）还是已过期（getHasNotFinishTask）界面

	//表格的列为名称、开始、结束、巡检点、已巡检、状态、操作，
    // 待巡检只显示名称、开始、结束、巡检点、操作；
    // 已巡检和已过期只显示名称、开始、结束、巡检点、已巡检、状态
    //即待巡检隐藏已巡检和状态列，已巡检和已过期隐藏操作列
    private TextView table_head_hasrouted;
    private TextView table_head_state;
    private TextView table_head_operate;
    private View line5;
    private View line6;
    private View line7;


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routing_task);

		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_task));

		webServiceManager = new WebServiceManager(getApplicationContext());
		ReQueryRoutingTask();
	}

	private void initComponent() {

		routing_wait_layout = (LinearLayout) findViewById(R.id.routing_wait_layout);
		routing_complete_layout = (LinearLayout) findViewById(R.id.routing_complete_layout);
		routing_outofdate_layout = (LinearLayout) findViewById(R.id.routing_outofdate_layout);

		routing_task_wait_imageview = (ImageView) findViewById(R.id.routing_task_wait_imageview);
		routing_task_complete_imageview = (ImageView) findViewById(R.id.routing_task_complete_imageview);
		routing_task_outofdate_imageview = (ImageView) findViewById(R.id.routing_task_outofdate_imageview);

		routing_task_wait_textview = (TextView) findViewById(R.id.routing_task_wait_textview);
		routing_task_complete_textview = (TextView) findViewById(R.id.routing_task_complete_textview);
		routing_task_outofdate_textview = (TextView) findViewById(R.id.routing_task_outofdate_textview);

        table_head_hasrouted = (TextView) findViewById(R.id.table_head_hasrouted);
        table_head_state = (TextView) findViewById(R.id.table_head_state);
        table_head_operate = (TextView) findViewById(R.id.table_head_operate);
        line5 = (View) findViewById(R.id.line5);
        line6 = (View) findViewById(R.id.line6);
        line7 = (View) findViewById(R.id.line7);

		task_listview = (ListView) findViewById(R.id.task_listview);

		task_listview.setSelector(new ColorDrawable(Color.argb(150, 33, 155, 241)));
		adapter = new RoutingTaskListViewAdapter(this);
		task_listview.setAdapter(adapter);

	}

	private void initEventListener() {
		// 待巡检
		routing_wait_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
                currentTag = "getNowTask";
				routing_wait_layout.setBackgroundResource(R.drawable.round_white);
				routing_task_wait_textview.setTextColor(getResources().getColor(R.color.main_blue));
				routing_task_wait_imageview.setBackgroundResource(R.drawable.routing_task_wait_icon_blue);
				ReQueryRoutingTask();
			}
		});

		// 已巡检
		routing_complete_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
                currentTag = "getHasFinishTask";
				routing_complete_layout.setBackgroundResource(R.drawable.round_white);
				routing_task_complete_textview.setTextColor(getResources().getColor(R.color.main_blue));
				routing_task_complete_imageview.setBackgroundResource(R.drawable.routing_task_complete_icon_blue);
				ReQueryRoutingTask();
			}
		});

		// 已过期
		routing_outofdate_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
                currentTag = "getHasNotFinishTask";
				routing_outofdate_layout.setBackgroundResource(R.drawable.round_white);
				routing_task_outofdate_textview.setTextColor(getResources().getColor(R.color.main_blue));
				routing_task_outofdate_imageview.setBackgroundResource(R.drawable.routing_task_outofdate_icon_blue);
				ReQueryRoutingTask();
			}
		});
	}

	/**
	 * 重新查询巡检任务
     */
	private void ReQueryRoutingTask(){
        //待巡检隐藏已巡检和状态列
        if("getNowTask".equals(currentTag)){
            table_head_hasrouted.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);

            table_head_state.setVisibility(View.GONE);
            line6.setVisibility(View.GONE);

            table_head_operate.setVisibility(View.VISIBLE);
            line7.setVisibility(View.VISIBLE);
        }
        //已巡检和已过期隐藏操作列
        else{
            table_head_hasrouted.setVisibility(View.VISIBLE);
            line5.setVisibility(View.VISIBLE);

            table_head_state.setVisibility(View.VISIBLE);
            line6.setVisibility(View.VISIBLE);

            table_head_operate.setVisibility(View.GONE);
            line7.setVisibility(View.GONE);
        }

		new Thread() {
			@Override
			public void run() {
				String[][] paras = { { "UserID", userBean.getUSER_ID() } };
				String test = "{\"NowTaskList\":[{\"ID\":\"11\",\"TID\":\"3\",\"TaskName\":\"厂区巡检\",\"TaskContent\":\" \",\"TaskDay\":\"2016-05-09 \",\"TaskBegTime\":\"2016-05-09 09:00:00\",\"TaskEndTime\":\"2016-05-09 22:00:00\",\"PointNum\":\"2\",\"MapLine\":\"118.471233\",\"MapArea\":\"37.416975\",\"UserID\":\"4820c7b5-2c84-4a61-a782-b0ed33fa77db \",\"User_Name\":\"张三\",\"HasInspection\":\"0\",\"CreateTime\":\"2016-05-09 20:02:29\",\"Organization_Name\":\"巡检一组\"}]}";
				RoutingTaskListBean routingTaskListBean = (RoutingTaskListBean)webServiceManager.GetRemoteData(currentTag, paras, RoutingTaskListBean.class, test);
				if(routingTaskListBean != null){
					if("getNowTask".equals(currentTag)){
						routingTaskBeans = routingTaskListBean.getNowTaskList();
					}
					else if("getHasFinishTask".equals(currentTag)){
						routingTaskBeans = routingTaskListBean.getFinishTaskList();
					}
					else if("getHasNotFinishTask".equals(currentTag)){
						routingTaskBeans = routingTaskListBean.getNoFinishTaskList();
					}
				}

				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString(ACTION_FLAG, "updatelist");
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}.start();

	}

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String actionFlag = b.getString(ACTION_FLAG);
			if ("updatelist".equals(actionFlag)) {
				adapter.notifyDataSetChanged();
			}
			else if("clickItem".equals(actionFlag)){
				int position = b.getInt("position");
				RoutingTaskBean routingTaskBean = routingTaskBeans.get(position);
				Intent intent = new Intent();
				intent.putExtra("routingTaskBean", routingTaskBean);
				intent.setClass(getApplicationContext(), RoutingPointListActivity.class);
				startActivity(intent);
			}
		}
	};

	/**
	 *
	 * @author Administrator
	 *
	 */
	public class RoutingTaskListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public RoutingTaskListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return routingTaskBeans.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return routingTaskBeans.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.routing_task_listview_item, null);

				holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
				holder.task_begin = (TextView) convertView.findViewById(R.id.task_begin);
				holder.task_end = (TextView) convertView.findViewById(R.id.task_end);
				holder.task_point = (TextView) convertView.findViewById(R.id.task_point);
				holder.task_routed = (TextView) convertView.findViewById(R.id.task_routed);
				holder.task_state = (TextView) convertView.findViewById(R.id.task_state);

                holder.line5 = (View) convertView.findViewById(R.id.line5);
                holder.line6 = (View) convertView.findViewById(R.id.line6);
                holder.line7 = (View) convertView.findViewById(R.id.line7);

                holder.operate_button = (Button) convertView.findViewById(R.id.operate_button);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			RoutingTaskBean routingTaskBean = routingTaskBeans.get(position);

            if("getNowTask".equals(currentTag)){
                holder.task_routed.setVisibility(View.GONE);
                holder.line5.setVisibility(View.GONE);

                holder.task_state.setVisibility(View.GONE);
                holder.line6.setVisibility(View.GONE);

                holder.operate_button.setVisibility(View.VISIBLE);
                holder.line7.setVisibility(View.VISIBLE);
            }
            //已巡检和已过期隐藏操作列
            else{
                holder.task_routed.setVisibility(View.VISIBLE);
                holder.line5.setVisibility(View.VISIBLE);

                holder.task_state.setVisibility(View.VISIBLE);
                holder.line6.setVisibility(View.VISIBLE);

                holder.operate_button.setVisibility(View.GONE);
                holder.line7.setVisibility(View.GONE);
            }

			holder.task_name.setText(routingTaskBean.getTaskName());
			holder.task_begin.setText(routingTaskBean.getTaskBegTime());
			holder.task_end.setText(routingTaskBean.getTaskEndTime());
			holder.task_point.setText(routingTaskBean.getPointNum());
			holder.task_routed.setText(routingTaskBean.getHasInspection());
			holder.task_state.setText("未知");

            holder.operate_button.setOnClickListener(new View.OnClickListener() {

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

		public TextView task_name;
		public TextView task_begin;
		public TextView task_end;

		public TextView task_point;
		public TextView task_routed;
		public TextView task_state;

        public View line5;
        public View line6;
        public View line7;

        public Button operate_button;
	}

	// 清除底部栏选中状态
	private void clearButtonStates() {
		routing_wait_layout.setBackgroundResource(R.color.main_blue);
		routing_complete_layout.setBackgroundResource(R.color.main_blue);
		routing_outofdate_layout.setBackgroundResource(R.color.main_blue);

		routing_task_wait_textview.setTextColor(getResources().getColor(R.color.white));
		routing_task_complete_textview.setTextColor(getResources().getColor(R.color.white));
		routing_task_outofdate_textview.setTextColor(getResources().getColor(R.color.white));

		routing_task_wait_imageview.setBackgroundResource(R.drawable.routing_task_wait_icon_white);
		routing_task_complete_imageview.setBackgroundResource(R.drawable.routing_task_complete_icon_white);
		routing_task_outofdate_imageview.setBackgroundResource(R.drawable.routing_task_outofdate_icon_white);
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
