package com.tidepool.activities;

import java.util.Date;
import java.util.ArrayList;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
import com.tidepool.util.Constant;
import com.tidepool.util.UserSession;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlertFragment extends ListFragment {
	private String[] alerts = {
			"DANGER! BG is lower than " + Constant.DANGER_LOW,
			"BG is lower than " + Constant.LOW, 
			"BG is higher than " + Constant.HIGH,
			"DANGER! BG is higher than " + Constant.DANGER_HIGH };
	
	private ArrayList<Alert> theAlert;
	private static int status = -1;
	private ClientNode client = ClientNode.getInstance();
	private ArrayList<Data> data;
	private User me;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		theAlert = new ArrayList<Alert>();
		// Get the user
		me = UserSession.getUser(this.getActivity());
		if(me.getRole().equals(Constant.PARENT))
			return;
		
		// Get the user data
		data = client.getData(me.getId());
		theAlert.addAll(inserHistory());
		
		UserAdapter adapter = new UserAdapter(theAlert);
		setListAdapter(adapter);
	}
	
	public ArrayList<Alert> inserHistory() {
		ArrayList<Alert> history = new ArrayList<Alert>();
		ArrayList<String> parentPhone = parentPhoneNo();
		String sms;
		
		for(Data d: data) {
			if(d.getBg()<Constant.DANGER_LOW) {
				if(status!=0) {
					history.add(new Alert(d, alerts[0]));
				
					// send SMS
					sms = "DANGER!" + me.getUsername() + "'s BG is " + d.getBg() + " now!";
					for(String p: parentPhone)
						sendSmsByManager(p, sms);
				}
				status = 0;
			}
			else if(d.getBg()<Constant.LOW) {
				if(status!=1) history.add(new Alert(d, alerts[1]));
				status = 1;
			} 
			else if(d.getBg()>Constant.DANGER_HIGH) {
				if(status!=3) { 
					history.add(new Alert(d, alerts[3]));
					
					// send SMS
					sms = "DANGER!" + me.getUsername() + "'s BG is " + d.getBg() + " now!";
					for(String p: parentPhone)
						sendSmsByManager(p, sms);
				}
				status = 3;
			}
			else if(d.getBg()>Constant.HIGH) {
				if(status!=2) history.add(new Alert(d, alerts[2]));
				status = 2;
			}
		}
		
		return history;
	}
	
	public ArrayList<String> parentPhoneNo() {
		ArrayList<String> phone = new ArrayList<String>();
		ArrayList<User> parents = client.getFriends();
		
		for(User p: parents)
			if(p.getRole().equals(Constant.PARENT))
				phone.add(p.getPhoneNo());
		
		return phone;
	}
	
	public void sendSmsByManager(String phoneNo, String sms) {
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, 
					null,  
					sms,
					null, 
					null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

	/**
	 * Inner class to handle alert message
	 * @author wenjia
	 *
	 */
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
}
