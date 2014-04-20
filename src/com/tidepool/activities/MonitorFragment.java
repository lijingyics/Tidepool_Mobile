package com.tidepool.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;


public class MonitorFragment extends ListFragment {
	private ArrayList<User> users;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
		String email = pref.getString("email", "becky@gmail.com");

		UserDbSource userSource = new UserDbSource(getActivity());
		User user = userSource.getUser(email);
		String role = user.getRole();
		users = new ArrayList<User>();
		if(role.equals("patient")) {
			users.add(user);
		}
		else if(role.equals("parent")) {
			// get all children of this parent

		}
		UserAdapter adapter = new UserAdapter(users);
		setListAdapter(adapter);

		//setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, patients));
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		User user = ((UserAdapter)getListAdapter()).getItem(position);
		Intent i = new Intent(getActivity(), DataActivity.class);

		DataDbSource dataSource = new DataDbSource(getActivity());
		ArrayList<Data> datas = dataSource.getDataByUser(user.getId());
		if(datas.size() > 0) {
			i.putExtra("data", datas.get(0));
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


}
