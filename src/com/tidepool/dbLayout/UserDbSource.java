package com.tidepool.dbLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
import com.tidepool.entities.User;

public class UserDbSource {
	private TidepoolDbHelper dbHelper;
	  
	private String[] userColumns = {
		  FeedEntry._ID,
		  FeedEntry.COLUMN_EMAIL, 
		  FeedEntry.COLUMN_USERNAME,
		  FeedEntry.COLUMN_PASSWORD,
		  FeedEntry.COLUMN_PHONE,
		  FeedEntry.COLUMN_BIRTH,
		  FeedEntry.COLUMN_GENDER,
		  FeedEntry.COLUMN_ROLE };
	
	public UserDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
	public void close() { dbHelper.close();}

	/**
	 * Insert user table
	 * @param user
	 * @return uID
	 */
	public long insertUser(User user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_EMAIL, user.getEmail());
		values.put(FeedEntry.COLUMN_USERNAME, user.getUsername());
		values.put(FeedEntry.COLUMN_PASSWORD, user.getPassword());
		values.put(FeedEntry.COLUMN_PHONE, user.getPhoneNo());
		values.put(FeedEntry.COLUMN_BIRTH, getDate(user.getDateOfBirth()));
		values.put(FeedEntry.COLUMN_GENDER, user.getGender());
		values.put(FeedEntry.COLUMN_ROLE, user.getRole());
		  
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	/**
	 * Get one user from the database
	 * @param email
	 * @return
	 */
	public User getUser(String email) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(FeedEntry.TABLE_USER, userColumns, 
				FeedEntry.COLUMN_EMAIL + "=?", new String[] { email }, null, null, null, null);
		
		if ( !cursor.moveToFirst())
			return null;
		  
		User user = new User();
		user.setId(cursor.getLong(0));
		user.setEmail(cursor.getString(1));
		user.setUsername(cursor.getString(2));
		user.setPassword(cursor.getString(3));
		user.setPhoneNo(cursor.getString(4));
		try {
			Date date;
			date = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(cursor.getString(5));
			user.setDateOfBirth(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	  
		user.setGender(cursor.getString(6));
		user.setRole(cursor.getString(7));
		  
		return user;
	}
	
	/**
	 * Used for debug
	 * @return
	 */
	public ArrayList<User> getAllUser() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<User> userList = new ArrayList<User>();
		  // Select All Query
		String selectQuery = "SELECT  * FROM " + FeedEntry.TABLE_USER;
	 
		Cursor cursor = db.rawQuery(selectQuery, null);
		Log.d("Total user: ", "" + cursor.getCount());
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				User user = new User();
				user.setId(cursor.getLong(0));
				user.setEmail(cursor.getString(1));
				user.setUsername(cursor.getString(2));
				user.setPassword(cursor.getString(3));
				user.setRole(cursor.getString(7));  

				//For debug
				Log.d("User: ", user.getId() + " " + user.getEmail() + " " + user.getRole());
				
				// Adding user to list
				userList.add(user);
	        } while (cursor.moveToNext());
	    }
	 
	    return userList;
	}
	
	/**
	 * Update user table
	 * @param user
	 * @return
	 */
	public int updateUser(User user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_EMAIL, user.getEmail());
		values.put(FeedEntry.COLUMN_USERNAME, user.getUsername());
		values.put(FeedEntry.COLUMN_PASSWORD, user.getPassword());
		values.put(FeedEntry.COLUMN_PHONE, user.getPhoneNo());
		values.put(FeedEntry.COLUMN_BIRTH, getDate(user.getDateOfBirth()));
		values.put(FeedEntry.COLUMN_GENDER, user.getGender());
		values.put(FeedEntry.COLUMN_ROLE, user.getRole());
		
		// updating row
		return db.update(FeedEntry.TABLE_USER, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(user.getId()) });
	}

	/**
	 * Delete User by id
	 * @param id
	 */
	public void deleteUser(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.delete(FeedEntry.TABLE_USER, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}
	
	/**
	 * @param date
	 * @return "yyyy-mm-dd"
	 */
	private String getDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-mm-dd", Locale.getDefault());
        return dateFormat.format(date);
    }
}
