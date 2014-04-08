package com.example.tidepool_mobile;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class ChatFragment extends ListFragment {
	String[] chats = {"13:10 03/10/2014", "13:20 03/19/2014"};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, chats));
	}
	

}
