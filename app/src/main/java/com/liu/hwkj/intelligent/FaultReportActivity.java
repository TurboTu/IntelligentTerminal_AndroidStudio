package com.liu.hwkj.intelligent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.hwkj.intelligent.bean.FaultTypeBean;
import com.liu.hwkj.intelligent.bean.FaultTypeListBean;
import com.liu.hwkj.intelligent.bean.RoutingPointBean;
import com.liu.hwkj.intelligent.bean.RoutingPointListBean;
import com.liu.hwkj.intelligent.bean.RoutingTaskBean;
import com.liu.hwkj.intelligent.net.WebServiceManager;
import com.liu.hwkj.intelligentterminal.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能巡检
 *
 * @author Administrator
 *
 */
public class FaultReportActivity extends BaseActivity {

    private WebServiceManager webServiceManager;

    private RoutingTaskBean routingTaskBean; // 任务对象
    private RoutingPointBean routingPointBean; //巡检点

    private List<FaultTypeBean> faultTypeBeans = new ArrayList<FaultTypeBean>(); //故障类型列表
    private FaultTypeBean faultTypeBean;  //当前选择的故障类型

    private LinearLayout main_layout;

	private TextView routing_task_content; //巡检任务标题
	private TextView routing_starttime_content; //巡检开始时间
	private TextView fault_title; // 故障标题
	private LinearLayout arrow_down_layout;
	private TextView fault_type; // 故障类型
    private ImageView arrow_image;

	private ListView fault_type_listview; //故障类型列表
    private FaultTypeListViewAdapter adapter;

	private EditText fault_desc; //故障描述
	private TextView tips; // 错误提示

	private LinearLayout capture_layout;
	private LinearLayout record_layout;
	private Button button_submit;

    private int typeClickPos = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fault_report);

        Intent intent = getIntent();
        routingTaskBean = (RoutingTaskBean)intent.getSerializableExtra("routingTaskBean");
        routingPointBean = (RoutingPointBean)intent.getSerializableExtra("routingPointBean");

		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_task));
        webServiceManager = new WebServiceManager(getApplicationContext());
        //获取故障类型
        new Thread() {
            @Override
            public void run() {
                String[][] paras = { {} };
                String test = "{\"FaultTypeList\":[{\"ID\":\"2\",\"TypeName\":\"巡检故障 \",\"DelMark\":\"0\"},{\"ID\":\"3\",\"TypeName\":\"其他故障 \",\"DelMark\":\"0\"}]}";
                FaultTypeListBean faultTypeListBean = (FaultTypeListBean)webServiceManager.GetRemoteData("getFaultType", paras, FaultTypeListBean.class, test);

                if(faultTypeListBean != null){
                    faultTypeBeans = faultTypeListBean.getFaultTypeList();
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
        main_layout  = (LinearLayout)findViewById(R.id.main_layout);
		routing_task_content = (TextView)findViewById(R.id.routing_task_content);
        routing_task_content.setText(routingTaskBean.getTaskName());

		routing_starttime_content = (TextView)findViewById(R.id.routing_starttime_content);
        routing_starttime_content.setText(routingTaskBean.getTaskBegTime());

		fault_title = (TextView)findViewById(R.id.fault_title);
        fault_title.setText(routingPointBean.getPointName());

        arrow_down_layout = (LinearLayout)findViewById(R.id.arrow_down_layout);
		fault_type = (TextView)findViewById(R.id.fault_type);
        arrow_image = (ImageView) findViewById(R.id.arrow_image);

		fault_type_listview = (ListView)findViewById(R.id.fault_type_listview);
        adapter = new FaultTypeListViewAdapter(this);
        fault_type_listview.setAdapter(adapter);

		fault_desc = (EditText)findViewById(R.id.fault_desc);
		tips = (TextView)findViewById(R.id.tips);

		capture_layout = (LinearLayout)findViewById(R.id.capture_layout);
		record_layout = (LinearLayout)findViewById(R.id.record_layout);
		button_submit = (Button)findViewById(R.id.button_submit);
	}

	private void initEventListener() {
        main_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTypeListView(false);
            }
        });
		//拍照
		capture_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                showTypeListView(false);
				Toast.makeText(getApplicationContext(), "拍照", Toast.LENGTH_LONG).show();
			}
		});

		//录像
		record_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                showTypeListView(false);
				Toast.makeText(getApplicationContext(), "录像", Toast.LENGTH_LONG).show();

			}
		});

		//提交
		button_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                showTypeListView(false);
				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_LONG).show();

			}
		});

        fault_desc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTypeListView(false);
            }
        });

        arrow_down_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fault_type_listview.getVisibility() == View.GONE){
                    showTypeListView(true);
                }
                else{
                    showTypeListView(false);
                }
            }
        });
	}

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String actionFlag = b.getString(ACTION_FLAG);
            if ("updatelist".equals(actionFlag)) {
                if(faultTypeBeans.size() > 0){
                    fault_type.setText(faultTypeBeans.get(0).getTypeName());
                }
                adapter.notifyDataSetChanged();
            }
            // 点击列表标题
            else if ("click_list_title_layout".equals(actionFlag)) {
                int position = b.getInt("position");
                faultTypeBean = faultTypeBeans.get(position);
                fault_type.setText(faultTypeBean.getTypeName());
                showTypeListView(false);
            }
        }
    };

    private void showTypeListView(boolean isShow){
        if(isShow){
            adapter.notifyDataSetChanged();
            arrow_image.setBackgroundResource(R.drawable.blue_arrow_up);
            fault_type_listview.setVisibility(View.VISIBLE);
        }
        else{
            arrow_image.setBackgroundResource(R.drawable.blue_arrow_down);
            fault_type_listview.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    public class FaultTypeListViewAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public FaultTypeListViewAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return faultTypeBeans.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return faultTypeBeans.get(arg0);
        }

        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.fault_type_listview_item, null);

                holder.fault_type_layout = (LinearLayout) convertView.findViewById(R.id.fault_type_layout);
                holder.fault_type = (TextView) convertView.findViewById(R.id.fault_type);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            FaultTypeBean bean = faultTypeBeans.get(position);
            holder.fault_type.setText(bean.getTypeName());

            if(typeClickPos == position){
                holder.fault_type_layout.setBackgroundResource(R.color.main_blue);
                holder.fault_type.setTextColor(getResources().getColor(R.color.white));
            }
            else{
                holder.fault_type_layout.setBackgroundResource(R.color.white);
                holder.fault_type.setTextColor(getResources().getColor(R.color.main_gray));
            }

            holder.fault_type_layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    typeClickPos = position;
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString(ACTION_FLAG, "click_list_title_layout");
                    b.putInt("position", position);
                    msg.setData(b);
                    handler.sendMessage(msg);
                }
            });

            return convertView;
        }
    }

    public final class ViewHolder {

        public LinearLayout fault_type_layout;
        public TextView fault_type;
    }
}
