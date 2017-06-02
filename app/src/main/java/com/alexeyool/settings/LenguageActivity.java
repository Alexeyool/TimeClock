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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LenguageActivity extends Activity {
	
	Context mContext;

	TextView titleActionBar;
	ImageButton homeActionBar;
	
	LinearLayout englishLL, russianLL, hebrowLL; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lenguage);
		
		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(getString(R.string.lenguage));

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
		
		englishLL = (LinearLayout) findViewById(R.id.linearLayoutAL_english);
		russianLL = (LinearLayout) findViewById(R.id.linearLayoutAL_russian);
		hebrowLL  = (LinearLayout) findViewById(R.id.linearLayoutAL_hebrow);
		
		englishLL.setOnClickListener(onClickListener);
		russianLL.setOnClickListener(onClickListener);
		hebrowLL.setOnClickListener(onClickListener);
		
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.linearLayoutAL_english:

				break;
			case R.id.linearLayoutAL_russian:

				break;
			case R.id.linearLayoutAL_hebrow:

				break;

			default:
				break;
			}
			
		}
	};
}
