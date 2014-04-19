package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
				UserDbSource userSource = new UserDbSource(LoginActivity.this);
				User user = new User();
				user.setEmail("becky@gmail.com");
				user.setGender("female");
				user.setPassword("pwd");
				user.setPhoneNo("123");
				user.setRole("patient");
				user.setUsername("becky");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = "1992-01-01";
				try {
					user.setDateOfBirth(formatter.parse(dateStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				userSource.insertUser(user);
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivityForResult(i, 0);
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
