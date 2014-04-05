package com.example.tidepool_mobile;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class ContactFragment extends ListFragment {
	String[] contacts = {"Becky", "Bob"};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contacts));
	}

}
