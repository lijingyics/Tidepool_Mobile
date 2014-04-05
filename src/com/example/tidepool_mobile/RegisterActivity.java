package com.example.tidepool_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	Button button;
	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_activity);
		
		addButtonListener();
		addLinkListener();
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

}
