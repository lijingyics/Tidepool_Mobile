package com.tidepool.dbLayout;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
import com.tidepool.entities.Alert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AlertDbSource {
	private TidepoolDbHelper dbHelper;
	
	private String[] alertColumns = {
			  FeedEntry._ID,
			  FeedEntry.COLUMN_CONTENT };
	
	public AlertDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
	/**
	 * Insert the alert table
	 * @param alert
	 * @return aID
	 */
	public long insertAlert(Alert alert) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_CONTENT, alert.getContent());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_ALERT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

}
