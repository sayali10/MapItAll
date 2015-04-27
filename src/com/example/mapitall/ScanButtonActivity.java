package com.example.mapitall;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScanButtonActivity extends ActionBarActivity implements OnClickListener {
	
	private Button scanBtn;
	private TextView locTxt, buildTxt, floorTxt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scan_button);

		scanBtn = (Button)findViewById(R.id.scanButton);
		locTxt = (TextView)findViewById(R.id.scan_location);
		buildTxt = (TextView)findViewById(R.id.scan_building);
		floorTxt = (TextView)findViewById(R.id.scan_floor);

		scanBtn.setOnClickListener((OnClickListener)this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.scan_button, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_scan_button,
					container, false);
			return rootView;
		}
	}
	/*
	 /** Called when the user clicks the Scan button 
    public void scanQR(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ScanQRCodeActivity.class);
        startActivity(intent);
    }*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId()==R.id.scanButton){
			//scan
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
			
		}	
	}
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

		if (scanningResult != null) {
			//we have a result
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			Intent displayintent = new Intent(this, ScanQRCodeActivity.class);
	        
	        //displayintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);		

	        displayintent.putExtra("KEY_scanContent", scanContent.toString());   
	        displayintent.putExtra("KEY_scanFormat", scanFormat.toString());
	        
	        startActivity(displayintent);
	        
		}
		else{
		    Toast toast = Toast.makeText(getApplicationContext(),
		        "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		}
	}
	
	
}
