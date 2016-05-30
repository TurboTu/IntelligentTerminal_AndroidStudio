package com.liu.hwkj.intelligent;

import com.liu.hwkj.intelligentterminal.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 智能巡检
 *
 * @author Administrator
 *
 */
public class MeasureTemperatureActivity extends BaseActivity {

	private TextView routing_task_content;
	private TextView routing_point_content;
	private TextView temperature_content;
	private TextView temperature_content1;
	private TextView density_content1;
	private LinearLayout capture_layout;
	private LinearLayout record_layout;
	private Button button_submit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measuretemperature);

		super.initOnCreate();
		initComponent();
		initEventListener();
		title.setText(getResources().getString(R.string.main_temperature));
	}

	private void initComponent() {
		routing_task_content = (TextView)findViewById(R.id.routing_task_content);
		routing_point_content = (TextView)findViewById(R.id.routing_point_content);
		temperature_content = (TextView)findViewById(R.id.temperature_content);
		temperature_content1 = (TextView)findViewById(R.id.temperature_content1);
		density_content1 = (TextView)findViewById(R.id.density_content1);
		capture_layout = (LinearLayout)findViewById(R.id.capture_layout);
		record_layout = (LinearLayout)findViewById(R.id.record_layout);
		button_submit = (Button)findViewById(R.id.button_submit);
	}

	private void initEventListener() {
		//拍照
		capture_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "拍照", Toast.LENGTH_LONG).show();
			}
		});

		//录像
		record_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "录像", Toast.LENGTH_LONG).show();

			}
		});

		//提交
		button_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_LONG).show();

			}
		});
	}
}
