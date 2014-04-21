package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
import com.tidepool.util.Constant;
import com.tidepool.util.UserSession;

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
					if(id == R.id.female) {
						sex = Constant.FEMALE;
					}
					else {
						sex = Constant.MALE;
					}
				}
				user.setGender(sex);

				String role = null;
				RadioGroup rgRole = (RadioGroup)findViewById(R.id.usertype);
				if(rgRole.getCheckedRadioButtonId()!=-1){
					int id = rgRole.getCheckedRadioButtonId();
					if(id == R.id.patient) {
						role = Constant.PATIENT;
					}
					else {
						role = Constant.PARENT;
					}
				}
				user.setRole(role);

				// Validate user 
				if(!user.validate()) {
					// Make a toast to the user
					Toast toast = Toast.makeText(RegisterActivity.this,
							"Double check your input", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else {
					// Check whether user already exists

					// Insert user
					UserDbSource userSource = new UserDbSource(RegisterActivity.this);
					userSource.insertUser(user);

					// Remember current user
					UserSession.addUser(RegisterActivity.this, user);

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
//		case R.id.doctor:
//			if (checked)
//				// userType = doctor
//				break;
		}
	}

}
