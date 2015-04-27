package com.example.mapitall;

import com.example.mapitall.TableofMaps.TableInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class DatabaseOperations extends SQLiteOpenHelper {

	public static final int database_version = 1;
	
	//public String CREATE_QUERY = "CREATE TABLE "+TableInfo.Table_name +"("+TableInfo.location_id+" TEXT, "+TableInfo.map_img+" TEXT, " + TableInfo.building_name+" TEXT, "+TableInfo.floor +" TEXT);";
	public String CREATE_QUERY = "CREATE TABLE "+TableInfo.Table_name +"("+TableInfo.location_id+" TEXT, "+ TableInfo.building_name+" TEXT, "+TableInfo.floor +" TEXT);";
	
	public DatabaseOperations(Context context) {
		super(context, TableInfo.Database_Name , null, database_version);
		// TODO Auto-generated constructor stub
		Log.d("DB ops: ", "Database Created ");
	}

	public DatabaseOperations(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase sdb) {
		// TODO Auto-generated method stub
		sdb.execSQL(CREATE_QUERY);
		Log.d("DB ops: ", "Table Created ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	
	public void putInformation(DatabaseOperations dop, String loc_id, String build_no, String floor){
		SQLiteDatabase SQ = dop.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TableInfo.location_id, loc_id);
		//cv.put(TableInfo.map_img, map);
		cv.put(TableInfo.building_name, build_no);
		cv.put(TableInfo.floor, floor);
		long k = SQ.insert(TableInfo.Table_name , null, cv);
		Log.d(" DB Ops:"," One row inserted");
	}

	
	public Cursor getInformation(DatabaseOperations dop){
		SQLiteDatabase SQ = dop.getReadableDatabase();
		//String[] columns = {TableInfo.location_id, TableInfo.map_img, TableInfo.building_name, TableInfo.floor};
		String[] columns = {TableInfo.location_id, TableInfo.building_name, TableInfo.floor};
		Cursor CR = SQ.query(false, TableInfo.Table_name, columns, null, null, null, null, null, null, null);		
		Log.d(" DB Ops:"," Information is passed");
		return CR;
	}
}
