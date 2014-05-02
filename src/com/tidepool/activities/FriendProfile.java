package com.tidepool.activities;

import java.text.SimpleDateFormat;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

public class FriendProfile extends FragmentActivity {
	private User friend = new User();
	private ClientNode client = ClientNode.getInstance();
	
	private TextView username;
	private TextView email;
	private TextView phoneNo;
	private TextView dateOfBirth;
	private TextView gender;
	private TextView role;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_profile);
		
		Bundle bundle = getIntent().getExtras();
		friend = (User) bundle.getSerializable("friend");
		
		findCol();
		setCol();
		
	}
	
	public void findCol() {
		username = (TextView) findViewById(R.id.friend_username);
		email = (TextView) findViewById(R.id.friend_email);
		phoneNo = (TextView) findViewById(R.id.friend_phonenum);
		dateOfBirth = (TextView) findViewById(R.id.friend_birth);
		gender = (TextView) findViewById(R.id.friend_gender);
		role = (TextView) findViewById(R.id.friend_role);		
	}
	
	public void setCol() {
		username.setText(friend.getUsername());
		email.setText(friend.getEmail());
		phoneNo.setText(friend.getPhoneNo());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		dateOfBirth.setText(formatter.format(friend.getDateOfBirth()));
		gender.setText(friend.getGender());
		role.setText(friend.getRole());
	}
}
