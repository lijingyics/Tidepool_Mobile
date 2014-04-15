package com.tidepool.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.tidepool.database.DatabaseContract.FeedEntry;
import com.tidepool.entities.Alert;
import com.tidepool.entities.Data;
import com.tidepool.entities.Message;
import com.tidepool.entities.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TidepoolDbSource {
	// Database fields
	private SQLiteDatabase db;
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
	  
	private String[] dataColumns = { 
		  FeedEntry._ID,
		  FeedEntry.COLUMN_TIME, 
		  FeedEntry.COLUMN_BG,
		  FeedEntry.COLUMN_INSULIN,
		  FeedEntry.COLUMN_UID };
	  
	private String[] messageColumns = { 
		  FeedEntry._ID,
		  FeedEntry.COLUMN_TALK, 
		  FeedEntry.COLUMN_UID };
	  
	private String[] chatmessageColumns = { 
		  FeedEntry._ID,
		  FeedEntry.COLUMN_DID, 
		  FeedEntry.COLUMN_MID };
	  
	private String[] chatuserColumns = { 
		  FeedEntry._ID,
		  FeedEntry.COLUMN_DID, 
		  FeedEntry.COLUMN_UID };
	  
	private String[] friendsColumns = { 
		  FeedEntry._ID,
		  FeedEntry.COLUMN_UID_1, 
		  FeedEntry.COLUMN_UID_2 };
	  
	private String[] alertColumns = {
		  FeedEntry._ID,
		  FeedEntry.COLUMN_CONTENT };
	 
	private String[] alertuserColumns = {
		  FeedEntry._ID,
		  FeedEntry.COLUMN_AID, 
		  FeedEntry.COLUMN_UID,
		  FeedEntry.COLUMN_STATUS };
	  

	public TidepoolDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}

	public void openW() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}
	  
	public void openR() throws SQLException {
		db = dbHelper.getReadableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	  
	// INSERT
	/**
	 * Insert user table
	 * @param user
	 * @return uID
	 */
	public long insertUser(User user) {
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
		return db.insertWithOnConflict(FeedEntry.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	  
	/**
	 * Insert data table
	 * @param data
	 * @return dID
	 */
	public long insertData(Data data) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_TIME, getDateTime(data.getTime()));
		values.put(FeedEntry.COLUMN_BG, data.getBg());
		values.put(FeedEntry.COLUMN_INSULIN, data.getInsulin());
		values.put(FeedEntry.COLUMN_UID, data.getUserId());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_DATA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	  
	/**
	 * Insert message table
	 * @param message
	 * @return mID
	 */
	public long insertMessage(Message message) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_TALK, message.getContent());
		values.put(FeedEntry.COLUMN_UID, message.getUserId());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_MESSAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	/**
	 * Insert chat_message table
	 * @param dID is data ID
	 * @param mID is message ID
	 * @return chat_message ID
	 */
	public long insertChatMessage(long dID, long mID) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_DID, dID);
		values.put(FeedEntry.COLUMN_MID, mID);
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_CHAT_MESSAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	  
	/**
	 * Insert the chat_user table
	 * @param dID is data ID
	 * @param uID is user ID
	 * @return chat_user ID
	 */
	public long insertChatUser(long dID, long uID) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_DID, dID);
		values.put(FeedEntry.COLUMN_UID, uID);
		 
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_CHAT_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	  
	/**
	 * Insert the friends table
	 * Being cautious that the relationship may already exist
	 * @param uID1
	 * @param uID2
	 * @return friends ID
	 */
	public long insertFriends(long uID1, long uID2) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_UID_1, uID1);
		values.put(FeedEntry.COLUMN_UID_2, uID2);
		 
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_FRIENDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	  
	/**
	 * Insert the alert table
	 * @param alert
	 * @return aID
	 */
	public long insertAlert(Alert alert) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_CONTENT, alert.getContent());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_ALERT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	  
	/**
	 * Insert alert_user table
	 * @param alert
	 * @param uID
	 * @return alert_user ID
	 */
	public long insertAlertUser(Alert alert, long uID) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_AID, alert.getId());
		values.put(FeedEntry.COLUMN_UID, uID );
		values.put(FeedEntry.COLUMN_STATUS, alert.getStatus());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_ALERT_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	// Select
	/**
	 * Get one user from the database
	 * @param email
	 * @return
	 */
	public User getUser(String email) {
		Cursor cursor = db.query(FeedEntry.TABLE_USER, userColumns, 
				FeedEntry.COLUMN_EMAIL + "=?", new String[] { email }, null, null, null, null);
		if (cursor!=null && cursor.getColumnCount()==8)
			cursor.moveToFirst();
		else {
			Log.d("User column num:", "" + cursor.getColumnCount());
			return null;
		}
		  
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
	 * Select one data
	 * @param id
	 * @return one data
	 */
	public Data getData(long id) {
		Cursor cursor = db.query(FeedEntry.TABLE_DATA, dataColumns, 
				FeedEntry._ID + "=?", 
				new String[] { String.valueOf(id) }, 
				null, null, null, null);
		if (cursor!=null)
			cursor.moveToFirst();
		else
			return null;
		
		Data data = new Data();
		data.setId(cursor.getLong(0));
		try {
			Date date;
			date = new SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.ENGLISH).
					parse(cursor.getString(1));
			data.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.setBg(cursor.getInt(2));
		data.setInsulin(cursor.getInt(3));
		data.setUserId(cursor.getLong(4));
			  
		return data;
	}
	
	/**
	 * Select all data by the user ID
	 * @param uID
	 * @return all data of one user
	 */
	public ArrayList<Data> getDataByUser(long uID) {
		Cursor cursor = db.query(FeedEntry.TABLE_DATA, dataColumns, 
				FeedEntry.COLUMN_UID + "=?", 
				new String[] { String.valueOf(uID) }, 
				null, null, FeedEntry.COLUMN_TIME, null);
		if (cursor!=null)
			cursor.moveToFirst();
		else
			return null;
		  
		ArrayList<Data> userData = new ArrayList<Data>();
		  
		do {
			Data data = new Data();
			data.setId(cursor.getLong(0));
			try {
				Date date;
				date = new SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.ENGLISH).
						parse(cursor.getString(1));
				data.setTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setBg(cursor.getInt(2));
			data.setInsulin(cursor.getInt(3));
			data.setUserId(uID);
			
			userData.add(data);
		} while(cursor.moveToNext());
		  
		return userData;
	}
	    
	/**
	 * Updating single contact
	 * @param stud
	 * @param id
	 * @return
	 */
	public int updateScore(Student stud, int id) {
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, stud.getStudentID());
		  values.put(FeedEntry.COLUMN_QUIZ_1, stud.getScores(0));
		  values.put(FeedEntry.COLUMN_QUIZ_2, stud.getScores(1));
		  values.put(FeedEntry.COLUMN_QUIZ_3, stud.getScores(2));
		  values.put(FeedEntry.COLUMN_QUIZ_4, stud.getScores(3));
		  values.put(FeedEntry.COLUMN_QUIZ_5, stud.getScores(4));

		  // updating row
		  return db.update(FeedEntry.TABLE_NAME, values, FeedEntry._ID + " = ?",
				  new String[] { String.valueOf(id) });
	 }
	 
	/**
	 * Deleting single contact
	 * @param id
	 */
	public void deleteScore(int id) {
		  db.delete(FeedEntry.TABLE_NAME, FeedEntry._ID + " = ?",
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
	
	/**
	 * @param date
	 * @return "yyyy-mm-dd hh:mm:ss"
	 */
	private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-mm-dd hh:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

}
