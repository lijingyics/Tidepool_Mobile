package com.example.tidepool_mobile;

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
