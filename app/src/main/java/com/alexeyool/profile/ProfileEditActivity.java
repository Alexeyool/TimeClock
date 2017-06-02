package com.alexeyool.profile;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.Calculation;
import com.alexeyool.timeclock.profiles.Checkup;
import com.alexeyool.timeclock.profiles.ProfileData;

public class ProfileEditActivity extends Activity {
	
	Context mContext;

	TextView titleActionBar;
	ImageButton homeActionBar;
	
	EditText profileNameET, payPerHourET, travelPerDayET, notPayBreakET, 
	         sickPayET, vacationET, holidayET;
	Spinner travelS, payPerS;
	int[] payPerArray = {R.string.hour, R.string.day};
	int[] travelArray = {R.string.day, R.string.month};
	
	Button editProcentsWeekday, editProcentsWeekend;
	Button save, delete;
	
	String profName;
	public static ProfileData prof;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_edit);
		
		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(R.string.shift_profile_edit);
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
		
		Intent intentIn = getIntent();
		profName = intentIn.getStringExtra(MainActivity.PROFILE_NAME);
		prof = new ProfileData(mContext);
		prof = prof.loadProfile(mContext, profName);
		
		identifiedWidgets();
		fillProfileData();
		
	}
	
	private void identifiedWidgets(){ 
		profileNameET = (EditText) findViewById(R.id.editTextAPE_profile_name);
		payPerHourET = (EditText) findViewById(R.id.editTextAPE_pay_per_hour);
		spinnerActivationPayPer();
		travelPerDayET = (EditText) findViewById(R.id.editTextAPE_travel_per_day);
		spinnerActivationTravel();
		notPayBreakET = (EditText) findViewById(R.id.editTextAPE_braek);
		sickPayET = (EditText) findViewById(R.id.editTextAPE_sick_pay);
		vacationET = (EditText) findViewById(R.id.editTextAPE_vacation);
		holidayET = (EditText) findViewById(R.id.EditTextAPE_holiday);
		editProcentsWeekday = (Button) findViewById(R.id.buttonAPE_procent_change_weekday);
		editProcentsWeekend = (Button) findViewById(R.id.buttonAPE_procent_change_weekend);
		save = (Button) findViewById(R.id.buttonAPE_save);
		delete = (Button) findViewById(R.id.buttonAPE_delete);
		save.setOnClickListener(onClickListener);
		delete.setOnClickListener(onClickListener);
		editProcentsWeekday.setOnClickListener(onClickListener);
		editProcentsWeekend.setOnClickListener(onClickListener);
	}
	
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonAPE_save:
				if(!checkDateCollectionForErrors(prof, profileNameET.getText().toString())) return;
				prof.saveProfile(collectProfileData(prof), profName);
				finish();
				break;

			case R.id.buttonAPE_delete:
				prof.removeProfile(profName);
				finish();
				break;
				
			case R.id.buttonAPE_procent_change_weekday:
				Intent intentTemp1 = new Intent(mContext, ProfileProcentEditActivity.class);
				intentTemp1.putExtra(MainActivity.SHIFT_TYPE, ProfileData.SHIFT_TYPE_WEEKDAY);
				startActivityForResult(intentTemp1, 175);
				break;
				
			case R.id.buttonAPE_procent_change_weekend:
				Intent intentTemp2 = new Intent(mContext, ProfileProcentEditActivity.class);
				intentTemp2.putExtra(MainActivity.SHIFT_TYPE, ProfileData.SHIFT_TYPE_WEEKEND);
				startActivityForResult(intentTemp2, 176);
				break;


			default:
				break;
			}

		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null) return;
		if(requestCode == 175) fillProcents(ProfileData.SHIFT_TYPE_WEEKDAY);
		else fillProcents(ProfileData.SHIFT_TYPE_WEEKEND);
	}

	private void fillProfileData(){		
		profileNameET.setText(profName);
		payPerS.setSelection(prof.payPerHourOrDay);
		payPerHourET.setText(""+prof.payPer);
		travelS.setSelection(prof.travelPerDayOrMonth);
		travelPerDayET.setText(""+prof.travelPer);
		sickPayET.setText(""+prof.sickPay);
		vacationET.setText(""+prof.vacation);
		holidayET.setText(""+prof.holiday);
		LinearLayout l = (LinearLayout) findViewById(R.id.linearLayoutAPE_procents);
		TableRow breakTR = (TableRow) findViewById(R.id.TableRowAPE_not_payde_break);
		if(prof.payPerHourOrDay == 0){
			l.setVisibility(View.VISIBLE);
			breakTR.setVisibility(View.VISIBLE);
			notPayBreakET.setText(""+prof.notPayBreak);
			fillProcents(ProfileData.SHIFT_TYPE_WEEKDAY);
			fillProcents(ProfileData.SHIFT_TYPE_WEEKEND);
		}
		else {
			l.setVisibility(View.GONE);
			breakTR.setVisibility(View.GONE);
		}
	}
	
	private void spinnerActivationPayPer() {
		String[] payPerArrayString = new String[payPerArray.length];
		payPerArrayString[0] = getString(payPerArray[0]);
		payPerArrayString[1] = getString(payPerArray[1]);
		payPerS = (Spinner) findViewById(R.id.SpinnerAPE_pay_per_hour);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_item, payPerArrayString);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		payPerS.setSelection(MainActivity.sPref.getInt(ProfileData.PAY_HOUR_DAY_ + prof.profileName, 0));
		payPerS.setOnItemSelectedListener(onItemSelectedListenerPayPer);
		payPerS.setAdapter(adapter);
	}

	OnItemSelectedListener onItemSelectedListenerPayPer = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch (position) {
			case 0:
				prof.payPerHourOrDay = 0;
				fillProfileData();
				break;

			case 1:
				prof.payPerHourOrDay = 1;
				fillProfileData();
				break;

			default:
				break;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private void spinnerActivationTravel() {
		travelS = (Spinner) findViewById(R.id.SpinnerAPE_travel_per_day_title);
		String[] travelArrayString = new String[travelArray.length];
		travelArrayString[0] = getString(travelArray[0]);
		travelArrayString[1] = getString(travelArray[1]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_item, travelArrayString);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		travelS.setOnItemSelectedListener(onItemSelectedListenerTravel);
		travelS.setAdapter(adapter);
	}

	OnItemSelectedListener onItemSelectedListenerTravel = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch (position) {
			case 0:
				prof.travelPerDayOrMonth = 0;
//				travelPerDayET.setText(R.string.zero);
				break;

			case 1:
				prof.travelPerDayOrMonth = 1;
//				travelPerDayET.setText(R.string.zero);
				break;

			default:
				break;
			}

		}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		};
	
	private void fillProcents(String _shiftType) {
		TableLayout table = new TableLayout(mContext);
		ArrayList<Integer> prosentsProcentArray = new ArrayList<Integer>();
		ArrayList<Long> prosentsHourArray = new ArrayList<Long>();
		if(_shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
			table = (TableLayout) findViewById(R.id.TableLayoutAPE_procents_weekend);
			prosentsProcentArray = prof.prosentsProcentWeekendArray;
			prosentsHourArray = prof.prosentsHourWeekendArray;
		}
		else{
			if(_shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)){
				table = (TableLayout) findViewById(R.id.TableLayoutAPE_procents_weekday);
				prosentsProcentArray = prof.prosentsProcentWeekdayArray;
				prosentsHourArray = prof.prosentsHourWeekdayArray;
			}
		}
		table.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		TableRow rowTitle = (TableRow) inflater.inflate(R.layout.profile_procent_text_row, null, false);
		TextView prosentTitleTV = (TextView) rowTitle.findViewById(R.id.textViewPPTR_prosent);
		TextView hourTitleTV = (TextView) rowTitle.findViewById(R.id.textViewPPTR_hour);
		TextView moneyTitleTV = (TextView) rowTitle.findViewById(R.id.textViewPPTR_money);

		prosentTitleTV.setText(getResources().getText(R.string.prosent));
		hourTitleTV.setText(getResources().getText(R.string.hours));
		moneyTitleTV.setText(getResources().getText(R.string.money));
		table.addView(rowTitle);
		
		for(int i=0; i<prosentsProcentArray.size(); i++){
			TableRow row = (TableRow) inflater.inflate(R.layout.profile_procent_text_row, null, false);
			TextView prosentTV = (TextView) row.findViewById(R.id.textViewPPTR_prosent);
			TextView hourTV = (TextView) row.findViewById(R.id.textViewPPTR_hour);
			TextView moneyTV = (TextView) row.findViewById(R.id.textViewPPTR_money);

			prosentTV.setText(""+prosentsProcentArray.get(i));
			if(prosentsHourArray.get(i) == -1) hourTV.setText("");
			else hourTV.setText(""+prosentsHourArray.get(i)/60);
			moneyTV.setText(""+Calculation.round((prosentsProcentArray.get(i)*Float.valueOf(payPerHourET.getText().toString())/100), 2));
			table.addView(row);
		}

	}

	private ProfileData collectProfileData(ProfileData p){		
		p.profileName = profileNameET.getText().toString();
		p.payPerHourOrDay = (int) payPerS.getSelectedItemId();
		p.payPer = Float.valueOf(payPerHourET.getText().toString());
		p.travelPerDayOrMonth = (int) travelS.getSelectedItemId();
		p.travelPer = Float.valueOf(travelPerDayET.getText().toString());
		p.sickPay = Float.valueOf(sickPayET.getText().toString());
		p.vacation = Float.valueOf(vacationET.getText().toString());
		p.holiday = Float.valueOf(holidayET.getText().toString());
		if(p.payPerHourOrDay == 0) 
			p.notPayBreak = Integer.valueOf(notPayBreakET.getText().toString());
		
		return p;
	}
	
	protected boolean checkDateCollectionForErrors(ProfileData _prof, String newProfName) {
		if(!_prof.profileName.equals(newProfName)){
			if(Checkup.containProfileName(newProfName)){
				if(!profileExistDialog(newProfName)) return false;
			}
		}
		return true;
	}
	
boolean yes24;
	
	private boolean profileExistDialog(String _profileName) {
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		builder.setTitle(getResources().getText(R.string.warning));
		builder.setMessage(getResources().getText(R.string.profile_name) + " " + _profileName + getString(R.string.alredy_exist) + "." + "/n" + getString(R.string.do_you_want_to_replace_those_profiles));

		builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				yes24 =  true;
			}
		});

		builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				yes24 = false;
			}
		});
		builder.show();
		return false;
	}
	
	
}
