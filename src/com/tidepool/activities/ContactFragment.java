package com.tidepool.activities;

import java.util.ArrayList;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.JoinTableDbSource;
import com.tidepool.entities.User;
import com.tidepool.util.UserSession;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFragment extends Fragment {
	private ArrayList<User> users;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View v = inflater.inflate(R.layout.contact_fragment, container, false);
        
		// Get all friends
		User user = UserSession.getUser(this.getActivity());
		JoinTableDbSource joinDb = new JoinTableDbSource(getActivity());
		users = joinDb.getFriends(user.getId());
		
		// Set list view
		ListView list = (ListView) v.findViewById(R.id.listview);
		UserAdapter adapter = new UserAdapter(users);
		list.setAdapter(adapter);
		
		// On list item click
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> parent, final View view,
		    		int position, long id) {
				User user = (User) parent.getItemAtPosition(position);
				Intent i = new Intent(getActivity(), FriendProfile.class);

				// Add friend information
				
				
				// Transfer
				startActivityForResult(i, 0);
		    }
		});
		
		return v;
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
