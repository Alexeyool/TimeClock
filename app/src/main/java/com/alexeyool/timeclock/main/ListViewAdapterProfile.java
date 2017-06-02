package com.alexeyool.timeclock.main;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alexeyool.timeclock.profiles.ProfileData;

public class ListViewAdapterProfile extends BaseAdapter{
	ListViewHolderProfile holder;
	
	ArrayList<String> titleList;
	ArrayList<View> contentViewArray;
	
	Context mContext;

	public ListViewAdapterProfile(Context context, ArrayList<String> _titleList){
		mContext = context;
		titleList = _titleList;
		contentViewArray = new ArrayList<View>();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getCount() {
		return titleList.size();
	
	}
	
	@Override
	public Object getItem(int position) {
		return titleList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
				convertView = fillListView(position, convertView, parent);
		} 
		else {
			holder = (ListViewHolderProfile) convertView.getTag();
		}

		return convertView;

	}
	

	private  View fillListView(final int position, View convertView, ViewGroup parent){
		holder = new ListViewHolderProfile();

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		convertView = inflater.inflate(R.layout.cell_list_profile, parent, false);

		holder.textView = (TextView) convertView.findViewById(R.id.textViewCLP_profile_name);
		holder.checkBox =  (CheckBox) convertView.findViewById(R.id.checkBoxCLP);
		holder.checkBox.setTag(position);
		
		holder.textView.setText(titleList.get(position));
		if(titleList.get(position).equals(ProfileData.getMainProfileName())) holder.checkBox.setChecked(true);
		else holder.checkBox.setChecked(false);
		
		holder.checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cBox = (CheckBox) v;
				if(cBox.isChecked()) {
					cBox.setChecked(true);
					int boxPosition = (Integer) v.getTag();
					cBox = (CheckBox) (contentViewArray.get(ProfileData.getChoisenBoxPosition())).findViewById(R.id.checkBoxCLP);
					cBox.setChecked(false);
					TextView t = (TextView) contentViewArray.get(boxPosition).findViewById(R.id.textViewCLP_profile_name);
					Editor editor = MainActivity.sPref.edit();
					editor.putString(MainActivity.MAIN_PROFILE_NAME, t.getText().toString());
					editor.commit();
				}
				else cBox.setChecked(true);
			}
		});
		
		convertView.setTag(holder);
		contentViewArray.add(convertView);
		return convertView;
	}

}

class ListViewHolderProfile {
	TextView textView;
	CheckBox checkBox;


}
