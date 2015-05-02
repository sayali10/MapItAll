package com.example.mapitall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;

public class LaunchActivity extends ActionBarActivity {

/*	SharedPreferences mPrefs;
	final String welcomeScreenShownPref = "welcomeScreenShown";
*/
	
    public final static String EXTRA_MESSAGE = "com.example.user.myapplication.MESSAGE";
    EditText Location, Building, Room;
    String loc, build, room;
    Context ctx = this;
    
    private ProgressDialog pDialog;
    
    // URL to get contacts JSON
    private static String url = "http://mapitall-netbadi.rhcloud.com/LH.json";
 
    // JSON Node names
    private static final String TAG_LOCATION = "location";
    
    private static final String TAG_LOCATION_ID = "location_id";
    private static final String TAG_BUILDING_NAME = "building_name";
    private static final String TAG_FLOOR = "floor";
    //private static final String TAG_MAP_IMG = "map_img";
 
    // contacts JSONArray
    JSONArray location = null;
 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> locationsList;
 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
	/*	mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	    // second argument is the default to use if the preference can't be found
	    Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

	    if (!welcomeScreenShown) {
	        // here you can launch another activity if you like
	        // the code below will display a popup
	    	Intent intent = new Intent(this, ScanButtonActivity.class);
	        startActivity(intent);	    	
	    	
	    }*/
	}
	


	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 *
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_launch,
					container, false);
			return rootView;
		}
	}
	*/
	
	
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ScanButtonActivity.class);
        EditText editText = (EditText) findViewById(R.id.use_loc);
        String loc = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, loc);
        
        // Parsing the JSON file 
        locationsList = new ArrayList<HashMap<String, String>>();
        
        // Calling async task to get json
        new GetLocations().execute();
       
        
        Toast.makeText(getBaseContext(), "Download Success", Toast.LENGTH_LONG).show();
        finish();
        
        startActivity(intent);
    }

    
	/**
     * Async task class to get json by making HTTP call
     * */
    private class GetLocations extends AsyncTask<Void, Void, Void> {
    	
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JSONFileHandler sh = new JSONFileHandler();
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, JSONFileHandler.GET);

            if (jsonStr != null) {
                try {

                	JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    location = jsonObj.getJSONArray(TAG_LOCATION);

                    // looping through All Contacts
                    for (int i = 0; i < location.length(); i++) {
                        JSONObject c = location.getJSONObject(i);
                        DatabaseOperations DB = new DatabaseOperations(ctx);                        
                        
                        String id = c.getString(TAG_LOCATION_ID);
                        //String mapimg = c.getString(TAG_MAP_IMG);
                        String building = c.getString(TAG_BUILDING_NAME);
                        String floor = c.getString(TAG_FLOOR);

                        Log.d("got Object: ", "-->" + id);    
                        Log.d("got Object: ", "-->" + building);                    	
                        Log.d("got Object: ", "-->" + floor);                    	

                        DB.putInformation(DB, id, building, floor);                        
                        downloadImagesToSdCard(id);
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
    
    private void downloadImagesToSdCard(String imageName)
    {
       	String filepath = null;
		try
    	{   
    	  URL url = new URL("http://mapitall-netbadi.rhcloud.com/"+ imageName +".jpg");
    	  HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    	  urlConnection.setRequestMethod("GET");
    	  urlConnection.setDoOutput(true);                   
    	  urlConnection.connect();                  
    	  File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
    	  String filename = imageName +".jpg";   
    	  Log.i("Local filename:",""+filename);
    	  File file = new File(SDCardRoot,filename);
    	  if(file.createNewFile())
    	  {
    	    file.createNewFile();
    	  }                 
    	  FileOutputStream fileOutput = new FileOutputStream(file);
    	  InputStream inputStream = urlConnection.getInputStream();
    	  int totalSize = urlConnection.getContentLength();
    	  int downloadedSize = 0;   
    	  byte[] buffer = new byte[1024];
    	  int bufferLength = 0;
    	  while ( (bufferLength = inputStream.read(buffer)) > 0 ) 
    	  {                 
    	    fileOutput.write(buffer, 0, bufferLength);                  
    	    downloadedSize += bufferLength;                 
    	    //Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
    	  }             
    	  fileOutput.close();
    	  if(downloadedSize==totalSize) filepath=file.getPath();    
    	} 
    	catch (MalformedURLException e) 
    	{
    	  e.printStackTrace();
    	} 
    	catch (IOException e)
    	{
    	  filepath=null;
    	  e.printStackTrace();
    	}
    	Log.i("filepath:"," "+filepath) ;
    	
    }
    }
}
