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
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JoinTableDbSource {
	private TidepoolDbHelper dbHelper;
	
	public JoinTableDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
	public void updateTable(int oldVersion, int newVersion) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	    dbHelper.onUpgrade(db, oldVersion, newVersion);
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
	
	/**
	 * Get friends from the friends table
	 * Noted the return only includes user id, email, 
	 * username, phone number and role
	 * @param id
	 * @return friends list
	 */
	public ArrayList<User> getFriends(long id) {
		ArrayList<User> friends = new ArrayList<User>();
		
		friends.addAll( getFriends(id, true) );
		friends.addAll( getFriends(id, false) );
		
		return friends;
	}
	
	/**
	 * The method help in getting friends on both side
	 * @param id
	 * @param left
	 * @return
	 */
	private ArrayList<User> getFriends(long id, boolean left) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<User> friends = new ArrayList<User>();
		String query1 = "SELECT * FROM " + FeedEntry.TABLE_USER + " a " +
				"INNER JOIN " + FeedEntry.TABLE_FRIENDS + " b ON a._id = b.user_id1" +
				" WHERE b.user_id2 =?";
		String query2 = "SELECT * FROM " + FeedEntry.TABLE_USER + " a " +
				"INNER JOIN " + FeedEntry.TABLE_FRIENDS + " b ON a._id = b.user_id2" +
				" WHERE b.user_id1 =?";
		
		Cursor cursor;
		if(left) 
			cursor = db.rawQuery(query1, new String[] { String.valueOf(id) });
		else
			cursor = db.rawQuery(query2, new String[] { String.valueOf(id) });
		
		
		if(!cursor.moveToFirst()) return friends;
		
		do {
			User user = new User();
			user.setId(cursor.getLong(0));
			user.setEmail(cursor.getString(1));
			user.setUsername(cursor.getString(2));
			user.setPhoneNo(cursor.getString(4));
			user.setRole(cursor.getString(7));  

			//For debug
			Log.d("Debug Friends: ", user.getId() + " " + user.getEmail() + " " + user.getRole());
				
			// Adding user to list
			friends.add(user);
	    } while (cursor.moveToNext());
	    
		return friends;
	}
	
	/**
	 * Find all messages in the chat room by data id
	 * @param id
	 * @return all messages
	 */
	public ArrayList<Message> getMessage(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<Message> msgs = new ArrayList<Message>();
		String query = "SELECT * FROM " + FeedEntry.TABLE_MESSAGE + " a " +
				"INNER JOIN " + FeedEntry.TABLE_CHAT_MESSAGE + " b ON a._id = b.message_id" +
				" WHERE b.data_id =?";
		
		Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(id) });
		if(!cursor.moveToFirst()) return msgs;
		
		do {
			Message msg = new Message();
			msg.setId(cursor.getLong(0));
			msg.setContent(cursor.getString(1));
			msg.setUserId(cursor.getLong(2));
			
			//For debug
			Log.d("Debug Message: ", msg.getId() + " " + msg.getContent() + " " + msg.getUserId());
				
			// Adding user to list
			msgs.add(msg);
	    } while (cursor.moveToNext());
	    
		return msgs;
	}
	
	/**
	 * Find all chat rooms of a user by user_id
	 * @param id
	 * @return data
	 */
	public ArrayList<Data> getChatRoom(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<Data> rooms = new ArrayList<Data>();
		String query = "SELECT * FROM " + FeedEntry.TABLE_DATA + " a " +
				"INNER JOIN " + FeedEntry.TABLE_CHAT_USER + " b ON a._id = b.data_id" +
				" WHERE b.user_id =?";
		
		Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(id) });
		if(!cursor.moveToFirst()) return rooms;
		
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
			data.setUserId(cursor.getLong(4));
			
			//For debug
			Log.d("Debug Chat Room: ", data.getId() + " " + 
					data.getTime() + " " + 
					data.getBg());
				
			// Adding data of the room to list
			rooms.add(data);
	    } while (cursor.moveToNext());
	    
		return rooms;
	}
	

	/**
	 * Find alert by user id
	 * @param id
	 * @return alert list
	 */
	public ArrayList<Alert> getAlertUser(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<Alert> alertList = new ArrayList<Alert>();
		String query = "SELECT * FROM " + FeedEntry.TABLE_ALERT + " a " +
				"INNER JOIN " + FeedEntry.TABLE_ALERT_USER + " b ON a._id = b.alert_id" +
				" WHERE b.user_id =?";
		
		Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(id) });
		if(!cursor.moveToFirst()) return alertList;
		
		do {
			Alert alert = new Alert();
			alert.setId(cursor.getLong(0));
			alert.setContent(cursor.getString(1));
			
			//For debug
			Log.d("Debug Alert: ", alert.getId() + " " + alert.getContent());
				
			// Adding user to list
			alertList.add(alert);
	    } while (cursor.moveToNext());
	    
		return alertList;
	}
	
	/**
	 * Updating message in chat room
	 * @param mID
	 * @param dID
	 * @param id
	 * @return
	 */
	public int updateChatMessage(long dID, long mID, int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_DID, dID);
		values.put(FeedEntry.COLUMN_MID, mID);
		
		// updating row
		return db.update(FeedEntry.TABLE_CHAT_MESSAGE, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	/**
	 * Update the user and its chat room
	 * @param dID
	 * @param uID
	 * @param id
	 * @return
	 */
	public int updateChatUser(long dID, long uID, int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_DID, dID);
		values.put(FeedEntry.COLUMN_UID, uID);
		
		// updating row
		return db.update(FeedEntry.TABLE_CHAT_USER, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}
	
	/**
	 * Update friends table
	 * @param uID1
	 * @param uID2
	 * @param id
	 * @return
	 */
	public int updateFriends(long uID1, long uID2, int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_UID_1, uID1);
		values.put(FeedEntry.COLUMN_UID_2, uID2);
		
		// updating row
		return db.update(FeedEntry.TABLE_FRIENDS, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}
	
	/**
	 * Update the alert user table
	 * @param aID
	 * @param uID
	 * @param id
	 * @return
	 */
	public int updateAlertUser(long aID, long uID, int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_AID, aID);
		values.put(FeedEntry.COLUMN_UID, uID);
		
		// updating row
		return db.update(FeedEntry.TABLE_ALERT_USER, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}
	
	/**
	 * Deleting single relation in chat_message
	 * @param id
	 */
	public void deleteChatMessage(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		  db.delete(FeedEntry.TABLE_CHAT_MESSAGE, FeedEntry._ID + " = ?",
            new String[] { String.valueOf(id) });
	}
	
	/**
	 * Deleting single relation in chat_user
	 * @param id
	 */
	public void deleteChatUser(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		  db.delete(FeedEntry.TABLE_CHAT_USER, FeedEntry._ID + " = ?",
            new String[] { String.valueOf(id) });
	}
	
	/**
	 * Deleting single relation in friends table
	 * @param id
	 */
	public void deleteFriends(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		  db.delete(FeedEntry.TABLE_FRIENDS, FeedEntry._ID + " = ?",
            new String[] { String.valueOf(id) });
	}
	
	/**
	 * Deleting single relation in alert_user table
	 * @param id
	 */
	public void deleteAlertUser(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		  db.delete(FeedEntry.TABLE_ALERT_USER, FeedEntry._ID + " = ?",
            new String[] { String.valueOf(id) });
	}

}