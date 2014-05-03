package com.tidepool.dbLayout;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
import com.tidepool.entities.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageDbSource {
	private TidepoolDbHelper dbHelper;
	
	private String[] messageColumns = { 
			  FeedEntry._ID,
			  FeedEntry.COLUMN_TALK, 
			  FeedEntry.COLUMN_UID };

	public MessageDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
	public void close() { dbHelper.close(); }
	
	/**
	 * Insert message table
	 * @param message
	 * @return mID
	 */
	public long insertMessage(Message message) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_TALK, message.getContent());
		values.put(FeedEntry.COLUMN_UID, message.getUserId());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_MESSAGE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	/**
	 * Select one message
	 * @param id
	 * @return one message
	 */
	public Message getMessage(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Message msg = new Message();
		
		Cursor cursor = db.query(FeedEntry.TABLE_MESSAGE, messageColumns, 
				FeedEntry._ID + "=?", 
				new String[] { String.valueOf(id) }, 
				null, null, null, null);
		
		if (!cursor.moveToFirst()) return null;
		
		msg.setId(cursor.getLong(0));
		msg.setContent(cursor.getString(1));
		msg.setUserId(cursor.getLong(2));
		
		return msg;
	}
	
	/**
	 * Update message table
	 * @param msg
	 * @return
	 */
	public int updateMessage(Message msg) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_TALK, msg.getContent());
		values.put(FeedEntry.COLUMN_UID, msg.getUserId());
		
		// updating row
		return db.update(FeedEntry.TABLE_MESSAGE, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(msg.getId()) });
	}

	/**
	 * Delete Message by id
	 * @param id
	 */
	public void deleteMessage(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.delete(FeedEntry.TABLE_MESSAGE, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}
}
