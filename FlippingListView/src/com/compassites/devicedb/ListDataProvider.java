package com.compassites.devicedb;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class ListDataProvider extends ContentProvider {
	
	private static final String DATABASE_NAME = "FlipListView";
	private static final String EVENTS_TABLE = "listview";
	private static final int DATABASE_VERSION = 4;
	private static final String  AUTHORITY = "com.compassites.devicedb.ListDataProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/listview");
	public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/listview/");
	private static final UriMatcher uriMatcher;
	
	public static final String ID = "_id";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String ZIP = "zip";
	public static final String RECORD_DATE = "record_date";
	private static final HashMap<String, String> mMap;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            createTables(db);
        }
        

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                              int newVersion) 
        {
            Log.w("CalendarProvider", "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS events");
            onCreate(db);
        }
        
    	private void createTables(SQLiteDatabase db){
    		db.execSQL("CREATE TABLE " + EVENTS_TABLE + "(" + ID + " integer primary key autoincrement, " +
    				LONGITUDE + " TEXT, " + LATITUDE + " TEXT, " + ZIP + " TEXT, "
    				+ RECORD_DATE + " INTEGER);");
    	}
    }
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		int num = uriMatcher.match(uri);
		if(num == 1){
			count = db.delete(EVENTS_TABLE, selection,selectionArgs);
		}else if(num == 2){
			String id = uri.getPathSegments().get(1);
			count = db.delete(EVENTS_TABLE, ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + 
		               selection + ')' : ""), 
		               selectionArgs);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = db.insert(EVENTS_TABLE,null, values);
		Uri _uri = null;
		if(rowID > 0){
			_uri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE,rowID);
			getContext().getContentResolver().notifyChange(uri,null);
			
		}else{
			throw new SQLException("Failed to insert row into " + uri);
		}
		return _uri;
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DBHelper = new DatabaseHelper(context);
		db = DBHelper.getWritableDatabase();
		return (db == null)? false:true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	    sqlBuilder.setTables(EVENTS_TABLE);

	    if(uriMatcher.match(uri) == 1){
	    	sqlBuilder.setProjectionMap(mMap);
	    }else if(uriMatcher.match(uri) == 2){
	    	sqlBuilder.setProjectionMap(mMap);
	    	sqlBuilder.appendWhere(ID + "=?");
	    	selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,new String[] {uri.getLastPathSegment()});
	    }else if(uriMatcher.match(uri) == 3){
	    	sqlBuilder.setProjectionMap(mMap);
	    	sqlBuilder.appendWhere(RECORD_DATE + ">=? OR ");
	    	List<String> list = uri.getPathSegments();
	    	String start = list.get(1);
	    	String end = list.get(2);
	    	selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,new String[] {start,end});
	    }
	    if(sortOrder == null || sortOrder == "")
	    	sortOrder = RECORD_DATE + " COLLATE LOCALIZED ASC";
		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs,null,null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		int num = uriMatcher.match(uri);
		if(num == 1){
			count = db.update(EVENTS_TABLE, values, selection, selectionArgs);
		}else if(num == 2){
			count = db.update(EVENTS_TABLE, values, ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + 
	                  selection + ')' : ""), 
	                  selectionArgs);
		}else{
			throw new IllegalArgumentException(
		            "Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY,EVENTS_TABLE,1);
		uriMatcher.addURI(AUTHORITY,EVENTS_TABLE + "/#",2);
		uriMatcher.addURI(AUTHORITY, EVENTS_TABLE+"/#/#", 3);
		
		mMap = new HashMap<String, String>();
		mMap.put(ID, ID);
		mMap.put(LONGITUDE, LONGITUDE);
		mMap.put(LATITUDE, LATITUDE);
		mMap.put(ZIP, ZIP);
		mMap.put(RECORD_DATE, RECORD_DATE);
	}

}
