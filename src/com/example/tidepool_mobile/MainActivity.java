package com.example.tidepool_mobile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends FragmentActivity {
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.tab1)),
				MonitorFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.tab2)),
				ChatFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(getString(R.string.tab3)),
				ContactFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(getString(R.string.tab4)),
				AlertFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab5").setIndicator(getString(R.string.tab5)),
				AccountFragment.class, null);
		
		mTabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
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
	                // userType = patient
	            break;
	        case R.id.parent:
	            if (checked)
	                // userType = parent
	            break;
	        case R.id.doctor:
	            if (checked)
	                // userType = doctor
	            break;
	    }
	}
}
