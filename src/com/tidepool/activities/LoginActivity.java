package com.tidepool.activities;

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
import com.tidepool.dbLayout.JoinTableDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Data;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
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
	
	@Override
	public void onDestroy() {
		client.close();
	}

	public void addButtonListener() {
		button = (Button) findViewById(R.id.sign_in_button);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
		
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
					
					// Get the friends of current user
					JoinTableDbSource joinSource = new JoinTableDbSource(LoginActivity.this);
					Log.d("Friends", "start");
					ArrayList<User> friends = client.getFriends();
					Log.d("Friends", "finish get data");
					
					for(User u: friends) {
						Log.d("Friends", "id " + u.getId() + " email" + u.getEmail() );
						joinSource.insertFriends(user.getId(), u.getId());
						userSource.insertUser(u);
					}
					userSource.getAllUser(); // For debug
					
					// Get the data of current user
					DataDbSource dataSource = new DataDbSource(LoginActivity.this);
					ArrayList<Data> data = client.getData();
					
					for(Data d: data) {
						Log.d("Data", "" + d.getBg() + " " + d.getTime());
						dataSource.insertData(d);
					}
					dataSource.getAllData(); // for debug
					
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
