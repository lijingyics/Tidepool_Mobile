package com.tidepool.dbLayout;

import com.tidepool.dbLayout.DatabaseContract.FeedEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TidepoolDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tidepool.db";

    //Table Create Statement
    //user table
    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_USER + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY," 
    		+ FeedEntry.COLUMN_EMAIL + " TEXT NOT NULL UNIQUE,"
    		+ FeedEntry.COLUMN_USERNAME + " TEXT NOT NULL UNIQUE,"
    		+ FeedEntry.COLUMN_PASSWORD + " TEXT,"
    		+ FeedEntry.COLUMN_PHONE + " TEXT,"
            + FeedEntry.COLUMN_BIRTH + " DATE," 
    		+ FeedEntry.COLUMN_GENDER + " TEXT,"
            + FeedEntry.COLUMN_ROLE + " TEXT NOT NULL" + ")";
    
    //data table
    private static final String CREATE_TABLE_DATA = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_DATA + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY," 
    		+ FeedEntry.COLUMN_TIME + " DATETIME NOT NULL,"
    		+ FeedEntry.COLUMN_BG + " INTEGER NOT NULL,"
    		+ FeedEntry.COLUMN_INSULIN + " INTEGER NOT NULL,"
            + FeedEntry.COLUMN_UID + " INTEGER,"
    		+ " UNIQUE(" + FeedEntry.COLUMN_TIME + "," + FeedEntry.COLUMN_UID + "),"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_UID 
    			+ ") REFERENCES " + FeedEntry.TABLE_USER 
    			+ "(" + FeedEntry._ID + ")" 
    			+ " ON DELETE CASCADE" + ")";
    
    //chat table
    /*private static final String CREATE_TABLE_CHAT = "CREATE TABLE "
            + FeedEntry.TABLE_CHAT + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
            + FeedEntry.COLUMN_DID + " INTEGER FOREIGN KEY" + ")";*/
    
    //message table
    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_MESSAGE + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY," 
    		+ FeedEntry.COLUMN_TALK + " TEXT,"
            + FeedEntry.COLUMN_UID + " INTEGER,"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_UID 
				+ ") REFERENCES " + FeedEntry.TABLE_USER 
				+ "(" + FeedEntry._ID + ")" 
				+ " ON DELETE CASCADE" + ")";
    
    //chat_message table
    private static final String CREATE_TABLE_CHAT_MESSAGE = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_CHAT_MESSAGE + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_DID + " INTEGER,"
            + FeedEntry.COLUMN_MID + " INTEGER,"
    		+ " UNIQUE(" + FeedEntry.COLUMN_DID + "," + FeedEntry.COLUMN_MID + "),"
    		+ " FOREIGN KEY (" + FeedEntry.COLUMN_DID 
				+ ") REFERENCES " + FeedEntry.TABLE_DATA 
				+ "(" + FeedEntry._ID + ")" 
				+ " ON DELETE CASCADE,"
			+ " FOREIGN KEY (" + FeedEntry.COLUMN_MID 
    			+ ") REFERENCES " + FeedEntry.TABLE_MESSAGE 
    			+ "(" + FeedEntry._ID + ")" 
    			+ " ON DELETE CASCADE"+ ")";
    
    //chat_user table
    private static final String CREATE_TABLE_CHAT_USER = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_CHAT_USER + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_DID + " INTEGER,"
            + FeedEntry.COLUMN_UID + " INTEGER,"
            + " UNIQUE(" + FeedEntry.COLUMN_DID + "," + FeedEntry.COLUMN_UID + "),"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_DID 
    			+ ") REFERENCES " + FeedEntry.TABLE_DATA 
    			+ "(" + FeedEntry._ID + ")" 
    			+ " ON DELETE CASCADE,"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_UID 
				+ ") REFERENCES " + FeedEntry.TABLE_USER 
				+ "(" + FeedEntry._ID + ")" 
				+ " ON DELETE CASCADE" + ")";
    
    //friends table
    private static final String CREATE_TABLE_FRIENDS = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_FRIENDS + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_UID_1 + " INTEGER,"
            + FeedEntry.COLUMN_UID_2 + " INTEGER,"
            + " UNIQUE(" + FeedEntry.COLUMN_UID_1 + "," + FeedEntry.COLUMN_UID_2 + ")," 
            + " FOREIGN KEY (" + FeedEntry.COLUMN_UID_1 
    			+ ") REFERENCES " + FeedEntry.TABLE_USER 
    			+ "(" + FeedEntry._ID + ")" 
    			+ " ON DELETE CASCADE,"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_UID_2 
				+ ") REFERENCES " + FeedEntry.TABLE_USER 
				+ "(" + FeedEntry._ID + ")" 
				+ " ON DELETE CASCADE" + ")";
    
    //alert table
    private static final String CREATE_TABLE_ALERT = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_ALERT + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
            + FeedEntry.COLUMN_CONTENT + " TEXT,"
            + FeedEntry.COLUMN_DID + " INTEGER UNIQUE,"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_DID 
				+ ") REFERENCES " + FeedEntry.TABLE_DATA 
				+ "(" + FeedEntry._ID + ")" 
				+ " ON DELETE CASCADE" + ")";
    
    //alert_user table
    private static final String CREATE_TABLE_ALERT_USER = "CREATE TABLE IF NOT EXISTS "
            + FeedEntry.TABLE_ALERT_USER + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_AID + " INTEGER,"
            + FeedEntry.COLUMN_UID + " INTEGER,"
            + FeedEntry.COLUMN_STATUS + " TEXT,"
            + " UNIQUE(" + FeedEntry.COLUMN_AID + "," + FeedEntry.COLUMN_UID + "),"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_AID 
    			+ ") REFERENCES " + FeedEntry.TABLE_ALERT 
    			+ "(" + FeedEntry._ID + ")" 
    			+ " ON DELETE CASCADE,"
            + " FOREIGN KEY (" + FeedEntry.COLUMN_UID 
				+ ") REFERENCES " + FeedEntry.TABLE_USER 
				+ "(" + FeedEntry._ID + ")" 
				+ " ON DELETE CASCADE" + ")";
    
    public TidepoolDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_DATA);
        //db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_CHAT_MESSAGE);
        db.execSQL(CREATE_TABLE_CHAT_USER);
        db.execSQL(CREATE_TABLE_FRIENDS);
        db.execSQL(CREATE_TABLE_ALERT);
        db.execSQL(CREATE_TABLE_ALERT_USER);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop Table
    	db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_CHAT_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_CHAT_USER);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_ALERT_USER);
        
    	db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_ALERT);
        
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
