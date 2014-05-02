package com.tidepool.activities;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;

public class DataPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Data> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);

		Bundle bundle = getIntent().getExtras();
		datas = (ArrayList<Data>)bundle.getSerializable("datas");
		Log.d("DEBUG", String.valueOf(datas.size()));

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return datas.size();
			}
			@Override
			public Fragment getItem(int pos) {
				Data data = datas.get(pos);
				return DataFragment.newInstance(data);
			}
		}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public ViewPager getViewPager() {
		if (mViewPager == null) {
			mViewPager = new ViewPager(this);
		}
		return mViewPager;
	}
	
	public int getDataIndex(Date date) {
		for(int i = 0; i < datas.size(); i++) {
			if(datas.get(i).getTime().equals(date))
				return i;
		}
		return -1;
	}


}
