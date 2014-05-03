package com.tidepool.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.example.tidepool_mobile.R;
import com.tidepool.entities.Data;

@SuppressLint("SimpleDateFormat")
public class DataPagerActivity extends FragmentActivity {
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private ViewPager mViewPager;
	private ArrayList<Data> datas;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);

		Bundle bundle = getIntent().getExtras();
		datas = (ArrayList<Data>)bundle.getSerializable("datas");

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
			Date time = datas.get(i).getTime();
			try {
				time = formatter.parse(formatter.format(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(time.equals(date))
				return i;
		}
		return -1;
	}


}
