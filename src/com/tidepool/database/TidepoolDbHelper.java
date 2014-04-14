package com.tidepool.database;

import com.tidepool.database.DatabaseContract.FeedEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TidepoolDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tidepool.db";

    //Table Create Statement
    //user table
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + FeedEntry.TABLE_USER + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY," 
    		+ FeedEntry.COLUMN_EMAIL + " TEXT NOT NULL UNIQUE,"
    		+ FeedEntry.COLUMN_USERNAME + " TEXT NOT NULL UNIQUE,"
    		+ FeedEntry.COLUMN_PASSWORD + " TEXT,"
    		+ FeedEntry.COLUMN_PHONE + " TEXT,"
            + FeedEntry.COLUMN_BIRTH + " TEXT," 
    		+ FeedEntry.COLUMN_GENDER + " TEXT,"
            + FeedEntry.COLUMN_ROLE + " TEXT NOT NULL" + ")";
    
    //data table
    private static final String CREATE_TABLE_DATA = "CREATE TABLE "
            + FeedEntry.TABLE_DATA + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY," 
    		+ FeedEntry.COLUMN_TIME + " TEXT NOT NULL,"
    		+ FeedEntry.COLUMN_BG + " INTEGER NOT NULL,"
    		+ FeedEntry.COLUMN_INSULIN + " INTEGER NOT NULL,"
            + FeedEntry.COLUMN_UID + " INTEGER FOREIGN KEY" + ")";
    
    //chat table
    private static final String CREATE_TABLE_CHAT = "CREATE TABLE "
            + FeedEntry.TABLE_CHAT + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
            + FeedEntry.COLUMN_DID + " INTEGER FOREIGN KEY" + ")";
    
    //message table
    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE "
            + FeedEntry.TABLE_MESSAGE + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY," 
    		+ FeedEntry.COLUMN_TALK + " TEXT,"
            + FeedEntry.COLUMN_UID + " INTEGER FOREIGN KEY" + ")";
    
    //chat_message table
    private static final String CREATE_TABLE_CHAT_MESSAGE = "CREATE TABLE "
            + FeedEntry.TABLE_CHAT_MESSAGE + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_CID + " INTEGER FOREIGN KEY"
            + FeedEntry.COLUMN_MID + " INTEGER FOREIGN KEY" + ")";
    
    //chat_user table
    private static final String CREATE_TABLE_CHAT_USER = "CREATE TABLE "
            + FeedEntry.TABLE_CHAT_USER + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_CID + " INTEGER FOREIGN KEY"
            + FeedEntry.COLUMN_UID + " INTEGER FOREIGN KEY" + ")";
    
    //friends table
    private static final String CREATE_TABLE_FRIENDS = "CREATE TABLE "
            + FeedEntry.TABLE_FRIENDS + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_UID_1 + " INTEGER FOREIGN KEY"
            + FeedEntry.COLUMN_UID_2 + " INTEGER FOREIGN KEY" + ")";
    
    //alert table
    private static final String CREATE_TABLE_ALERT = "CREATE TABLE "
            + FeedEntry.TABLE_ALERT + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
            + FeedEntry.COLUMN_CONTENT + " TEXT" + ")";
    
    //alert_user table
    private static final String CREATE_TABLE_ALERT_USER = "CREATE TABLE "
            + FeedEntry.TABLE_ALERT_USER + "(" 
    		+ FeedEntry._ID + " INTEGER PRIMARY KEY,"
    		+ FeedEntry.COLUMN_AID + " INTEGER FOREIGN KEY"
            + FeedEntry.COLUMN_UID + " INTEGER FOREIGN KEY"
            + FeedEntry.COLUMN_STATUS + " TEXT" + ")";
    
    public TidepoolDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_DATA);
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_CHAT_MESSAGE);
        db.execSQL(CREATE_TABLE_CHAT_USER);
        db.execSQL(CREATE_TABLE_FRIENDS);
        db.execSQL(CREATE_TABLE_ALERT);
        db.execSQL(CREATE_TABLE_ALERT_USER);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop Table
    	db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_CHAT_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_CHAT_USER);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_ALERT);
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_ALERT_USER);
        
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
