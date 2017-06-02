package com.alexeyool.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "datafor.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_SHIFT_TIME = "table_shft_time";
	public static final String UID = "_id";
	public static final String PROFILE_NAME = "profile_name";
	public static final String START_YEAR = "start_year";
	public static final String START_MONTH = "start_month";
	public static final String START_DAY = "start_day";
	public static final String START_HOUR = "start_hour";
	public static final String START_MINUTE = "start_minute";
	public static final String END_YEAR = "end_year";
	public static final String END_MONTH = "end_month";
	public static final String END_DAY = "end_day";
	public static final String END_HOUR = "end_hour";
	public static final String END_MINUTE = "end_minute";
	public static final String TEXT = "text";
	public static final String PAY_PER = "pay_per";
	public static final String AWARD = "award";
	public static final String FLOW = "flow";
	public static final String TYPE = "type";
	public static final String NOT_PAY_BREAK = "not_pay_break";
	public static final String TRAVEL_PER_DAY = "travel_per_day";
	public static final String DAY_OR_HOUR = "day_or_hour";
	public static final String PROCENT_PROCENT = "procrnt_procent";
	public static final String PROCENT_HOUR = "procent_hour";
	

	private static final String SQL_CREATE_ENTRIES_SHIFT = "CREATE TABLE "
			+ TABLE_SHIFT_TIME + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ PROFILE_NAME + " string, " + START_YEAR + " int, " + START_MONTH + " int, "
			+ START_DAY + " int, " + START_HOUR + " int, "+ START_MINUTE + " int, "
			+ END_YEAR + " int, " + END_MONTH + " int, " + END_DAY + " int, " + END_HOUR + " int, "
			+ END_MINUTE + " int, " + TEXT + " string, " + TYPE + " string, " + PAY_PER + " float, " 
			+ AWARD + " float, " + FLOW + " float, " + NOT_PAY_BREAK + " long, " + TRAVEL_PER_DAY + " float, " 
			+ DAY_OR_HOUR + " int, " + PROCENT_PROCENT + " string, " + PROCENT_HOUR + " string);";

	private static final String SQL_DELETE_ENTRIES_SHIFT = "DROP TABLE IF EXISTS "
			+ TABLE_SHIFT_TIME;

	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES_SHIFT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES_SHIFT);
		onCreate(db);
	}


}
