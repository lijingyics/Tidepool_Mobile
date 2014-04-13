package com.tidepool.activities;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class AlertFragment extends ListFragment {
	String[] alerts = {"BG: 20", "BG: 200"};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, alerts));
	}

}
