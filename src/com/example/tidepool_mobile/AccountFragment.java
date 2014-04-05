package com.example.tidepool_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class AccountFragment extends Fragment {
	Button button;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        
        button = (Button)v.findViewById(R.id.signout);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(i, 0);
			}
 
		});
        
        return v;
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton)view).isChecked();
	    
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
	                // sex = male
	            break;
	        case R.id.parent:
	            if (checked)
	                // sex = male
	            break;
	        case R.id.doctor:
	            if (checked)
	                // sex = male
	            break;
	    }
	}

}
