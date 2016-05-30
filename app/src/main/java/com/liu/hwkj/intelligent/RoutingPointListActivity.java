package com.liu.hwkj.intelligent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.liu.hwkj.intelligent.bean.RoutingPointBean;
import com.liu.hwkj.intelligent.bean.RoutingPointListBean;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 巡检点列表
 *
 * @author Administrator
 *
 */
public class RoutingPointListActivity extends BaseActivity {

	private LinearLayout map_mode_button_layout;
	private LinearLayout list_mode_button_layout;

	private ImageView map_mode_imageview;
	private ImageView list_mode_imageview;

	private TextView map_mode_textview;
	private TextView list_mode_textview;

	private TextView routing_task_content;
	private TextView routing_starttime_content;

	private MapView bmapView = null; // 百度地图view
	private BaiduMap mBaiduMap;

	private ListView listview;
	private RoutingPointListViewAdapter adapter;

	private WebServiceManager webServiceManager;
	private List<RoutingPointBean> routingPointBeans = new ArrayList<RoutingPointBean>();
	private RoutingTaskBean routingTaskBean;

	private int clickPosition = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routing_point);

		Intent intent = getIntent();
		routingTaskBean = (RoutingTaskBean) intent.getSerializableExtra("routingTaskBean");
		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_task));
		webServiceManager = new WebServiceManager(getApplicationContext());

		//获取巡检任务巡检点列表
		new Thread() {
			@Override
			public void run() {

				String[][] paras = { { "ID", routingTaskBean.getID() } };
				String test = "{\"TaskPoint\":[{\"ID\":\"1\",\"TCID\":\"7\",\"PointName\":\"巡检点1 \",\"PointContent\":\"我问问问问\",\"PointRFID\":\" \",\"PointLa\":\"118.495408 \",\"PointLo\":\"37.422019 \",\"PointOrder\":\"1\",\"UserID\":\"d6b0c019-3184-4c08-9744-d7bdd9f02cdc \",\"HasState\":\"0\",\"XunJianTime\":\"&nbsp;\",\"HasFault\":\"0\",\"CreateTime\":\"2016-05-05 11:03:00\",\"DelMark\":\"0\",\"WenDu\":\"&nbsp;\",\"QiTi\":\"&nbsp;\"},{\"ID\":\"1\",\"TCID\":\"7\",\"PointName\":\"巡检点2 \",\"PointContent\":\"发送到各大书店gas德国\",\"PointRFID\":\" \",\"PointLa\":\"118.515408 \",\"PointLo\":\"37.422019 \",\"PointOrder\":\"1\",\"UserID\":\"d6b0c019-3184-4c08-9744-d7bdd9f02cdc \",\"HasState\":\"0\",\"XunJianTime\":\"&nbsp;\",\"HasFault\":\"0\",\"CreateTime\":\"2016-05-05 11:03:00\",\"DelMark\":\"0\",\"WenDu\":\"&nbsp;\",\"QiTi\":\"&nbsp;\"}]}";
				RoutingPointListBean routingPointListBean = (RoutingPointListBean)webServiceManager.GetRemoteData("getTaskPoint", paras, RoutingPointListBean.class, test);

				if(routingPointListBean != null){
					routingPointBeans = routingPointListBean.getTaskPoint();
				}

				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString(ACTION_FLAG, "updatelist");
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void initComponent() {

		map_mode_button_layout = (LinearLayout) findViewById(R.id.map_mode_button_layout);
		list_mode_button_layout = (LinearLayout) findViewById(R.id.list_mode_button_layout);

		map_mode_imageview = (ImageView) findViewById(R.id.map_mode_imageview);
		list_mode_imageview = (ImageView) findViewById(R.id.list_mode_imageview);

		map_mode_textview = (TextView) findViewById(R.id.map_mode_textview);
		list_mode_textview = (TextView) findViewById(R.id.list_mode_textview);

		routing_task_content = (TextView) findViewById(R.id.routing_task_content);
		routing_starttime_content = (TextView) findViewById(R.id.routing_starttime_content);
		routing_task_content.setText(routingTaskBean.getTaskName());
		routing_starttime_content.setText(routingTaskBean.getTaskBegTime());

		listview = (ListView) findViewById(R.id.listview);

		listview.setSelector(new ColorDrawable(Color.argb(150, 33, 155, 241)));
		adapter = new RoutingPointListViewAdapter(this);
		listview.setAdapter(adapter);

		// 获取地图控件引用
		bmapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = bmapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);

	}

	private void initEventListener() {
		// 地图模式
		map_mode_button_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				bmapView.setVisibility(View.VISIBLE);
				listview.setVisibility(View.INVISIBLE);
				map_mode_button_layout.setBackgroundResource(R.color.main_blue);
				map_mode_textview.setTextColor(getResources().getColor(R.color.white));
				map_mode_imageview.setBackgroundResource(R.drawable.map_mode_select);
			}
		});

		// 列表模式
		list_mode_button_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				bmapView.setVisibility(View.INVISIBLE);
				listview.setVisibility(View.VISIBLE);
				list_mode_button_layout.setBackgroundResource(R.color.main_blue);
				list_mode_textview.setTextColor(getResources().getColor(R.color.white));
				list_mode_imageview.setBackgroundResource(R.drawable.list_mode_select);
			}
		});
	}

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String actionFlag = b.getString(ACTION_FLAG);
			if ("updatelist".equals(actionFlag)) {
				adapter.notifyDataSetChanged();
				// 在地图上添加工厂覆盖物
				initOverlay();
			}
			// 点击列表标题
			else if ("click_list_title_layout".equals(actionFlag)) {
				adapter.notifyDataSetChanged();
			}
			//点击列表巡检按钮
			else if ("click_routing_button".equals(actionFlag)) {
                int position = b.getInt("position");
				RoutingPointBean routingPointBean = routingPointBeans.get(position);

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), RoutingExcuteActivity.class);
				intent.putExtra("routingTaskBean", routingTaskBean);
				intent.putExtra("routingPointBeans", (Serializable)routingPointBeans);
                intent.putExtra("routingPointBean", routingPointBean);
                intent.putExtra("position", position);

				startActivity(intent);
			}

		}
	};

	// 清除底部栏选中状态
	private void clearButtonStates() {

		map_mode_button_layout.setBackgroundResource(R.color.white);
		list_mode_button_layout.setBackgroundResource(R.color.white);

		map_mode_textview.setTextColor(getResources().getColor(R.color.main_blue));
		list_mode_textview.setTextColor(getResources().getColor(R.color.main_blue));

		map_mode_imageview.setBackgroundResource(R.drawable.map_mode_normal);
		list_mode_imageview.setBackgroundResource(R.drawable.list_mode_normal);
	}

	/**
	 * 添加覆盖物
	 */
	public void initOverlay() {
		new Thread() {
			@Override
			public void run() {
				mBaiduMap.clear();
				LatLng latLng = null;
				OverlayOptions overlayOptions = null;
				Marker marker = null;
				for (RoutingPointBean bean : routingPointBeans) {
					// 位置
					latLng = new LatLng(Double.parseDouble(bean.getPointLa()), Double.parseDouble(bean.getPointLo()));
					// 图标
					BitmapDescriptor mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.overlay_normal);

					overlayOptions = new MarkerOptions().position(latLng).icon(mIconMaker).zIndex(5);
					OverlayOptions overlayText = new TextOptions().position(latLng).text(bean.getPointName())
							.fontSize(25).zIndex(6);
					marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
					mBaiduMap.addOverlay(overlayText);
					Bundle bundle = new Bundle();
					bundle.putSerializable("routingPointBean", bean);
					marker.setExtraInfo(bundle);
				}
				// 将地图移到到最后一个经纬度位置
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.setMapStatus(u);
			}
		}.start();

	}

	/**
	 *
	 */
	public class RoutingPointListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public RoutingPointListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return routingPointBeans.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return routingPointBeans.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.routing_point_listview_item, null);

				holder.clickable_title_layout = (LinearLayout) convertView.findViewById(R.id.clickable_title_layout);
				holder.routing_content_layout = (LinearLayout) convertView.findViewById(R.id.routing_content_layout);
				holder.routing_piont_name = (TextView) convertView.findViewById(R.id.routing_piont_name);
				holder.routing_content = (TextView) convertView.findViewById(R.id.routing_content);
				holder.routing_button = (Button) convertView.findViewById(R.id.routing_button);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (clickPosition == position) {

				if (holder.routing_content_layout.getVisibility() == View.VISIBLE) {
					holder.routing_content_layout.setVisibility(View.GONE);
				} else {
					holder.routing_content_layout.setVisibility(View.VISIBLE);
				}

			} else {
				holder.routing_content_layout.setVisibility(View.GONE);
			}

			RoutingPointBean routingPointInfo = routingPointBeans.get(position);
			holder.routing_piont_name.setText(routingPointInfo.getPointName());
			holder.routing_content.setText(routingPointInfo.getPointContent());

			holder.clickable_title_layout.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					clickPosition = position;

					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString(ACTION_FLAG, "click_list_title_layout");
					b.putInt("position", position);
					msg.setData(b);
					handler.sendMessage(msg);
				}
			});

			holder.routing_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString(ACTION_FLAG, "click_routing_button");
					b.putInt("position", position);
					msg.setData(b);
					handler.sendMessage(msg);
				}
			});

			return convertView;
		}
	}

	public final class ViewHolder {

		public LinearLayout routing_content_layout;
		public LinearLayout clickable_title_layout;
		public TextView routing_piont_name;
		public TextView routing_content;
		public Button routing_button;
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
