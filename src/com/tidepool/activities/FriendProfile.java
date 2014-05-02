package com.tidepool.activities;

import java.text.SimpleDateFormat;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.JoinTableDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
import com.tidepool.util.Constant;
import com.tidepool.util.UserSession;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendProfile extends FragmentActivity {
	private User friend = new User();
	private String relation;
	private ClientNode client = ClientNode.getInstance();
	
	private TextView username;
	private TextView email;
	private TextView phoneNo;
	private TextView dateOfBirth;
	private TextView gender;
	private TextView role;
	private Button delete;
	private Button admit;
	private Button refuse;
	private Button call;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_profile);
		
		Bundle bundle = getIntent().getExtras();
		friend = (User) bundle.getSerializable("friend");
		relation = (String) bundle.getSerializable("relation");
		
		findCol();
		setCol();
		
		deleteButtonListener();
		addButtonListener();
		refuseButtonListener();
		phoneCall();
		
	}
	
	public void findCol() {
		username = (TextView) findViewById(R.id.friend_username);
		email = (TextView) findViewById(R.id.friend_email);
		phoneNo = (TextView) findViewById(R.id.friend_phonenum);
		dateOfBirth = (TextView) findViewById(R.id.friend_birth);
		gender = (TextView) findViewById(R.id.friend_gender);
		role = (TextView) findViewById(R.id.friend_role);
		
		delete = (Button) findViewById(R.id.deleteFriend);
		admit = (Button) findViewById(R.id.addFriend);
		refuse = (Button) findViewById(R.id.refuseFriend);
		call = (Button) findViewById(R.id.callFriend);
		
	}
	
	public void setCol() {
		username.setText(friend.getUsername());
		email.setText(friend.getEmail());
		phoneNo.setText(friend.getPhoneNo());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		dateOfBirth.setText(formatter.format(friend.getDateOfBirth()));
		gender.setText(friend.getGender());
		role.setText(friend.getRole());
		
		if(relation.equals("Added")) {
			admit.setVisibility(View.INVISIBLE);
			refuse.setVisibility(View.INVISIBLE);
		}
		else {
			delete.setVisibility(View.INVISIBLE);
			call.setVisibility(View.INVISIBLE);
			
		}
	}
	
	private void show(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public void deleteButtonListener() {
        delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String feedback = client.deleteFriend(friend.getEmail());
			
				if(feedback.equals("success")) {
					JoinTableDbSource db = new JoinTableDbSource(FriendProfile.this);
					User me = UserSession.getUser(FriendProfile.this);
					
					long id = db.getFriends(me.getId(), friend.getId());
					db.deleteFriends(id);
					
					show("Success delete friend!");
				}
				else {
					show("Fail to delete friend!");
				}
					
			}
		});
	}
	
	public void addButtonListener() {
        admit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String feedback = client.sendRespond(friend.getId(), Constant.ADMIT);
			
				Log.d("admit", feedback);
				if(!feedback.equals("success"))
					show("Fail to respond!");
				else {
					JoinTableDbSource db = new JoinTableDbSource(FriendProfile.this);
					UserDbSource userdb = new UserDbSource(FriendProfile.this);
					
					User me = UserSession.getUser(FriendProfile.this);
					
					userdb.insertUser(friend);
					db.insertFriends(me.getId(), friend.getId());
					show("Send Response");
					
					admit.setVisibility(View.GONE);
					refuse.setVisibility(View.GONE);
					delete.setVisibility(View.VISIBLE);
					call.setVisibility(View.VISIBLE);
					
				}
			}
		});
	}
	
	public void refuseButtonListener() {
        refuse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String feedback = client.sendRespond(friend.getId(), Constant.REFUSE);
			
				Log.d("refuse", feedback);
				if(!feedback.equals("success"))
					show("Fail to respond!");
			
				show("Send Response");
			}
		});
	}
	
	public void phoneCall() {
		call.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	String number = phoneNo.getText().toString().trim();
		    	
		    	Intent callIntent = new Intent(Intent.ACTION_CALL);  
		    	callIntent.setData(Uri.parse("tel:" + number));
		    	startActivity(callIntent);
		    }
		});
	}
}
