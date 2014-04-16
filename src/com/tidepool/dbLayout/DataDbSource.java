package com.tidepool.dbLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;
import com.tidepool.entities.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataDbSource {
	private TidepoolDbHelper dbHelper;
	
	private String[] dataColumns = { 
			  FeedEntry._ID,
			  FeedEntry.COLUMN_TIME, 
			  FeedEntry.COLUMN_BG,
			  FeedEntry.COLUMN_INSULIN,
			  FeedEntry.COLUMN_UID };

	public DataDbSource(Context context) {
		dbHelper = new TidepoolDbHelper(context);
	}
	
	public void close() { dbHelper.close(); }
	
	/**
	 * Insert data table
	 * @param data
	 * @return dID
	 */
	public long insertData(Data data) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
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
	 * Select one data
	 * @param id
	 * @return one data
	 */
	public Data getData(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
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
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
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
	 * Update data table
	 * @param data
	 * @return
	 */
	public int updateData(Data data) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_TIME, getDateTime(data.getTime()));
		values.put(FeedEntry.COLUMN_BG, data.getBg());
		values.put(FeedEntry.COLUMN_INSULIN, data.getInsulin());
		values.put(FeedEntry.COLUMN_UID, data.getUserId());

		// updating row
		return db.update(FeedEntry.TABLE_DATA, values, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(data.getId()) });
	}

	/**
	 * Delete Data by id
	 * @param id
	 */
	public void deleteData(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.delete(FeedEntry.TABLE_DATA, FeedEntry._ID + " = ?",
				new String[] { String.valueOf(id) });
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
