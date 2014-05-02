package com.tidepool.activities;

import java.util.ArrayList;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.JoinTableDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
import com.tidepool.util.UserSession;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactFragment extends Fragment {
	private ArrayList<Friends> friends = new ArrayList<Friends>();
	private ClientNode client = ClientNode.getInstance();
	private User me;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View v = inflater.inflate(R.layout.contact_fragment, container, false);
        
		ArrayList<User> users;
		me = UserSession.getUser(this.getActivity());
		
		// Get all received requests
		users = client.receiveRequest();
		if(users!=null)
			friends.addAll(userToFriend(users, "Add me"));
		
		// Get all responds
		users = client.receiveRespond();
		if(users!=null)
			handleRespond(users, v);
		
		// Get all friends
		JoinTableDbSource joinDb = new JoinTableDbSource(getActivity());
		users = joinDb.getFriends(me.getId());
		friends.addAll(userToFriend(users, "Added"));
		
		// Set list view
		ListView list = (ListView) v.findViewById(R.id.listview);
		UserAdapter adapter = new UserAdapter(friends);
		list.setAdapter(adapter);
		
		// On list item click
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> parent, final View view,
		    		int position, long id) {
				Friends friend = (Friends) parent.getItemAtPosition(position);
				Intent i = new Intent(getActivity(), FriendProfile.class);

				// Add friend information
				i.putExtra("friend", friend.getFriend());
				
				// Transfer
				startActivityForResult(i, 0);
		    }
		});
		
		return v;
	}
	
	/**
	 * Quick combine the friend and the current relation
	 * @param users
	 * @param relation
	 * @return friends
	 */
	private ArrayList<Friends> userToFriend(ArrayList<User> users, String relation) {
		ArrayList<Friends> candidates = new ArrayList<Friends>();
		
		for(User user: users) {
			candidates.add(new Friends(user, relation));
		}

		return candidates;
	}
	
	private void handleRespond(ArrayList<User> users, View v) {
		JoinTableDbSource joindb = new JoinTableDbSource(getActivity());
		UserDbSource userdb = new UserDbSource(getActivity());
		
		boolean flag = false;
		if(users.size()==0) return;
		
		for(User user: users) {
			if(user.getEmail()==null) {
				// Make a toast to the user
				Toast toast = Toast.makeText(v.getContext(),
						user.getUsername() + "refused your request!", 
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			else {
				joindb.insertFriends(me.getId(), user.getId());
				userdb.insertUser(user);
				flag = true;
			}	
		}
		
		// Get all data
		if(flag) return;
		DataDbSource dataSource = new DataDbSource(getActivity());
		ArrayList<Data> data = client.getData();
		
		for(Data d: data)
			dataSource.insertData(d);
		
	}
	
	/**
	 * Inner Class for handling different kinds of contacts
	 * @author wenjia
	 *
	 */
	private class Friends {
		User friend;
		String relation;
		
		private Friends(User f, String r) {
			friend = f;
			relation = r;
		}
		
		public User getFriend() { return friend; }
		public String getRelation() { return relation; }
	}
	
	private class UserAdapter extends ArrayAdapter<Friends> {
		public UserAdapter(ArrayList<Friends> friends) {
			super(getActivity(), android.R.layout.simple_list_item_1, friends);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if we weren't given a view, inflate one
			if (null == convertView) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_contact, null);
			}

			// configure the view for this Crime
			Friends friend = getItem(position);

			TextView titleTextView =
					(TextView)convertView.findViewById(R.id.contact_friend);
			titleTextView.setText(friend.getFriend().getUsername());
			
			TextView remindTextView =
					(TextView)convertView.findViewById(R.id.contact_remind);
			remindTextView.setText(friend.getRelation());
			
			return convertView;
		}
	}
}
