package com.alexeyool.timeclock.main;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapterDrawer extends BaseAdapter{
	ListViewHolder holder;
	
	ArrayList<String> titleList;
	ArrayList<Bitmap> bitmapList;
	
	Context mContext;

	public ListViewAdapterDrawer(Context context, ArrayList<String> _titleList){//, ArrayList<Bitmap> _bitmapList) {
		mContext = context;
		titleList = _titleList;
	//	bitmapList = _bitmapList;
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
			holder = (ListViewHolder) convertView.getTag();
		}

		return convertView;

	}
	
//	int[] listId = {R.drawable.home, R.drawable.in_play, R.drawable.promotions, R.drawable.sports, R.drawable.more_games, R.drawable.casino, R.drawable.help, R.drawable.policies, R.drawable.policies, R.drawable.settings, R.drawable.profile};

	private  View fillListView(int position, View convertView, ViewGroup parent){
		holder = new ListViewHolder();

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		convertView = inflater.inflate(R.layout.cell_list_tools, parent, false);

		holder.textView = (TextView) convertView.findViewById(R.id.textViewCLT);
		holder.imageView =  (ImageView) convertView.findViewById(R.id.imageViewCLT);
		
		holder.textView.setText(titleList.get(position));
		if(position == 0)holder.imageView.setImageResource(R.drawable.tool_month);
		if(position == 1)holder.imageView.setImageResource(R.drawable.tool_profile);
		if(position == 2)holder.imageView.setImageResource(R.drawable.tool_shift);
		if(position == 3)holder.imageView.setImageResource(R.drawable.tool_email);
		convertView.setTag(holder);
		return convertView;
	}

}

class ListViewHolder {
	TextView textView;
	ImageView imageView;


}
