package com.tidepool.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tidepool_mobile.R;

public class AccountFragment extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        
        saveButtonListener(v);
        logoutButtonListener(v);
        
        return v;
	}
	
	public void saveButtonListener(View v) {
        Button button = (Button) v.findViewById(R.id.editAccount);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
        		
    		    // Replace whatever is in the fragment_container view with this fragment,
    		    // and add the transaction to the back stack so the user can navigate back
    		    transaction.replace(R.id.realtabcontent, new MonitorDetailFragment());
    		    transaction.addToBackStack(null);
    		
    		    // Commit the transaction
    		    transaction.commit();
			}
 
		});
	}
	
	public void logoutButtonListener(View v) {
        Button button = (Button) v.findViewById(R.id.signout);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// Remove user from sharedpreferences
				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
				Editor edit = pref.edit();
				edit.remove("email");
				edit.commit();
				
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(i, 0);
			}
 
		});
	}
	
}
