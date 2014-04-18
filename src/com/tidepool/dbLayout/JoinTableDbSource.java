package com.tidepool.dbLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
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

public class JoinTableDbSource {
	private TidepoolDbHelper dbHelper;
	
	  
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
	 
	private String[] alertuserColumns = {
		  FeedEntry._ID,
		  FeedEntry.COLUMN_AID, 
		  FeedEntry.COLUMN_UID,
		  FeedEntry.COLUMN_STATUS };
	  

	public JoinTableDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}

	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Insert chat_message table
	 * @param dID is data ID
	 * @param mID is message ID
	 * @return chat_message ID
	 */
	public long insertChatMessage(long dID, long mID) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_DID, dID);
		values.put(FeedEntry.COLUMN_MID, mID);
		
		// Insert the new row, returning the primary key value of the new row
		long id = db.insertWithOnConflict(FeedEntry.TABLE_CHAT_MESSAGE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		db.close();
		
		return id;
	}
	  
	/**
	 * Insert the chat_user table
	 * @param dID is data ID
	 * @param uID is user ID
	 * @return chat_user ID
	 */
	public long insertChatUser(long dID, long uID) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_DID, dID);
		values.put(FeedEntry.COLUMN_UID, uID);
		 
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_CHAT_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	  
	/**
	 * Insert the friends table
	 * Being cautious that the relationship may already exist
	 * @param uID1
	 * @param uID2
	 * @return friends ID
	 */
	public long insertFriends(long uID1, long uID2) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_UID_1, uID1);
		values.put(FeedEntry.COLUMN_UID_2, uID2);
		 
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_FRIENDS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	  
	/**
	 * Insert alert_user table
	 * @param alert
	 * @param uID
	 * @return alert_user ID
	 */
	public long insertAlertUser(Alert alert, long uID) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_AID, alert.getId());
		values.put(FeedEntry.COLUMN_UID, uID );
		values.put(FeedEntry.COLUMN_STATUS, alert.getStatus());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_ALERT_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public ArrayList<User> getFriends(long id) {
		ArrayList<User> friends = new ArrayList<User>();
		String query1 = "SELECT * FROM " + FeedEntry.TABLE_USER + " a " +
				"INNER JOIN " + FeedEntry.TABLE_FRIENDS + " b ON a._id = b.user_id1" +
				" WHERE b.user_id2 =?";
		
		String query2 = "SELECT * FROM table_user a " +
				"INNER JOIN table_friends b ON a.id = b.uid2" +
				" WHERE b.uid1 = id";
		
		
		
		return friends;
	}
	
	    
	/**
	 * Updating single contact
	 * @param stud
	 * @param id
	 * @return
	 */
	/*public int updateScore(Student stud, int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
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
	 }*/
	 
	/**
	 * Deleting single contact
	 * @param id
	 */
	/*public void deleteScore(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		  db.delete(FeedEntry.TABLE_NAME, FeedEntry._ID + " = ?",
            new String[] { String.valueOf(id) });
	}*/
	
	
	
	

}
