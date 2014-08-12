package com.compassites.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.compassites.flippinglistview.ListView_Activity.ButtonClickListener;
import com.compassites.flippinglistview.R;
import com.compassites.model.Location_Model;

public class LocationList_Adapter extends ArrayAdapter<Location_Model> {
	
	private static Context mContext;
	ArrayList<Location_Model> listevent;
	private ButtonClickListener mClickListener = null;
	
	static class ViewHolder {
		
		private TextView		text_longitude,
								text_latitude,
								text_zip,
								text_record_date;
		
		private ImageView		img_delete;
		private ViewFlipper		viewflipper;
	}
	
	public LocationList_Adapter(Context context, 
								int textViewResourceId, 
								ArrayList<Location_Model> listevent,
								ButtonClickListener mClickListener) {
		super(context, textViewResourceId, listevent);
		mContext = context;
		this.listevent = listevent;
		this.mClickListener = mClickListener;
	}
	
	public LocationList_Adapter(Context context, 
			int textViewResourceId, 
			ArrayList<Location_Model> listevent) {
super(context, textViewResourceId, listevent);
mContext = context;
this.listevent = listevent;
}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listevent.size();
	}

	@Override
	public Location_Model getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder viewHolder = new ViewHolder();
		final ViewFlipper viewflipperTemp;
		if (rowView == null) {
			LayoutInflater inflator = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflator.inflate(R.layout.list_item, parent, false);

			// configure a new holder
			
			viewHolder.viewflipper			= (ViewFlipper)rowView.findViewById(R.id.flipper);
			viewHolder.viewflipper.setDisplayedChild(0);
			viewHolder.text_longitude		= (TextView) rowView.findViewById(R.id.text_longitude);
			viewHolder.text_latitude		= (TextView) rowView.findViewById(R.id.text_latitude);
			viewHolder.text_zip				= (TextView) rowView.findViewById(R.id.text_zip);
			viewHolder.text_record_date		= (TextView) rowView.findViewById(R.id.text_date);
			viewHolder.img_delete			= (ImageView)rowView.findViewById(R.id.btn_delete);
			viewHolder.img_delete.setTag(position);
			viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 if(mClickListener != null)
						 mClickListener.onButtonClick((Integer) v.getTag());
					
				}
			});
			
			
			rowView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.viewflipper.setDisplayedChild(0);
		}
		
		viewflipperTemp						= (ViewFlipper)rowView.findViewById(R.id.flipper);
		

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text_longitude.setText(listevent.get(position).getLongitude());
		holder.text_latitude.setText(listevent.get(position).getLatitude());
		holder.text_zip.setText(listevent.get(position).getZip());
		holder.text_record_date.setText(listevent.get(position).getRecordDate("dd/MM/yyyy hh:mm:ss a"));
		

		return rowView;
	}

}
