package com.alexeyool.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;

public class ListViewAdapterSettings extends BaseAdapter{
	
	ListViewHolderMain holder;
	
	Context mContext;
	String[] settingsTitles = {"lenguage"};
	
	int days;
	long hours;
	float money;

	public ListViewAdapterSettings(Context context){
		mContext = context;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getCount() {
		return settingsTitles.length;
	
	}
	
	@Override
	public Object getItem(int position) {
		return settingsTitles[position];
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
				convertView = fillListView(position, convertView, parent);
		} 
		else {
			holder = (ListViewHolderMain) convertView.getTag();
		}

		return convertView;

	}
	
	private  View fillListView(int position, View convertView, ViewGroup parent){
		holder = new ListViewHolderMain();
		LinearLayout temp = new LinearLayout(mContext);
		convertView = temp;
		
		holder.first = new TextView(mContext);
		holder.second = new TextView(mContext);
		
		holder.first.setTextSize(15);
		
		holder.first.setText(mContext.getString(R.string.lenguage));
		holder.second.setText(MainActivity.sPref.getString(MainActivity.LENGUAGE, mContext.getString(R.string.english)));
		
		temp.addView(holder.first);
		temp.addView(holder.second);
		convertView = temp;
		convertView.setTag(holder);
		return convertView;
	}

}

class ListViewHolderMain {
	TextView first;
	TextView second;

}
