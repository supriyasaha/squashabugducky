package com.squashabug.app.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.squashabug.app.DebugApplication;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE = "debug.db";
	public static final int DATABASE_VERSION = 1;


	private static final String EVENT_LOG_TABLE =
			"CREATE TABLE IF NOT EXISTS " + DebugDatabase.EventsEntryTable.TABLE_NAME + " ("
					+ DebugDatabase.EventsEntryTable.ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
					+ DebugDatabase.EventsEntryTable.PROPERTIES + " TEXT UNIQUE,"
					+ DebugDatabase.EventsEntryTable.EVENT_NAME + " TEXT,"
					+ DebugDatabase.EventsEntryTable.PACKAGE_NAME + " VARCHAR,"
					+ DebugDatabase.EventsEntryTable.TIMESTAMP + " TIMESTAMP"
					+ ")";

	private static final String API_LOG_TABLE =
			"CREATE TABLE IF NOT EXISTS " + DebugDatabase.ApiLogTable.TABLE_NAME + " ("
					+ DebugDatabase.ApiLogTable.ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
					+ DebugDatabase.ApiLogTable.PACKAGE_NAME + " VARCHAR,"
					+ DebugDatabase.ApiLogTable.API_LOG + " TEXT,"
					+ DebugDatabase.ApiLogTable.TIMESTAMP + " TIMESTAMP" + ")";


	private static DatabaseHelper mDatabaseHelper = new DatabaseHelper(DebugApplication.getAppContext());
	private SQLiteDatabase mDatabaseManipulator;


	public DatabaseHelper(Context context) {
		super(context, DATABASE, null, DATABASE_VERSION);
	}

	public static synchronized DatabaseHelper getInstance() {
		return mDatabaseHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(EVENT_LOG_TABLE);
		db.execSQL(API_LOG_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public synchronized SQLiteDatabase getDatabseManipulater() {
		if (mDatabaseManipulator == null) {
			mDatabaseManipulator = getWritableDatabase();
		}
		return mDatabaseManipulator;
	}
}
