package com.tidepool.activities;

import java.util.ArrayList;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.JoinTableDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
import com.tidepool.util.Constant;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class ContactFragment extends Fragment {
	private ArrayList<Friends> friends;
	private ClientNode client = ClientNode.getInstance();
	private User me;
	private EditText text;
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View v = inflater.inflate(R.layout.contact_fragment, container, false);
    	
		// Click add
		addButtonListener(v);
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// clean the view list
		friends = new ArrayList<Friends>();
		
		// Start with clean workspace
		ArrayList<User> users;
		me = UserSession.getUser(this.getActivity());
		
		// Get all received requests
		users = client.receiveRequest();
		if(users!=null)
			friends.addAll(userToFriend(users, "Add me"));
		
		// Get all responds
		users = client.receiveRespond();
		if(users!=null)
			handleRespond(users, this.getView());
		
		// Get all friends
		users = client.getFriends();
		friends.addAll(userToFriend(users, "Added"));
		
		// Set list view
		fillViewList(this.getView());
		
		// On list item click
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> parent, final View view,
		    		int position, long id) {
				Friends friend = (Friends) parent.getItemAtPosition(position);
				Intent i = new Intent(getActivity(), FriendProfile.class);

				// Add friend information
				i.putExtra("friend", friend.getFriend());
				i.putExtra("relation", friend.getRelation());
				
				// Transfer
				startActivityForResult(i, 0);
		    }
		});
		
	}
	
	/**
	 * The user send "add friend" request
	 * @param v
	 */
	public void addButtonListener(View v) {
        Button button = (Button) v.findViewById(R.id.requestFriend);
        text = (EditText) v.findViewById(R.id.friendEmail);
        
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String email = text.getText().toString();
				String feedback = sendRequest(email);
				
				show(v, feedback);
				if(feedback.equals("success")) {
					// Send email
					sendEmail(email);
				}
			}
		});
	}
	
	public void sendEmail(String friend_email) {
		Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

		String title = me.getUsername() + " send you ADD FRIEND request";
		String content = "Please go to app the \"contact\" page to reply.";
		
		/* Fill it with Data */
		sendEmail.setType("plain/text");
		sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{friend_email});
		sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		sendEmail.putExtra(android.content.Intent.EXTRA_TEXT, content);

		/* Send it off to the Activity-Chooser */
		startActivity(Intent.createChooser(sendEmail, "Send mail..."));
	}
	
	private String sendRequest(String email) {
		return client.sendRequest(email);
	}
	
	/**
	 * Set adapter and load the view list
	 * @param v
	 */
	private void fillViewList(View v) {
		list = (ListView) v.findViewById(R.id.listview);
		UserAdapter adapter = (UserAdapter) list.getAdapter();
		
		if(adapter!=null) {
			adapter.clear();
            list.deferNotifyDataSetChanged();
		}
		
		adapter = new UserAdapter(friends);
		list.setAdapter(adapter);
	}
	

	private void show(View v, String msg) {
		Toast toast = Toast.makeText(v.getContext(), msg, 
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * Quick combine the friend and the current relation
	 * @param users
	 * @param relation
	 * @return friends
	 */
	private ArrayList<Friends> userToFriend(ArrayList<User> users, String relation) {
		ArrayList<Friends> candidates = new ArrayList<Friends>();
		
		for(User user: users)
			candidates.add(new Friends(user, relation));
		
		return candidates;
	}
	
	/**
	 * Handle the candidates
	 * @param users
	 * @param v
	 */
	private void handleRespond(ArrayList<User> users, View v) {
		JoinTableDbSource joindb = new JoinTableDbSource(getActivity());
		UserDbSource userdb = new UserDbSource(getActivity());
		
		boolean flag = false;
		if(users.size()==0) return;
		
		for(User user: users) {
			if(user.getEmail()==null) {
				String msg = user.getUsername() + " refused your request!";
				
				// Make a toast to the user
				show(v, msg);
			}
			else {
				joindb.insertFriends(me.getId(), user.getId());
				userdb.insertUser(user);
				flag = true;
			}	
		}
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
