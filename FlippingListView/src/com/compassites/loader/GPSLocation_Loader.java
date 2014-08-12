package com.compassites.loader;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;

import com.compassites.devicedb.ListDataProvider;
import com.compassites.model.Location_Model;



public class GPSLocation_Loader {

	Context context;
	BaseAdapter adapter;
	ArrayList<Location_Model> records = new ArrayList<Location_Model>();

	
	public int getNumOfRecords(){
		return records.size();
	}
	
	public ArrayList<Location_Model> getRecords(){
		return records;
	}
	
	public void setAdapter(BaseAdapter adapter){
		this.adapter = adapter;
	}
	
	public ArrayList<Location_Model> getData(Context context) {
		Cursor c = context.getContentResolver().query(ListDataProvider.CONTENT_URI,new String[] { ListDataProvider.ID, ListDataProvider.LATITUDE, ListDataProvider.LONGITUDE, ListDataProvider.ZIP, ListDataProvider.RECORD_DATE},
				null,
				null, 
				null);
					if(c != null && c.moveToFirst()){
					do{
					Location_Model record = new Location_Model(	c.getLong(0),
																c.getString(1), 
																c.getString(2), 
																c.getString(3), 
																c.getLong(4));
					records.add(record);
					}while(c.moveToNext());	
					c.close();
					}

		return records;
	}
	

}
