package com.alexeyool.profile;

import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alexeyool.timeclock.main.ListViewAdapterProfile;
import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.ProfileData;

public class ProfileActivity extends Activity {
	
	Context mContext;

	TextView titleActionBar;
	ImageButton homeActionBar;
	
	Button add, edit, delete;
	ListView list;
	
	String choisenProfile;
	public static int choisenBox;
	View choisenView;
	ArrayList<String> profilesNamesList;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(R.string.shift_profiles);
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
		
		add = (Button) findViewById(R.id.buttonAP_add_profile);
		edit = (Button) findViewById(R.id.buttonAP_edit_profile);
		delete = (Button) findViewById(R.id.buttonAP_delete);
		add.setOnClickListener(onClickListener);
		edit.setOnClickListener(onClickListener);
		delete.setOnClickListener(onClickListener);
		
		choisenBox = ProfileData.getChoisenBoxPosition();
		fillListView();
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, ProfileEditActivity.class);
			switch (v.getId()) {
			case R.id.buttonAP_add_profile:
				intent.putExtra(MainActivity.PROFILE_NAME, MainActivity.NEW_PROFILE);
				startActivity(intent);
				break;

			case R.id.buttonAP_edit_profile:
				if(choisenProfile != null){
					intent.putExtra(MainActivity.PROFILE_NAME, choisenProfile);
					startActivity(intent);
				}
				break;

			case R.id.buttonAP_delete:
				ProfileData profTemp = new ProfileData(mContext);
				profTemp.removeProfile(choisenProfile);
				fillListView();
				break;

			default:
				break;
			}

		}
	};
	
	private void fillListView() {
		list = (ListView) findViewById(R.id.listViewAP);
		list.setOnItemClickListener(onItemClickListener);
		profilesNamesList = ProfileData.getProfilesNamseArrayList();
		choisenBox = ProfileData.getChoisenBoxPosition();
		ListViewAdapterProfile listAdapter = new ListViewAdapterProfile(mContext, profilesNamesList);
		list.setAdapter(listAdapter);
		
	}
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			choisenProfile = profilesNamesList.get(position);
			view.setBackgroundResource(R.color.set2_backgraund_for_choisen_cell);
			if(choisenView != null)choisenView.setBackgroundResource(R.color.set2_main_backgraund);
			choisenView = view;
			
		}
	};
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent(mContext, ProfileActivity.class);
		startActivity(intent);
		finish();
	}
	
}
