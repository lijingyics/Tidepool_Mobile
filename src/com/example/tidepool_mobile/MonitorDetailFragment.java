package com.example.tidepool_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MonitorDetailFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.monitor_main, container, false);
        
        return v;
	}
	

	public void getLocation(View view) {
	}
	
	public void viewHistory(View view) {
		Intent in = new Intent(getActivity(), MonitorDetailFragment.class);
		startActivity(in);
	}
	
	public void goChatting(View view) {
		Intent in = new Intent(getActivity(), ChatRoomFragment.class);
		startActivity(in);
	}
	
}
