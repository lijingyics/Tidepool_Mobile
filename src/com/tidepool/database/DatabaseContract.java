package com.tidepool.database;

import android.provider.BaseColumns;

public final class DatabaseContract {
	
	public DatabaseContract() {}
	
	public static abstract class FeedEntry implements BaseColumns {
		//table name
        public static final String TABLE_USER = "user";
        public static final String TABLE_DATA = "data";
        //public static final String TABLE_CHAT = "chat";
        public static final String TABLE_MESSAGE = "message";
        public static final String TABLE_CHAT_MESSAGE = "chat_message";
        public static final String TABLE_CHAT_USER = "chat_user";
        public static final String TABLE_FRIENDS = "friends";
        public static final String TABLE_ALERT = "alert";
        public static final String TABLE_ALERT_USER = "alert_user";
        
        //user table - column names
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_BIRTH = "date_of_birth";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_ROLE = "role";
        
        //data table - column names
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_BG = "blood_glucose";
        public static final String COLUMN_INSULIN = "insulin";
        public static final String COLUMN_UID = "user_id";
        
        //chat table - column names
        public static final String COLUMN_DID = "data_id";

        //message table - column names
        public static final String COLUMN_TALK = "talk";
        //public static final String COLUMN_CID = "chat_id";
        
        //chat_message table - column names
        public static final String COLUMN_MID = "message_id";
        
        //friends table - column names
        public static final String COLUMN_UID_1 = "user_id1";
        public static final String COLUMN_UID_2 = "user_id2";
        
        //alert table - column names
        public static final String COLUMN_CONTENT = "content";
        
        //alert_user table - column names
        public static final String COLUMN_AID = "alert_id";
        public static final String COLUMN_STATUS = "alert_status";
        
    }
}
