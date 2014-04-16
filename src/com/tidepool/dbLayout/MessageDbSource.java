package com.tidepool.dbLayout;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
import com.tidepool.entities.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MessageDbSource {
	private TidepoolDbHelper dbHelper;

	public MessageDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
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
		return db.insertWithOnConflict(FeedEntry.TABLE_MESSAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
}
