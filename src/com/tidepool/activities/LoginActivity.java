package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.TidepoolDbHelper;
import com.tidepool.entities.User;
import com.tidepool.remote.ClientNode;
import com.tidepool.util.LocationHelper;
import com.tidepool.util.UserSession;

public class LoginActivity extends Activity {
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Button button;
	TextView text;
	ClientNode client = ClientNode.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TidepoolDbHelper dbhelper = new TidepoolDbHelper(LoginActivity.this);
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		dbhelper.onUpgrade(db, 1, 2);
		// Check whether user has already signed in
		if(UserSession.isAdded(LoginActivity.this)) {
			User user = UserSession.getUser(LoginActivity.this);
			client.signin(user.getEmail(), user.getPassword());
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

				//UserDbSource userSource = new UserDbSource(LoginActivity.this);
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
					User user = client.getUser();
					Log.d("DEBUG", user.getUsername());
					// get current location
					LocationHelper locationHelper = new LocationHelper(LoginActivity.this);
					double lat = locationHelper.getLat();
					double lng = locationHelper.getLng();

					// update user
					user.setLocation_lat(lat);
					user.setLocation_lng(lng);

					client.updateUser(user);

					Date birth = user.getDateOfBirth();
					Date parsedBirth = null;
					try {
						parsedBirth = formatter.parse(formatter.format(birth));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					user.setDateOfBirth(parsedBirth);

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
