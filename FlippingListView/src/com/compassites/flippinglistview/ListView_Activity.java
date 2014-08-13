package com.compassites.flippinglistview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.compassites.GPS.GPSTracker;
import com.compassites.adapter.LocationList_Adapter;
import com.compassites.devicedb.ListDataProvider;
import com.compassites.loader.GPSLocation_Loader;
import com.compassites.model.Location_Model;

public class ListView_Activity extends Activity  {
	
	private final String TAG = getClass().getSimpleName();
	Button		addNew,
				flipView;
	ListView	flipListView;
	
	LocationManager locationManager;
	Context mContext;
	GPSTracker gpsTracker;
	
	LocationList_Adapter locationAdapter;
	ArrayList<Location_Model> records;
	GPSLocation_Loader gpsLoader;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    						WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.fliplistview);
		
		try {
			
			gpsTracker = new GPSTracker(ListView_Activity.this);
			
			flipListView = (ListView)findViewById(R.id.list_table);
			gpsLoader = new GPSLocation_Loader();
			records = gpsLoader.getData(ListView_Activity.this);
			
			locationAdapter = new LocationList_Adapter(getApplicationContext(), R.layout.list_item, records, new ButtonClickListener() {
				
				@Override
				public void onButtonClick(int position) {
					try {
						
						Log.d(TAG, String.format("onLoaderFinish(): position clicked: [%d]", position));
						long id = records.get(position).getRecordId();
						int result = getContentResolver().delete(ListDataProvider.CONTENT_URI, ListDataProvider.ID + "=" + id , null);
						if(result != 0){
							
							locationAdapter.remove(records.get(position));
							locationAdapter.notifyDataSetChanged();
							
							Toast.makeText(getApplicationContext(), 
									"Record Deleted!!!", 
									Toast.LENGTH_SHORT)
									.show();
							
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
				}
			});
			locationAdapter.notifyDataSetChanged();
			flipListView.setAdapter(locationAdapter);

			
			
			addNew = (Button)findViewById(R.id.btn_addnew);
			addNew.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					try {
						
						if (gpsTracker.canGetLocation()) {
							
							 String stringLongitude = String.valueOf(gpsTracker.getLongitude());
							 System.out.print("Longitude: " + stringLongitude);
							 
							 String stringLatitude = String.valueOf(gpsTracker.getLatitude());
							 System.out.print("Latitude: " + stringLatitude);
							 
							 String postalCode = "";
							 Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
							 List<Address>  addresses = gcd.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
							 if(addresses !=null && addresses.size() > 0) {
								 postalCode = addresses.get(0).getPostalCode();
								 System.out.print("Zip: " + postalCode);
							 }
							 
							 
							 Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
							 Date currentDateandTime = cal.getTime();
							 
							 insertLocation(stringLongitude, 
									 		stringLatitude, 
									 		postalCode, 
									 		currentDateandTime);
					        
					        }
						 else {
					            gpsTracker.showSettingsAlert();
					        }
						
						
						
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), 
								e.getMessage() + "\nReboot the device and try again.", 
								Toast.LENGTH_LONG)
								.show();
					}
					
					
				}
			});
			
			flipView = (Button)findViewById(R.id.btn_flip);
			flipView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					try {
						
					
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
					
				}
			});
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void insertLocation(String longitude, String latitude, String zip, Date datetime) {
		
		try {
			if(datetime != null) {
				
				ContentValues cv = new ContentValues();
				Calendar cal = Calendar.getInstance();
				
				cv.put(ListDataProvider.LONGITUDE, longitude);
				cv.put(ListDataProvider.LATITUDE, latitude);
				cv.put(ListDataProvider.ZIP, zip);
				cal.setTime(datetime);
				cv.put(ListDataProvider.RECORD_DATE, cal.getTimeInMillis());
				
				Uri result = getContentResolver().insert(ListDataProvider.CONTENT_URI, cv);
				
				if(result !=null) {
					records.removeAll(records);
					records = gpsLoader.getData(ListView_Activity.this);
					locationAdapter.notifyDataSetChanged();
					
					Toast.makeText(getApplicationContext(), 
							"New Record Added!!!", 
							Toast.LENGTH_SHORT)
							.show();

				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public interface ButtonClickListener {
	    public abstract void onButtonClick(int position);
	    
	}
	
}
