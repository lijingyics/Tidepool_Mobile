package com.tidepool.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;


public class LocationHelper {
	private double lat;
	private double lng;

	public LocationHelper(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		if(location != null) {
			lat = (location.getLatitude());
			lng = (location.getLongitude());
		}
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
}
