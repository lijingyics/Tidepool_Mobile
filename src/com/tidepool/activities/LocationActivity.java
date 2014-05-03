package com.tidepool.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.tidepool_mobile.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		// Get a handle to the Map Fragment
		GoogleMap map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		Bundle bundle = getIntent().getExtras();
		double lat = bundle.getDouble("lat");
		double lng = bundle.getDouble("lng");
		LatLng location = new LatLng(lat, lng);

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

		map.addMarker(new MarkerOptions()
		.title("Current Location")
		.position(location));
	}

}
