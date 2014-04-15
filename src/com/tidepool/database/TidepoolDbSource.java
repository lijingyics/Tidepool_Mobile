package com.tidepool.database;

import java.util.ArrayList;

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

public class TidepoolDbSource {
	  // Database fields
	  private SQLiteDatabase db;
	  private TidepoolDbHelper dbHelper;
	  
	  private String[] userColumns = { 
			  FeedEntry.COLUMN_EMAIL, 
			  FeedEntry.COLUMN_USERNAME,
			  FeedEntry.COLUMN_PASSWORD,
			  FeedEntry.COLUMN_PHONE,
			  FeedEntry.COLUMN_BIRTH,
			  FeedEntry.COLUMN_GENDER,
			  FeedEntry.COLUMN_ROLE };
	  
	  private String[] dataColumns = { 
			  FeedEntry.COLUMN_TIME, 
			  FeedEntry.COLUMN_BG,
			  FeedEntry.COLUMN_INSULIN,
			  FeedEntry.COLUMN_UID };
	  
	  private String[] messageColumns = { 
			  FeedEntry.COLUMN_TALK, 
			  FeedEntry.COLUMN_UID };
	  
	  private String[] chatmessageColumns = { 
			  FeedEntry.COLUMN_DID, 
			  FeedEntry.COLUMN_MID };
	  
	  private String[] chatuserColumns = { 
			  FeedEntry.COLUMN_DID, 
			  FeedEntry.COLUMN_UID };
	  
	  private String[] friendsColumns = { 
			  FeedEntry.COLUMN_UID_1, 
			  FeedEntry.COLUMN_UID_2 };
	  
	  private String[] alertColumns = { 
			  FeedEntry.COLUMN_CONTENT };
	  
	  private String[] alertuserColumns = { 
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
	  
	  /**
	   * Insert user table
	   * @param user
	   */
	  public void insertUser(User user) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_EMAIL, user.getEmail());
		  values.put(FeedEntry.COLUMN_USERNAME, user.getUsername());
		  values.put(FeedEntry.COLUMN_PASSWORD, user.getPassword());
		  values.put(FeedEntry.COLUMN_PHONE, user.getPhoneNo());
		  values.put(FeedEntry.COLUMN_BIRTH, user.getDateOfBirth().toString());
		  values.put(FeedEntry.COLUMN_GENDER, user.getGender());
		  values.put(FeedEntry.COLUMN_ROLE, user.getRole());
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert data table
	   * @param data
	   */
	  public void insertData(Data data) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_TIME, data.getTime().toString());
		  values.put(FeedEntry.COLUMN_BG, data.getBg());
		  values.put(FeedEntry.COLUMN_INSULIN, data.getInsulin());
		  values.put(FeedEntry.COLUMN_UID, data.getUserId());
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_DATA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert message table
	   * @param message
	   */
	  public void insertMessage(Message message) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_TALK, message.getContent());
		  values.put(FeedEntry.COLUMN_UID, message.getUserId());
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_MESSAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert chat_message table
	   * @param dID is data ID
	   * @param mID is message ID
	   */
	  public void insertChatMessage(long dID, long mID) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_DID, dID);
		  values.put(FeedEntry.COLUMN_MID, mID);
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_CHAT_MESSAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert the chat_user table
	   * @param dID is data ID
	   * @param uID is user ID
	   */
	  public void insertChatUser(long dID, long uID) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_DID, dID);
		  values.put(FeedEntry.COLUMN_UID, uID);
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_CHAT_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert the friends table
	   * Being cautious that the relationship may already exist
	   * @param uID1
	   * @param uID2
	   */
	  public void insertFriends(long uID1, long uID2) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_UID_1, uID1);
		  values.put(FeedEntry.COLUMN_UID_2, uID2);
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_FRIENDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert the alert table
	   * @param alert
	   */
	  public void insertAlert(Alert alert) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_CONTENT, alert.getContent());
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_ALERT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  /**
	   * Insert alert_user table
	   * @param alert
	   * @param uID
	   */
	  public void insertAlertUser(Alert alert, long uID) {
		  // Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(FeedEntry.COLUMN_AID, alert.getId());
		  values.put(FeedEntry.COLUMN_UID, uID );
		  values.put(FeedEntry.COLUMN_STATUS, alert.getStatus());
		  
		  // Insert the new row, returning the primary key value of the new row
		  db.insertWithOnConflict(FeedEntry.TABLE_ALERT_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	  }
	  
	  
	  
	  
	  // Continue Here!
	  
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
