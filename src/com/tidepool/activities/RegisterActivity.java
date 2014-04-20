package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.User;

public class RegisterActivity extends Activity {
	Button button;
	TextView text;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_activity);

		addButtonListener();
		addLinkListener();
	}

	//	public void updateTable() {
	//		JoinTableDbSource join = new JoinTableDbSource(this);
	//		
	//		join.updateTable(2,3);
	//		join.close();
	//	}
	//	
	//	public void dummyUserDatabase() {
	//		UserDbSource userHelper = new UserDbSource(this);
	//		User dummy1 = new User();
	//		
	//		Log.d("Insert: ", "Inserting ..");
	//		dummy1.setEmail("dummy3@example.com");
	//		dummy1.setUsername("dummy3");
	//		dummy1.setPassword("123456789");
	//		try {
	//			dummy1.setDateOfBirth(new SimpleDateFormat( "yyyy-MM-dd" ).
	//					parse( "2009-10-20" ));
	//		} catch (ParseException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		dummy1.setGender("M");
	//		dummy1.setRole("PAT");
	//		dummy1.setId(3);
	//		
	//		userHelper.insertUser(dummy1);
	//		Log.d("Username: ", dummy1.getUsername());
	//		Log.d("Date of birth: ", dummy1.getDateOfBirth().toString());
	//		
	//		// Read the user from database
	//		Log.d("Reading:", "Reading...");
	//		user = userHelper.getUser(dummy1.getEmail());
	//		Log.d("Success reading new user! ", user.getUsername() + " " + user.getId());
	//		Log.d("Birth: ", user.getDateOfBirth().toString());
	//		
	//		userHelper.getAllUser();
	//		userHelper.close();
	//	}
	//	
	//	public void dummyDataDatabase() {
	//		DataDbSource dataHelper = new DataDbSource(this);
	//		Data data = new Data();
	//		
	//		Log.d("Insert: ", "Inserting ..");
	//		try {
	//			data.setTime(new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).
	//					parse( "2013-04-27 10:00" ));
	//		} catch (ParseException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		data.setId(5);
	//		data.setBg(130);
	//		data.setInsulin(0);
	//		data.setUserId(user.getId());
	//		
	//		long id = dataHelper.insertData(data);
	//		Log.d("Time: ", data.getTime().toString());
	//		
	//		// Read the user from database
	//		Log.d("Reading:", "Reading...");
	//		Data d = dataHelper.getData(id);
	//		Log.d("Success! ", d.getTime() + " " + d.getId());
	//		
	//		
	//		dataHelper.getAllData();
	//		dataHelper.close();
	//	}
	//	
	//	public void dummyMessageDatabase() {
	//		MessageDbSource msgHelper = new MessageDbSource(this);
	//		Message msg = new Message();
	//		
	//		Log.d("Insert: ", "Inserting ..");
	//		msg.setContent("Test! Hello Chat Room 2th");
	//		msg.setUserId(user.getId());
	//		
	//		long id = msgHelper.insertMessage(msg);
	//		Log.d("Chat: ", msg.getContent());
	//		
	//		// Read the user from database
	//		Log.d("Reading:", "Reading...");
	//		Message m = msgHelper.getMessage(id);
	//		Log.d("Success! ", m.getContent() + " " + m.getId());
	//		
	//		msgHelper.close();
	//	}
	//	
	//	public void dummyAlertDatabase() {
	//		AlertDbSource alertHelper = new AlertDbSource(this);
	//		Alert alert = new Alert();
	//		
	//		Log.d("Insert: ", "Inserting ..");
	//		alert.setContent("Too low BG! 2th");
	//		alert.setDataId(2);
	//		
	//		long id = alertHelper.insertAlert(alert);
	//		Log.d("Chat: ", alert.getContent());
	//		
	//		// Read the user from database
	//		Log.d("Reading:", "Reading...");
	//		Alert a = alertHelper.getAlert(id);
	//		Log.d("Success! ", a.getContent() + ", data id:" + a.getDataId());
	//		
	//		alertHelper.close();
	//	}
	//	
	//	public void testJoinTable() {
	//		JoinTableDbSource joinHelper = new JoinTableDbSource(this);
	//		
	//		MessageDbSource msgHelper = new MessageDbSource(this);
	//		Message m = msgHelper.getMessage(1);
	//		Log.d("Message ", m.getContent() + " id: " + m.getId());
	//		
	//		long chat_message_id = joinHelper.insertChatMessage(1, 1);
	//		long friend_id = joinHelper.insertFriends(1, 2);
	//		long chat_user_id = joinHelper.insertChatUser(1, m.getUserId());
	//		
	//		// Test join friends
	//		Log.d("Reading:", "Reading Relationship...");
	//		ArrayList<User> userList = joinHelper.getFriends(user.getId());
	//		Log.d("Find Friends: ", String.valueOf(userList.size()) );
	//		
	//		// Test join chat user
	//		ArrayList<Message> msgList = joinHelper.getMessage(1);
	//		Log.d("Find Friends: ", String.valueOf(msgList.size()) );
	//		
	//		// Test join chat user
	//		ArrayList<Data> dataList = joinHelper.getChatRoom(m.getUserId());
	//		Log.d("Find Friends: ", String.valueOf(dataList.size()) );
	//		
	//		
	//		msgHelper.close();
	//		joinHelper.close();
	//	}

	public void addButtonListener() {
		button = (Button) findViewById(R.id.signup);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				User user = new User();
				// Get all inputs from register form
				EditText username = (EditText)findViewById(R.id.username);
				EditText password = (EditText)findViewById(R.id.password);
				EditText confirmPwd = (EditText)findViewById(R.id.confirmpassword);
				EditText email = (EditText)findViewById(R.id.email);
				EditText phoneNo = (EditText)findViewById(R.id.phonenum);
				EditText dateOfBirth = (EditText)findViewById(R.id.birth);

				user.setUsername(username.getText().toString());
				user.setPassword(password.getText().toString());
				user.setConfirmPwd(confirmPwd.getText().toString());
				user.setEmail(email.getText().toString());
				user.setPhoneNo(phoneNo.getText().toString());
				String dateOfBirthStr = dateOfBirth.getText().toString();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date birth = null;
				try {
					birth = formatter.parse(dateOfBirthStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				user.setDateOfBirth(birth);

				String sex = null;
				RadioGroup rgSex = (RadioGroup)findViewById(R.id.sex);
				if(rgSex.getCheckedRadioButtonId()!=-1){
					int id = rgSex.getCheckedRadioButtonId();
					RadioButton radioBtn = (RadioButton)findViewById(id);
					sex = (String)radioBtn.getText();
				}
				user.setGender(sex);

				String role = null;
				RadioGroup rgRole = (RadioGroup)findViewById(R.id.usertype);
				if(rgRole.getCheckedRadioButtonId()!=-1){
					int id = rgSex.getCheckedRadioButtonId();
					RadioButton radioBtn = (RadioButton)findViewById(id);
					role = (String)radioBtn.getText();
				}
				user.setRole(role);

				// Validate user 
				if(!user.validate()) {
					// Make a toast to the user
					Toast.makeText(RegisterActivity.this,
							"Double check your input", Toast.LENGTH_SHORT).show();
				}
				else {
					// Check whether user already exists

					// Insert user
					UserDbSource userSource = new UserDbSource(RegisterActivity.this);
					userSource.insertUser(user);

					// Remember current user
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
					Editor editor = pref.edit();
					editor.putString("email", user.getEmail());
					editor.commit();

					Intent i = new Intent(RegisterActivity.this, MainActivity.class);
					startActivityForResult(i, 0);
				}
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
