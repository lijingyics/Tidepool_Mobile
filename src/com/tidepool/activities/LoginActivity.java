package com.tidepool.activities;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.util.UserSession;

public class LoginActivity extends Activity {
	Button button;
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check whether user has already signed in
		if(UserSession.isAdded(LoginActivity.this)) {
			Intent i = new Intent(LoginActivity.this, MainActivity.class);
			startActivityForResult(i, 0);
		}

		setContentView(R.layout.login_activity);
		addButtonListener();
		addLinkListener();
	}

	public void addButtonListener() {
		button = (Button) findViewById(R.id.sign_in_button);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// insert dummy data into database
				UserDbSource userSourceDummy = new UserDbSource(LoginActivity.this);
				User userDummy = new User();
				userDummy.setEmail("becky@gmail.com");
				userDummy.setGender("female");
				userDummy.setPassword("pwd");
				userDummy.setPhoneNo("123");
				userDummy.setRole("patient");
				userDummy.setUsername("becky");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = "1992-01-01";
				try {
					userDummy.setDateOfBirth(formatter.parse(dateStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				userSourceDummy.insertUser(userDummy);
<<<<<<< HEAD
				userSourceDummy.getAllUser();
				
=======


>>>>>>> f0de1827142281abf5e8a7814e941b37536507db
				DataDbSource dataSource = new DataDbSource(LoginActivity.this);
				Data data1 = new Data();
				data1.setBg(10);
				data1.setChatId(1);
				data1.setInsulin(0);
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String timeStr = "2014-02-02 12:00";
				try {
					data1.setTime(formatter2.parse(timeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				data1.setUserId(4);
				
				Data data2 = new Data();
				data2.setBg(20);
				data2.setChatId(2);
				data2.setInsulin(0);
				String timeStr2 = "2014-02-02 12:05";
				try {
					data2.setTime(formatter2.parse(timeStr2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				data2.setUserId(4);
				
				//dataSource.insertData(data1);
				//dataSource.insertData(data2);
				dataSource.getAllData();
		
				// Get all inputs from login form
				EditText email = (EditText)findViewById(R.id.login_email);
				EditText password = (EditText)findViewById(R.id.login_password);

				String emailStr = email.getText().toString();
				String pwdStr = password.getText().toString();

				UserDbSource userSource = new UserDbSource(LoginActivity.this);
				User user = userSource.getUser(emailStr);
				if(user == null) {
					Toast toast = Toast.makeText(LoginActivity.this,
							"User does not exist", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else if(!user.getPassword().equals(pwdStr)) {
					Toast toast = Toast.makeText(LoginActivity.this,
							"Wrong password", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else {
					// Remember user
					UserSession.addUser(LoginActivity.this, user);

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
