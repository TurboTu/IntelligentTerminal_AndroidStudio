package com.liu.hwkj.intelligent;

import java.util.ArrayList;
import java.util.List;

import com.liu.hwkj.intelligent.bean.FaultDealBean;
import com.liu.hwkj.intelligent.bean.FaultDealListBean;
import com.liu.hwkj.intelligent.bean.FaultTypeBean;
import com.liu.hwkj.intelligent.bean.FaultTypeListBean;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 巡检点列表
 *
 * @author Administrator
 *
 */
public class FaultDealListActivity extends BaseActivity {

	private WebServiceManager webServiceManager;

	private LinearLayout has_deal_layout;
	private LinearLayout wait_deal_layout;

	private TextView has_deal_textview;
	private TextView wait_deal_textview;

	private View has_deal_underline;
	private View wait_deal_underline;

	private ListView fault_listview;
	private FaultDealListViewAdapter adapter;

	private List<FaultDealBean> faultDealBeans = new ArrayList<FaultDealBean>();
	private FaultDealBean faultDealBean;

	private int clickPosition = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fault_deal);

		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_fault));
		webServiceManager = new WebServiceManager(getApplicationContext());

		getFaultList("0");
	}

	private void initComponent() {

		has_deal_layout = (LinearLayout) findViewById(R.id.has_deal_layout);
		wait_deal_layout = (LinearLayout) findViewById(R.id.wait_deal_layout);

		has_deal_textview = (TextView) findViewById(R.id.has_deal_textview);
		wait_deal_textview = (TextView) findViewById(R.id.wait_deal_textview);

		has_deal_underline = (View) findViewById(R.id.has_deal_underline);
		wait_deal_underline = (View) findViewById(R.id.wait_deal_underline);

		fault_listview = (ListView) findViewById(R.id.fault_listview);

		fault_listview.setSelector(new ColorDrawable(Color.argb(150, 33, 155, 241)));
		adapter = new FaultDealListViewAdapter(this);
		fault_listview.setAdapter(adapter);
	}

	private void initEventListener() {
		// 已处理故障
		has_deal_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				has_deal_textview.setTextColor(getResources().getColor(R.color.main_blue));
				has_deal_underline.setVisibility(View.VISIBLE);
			}
		});

		// 待处理故障
		wait_deal_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				wait_deal_textview.setTextColor(getResources().getColor(R.color.main_blue));
				wait_deal_underline.setVisibility(View.VISIBLE);
			}
		});
	}

	private void getFaultList(final String HasDeal){
		new Thread() {
			@Override
			public void run() {
				//HasDeal为0获取待处理故障列表，HasDeal为1获取已处理故障信息列表
				String[][] paras = { {"UserID", userBean.getUSER_ID()}, {"HasDeal", HasDeal} };
				String test = "{\"PointFault\":[{\"ID\":\"1\",\"TCID\":\"7\",\"TCPID\":\"1\",\"FaultTitle\":\"wwww \",\"FaultType\":\"2\",\"FaultContent\":\"sdfsdfsd\",\"CreateTime\":\"2016-05-06 23:09:00\",\"UserID\":\"4820c7b5-2c84-4a61-a782-b0ed33fa77db \",\"HasSend\":\"1\",\"SUserID\":\"d6b0c019-3184-4c08-9744-d7bdd9f02cdc \",\"SendTime\":\"2016-05-07 00:22:22\",\"HasDeal\":\"0\",\"DealContent\":\"&nbsp;\",\"DealUID\":\"&nbsp;\",\"DealTime\":\"&nbsp;\",\"SetNoAlert\":\"0\",\"SetUID\":\"&nbsp;\",\"SetTime\":\"&nbsp;\",\"DelMark\":\"0\"}]}";
				FaultDealListBean faultDealListBean = (FaultDealListBean)webServiceManager.GetRemoteData("getPointFault", paras, FaultDealListBean.class, test);

				if(faultDealListBean != null){
					faultDealBeans = faultDealListBean.getPointFault();
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
			// 点击列表标题
			else if ("click_list_title_layout".equals(actionFlag)) {
				adapter.notifyDataSetChanged();
			}
			//点击列表巡检按钮
			else if ("click_deal_button".equals(actionFlag)) {
//				FaultInfo faultInfo = faultInfos.get(b.getInt("position"));
//				Intent intent = new Intent();
//				intent.setClass(getApplicationContext(), RoutingExcuteActivity.class);
//				intent.putExtra("faultInfo", faultInfo);
//
//				startActivity(intent);
			}

		}
	};

	/**
	 *
	 * @author Administrator
	 *
	 */
	public class FaultDealListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public FaultDealListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return faultDealBeans.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return faultDealBeans.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.fault_deal_listview_item, null);

				holder.clickable_title_layout = (LinearLayout) convertView.findViewById(R.id.clickable_title_layout);
				holder.fault_desc_layout = (LinearLayout) convertView.findViewById(R.id.fault_desc_layout);
				holder.fault_title = (TextView) convertView.findViewById(R.id.fault_title);
				holder.fault_desc = (TextView) convertView.findViewById(R.id.fault_desc);
				holder.deal_button = (Button) convertView.findViewById(R.id.deal_button);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (clickPosition == position) {

				if (holder.fault_desc_layout.getVisibility() == View.VISIBLE) {
					holder.fault_desc_layout.setVisibility(View.GONE);
				} else {
					holder.fault_desc_layout.setVisibility(View.VISIBLE);
				}

			} else {
				holder.fault_desc_layout.setVisibility(View.GONE);
			}

			FaultDealBean beans = faultDealBeans.get(position);
			holder.fault_title.setText(beans.getFaultTitle());
			holder.fault_desc.setText(beans.getFaultContent());

			holder.clickable_title_layout.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					clickPosition = position;

                    faultDealBean = faultDealBeans.get(position);

					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString(ACTION_FLAG, "click_list_title_layout");
					b.putInt("position", position);
					msg.setData(b);
					handler.sendMessage(msg);
				}
			});

			holder.deal_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString(ACTION_FLAG, "click_deal_button");
					b.putInt("position", position);
					msg.setData(b);
					handler.sendMessage(msg);
				}
			});

			return convertView;
		}
	}

	public final class ViewHolder {

		public LinearLayout clickable_title_layout;
		public LinearLayout fault_desc_layout;
		public TextView fault_title;
		public TextView fault_desc;
		public Button deal_button;
	}

	// 清除底部栏选中状态
	private void clearButtonStates() {

		has_deal_textview.setTextColor(getResources().getColor(R.color.main_gray));
		has_deal_underline.setVisibility(View.GONE);

		wait_deal_textview.setTextColor(getResources().getColor(R.color.main_gray));
		wait_deal_underline.setVisibility(View.GONE);

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
