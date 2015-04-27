package com.example.mapitall;

import java.io.File;
import java.io.FileOutputStream;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ScanQRCodeActivity extends ActionBarActivity {
	
	//private Button scanBtn;
	private TextView locTxt, buildTxt, floorTxt;
	private ImageView imgView;
	Context CTX = this;
	Bundle extras;
	
//	@Override
//	protected void onNewIntent(Intent intent) {
//	    super.onNewIntent(intent);
//	    setIntent(intent);
//	}
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_qrcode);
		
		
	//	scanBtn = (Button)findViewById(R.id.scan_button);
		locTxt = (TextView)findViewById(R.id.scan_location);
		buildTxt = (TextView)findViewById(R.id.scan_building);
		floorTxt = (TextView)findViewById(R.id.scan_floor);
		imgView = (ImageView)findViewById(R.id.scan_image);

		
		// Get the value from QRcode 
		String newString;
		
		if (savedInstanceState == null) {
		    extras = getIntent().getExtras();
		    if(extras == null) {
	    		Log.d("DB ops: ", "extras is null");
		        newString= null;
		    } else {
		        newString= extras.getString("KEY_scanContent");
		    }
		} else {
    		Log.d("DB ops: ", "savedInstanceState is not null");
		    newString= (String) savedInstanceState.getSerializable("KEY_scanContent");
		}
		
		
        DatabaseOperations DOP = new DatabaseOperations(CTX);
        Cursor CR = DOP.getInformation(DOP);
        String locContent = null;
        //String mapContent = null;
        String buildContent = null;
        String floorContent = null;
        Bitmap bmp = null;
    
		
        if (CR == null){
    		Log.d("DB ops: ", "CR is null");
        }
        
        CR.moveToFirst();
        do{
        	if (newString == null){
        		//Log.d("DB ops: ", "newString is null");
        	}
        	else if(newString.equals(CR.getString(0)))
            {
        		locContent = CR.getString(0);
        		//mapContent = CR.getString(1);
				buildContent = CR.getString(1);
				floorContent = CR.getString(2);
				
				File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
		    	String filename = locContent +".jpg";   
		    	// Log.i("Local filename:",""+filename);
		    	//File file = new File(SDCardRoot,filename);
		    	  
		    	String path = SDCardRoot + "/" + filename;
		    	Log.i("Path:",""+ path);
				//String path = "/sdcard/"+locContent+".jpg";
				File f = new File(path);
				bmp = BitmapFactory.decodeFile(f.getAbsolutePath());				
				
            }
        }while(CR.moveToNext());
       
        locTxt.setText("Location: " + locContent);
		buildTxt.setText("Building: " + buildContent);
		floorTxt.setText("Room: "+ floorContent);
		imgView.setImageBitmap(bmp);
		
		
		Log.d(" DB Ops:"," Inside Retrieval Loop ");

        //finish();
         
        }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.scan_qrcode, menu);
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
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_scan_qrcode,
					container, false);
			return rootView;
		}
	}

}
