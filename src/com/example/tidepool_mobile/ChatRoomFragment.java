package com.example.tidepool_mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatRoomFragment extends Activity {
	//@overwrite
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
       // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.chat_fragment, container, false);
       
       return v;
	}

}
