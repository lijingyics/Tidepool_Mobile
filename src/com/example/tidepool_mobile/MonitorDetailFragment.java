package com.example.tidepool_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MonitorDetailFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.monitor_main, container, false);
        
        /*FragmentTabHost mTabHost;
        mTabHost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
        
        mTabHost.setCurrentTab(0);
        chattingButtonListener(v);
        */
        return v;
	}
	

	public void getLocation(View view) {
	}
	
	public void viewHistory(View view) {
		Intent in = new Intent(getActivity(), MonitorDetailFragment.class);
		startActivity(in);
	}
	
	public void chattingButtonListener(View view) {
		Button button = (Button) view.findViewById(R.id.chatting);
		 
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
        		
    		    // Replace whatever is in the fragment_container view with this fragment,
    		    // and add the transaction to the back stack so the user can navigate back
    		    transaction.replace(R.id.realtabcontent, new ChatRoomFragment());
    		    transaction.addToBackStack(null);
    		
    		    // Commit the transaction
    		    transaction.commit();
			}
 
		});
	}
	
}
