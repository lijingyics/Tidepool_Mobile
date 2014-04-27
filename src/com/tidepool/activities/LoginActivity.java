package com.tidepool.activities;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.tidepool.remote.ClientNode;
import com.tidepool.util.Constant;
import com.tidepool.util.UserSession;

public class LoginActivity extends Activity {
	Button button;
	TextView text;
	ClientNode client = ClientNode.getInstance();

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
				/*UserDbSource userSourceDummy = new UserDbSource(LoginActivity.this);
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
				userSourceDummy.getAllUser();

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
				dataSource.getAllData();*/
		
				// Get all inputs from login form
				EditText email = (EditText)findViewById(R.id.login_email);
				EditText password = (EditText)findViewById(R.id.login_password);

				String emailStr = email.getText().toString();
				String pwdStr = password.getText().toString();

				//Connect the Server
				client.signin(emailStr, pwdStr);
				String fromServer = client.getFeedback();
				
				UserDbSource userSource = new UserDbSource(LoginActivity.this);
				if(fromServer.equalsIgnoreCase("No such user!")) {
					Toast toast = Toast.makeText(LoginActivity.this,
							"User does not exist", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else if(fromServer.equalsIgnoreCase("Wrong Password!")) {
					Toast toast = Toast.makeText(LoginActivity.this,
							"Wrong password", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else {
					// Save the user to database
					User user = client.getUser();
					userSource.insertUser(user);
					
					// Read from the sqlite to ensure the date format
					user = userSource.getUser(user.getEmail());
					userSource.getAllUser(); // For debug
					
					// Get the data of current user
					/*DataDbSource dataSource = new DataDbSource(LoginActivity.this);
					Log.d("Data", "start");
					ArrayList<Data> data = client.getData();
					Log.d("Data", "finish get data");
					if(user.getRole().equals(Constant.PATIENT))
						for(Data d: data) {
							d.setUserId(user.getId());
							dataSource.insertData(d);
						}
					*/
					//Login
					UserSession.addUser(LoginActivity.this, user);

					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					startActivityForResult(i, 0);
				}
				
				/*UserDbSource userSource = new UserDbSource(LoginActivity.this);
				User user = userSource.getUser(emailStr);
				userSource.getAllUser();
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
					Log.d("here", "" + user.getDateOfBirth() );
					UserSession.addUser(LoginActivity.this, user);

					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					startActivityForResult(i, 0);
				}*/
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
