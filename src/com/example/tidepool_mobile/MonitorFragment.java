package com.example.tidepool_mobile;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class MonitorFragment extends ListFragment {
	
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//        Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View V = inflater.inflate(R.layout.monitor_fragment, container, false);
//
//        return V;
//	}

	String[] patients = {"Becky", "Bob"};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		String show[] = null;
//		Bundle bundle = getArguments();
//		if(bundle == null)
//			show = show1;
//		else {
//			show = show2;
//			System.out.println(bundle.get("key"));
//		}
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, patients));
	}
	

}
