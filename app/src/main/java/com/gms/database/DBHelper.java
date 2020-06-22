package com.gms.database;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import android.util.Log;

import androidx.annotation.Keep;


public class DBHelper extends SQLiteOpenHelper {
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "GMS.db";

   
	public DBHelper(Context ctx) {
		super(ctx, DB_NAME, null, DB_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
          createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public void createTables(SQLiteDatabase database) {



		//Users Table
		String user_table_sql = "create table " + Database.USERS_TABLE_NAME + "( " +
				Database.ROW_ID + " integer  primary key autoincrement," +
				Database.FULLNAME + " TEXT," +
				Database.MEMBERCODE + " TEXT," +
				Database.NATIONALID  + " TEXT," +
				Database.EMAIL  + " TEXT," +
				Database.MOBILENO  + " TEXT," +
				Database.PASSWORD  + " TEXT," +
				Database.VERYCODE  + " TEXT," +
				Database.CLOUDID + " TEXT)";


		//Minute Table
		String minute_table_sql = "create table " + Database.MINUTE_TABLE_NAME + "( " +
				Database.ROW_ID + " integer  primary key autoincrement," +
				Database.MIN_NAME + " TEXT," +
				Database.MIN_DATE + " TEXT," +
				Database.MIN_TIME + " TEXT," +
				Database.CLOUDID + " TEXT)";


		//Event Table
		String event_table_sql = "create table " + Database.EVENT_TABLE_NAME + "( " +
				Database.ROW_ID + " integer  primary key autoincrement," +
				Database.EVENT_NAME + " TEXT," +
				Database.EVENT_DATE + " TEXT," +
				Database.EVENT_TIME + " TEXT," +
				Database.CLOUDID + " TEXT)";




        String DefaultUser = "INSERT INTO " + Database.USERS_TABLE_NAME + " ("
                + Database.ROW_ID + ", "
                + Database.FULLNAME + ", "
                + Database.MEMBERCODE + ", "
				+ Database.NATIONALID + ", "
				+ Database.EMAIL + ", "
				+ Database.MOBILENO + ", "
				+ Database.PASSWORD + ", "
				+ Database.VERYCODE + ", "
                + Database.CLOUDID + ") Values ('1','Michael Nyagwachi','1234','30050634','orengemichael12@gmail.com','0715881439','1234','0000','0')";






		try {
			database.execSQL(user_table_sql);
			database.execSQL(minute_table_sql);
			database.execSQL(event_table_sql);
			// These are Default Values
			database.execSQL(DefaultUser);











        }
		catch(Exception ex) {
			Log.d("GMSDB", "Error in DBHelper.onCreate() : " + ex.getMessage());
		}
	}
	public Cursor fetchMemberCode(String s_membercode) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.USERS_TABLE_NAME,
				new String[]{Database.ROW_ID,Database.MEMBERCODE},
				"membercode" + "='" + s_membercode + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	public Cursor fetchNationalID(String s_nationalid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.USERS_TABLE_NAME,
				new String[]{Database.ROW_ID,Database.NATIONALID},
				"nationalid" + "='" + s_nationalid + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	public Cursor fetchMobileNo(String s_mobileno) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.USERS_TABLE_NAME,
				new String[]{Database.ROW_ID,Database.NATIONALID},
				"mobileno" + "='" + s_mobileno + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	public Cursor fetchUsername(String username) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.USERS_TABLE_NAME,
				new String[]{Database.ROW_ID,Database.MOBILENO},
				"idno" + "='" + username + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	/**
	 * cursor for viewing Given Name
	 */
	public Cursor getGivenName(String mobileno) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = new String[] {Database.ROW_ID,Database.FULLNAME};
		Cursor c = db.query(Database.USERS_TABLE_NAME, allColumns, "mobileno" + "='" + mobileno + "'", null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	public boolean UserLogin(String username, String password)  {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + Database.USERS_TABLE_NAME
				+ " WHERE mobileno=? COLLATE NOCASE AND password=?", new String[]{username, password});
		if (mCursor != null) {
			if (mCursor.getCount() > 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * cursor for viewing password
	 */
	public Cursor getPassword(String username) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = new String[] {Database.PASSWORD,Database.MOBILENO};
		Cursor c = db.query(Database.USERS_TABLE_NAME, allColumns, "mobileno COLLATE NOCASE" + "='" + username + "'", null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS///////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////

	public Cursor SearchMember(String Name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor myCursor = db.query(Database.USERS_TABLE_NAME,
				new String[] { Database.ROW_ID,Database.MEMBERCODE,Database.FULLNAME , Database.NATIONALID, Database.MOBILENO },
				Database.FULLNAME + " LIKE ? OR "+Database.NATIONALID + " LIKE ? OR "+Database.MOBILENO + " LIKE ?",
				new String[] {"%"+  Name+ "%" ,"%"+  Name+ "%","%"+  Name+ "%"},null,null,Database.FULLNAME +" COLLATE NOCASE ASC");

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		db.close();
		return myCursor;
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public Cursor SearchSpecificMember(String Name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor myCursor = db.query(true, Database.USERS_TABLE_NAME, null, Database.FULLNAME + "='" + Name + "' OR " + Database.NATIONALID + "='" + Name + "' OR " +Database.MOBILENO + "='" + Name + "' ", null, null, null, Database.FULLNAME + " ASC ", null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		db.close();
		return myCursor;
	}
	/**
     * cursor for viewing password
     */

	/////////////////////////////////////////////////////////////////////
	//USERS FUNCTIONS///////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	public long AddUsers(String s_fullname, String s_membercode, String s_nationalid,
                         String s_email, String s_mobileno, String s_password) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.FULLNAME, s_fullname);
		initialValues.put(Database.MEMBERCODE, s_membercode);
		initialValues.put(Database.NATIONALID, s_nationalid);
		initialValues.put(Database.EMAIL, s_email);
		initialValues.put(Database.MOBILENO, s_mobileno);
		initialValues.put(Database.PASSWORD, s_password);
		initialValues.put(Database.CLOUDID, 0);

		return db.insert(Database.USERS_TABLE_NAME, null, initialValues);

	}

	public Cursor CheckRecord(String RowID) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.USERS_TABLE_NAME,
				new String[] { "idno", Database.NATIONALID },
				Database.ROW_ID + "='" + RowID + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		db.close();
		return myCursor;
	}


	public long AddMinute(String s_minName,String s_minDate,String s_minTime) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.MIN_NAME, s_minName);
		initialValues.put(Database.MIN_DATE, s_minDate);
		initialValues.put(Database.MIN_TIME, s_minTime);
		return db.insert(Database.MINUTE_TABLE_NAME, null, initialValues);

	}
	public long AddEvent(String s_evName,String s_evDate,String s_evTime) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.EVENT_NAME, s_evName);
		initialValues.put(Database.EVENT_DATE, s_evDate);
		initialValues.put(Database.EVENT_TIME, s_evTime);
		return db.insert(Database.EVENT_TABLE_NAME, null, initialValues);

	}
}
