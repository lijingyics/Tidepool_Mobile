package com.tidepool.activities;

import java.util.ArrayList;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.util.Constant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlertFragment extends ListFragment {
	private String[] alerts = {
			"DANGER! BG is lower than " + Constant.DANGER_LOW,
			"BG is lower than " + Constant.LOW, 
			"BG is higher than " + Constant.HIGH,
			"DANGER! BG is lower than " + Constant.DANGER_HIGH };
	
	private class Alert {
		private Data data = new Data();
		private String msg;
		
		public Alert(Data d, String m) {
			data = d;
			msg = m;
		}
		
		public Data getData() { return data; }
		public String getMsg() { return msg; }
	}
	private ArrayList<Alert> theAlert;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		UserAdapter adapter = new UserAdapter(theAlert);
		setListAdapter(adapter);
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		((UserAdapter)getListAdapter()).notifyDataSetChanged();
	}

	private class UserAdapter extends ArrayAdapter<Alert> {
		public UserAdapter(ArrayList<Alert> alerts) {
			super(getActivity(), android.R.layout.simple_list_item_1, alerts);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if we weren't given a view, inflate one
			if (null == convertView) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_alert, null);
			}

			// configure the view for this Crime
			Alert currentAlert = getItem(position);

			// Set alert content
			TextView alert =
					(TextView)convertView.findViewById(R.id.alert_msg);
			alert.setText(currentAlert.getMsg());

			// Set glucose color
			int bg = currentAlert.getData().getBg();
			TextView bgText =
					(TextView)convertView.findViewById(R.id.alert_bg);
			bgText.setText(String.valueOf(bg));
			if(bg<Constant.LOW)
				bgText.setTextColor(Color.RED);
			else
				bgText.setTextColor(Color.BLUE);
			
			return convertView;
		}
	}

}
