package com.tidepool.activities;

import java.util.ArrayList;
import java.util.Date;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.AlertDbSource;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.JoinTableDbSource;
import com.tidepool.dbLayout.MessageDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Alert;
import com.tidepool.entities.Data;
import com.tidepool.entities.Message;
import com.tidepool.entities.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	Button button;
	TextView text;
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_activity);
		
		//updateTable();
		/*dummyUserDatabase();
		dummyDataDatabase();
		dummyMessageDatabase();
		dummyAlertDatabase();
		testJoinTable();
		*/
		
		addButtonListener();
		addLinkListener();
	}
	
	public void updateTable() {
		JoinTableDbSource join = new JoinTableDbSource(this);
		
		join.updateTable(2,3);
		join.close();
	}
	
	public void dummyUserDatabase() {
		UserDbSource userHelper = new UserDbSource(this);
		User dummy1 = new User();
		Date birth = new Date(0);
		
		Log.d("Insert: ", "Inserting ..");
		dummy1.setEmail("dummy2@example.com");
		dummy1.setUsername("dummy2");
		dummy1.setPassword("123456789");
		dummy1.setDateOfBirth(birth);
		dummy1.setGender("M");
		dummy1.setRole("PAT");
		
		userHelper.insertUser(dummy1);
		Log.d("Username: ", dummy1.getUsername());
		Log.d("Date of birth: ", dummy1.getDateOfBirth().toString());
		
		// Read the user from database
		Log.d("Reading:", "Reading...");
		user = userHelper.getUser(dummy1.getEmail());
		Log.d("Success reading new user! ", user.getUsername() + " " + user.getId());
		Log.d("Birth: ", user.getDateOfBirth().toString());
		
		userHelper.getAllUser();
		userHelper.close();
	}
	
	public void dummyDataDatabase() {
		DataDbSource dataHelper = new DataDbSource(this);
		Data data = new Data();
		Date time = new Date();
		
		Log.d("Insert: ", "Inserting ..");
		data.setTime(time);
		data.setBg(130);
		data.setInsulin(0);
		data.setUserId(user.getId());
		
		long id = dataHelper.insertData(data);
		Log.d("Time: ", data.getTime().toString());
		
		// Read the user from database
		Log.d("Reading:", "Reading...");
		Data d = dataHelper.getData(id);
		Log.d("Success! ", d.getTime() + " " + d.getId());
		
		
		dataHelper.getAllData();
		dataHelper.close();
	}
	
	public void dummyMessageDatabase() {
		MessageDbSource msgHelper = new MessageDbSource(this);
		Message msg = new Message();
		
		Log.d("Insert: ", "Inserting ..");
		msg.setContent("Test! Hello Chat Room 2th");
		msg.setUserId(user.getId());
		
		long id = msgHelper.insertMessage(msg);
		Log.d("Chat: ", msg.getContent());
		
		// Read the user from database
		Log.d("Reading:", "Reading...");
		Message m = msgHelper.getMessage(id);
		Log.d("Success! ", m.getContent() + " " + m.getId());
		
		msgHelper.close();
	}
	
	public void dummyAlertDatabase() {
		AlertDbSource alertHelper = new AlertDbSource(this);
		Alert alert = new Alert();
		
		Log.d("Insert: ", "Inserting ..");
		alert.setContent("Too low BG! 2th");
		alert.setDataId(2);
		
		long id = alertHelper.insertAlert(alert);
		Log.d("Chat: ", alert.getContent());
		
		// Read the user from database
		Log.d("Reading:", "Reading...");
		Alert a = alertHelper.getAlert(id);
		Log.d("Success! ", a.getContent() + ", data id:" + a.getDataId());
		
		alertHelper.close();
	}
	
	public void testJoinTable() {
		JoinTableDbSource joinHelper = new JoinTableDbSource(this);
		
		MessageDbSource msgHelper = new MessageDbSource(this);
		Message m = msgHelper.getMessage(1);
		Log.d("Message ", m.getContent() + " id: " + m.getId());
		
		long chat_message_id = joinHelper.insertChatMessage(1, 1);
		long friend_id = joinHelper.insertFriends(1, 2);
		long chat_user_id = joinHelper.insertChatUser(1, m.getUserId());
		
		// Test join friends
		Log.d("Reading:", "Reading Relationship...");
		ArrayList<User> userList = joinHelper.getFriends(user.getId());
		Log.d("Find Friends: ", String.valueOf(userList.size()) );
		
		// Test join chat user
		ArrayList<Message> msgList = joinHelper.getMessage(1);
		Log.d("Find Friends: ", String.valueOf(msgList.size()) );
		
		// Test join chat user
		ArrayList<Data> dataList = joinHelper.getChatRoom(m.getUserId());
		Log.d("Find Friends: ", String.valueOf(dataList.size()) );
		
		
		msgHelper.close();
		joinHelper.close();
	}
	
	public void addButtonListener() {
		button = (Button) findViewById(R.id.signup);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(RegisterActivity.this, MainActivity.class);
				startActivityForResult(i, 0);
			}
 
		});
	}
	
	public void addLinkListener() {
		text = (TextView)findViewById(R.id.login);
		
		text.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivityForResult(i, 0);
			}
 
		});
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.female:
	            if (checked)
	                // sex = female
	            break;
	        case R.id.male:
	            if (checked)
	                // sex = male
	            break;
	        case R.id.patient:
	            if (checked)
	                // userType = patient
	            break;
	        case R.id.parent:
	            if (checked)
	                // userType = parent
	            break;
	        case R.id.doctor:
	            if (checked)
	                // userType = doctor
	            break;
	    }
	}
	
}
