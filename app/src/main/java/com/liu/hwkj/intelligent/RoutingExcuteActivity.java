package com.liu.hwkj.intelligent;

import com.liu.hwkj.intelligent.bean.RoutingPointBean;
import com.liu.hwkj.intelligent.bean.RoutingPointListBean;
import com.liu.hwkj.intelligent.bean.RoutingTaskBean;
import com.liu.hwkj.intelligent.net.WebServiceManager;
import com.liu.hwkj.intelligentterminal.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能巡检
 *
 * @author Administrator
 *
 */
public class RoutingExcuteActivity extends BaseActivity {

	private TextView routing_task_content;
	private TextView routing_starttime_content;

	private TextView routing_piont_name;
	private TextView routing_content;

	private Button routing_button;

	private LinearLayout read_card_layout;
	private LinearLayout fault_upload_layout;
	private LinearLayout confirm_layout;
	private LinearLayout finish_layout;

	private ImageView read_card_imageview;
	private ImageView confirm_imageview;
	private ImageView fault_upload_imageview;
	private ImageView finish_imageview;

	private TextView read_card_textview;
	private TextView fault_upload_textview;
	private TextView confirm_textview;
	private TextView finish_textview;

	private RoutingTaskBean routingTaskBean; // 任务对象
	private List<RoutingPointBean> routingPointBeans; //巡检点列表
	private RoutingPointBean routingPointBean; //巡检点
	private int position; //当前巡检点在巡检列表中的位置

    private WebServiceManager webServiceManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routing_execute);

		Intent intent = getIntent();
		routingTaskBean = (RoutingTaskBean)intent.getSerializableExtra("routingTaskBean");
		routingPointBeans = (List<RoutingPointBean>)intent.getSerializableExtra("routingPointBeans");
		routingPointBean = (RoutingPointBean)intent.getSerializableExtra("routingPointBean");
		position = intent.getIntExtra("position", -1);

        webServiceManager = new WebServiceManager(getApplicationContext());

		super.initOnCreate();
		initComponent();
		initEventListener();

		title.setText(getResources().getString(R.string.routing_task));
	}

	private void initComponent() {
		routing_task_content = (TextView) findViewById(R.id.routing_task_content);
		routing_starttime_content = (TextView) findViewById(R.id.routing_starttime_content);

		routing_task_content.setText(routingTaskBean.getTaskName());
		routing_starttime_content.setText(routingTaskBean.getTaskBegTime());

		routing_piont_name = (TextView) findViewById(R.id.routing_piont_name);
		routing_content = (TextView) findViewById(R.id.routing_content);

        setRoutingView();

		routing_button = (Button) findViewById(R.id.routing_button);

		read_card_layout = (LinearLayout) findViewById(R.id.read_card_layout);
		fault_upload_layout = (LinearLayout) findViewById(R.id.fault_upload_layout);
		confirm_layout = (LinearLayout) findViewById(R.id.confirm_layout);
		finish_layout = (LinearLayout) findViewById(R.id.finish_layout);

		read_card_imageview = (ImageView) findViewById(R.id.read_card_imageview);
		confirm_imageview = (ImageView) findViewById(R.id.confirm_imageview);
		fault_upload_imageview = (ImageView) findViewById(R.id.fault_upload_imageview);
		finish_imageview = (ImageView) findViewById(R.id.finish_imageview);

		read_card_textview = (TextView) findViewById(R.id.read_card_textview);
		fault_upload_textview = (TextView) findViewById(R.id.fault_upload_textview);
		confirm_textview = (TextView) findViewById(R.id.confirm_textview);
		finish_textview = (TextView) findViewById(R.id.finish_textview);

	}

    /**
     * 设置巡检点内容
     */
    private void setRoutingView(){
        routing_piont_name.setText(routingPointBean.getPointName());
        routing_content.setText(routingPointBean.getPointContent());

    }

	private void initEventListener() {
		// 读卡
		read_card_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				read_card_layout.setBackgroundResource(R.drawable.capture_bg_blue_selector);
				read_card_imageview.setBackgroundResource(R.drawable.read_card_select);
				read_card_textview.setTextColor(getResources().getColor(R.color.white));
			}
		});

		// 确认正常
		confirm_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				confirm_layout.setBackgroundResource(R.drawable.capture_bg_blue_selector);
				confirm_imageview.setBackgroundResource(R.drawable.routing_confirm_select);
				confirm_textview.setTextColor(getResources().getColor(R.color.white));

                //获取巡检任务巡检点列表
                new Thread() {
                    @Override
                    public void run() {

                        String[][] paras = { { "TCID", routingTaskBean.getID() }, { "TCID" ,routingPointBean.getID()}, {"HasFault" , "0"}};
                        String test = "1";
                        String result = (String) webServiceManager.GetRemoteData("setTaskPointInfor", paras, String.class, test);

                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString(ACTION_FLAG, "confirm_normal");
                        b.putString("result", result);
                        msg.setData(b);
                        handler.sendMessage(msg);
                    }
                }.start();
			}
		});

		// 故障上报
		fault_upload_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				fault_upload_layout.setBackgroundResource(R.drawable.capture_bg_blue_selector);
				fault_upload_imageview.setBackgroundResource(R.drawable.fault_upload_select);
				fault_upload_textview.setTextColor(getResources().getColor(R.color.white));

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), FaultReportActivity.class);
                intent.putExtra("routingTaskBean", routingTaskBean);
                intent.putExtra("routingPointBean", routingPointBean);

                startActivity(intent);
			}
		});

		// 结束巡检
		finish_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearButtonStates();
				finish_layout.setBackgroundResource(R.drawable.capture_bg_blue_selector);
				finish_imageview.setBackgroundResource(R.drawable.routing_finish_select);
				finish_textview.setTextColor(getResources().getColor(R.color.white));

			}
		});

		//巡检
		routing_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "巡检", Toast.LENGTH_LONG).show();

			}
		});
	}

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String actionFlag = b.getString(ACTION_FLAG);
            //确认正常
            if ("confirm_normal".equals(actionFlag)) {
                String result = b.getString("result");
                //设置成功
                if("1".equals(result)){
                    //开始巡检下一个巡检点
                    if(position < routingPointBeans.size() - 1){
                        position++;
                        routingPointBean = routingPointBeans.get(position);
                        setRoutingView();
                    }
                    else if(position == routingPointBeans.size() - 1){
                        Toast.makeText(getApplicationContext(), "巡检完成!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            // 点击列表标题
            else if ("click_list_title_layout".equals(actionFlag)) {

            }
        }
    };

	private void clearButtonStates(){

		read_card_layout.setBackgroundResource(R.drawable.capture_bg_border_selector);
		fault_upload_layout.setBackgroundResource(R.drawable.capture_bg_border_selector);
		confirm_layout.setBackgroundResource(R.drawable.capture_bg_border_selector);
		finish_layout.setBackgroundResource(R.drawable.capture_bg_border_selector);

		read_card_imageview.setBackgroundResource(R.drawable.read_card_normal);
		confirm_imageview.setBackgroundResource(R.drawable.routing_confim_normal);
		fault_upload_imageview.setBackgroundResource(R.drawable.fault_upload_normal);
		finish_imageview.setBackgroundResource(R.drawable.routing_finish_normal);

		read_card_textview.setTextColor(getResources().getColor(R.color.main_blue));
		fault_upload_textview.setTextColor(getResources().getColor(R.color.main_blue));
		confirm_textview.setTextColor(getResources().getColor(R.color.main_blue));
		finish_textview.setTextColor(getResources().getColor(R.color.main_blue));
	}
}
