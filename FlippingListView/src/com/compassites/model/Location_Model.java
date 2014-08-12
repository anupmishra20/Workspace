package com.compassites.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Location_Model {
	
	private long 	id,
					record_date;
	private String 	longitude,
					latitude,
					zip;
	
	public Location_Model (long id, String longitude, String latitude, String zip, long record_date) {

		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.zip = zip;
		this.record_date = record_date;
}
	
	public long getRecordId () {
		return id;
	}
	
	public void setRecordId(long id) {
		this.id = id;
	}
	
	public String getLongitude () {
		return longitude;
	}
	
	public void setLongitude (String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude () {
		return latitude;
	}
	
	public void setLatitude (String latitude) {
		this.latitude = latitude;
	}
	
	public String getZip () {
		return zip;
	}
	
	public void setZip (String zip) {
		this.zip = zip;
	}
	
	public String getRecordDate (String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat, Locale.getDefault());
		String date = df.format(record_date);
		return date;
	}
	
	public void setRecordDate(long date) {
		this.record_date = date;
		
	}
	
}
