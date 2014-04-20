package com.tidepool.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;

public class DataFragment extends Fragment {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
	private Data data;
	private Button mDateButton;
	private Button mTimeButton;
	
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
		//TextView currTime = (TextView)v.findViewById(R.id.current_time_value);

		titleName.setText(username);
		bg.setText(String.valueOf(data.getBg()));
		insulin.setText(String.valueOf(data.getInsulin()));

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = formatter.format(data.getTime());
		time.setText(dateStr);
		//currTime.setText(dateStr);

		// Add time picker
		mDateButton = (Button)v.findViewById(R.id.change_date);
		mDateButton.setText(dateFormat.format(data.getTime()));
		
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

		mTimeButton = (Button)v.findViewById(R.id.change_time);
		mTimeButton.setText(timeFormat.format(data.getTime()));
		
		return v;
	}

//	public void updateDate() {
//		mDateButton.setText(formatter.format(data.getTime()));
//	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mDateButton.setText(dateFormat.format(date));
        }
    }

}
