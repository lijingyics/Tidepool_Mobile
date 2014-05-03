package com.tidepool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.tidepool.entities.User;



public class UserSession {
	private static final String PREF_NAME = "MyPref";
	private static final String KEY = "user";
	
	public static void addUser(Context context, User user) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		Gson gson = new Gson();
		String userJson = gson.toJson(user);
		editor.putString(KEY, userJson);
		editor.commit();
	}
	
	public static boolean isAdded(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.contains(KEY);
	}
	
	public static void removeUser(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.remove(KEY);
		editor.commit();
	}
	
	public static User getUser(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String userJson = pref.getString(KEY, null);
		if(userJson == null) {
			Log.d("DEBUG", "user is null");
			return null;
		}
		Gson gson = new Gson();
		User user = gson.fromJson(userJson, User.class);
		return user;
	}
	
	public static void updateUser(Context context, User user) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.remove(KEY);
		Gson gson = new Gson();
		String userJson = gson.toJson(user);
		editor.putString(KEY, userJson);
		editor.commit();
	}

}
