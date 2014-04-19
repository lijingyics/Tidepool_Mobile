package com.tidepool.dbLayout;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
import com.tidepool.entities.Alert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlertDbSource {
	private TidepoolDbHelper dbHelper;
	
	private String[] alertColumns = {
			  FeedEntry._ID,
			  FeedEntry.COLUMN_CONTENT,
			  FeedEntry.COLUMN_DID };
	
	public AlertDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
	public void close() { dbHelper.close(); }
	
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
		values.put(FeedEntry.COLUMN_DID, alert.getDataId());
		
		// Insert the new row, returning the primary key value of the new row
		return db.insertWithOnConflict(FeedEntry.TABLE_ALERT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	/**
	 * Select one alert
	 * @param id
	 * @return one alert
	 */
	public Alert getAlert(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(FeedEntry.TABLE_ALERT, alertColumns, 
				FeedEntry._ID + "=?", 
				new String[] { String.valueOf(id) }, 
				null, null, null, null);
		if (!cursor.moveToFirst())
			return null;
		
		Alert alert = new Alert();
		alert.setId(cursor.getLong(0));
		alert.setContent(cursor.getString(1));
		alert.setDataId(cursor.getLong(2));
		
		return alert;
	}
	
	/**
	 * Update alert table
	 * @param alert
	 * @return
	 */
	public int updateAlert(Alert alert) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_CONTENT, alert.getContent());
		values.put(FeedEntry.COLUMN_DID, alert.getDataId());
		
		// updating row
		return db.update(FeedEntry.TABLE_ALERT, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(alert.getId()) });
	}

	/**
	 * Delete Alert by id
	 * @param id
	 */
	public void deleteAlert(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.delete(FeedEntry.TABLE_ALERT, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
	}

}
