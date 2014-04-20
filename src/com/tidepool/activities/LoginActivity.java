package com.tidepool.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.User;

public class LoginActivity extends Activity {
	Button button;
	TextView text;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity);
		
		addButtonListener();
		addLinkListener();
	}
	
	public void addButtonListener() {
		button = (Button) findViewById(R.id.sign_in_button);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// insert dummy data into database
//				UserDbSource userSource = new UserDbSource(LoginActivity.this);
//				User user = new User();
//				user.setEmail("becky@gmail.com");
//				user.setGender("female");
//				user.setPassword("pwd");
//				user.setPhoneNo("123");
//				user.setRole("patient");
//				user.setUsername("becky");
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//				String dateStr = "1992-01-01";
//				try {
//					user.setDateOfBirth(formatter.parse(dateStr));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				userSource.insertUser(user);
//				
//				DataDbSource dataSource = new DataDbSource(LoginActivity.this);
//				Data data1 = new Data();
//				data1.setBg(10);
//				data1.setChatId(1);
//				data1.setInsulin(0);
//				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				String timeStr = "2014-02-02 12:00";
//				try {
//					data1.setTime(formatter2.parse(timeStr));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				data1.setUserId(1);
//				
//				Data data2 = new Data();
//				data2.setBg(20);
//				data2.setChatId(2);
//				data2.setInsulin(0);
//				String timeStr2 = "2014-02-02 12:05";
//				try {
//					data2.setTime(formatter2.parse(timeStr2));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				data2.setUserId(1);
//				
//				dataSource.insertData(data1);
//				dataSource.insertData(data2);
				
				// Get all inputs from login form
				EditText email = (EditText)findViewById(R.id.login_email);
				EditText password = (EditText)findViewById(R.id.login_password);
				
				String emailStr = email.getText().toString();
				String pwdStr = password.getText().toString();
				
				UserDbSource userSource = new UserDbSource(LoginActivity.this);
				User user = userSource.getUser(emailStr);
				if(user == null) {
					Toast.makeText(LoginActivity.this,
							"User does not exist", Toast.LENGTH_SHORT).show();
				}
				else if(!user.getPassword().equals(pwdStr)) {
					Toast.makeText(LoginActivity.this,
							"Wrong password", Toast.LENGTH_SHORT).show();
				}
				else {
					// Remember user
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
					Editor editor = pref.edit();
					editor.putString("email", user.getEmail());
					editor.commit();
					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					startActivityForResult(i, 0);
				}
				
				
				
				
			}
 
		});
	}
	
	public void addLinkListener() {
		text = (TextView)findViewById(R.id.register);
		
		text.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(i, 0);
			}
 
		});
	}
	
	
	
	

}
