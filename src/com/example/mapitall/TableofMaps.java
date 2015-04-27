package com.example.mapitall;
 
import android.provider.BaseColumns;

public class TableofMaps {
	
	public TableofMaps() {
		// TODO Auto-generated constructor stub
	}

	public static abstract class TableInfo implements BaseColumns{

		// Database Columns
		public static final String location_id = "location_id";
		//public static final String map_img = "map_img";
		public static final String building_name = "building_name";
		public static final String floor = "floor";

		
		public static final String Database_Name = "MapsAll";
		public static final String Table_name = "MapsInfo";	
	}
}
