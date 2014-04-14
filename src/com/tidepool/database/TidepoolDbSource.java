package com.tidepool.database;

import java.util.ArrayList;

import com.tidepool.database.DatabaseContract.FeedEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TidepoolDbSource {
	  // Database fields
	  private SQLiteDatabase db;
	  private TidepoolDbHelper dbHelper;
	  private String[] allColumns = { 
			  FeedEntry.COLUMN_NAME_ENTRY_ID, 
			  FeedEntry.COLUMN_QUIZ_1,
			  FeedEntry.COLUMN_QUIZ_2,
			  FeedEntry.COLUMN_QUIZ_3,
			  FeedEntry.COLUMN_QUIZ_4,
			  FeedEntry.COLUMN_QUIZ_5 };
	  

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
	  
	  public void addScore(Student stud) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, stud.getStudentID());
		  values.put(FeedEntry.COLUMN_QUIZ_1, stud.getScores(0));
		  values.put(FeedEntry.COLUMN_QUIZ_2, stud.getScores(1));
		  values.put(FeedEntry.COLUMN_QUIZ_3, stud.getScores(2));
		  values.put(FeedEntry.COLUMN_QUIZ_4, stud.getScores(3));
		  values.put(FeedEntry.COLUMN_QUIZ_5, stud.getScores(4));
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	 /**
	 * Getting single student
	 * @param studID
	 * @return
	 */
	public Student getStudent(String studID) {
		  Cursor cursor = db.query(FeedEntry.TABLE_NAME, allColumns, 
				  FeedEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[] { studID }, null, null, null, null);
		  if (cursor != null)
			  cursor.moveToFirst();
 
		  Student student = new Student();
		  
		  int sID = Integer.parseInt(cursor.getString(0));
		  int score[] = new int[5];
		  for(int i=1; i<6; ++i)
			  score[i-1] = Integer.parseInt(cursor.getString(i));
		  
		  student.setter(sID, score);
		  return student;
	  }
	  
	  /**
	 * @return all students and score
	 */
	public ArrayList<Student> getAllStudents() {
		  ArrayList<Student> studList = new ArrayList<Student>();
		  // Select All Query
		  String selectQuery = "SELECT  * FROM " + FeedEntry.TABLE_NAME;
	 
		  Cursor cursor = db.rawQuery(selectQuery, null);
	 
		  // looping through all rows and adding to list
		  if (cursor.moveToFirst()) {
			  do {
				  Student stud = new Student();
				  int sID = Integer.parseInt(cursor.getString(1)); //First is normal ID
				  int score[] = new int[5];
				  for(int i=0; i<5; ++i)
					  score[i] = Integer.parseInt(cursor.getString(i+2));
				  
				  stud.setter(sID, score);
				  
				  // Adding student to list
				  studList.add(stud);
	        } while (cursor.moveToNext());
	    }
	 
	    return studList;
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
	 * Get the highest score of each quiz
	 * @return
	 */
	public int[] getHighScore() {
		  int high[] = new int[5];
		  
		  for(int i=0; i<5; i++)
			  high[i] = getHighScore(i+1);
		  
		  return high;
	  }
	  
	  private int getHighScore(int i) {
		  Cursor cursor = db.query(FeedEntry.TABLE_NAME, 
				  new String[] { "MAX(Q" + i + ")" }, 
				  null, null, null, null, null);
		  try {
			  cursor.moveToFirst();
			  return cursor.getInt(0);
		  } finally {
			  cursor.close();
		  }
	  }
	  
	/**
	 * Get the lowest score of each quiz
	 * @return
	 */
	public int[] getLowScore() {
		  int low[] = new int[5];
		  
		  for(int i=0; i<5; i++)
			  low[i] = getLowScore(i+1);
		  
		  return low;
	  }
	  
	  private int getLowScore(int i) {
		  Cursor cursor = db.query(FeedEntry.TABLE_NAME, 
				  new String[] { "MIN(Q" + i + ")" }, 
				  null, null, null, null, null);
		  try {
			  cursor.moveToFirst();
			  return cursor.getInt(0);
		  } finally {
			  cursor.close();
		  }
	  }

	/**
	 * Get the average score of each quiz
	 * @return
	 */
	public float[] getAvgScore() {
		  float avg[] = new float[5];
		  
		  for(int i=0; i<5; i++)
			  avg[i] = getAvgScore(i+1);
		  
		  return avg;
	  }
	  
	  private float getAvgScore(int i) {
		  Cursor cursor = db.query(FeedEntry.TABLE_NAME, 
				  new String[] { "AVG(Q" + i + ")" }, 
				  null, null, null, null, null);
		  try {
			  cursor.moveToFirst();
			  return cursor.getFloat(0);
		  } finally {
			  cursor.close();
		  }
	  }

}
