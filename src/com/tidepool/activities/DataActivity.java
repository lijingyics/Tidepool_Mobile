package com.tidepool.activities;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;

public class DataActivity extends Activity {
	private Data data;
	private String username;
	private TextView titleName;
	private TextView bg;
	private TextView insulin;
	private TextView time;
	private TextView currTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor_main);

		Bundle bundle = getIntent().getExtras();

		if(bundle == null) {
			Log.d("DEBUG", "bundle is null");
		}
		else {

			data = (Data)bundle.getSerializable("data");
			if(data == null) {
				Log.d("DEBUG", "data is null");
			}
			Log.d("DEBUG", String.valueOf(data.getBg()));
			username = bundle.getString("username");
			if(username == null) {
				Log.d("DEBUG", "username is null");
			}
			Log.d("DEBUG", username);

			titleName = (TextView)findViewById(R.id.title_name_value);
			bg = (TextView)findViewById(R.id.bg_value);
			insulin = (TextView)findViewById(R.id.insulin_value);
			time = (TextView)findViewById(R.id.time_value);
			currTime = (TextView)findViewById(R.id.current_time_value);

			titleName.setText(username);
			bg.setText(String.valueOf(data.getBg()));
			insulin.setText(String.valueOf(data.getInsulin()));

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String dateStr = formatter.format(data.getTime());
			time.setText(dateStr);
			currTime.setText(dateStr);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
