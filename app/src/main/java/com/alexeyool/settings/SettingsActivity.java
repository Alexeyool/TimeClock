package com.alexeyool.settings;

import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	Context mContext;

	TextView titleActionBar;
	ImageButton homeActionBar;
	
	ListView list;
	ListViewAdapterSettings listAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(getString(R.string.settings));

		homeActionBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
				
			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		listAdapter = new ListViewAdapterSettings(mContext);
		list = (ListView) findViewById(R.id.listViewAS);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(onItemClickListener);
		
	}
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Intent intent = new Intent(mContext, LenguageActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
			
		}
	};
}
