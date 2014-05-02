package com.tidepool.activities;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	public static final String EXTRA_TIME = "time";
	public static final String EXTRA_BUTTON = "btn";
	Date mDate;

	public static TimePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);

		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date)getArguments().getSerializable(EXTRA_TIME);
		final Calendar c = Calendar.getInstance();
		c.setTime(mDate);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		return new CustomTimePickerDialog(getActivity(), 
				this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		//return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String time = padding(hourOfDay) + ":" + padding(minute);
		sendResult(Activity.RESULT_OK, time);
	}
	
	private void sendResult(int resultCode, String time) {
        if (getTargetFragment() == null) 
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, time);

        getTargetFragment()
            .onActivityResult(getTargetRequestCode(), resultCode, i);
    }
	
	private String padding(int num) {
		String res = String.valueOf(num);
		if(num < 10) {
			res = "0" + res;
		}
		return res;
	}
}
