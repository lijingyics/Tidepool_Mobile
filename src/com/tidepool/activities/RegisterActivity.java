package com.tidepool.activities;

import java.util.Date;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.DataDbSource;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.Data;
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
		
		dummyUserDatabase();
		dummyDataDatabase();
		
		addButtonListener();
		addLinkListener();
	}
	
	public void dummyUserDatabase() {
		UserDbSource userHelper = new UserDbSource(this);
		User dummy1 = new User();
		Date birth = new Date(0);
		
		Log.d("Insert: ", "Inserting ..");
		dummy1.setEmail("dummy1@example.com");
		dummy1.setUsername("dummy1");
		dummy1.setPassword("123456789");
		dummy1.setDateOfBirth(birth);
		dummy1.setGender("F");
		dummy1.setRole("PAT");
		
		userHelper.insertUser(dummy1);
		Log.d("Gender: ", dummy1.getGender());
		Log.d("Role: ", dummy1.getRole());
		
		// Read the user from database
		Log.d("Reading:", "Reading...");
		user = userHelper.getUser(dummy1.getEmail());
		Log.d("Success! ", user.getUsername() + " " + user.getId());
		Log.d("Birth: ", user.getDateOfBirth().toString());
		
		userHelper.close();
	}
	
	public void dummyDataDatabase() {
		DataDbSource dataHelper = new DataDbSource(this);
		Data data = new Data();
		Date time = new Date();
		
		Log.d("Insert: ", "Inserting ..");
		data.setTime(time);
		data.setBg(150);
		data.setInsulin(0);
		data.setUserId(user.getId());
		
		long id = dataHelper.insertData(data);
		Log.d("Time: ", data.getTime().toString());
		
		// Read the user from database
		Log.d("Reading:", "Reading...");
		Data d = dataHelper.getData(id);
		Log.d("Success! ", d.getTime() + " " + d.getId());
		
		dataHelper.close();
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
