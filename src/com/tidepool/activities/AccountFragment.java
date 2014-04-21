package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tidepool_mobile.R;
import com.tidepool.dbLayout.UserDbSource;
import com.tidepool.entities.User;
import com.tidepool.util.Constant;
import com.tidepool.util.UserSession;

public class AccountFragment extends Fragment {
	private EditText username;
	private EditText password;
	private EditText confirmPwd;
	private EditText email;
	private EditText phoneNo;
	private EditText dateOfBirth;
	private RadioGroup rgSex;
	private RadioGroup rgRole;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        
        username = (EditText)v.findViewById(R.id.account_username);
		password = (EditText)v.findViewById(R.id.account_password);
		confirmPwd = (EditText)v.findViewById(R.id.account_confirmpassword);
		email = (EditText)v.findViewById(R.id.account_email);
		phoneNo = (EditText)v.findViewById(R.id.account_phonenum);
		dateOfBirth = (EditText)v.findViewById(R.id.account_birth);
		rgSex = (RadioGroup)v.findViewById(R.id.account_sex);
		rgRole = (RadioGroup)v.findViewById(R.id.account_usertype);
		
		User user = UserSession.getUser(this.getActivity());
		if(user != null) {
			username.setText(user.getUsername());
			password.setText(user.getPassword());
			email.setText(user.getEmail());
			phoneNo.setText(user.getPhoneNo());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateOfBirth.setText(formatter.format(user.getDateOfBirth()));
			if(user.getGender().equals(Constant.FEMALE)) {
				rgSex.check(R.id.account_female);
			}
			else {
				rgSex.check(R.id.account_male);
			}
			Log.d("DEBUG", user.getRole());
			if(user.getRole().equals(Constant.PATIENT)) {
				rgRole.check(R.id.account_patient);
			}
			else {
				rgRole.check(R.id.account_parent);
			}
		}
		
        saveButtonListener(v);
        logoutButtonListener(v);
        
        return v;
	}
	
	public void saveButtonListener(View v) {
        Button button = (Button) v.findViewById(R.id.editAccount);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				User user = UserSession.getUser(arg0.getContext());
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
				if(rgSex.getCheckedRadioButtonId()!=-1){
					int id = rgSex.getCheckedRadioButtonId();
					if(id == R.id.account_female) {
						sex = Constant.FEMALE;
					}
					else {
						sex = Constant.MALE;
					}
				}
				user.setGender(sex);

				String role = null;
				if(rgRole.getCheckedRadioButtonId()!=-1){
					int id = rgRole.getCheckedRadioButtonId();
					if(id == R.id.account_patient) {
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
					Toast toast = Toast.makeText(arg0.getContext(),
							"Double check your input", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else { // Update user
					UserDbSource userSource = new UserDbSource(arg0.getContext());
					int result = userSource.updateUser(user);
					if(result == -1) {
						Toast toast = Toast.makeText(arg0.getContext(),
								"Update failed", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}
					
					// Update user object in session
					UserSession.updateUser(arg0.getContext(), user);
					
					Toast toast = Toast.makeText(arg0.getContext(),
							"Update succeed", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
	}
	
	public void logoutButtonListener(View v) {
        Button button = (Button) v.findViewById(R.id.signout);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// Remove user from session
				UserSession.removeUser(arg0.getContext());
				// Back to main page
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(i, 0);
			}
 
		});
	}
	
}
