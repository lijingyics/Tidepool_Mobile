package com.tidepool.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
import com.tidepool.util.Constant;
import com.tidepool.util.UserSession;


public class MonitorFragment extends ListFragment {
	ClientNode client = ClientNode.getInstance();
	UserAdapter adapter;
	private ArrayList<User> users;
	private User user;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		user = UserSession.getUser(this.getActivity());
		Log.d("DEBUG", user.getRole());
		String role = user.getRole();
		users = new ArrayList<User>();
		if(role.equals(Constant.PATIENT)) {
			users.add(user);
		}
		else if(role.equals(Constant.PARENT)) {
			ArrayList<User> friends = client.getFriends();
			for(User u : friends) {
				if(u.getRole().equals(Constant.PATIENT)) {
					users.add(u);
				}
			}
		}
		adapter = new UserAdapter(users);
		setListAdapter(adapter);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		User user = ((UserAdapter)getListAdapter()).getItem(position);
		Intent i = new Intent(getActivity(), DataPagerActivity.class);
		
		ArrayList<Data> datas = client.getData(user.getId());
		
		if(datas.size() > 0) {
			i.putExtra("datas", datas);
			i.putExtra("username", user.getUsername());
			startActivityForResult(i, 0);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		((UserAdapter)getListAdapter()).notifyDataSetChanged();
	}

	private class UserAdapter extends ArrayAdapter<User> {
		public UserAdapter(ArrayList<User> users) {
			super(getActivity(), android.R.layout.simple_list_item_1, users);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if we weren't given a view, inflate one
			if (null == convertView) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_user, null);
			}

			// configure the view for this Crime
			User user = getItem(position);

			TextView titleTextView =
					(TextView)convertView.findViewById(R.id.user_list_item_titleTextView);
			titleTextView.setText(user.getUsername());
			
			return convertView;
		}
	}

	@Override
	public void onResume() {
		String role = user.getRole();
		users.clear();
		if(role.equals(Constant.PATIENT)) {
			User newUser = client.getUser(user.getId());	
			users.add(newUser);
			//UserSession.updateUser(getActivity(), newUser);
		}
		else {
			ArrayList<User> friends = client.getFriends();
			for(User u : friends) {
				if(u.getRole().equals(Constant.PATIENT)) {
					users.add(u);
				}
			}
		}
		adapter.notifyDataSetChanged();
		super.onResume();
	}
}
