package com.liu.hwkj.intelligent;

import java.util.ArrayList;
import java.util.List;

import com.liu.hwkj.intelligent.bean.VideoBean;
import com.liu.hwkj.intelligent.net.WebServiceManager;
import com.liu.hwkj.intelligentterminal.R;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

/**
 * 巡检任务列表，每个巡检任务包含多个巡检点
 *
 * @author Administrator
 *
 */
public class VideoPlaybackListActivity extends BaseActivity {

	private List<VideoBean> videoInfos = new ArrayList<VideoBean>();
	private ListView video_listview;
	private VideoPlaybackListViewAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_playback);

		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_video));
		VideoBean data1 = new VideoBean();
		data1.setVideo_title("视频标题1");
		data1.setVideo_url("http://10.25.254.22");

		videoInfos.add(data1);

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

		video_listview = (ListView) findViewById(R.id.video_listview);

		video_listview.setSelector(new ColorDrawable(Color.argb(150, 33, 155, 241)));
		adapter = new VideoPlaybackListViewAdapter(this);
		video_listview.setAdapter(adapter);

	}

	private void initEventListener() {

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
	public class VideoPlaybackListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public VideoPlaybackListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return videoInfos.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return videoInfos.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.video_playback_listview_item, null);

				holder.video_title = (TextView) convertView.findViewById(R.id.video_title);
				holder.play_button = (Button) convertView.findViewById(R.id.play_button);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			VideoBean videoInfo = videoInfos.get(position);

			holder.video_title.setText(videoInfo.getVideo_title());

			holder.play_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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

		public TextView video_title;
		public Button play_button;
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
