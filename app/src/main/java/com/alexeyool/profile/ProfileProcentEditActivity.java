package com.alexeyool.profile;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.Checkup;
import com.alexeyool.timeclock.profiles.ProfileData;

public class ProfileProcentEditActivity extends Activity {
	Context mContext;
	String profileName;
	String shiftType;
	EditText procentET1, procentET2, procentET3, procentET4, hourET1, hourET2, hourET3, hourET4;
	TextView errorTV;
	Button set, cancel;
	
	TextView titleActionBar;
	ImageButton homeActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_prosent_edit);
		mContext = this;
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(R.string.prosents_edit);
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
		
		Intent intentTemp = getIntent();
		profileName = ProfileEditActivity.prof.profileName;
		shiftType = intentTemp.getStringExtra(MainActivity.SHIFT_TYPE);
		
		procentET1 = (EditText) findViewById(R.id.editTextPPE_1_prosent);
		procentET2 = (EditText) findViewById(R.id.editTextPPE_2_prosent);
		procentET3 = (EditText) findViewById(R.id.editTextPPE_3_procent);
		procentET4 = (EditText) findViewById(R.id.editTextPPE_4_procent);
		hourET1 = (EditText) findViewById(R.id.editTextPPE_1_hour);
		hourET2 = (EditText) findViewById(R.id.editTextPPE_2_hour);
		hourET3 = (EditText) findViewById(R.id.editTextPPE_3_hour);
		hourET4 = (EditText) findViewById(R.id.editTextPPE_4_hour);
		hourET1.setOnTouchListener(onTouchListener);
		hourET2.setOnTouchListener(onTouchListener);
		hourET3.setOnTouchListener(onTouchListener);
		hourET4.setOnTouchListener(onTouchListener);
		set = (Button) findViewById(R.id.buttonPPE_set);
		cancel = (Button) findViewById(R.id.buttonPPE_cancel);
		set.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		
		fillLayout();
	}

	private void fillLayout() {
		ArrayList<Integer> prosentsProcentArray = new ArrayList<Integer>();
		ArrayList<Long> prosentsHourArray = new ArrayList<Long>();
		if(shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
			prosentsProcentArray = ProfileEditActivity.prof.prosentsProcentWeekendArray;
			prosentsHourArray = ProfileEditActivity.prof.prosentsHourWeekendArray;
		}
		else{
			if(shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)){
				prosentsProcentArray = ProfileEditActivity.prof.prosentsProcentWeekdayArray;
				prosentsHourArray = ProfileEditActivity.prof.prosentsHourWeekdayArray;
			}
		}
		
		int stop = 0;
		for(int i = 0; i<prosentsProcentArray.size() && stop == 0; i++){
			String a = "";
			if(!(prosentsHourArray.get(i) == -1)) a = ""+prosentsHourArray.get(i)/60;
			else {
				stop = 1;
				a = "";
			}
			switch (i) {
			case 0:
				procentET1.setText(""+prosentsProcentArray.get(i));
				hourET1.setText(a);
				break;

			case 1:
				procentET2.setText(""+prosentsProcentArray.get(i));
				hourET2.setText(a);
				break;
			case 2:
				procentET3.setText(""+prosentsProcentArray.get(i));
				hourET3.setText(a);
				break;
			case 3:
				procentET4.setText(""+prosentsProcentArray.get(i));
				hourET4.setText(a);
				break;

			default:
				break;
			}
		}
	}
	
	private boolean collectData(){
		if(!checkDateCollectionForErrors(
				procentET1.getText().toString(), procentET2.getText().toString(),
				procentET3.getText().toString(), procentET4.getText().toString(),
				hourET1.getText().toString(), hourET2.getText().toString(),
				hourET3.getText().toString(), hourET4.getText().toString())) return false;
		ArrayList<Integer> procent = new ArrayList<Integer>();
		ArrayList<Long> hour = new ArrayList<Long>();
		for(int i = 0; i<4; i++){
			switch (i) {
			case 0:
				if(hourET1.getText().toString().equals("") || hourET1.getText().toString().equals("0")){
					procent.add(Integer.valueOf(procentET1.getText().toString()));
					hour.add((long) -1);
					i = 10;
				}
				else{
				procent.add(Integer.valueOf(procentET1.getText().toString()));
				hour.add(Long.valueOf(hourET1.getText().toString())*60/1);
				}
				break;
				
			case 1:
				if(hourET2.getText().toString().equals("") || hourET2.getText().toString().equals("0")){
					procent.add(Integer.valueOf(procentET2.getText().toString()));
					hour.add((long) -1);
					i = 10;
				}
				else{
				procent.add(Integer.valueOf(procentET2.getText().toString()));
				hour.add(Long.valueOf(hourET2.getText().toString())*60/1);
				}
				break;
				
			case 2:
				if(hourET3.getText().toString().equals("") || hourET3.getText().toString().equals("0")){
					procent.add(Integer.valueOf(procentET3.getText().toString()));
					hour.add((long) -1);
					i = 10;
				}
				else{
				procent.add(Integer.valueOf(procentET3.getText().toString()));
				hour.add(Long.valueOf(hourET3.getText().toString())*60/1);
				}
				break;
				
			case 3:
				if(hourET4.getText().toString().equals("") || hourET4.getText().toString().equals("0")){
					procent.add(Integer.valueOf(procentET4.getText().toString()));
					hour.add((long) -1);
					i = 10;
				}
				else{
				procent.add(Integer.valueOf(procentET4.getText().toString()));
				hour.add(Long.valueOf(hourET4.getText().toString())*60/1);
				}
				break;

			default:
				break;
			}
		}
		if(shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)){
			ProfileEditActivity.prof.prosentsProcentWeekdayArray = procent;
			ProfileEditActivity.prof.prosentsHourWeekdayArray = hour;
		}
		else{
			ProfileEditActivity.prof.prosentsProcentWeekendArray = procent;
			ProfileEditActivity.prof.prosentsHourWeekendArray = hour;
		}
		return true;
	}
	
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("z", "1");
			switch (v.getId()) {
			case R.id.buttonPPE_set:
				if(!collectData()) return;
				setResult(RESULT_OK, intent);
				finish();
				break;

			case R.id.buttonPPE_cancel:
				setResult(RESULT_OK, intent);
				finish();

				break;

			default:
				break;
			}

		}
	};
	
	public boolean checkDateCollectionForErrors(String procent0, String procent1, String procent2, String procent3,
			String hour0, String hour1, String hour2, String hour3){
		errorTV = (TextView) findViewById(R.id.textViewPPE_error);
		if(!Checkup.isProcentsDataFillIncorrectWay(procent0, procent1, procent2, procent3,
				hour0, hour1, hour2, hour3)){
			errorTV.setText(R.string.the_data_not_filled_correctle);
			return false;
		}
		return true;
	}
	
	OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.editTextPPE_1_hour:
				if(hourET1.getText().toString().equals("0")) hourET1.setText("");
				break;
			case R.id.editTextPPE_2_hour:
				if(hourET2.getText().toString().equals("0")) hourET2.setText("");
				break;
			case R.id.editTextPPE_3_hour:
				if(hourET3.getText().toString().equals("0")) hourET3.setText("");
				break;
			case R.id.editTextPPE_4_hour:
				if(hourET4.getText().toString().equals("0")) hourET4.setText("");
				break;

			default:
				break;
			}
			return false;
		}
	};
	
}
