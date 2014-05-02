package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;

@SuppressLint("SimpleDateFormat")
public class DataFragment extends Fragment {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private Data data;
	private Button mDateButton;
	private Button mTimeButton;
	private Button searchButton;
	private Button locationButton;
	private Button chatButton;
	private LocationManager locationManager;
	private String provider;

	public static DataFragment newInstance(Data data) {
		Bundle args = new Bundle();
		args.putSerializable("data", data);

		DataFragment fragment = new DataFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = (Data)getArguments().getSerializable("data");
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.monitor_main, parent, false);

		Bundle bundle = getActivity().getIntent().getExtras();
		String username = bundle.getString("username");

		TextView titleName = (TextView)v.findViewById(R.id.title_name_value);
		TextView bg = (TextView)v.findViewById(R.id.bg_value);
		TextView insulin = (TextView)v.findViewById(R.id.insulin_value);
		TextView time = (TextView)v.findViewById(R.id.time_value);
		mDateButton = (Button)v.findViewById(R.id.change_date);
		mTimeButton = (Button)v.findViewById(R.id.change_time);
		searchButton = (Button)v.findViewById(R.id.search_data);
		locationButton = (Button)v.findViewById(R.id.location);
		chatButton = (Button)v.findViewById(R.id.chatting);

		titleName.setText(username);
		bg.setText(String.valueOf(data.getBg()));
		insulin.setText(String.valueOf(data.getInsulin()));

		String dateStr = formatter.format(data.getTime());
		time.setText(dateStr);

		mDateButton.setText(dateFormat.format(data.getTime()));
		mTimeButton.setText(timeFormat.format(data.getTime()));

		addDatePicker();
		addTimePicker();

		addLocationBtnListener();
		addChatBtnListener();
		addSearchListener();

		return v;
	}

	private void addLocationBtnListener() {
		locationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {   
				// Get the location manager
				locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				// Define the criteria how to select the locatioin provider -> use
				// default
				Criteria criteria = new Criteria();
				provider = locationManager.getBestProvider(criteria, false);
				Location location = locationManager.getLastKnownLocation(provider);
				
				if(location != null) {
					// send sms message and toast
					int lat = (int) (location.getLatitude());
				    int lng = (int) (location.getLongitude());
				    String message = "Current location";
				    message += "\nLatitude: " + lat;
				    message += "\nLogtitude: " + lng;
				    
				    Toast toast = Toast.makeText(getActivity(),
							message, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
	}

	private void addChatBtnListener() {
		chatButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {                	            	

			}
		});
	}

	private void addSearchListener() {
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {                	            	
				String dateStr = mDateButton.getText().toString() + " " +
						mTimeButton.getText().toString();
				try {
					Date date = formatter.parse(dateStr);

					DataPagerActivity activity = (DataPagerActivity)getActivity();
					int index = activity.getDataIndex(date);
					if(index == -1) {
						Toast toast = Toast.makeText(getActivity(),
								"Wrong time", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}
					activity.getViewPager().setCurrentItem(index);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void addDatePicker() {
		mDateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {                	            	
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment
						.newInstance(data.getTime());
				dialog.setTargetFragment(DataFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);              	
			}
		});
	}

	private void addTimePicker() {
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// show the time picker dialog
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				DialogFragment dialog = TimePickerFragment.newInstance(data.getTime());
				dialog.setTargetFragment(DataFragment.this, REQUEST_TIME);
				dialog.show(fm, DIALOG_TIME);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mDateButton.setText(dateFormat.format(date));
		} else if(requestCode == REQUEST_TIME) {
			String time = data.getStringExtra(TimePickerFragment.EXTRA_TIME);
			mTimeButton.setText(time);
		}
	}

}
