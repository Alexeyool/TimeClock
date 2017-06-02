package com.alexeyool.timeclock.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.day_information.DayInformationActivity;
import com.alexeyool.profile.ProfileActivity;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	
	private static final String MAIN_PREF = "main_pref";
	public static final String DEF = "def";
	public static final String NEW_PROFILE = "new_profile";
	
	public static final String PROFILE_NAME = "profile_name";
	private static final String START_YEAR = "start_year";
	private static final String START_MONTH = "start_month";
	private static final String START_DAY = "start_day";
	private static final String START_HOUR = "start_hour";
	private static final String START_MINUTE = "start_minute";
	private static final String TEXT = "text";
	public static final String MAIN_PROFILE_NAME = "main_profile_name";
	public static final String SHIFT_ID = "shift_id";
	public static final String DONT_OPEN_START_DIALOG = "open_start_dialog";
	public static final String SHIFT_TYPE = "shift_type";
	public static final String LENGUAGE = "lenguage";
	private static final String START_ON = "start_on";

	Context mContext;
	Calendar currentDate;
	
	WorkeShift wShift;
	
	TextView dateActionBar;
	ImageButton settingsActionBar;
	
	DrawerLayout mDrawer;	
	ActionBarDrawerToggle mDrawerToggle;
	ListView listViewDrawer;
	ListViewAdapterDrawer drawerListViewAdapter;
	public ArrayList<String> titleForList = new ArrayList<String>();
	
	Button start, stop, cancel, self;
	public static TextView daysTV, hoursTV, moneyTV;
	
	ListView listViewMain;
	ListViewAdapterMain listViewAdapterMain;
	WorkeShift[] wShiftArray;
	
	public static SharedPreferences sPref;
	Editor editor;
	
	TextView monthRollTV;
	Button nextB, previewB;
	Calendar[] monthArray;
	int positionInMonthArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		currentDate = Calendar.getInstance(); 
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
 
		View mCustomView = mInflater.inflate(R.layout.action_bar_main, null);
		dateActionBar =  (TextView) mCustomView.findViewById(R.id.TextViewABM_title);
		settingsActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABM_settings);
		
		dateActionBar.setText(R.string.shifts_list);
		settingsActionBar.setOnClickListener(imageButtonoClickListener);
		
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		start = (Button) findViewById(R.id.buttonMA_start);
		stop = (Button) findViewById(R.id.buttonMA_stop);
		cancel = (Button) findViewById(R.id.buttonMA_cancel);
		self = (Button) findViewById(R.id.buttonMA_self);
		
		start.setOnClickListener(onClickListener);
		stop.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		self.setOnClickListener(onClickListener);
		
		listViewMain = (ListView) findViewById(R.id.listViewMA);
		
		sPref = getSharedPreferences(MAIN_PREF,MODE_PRIVATE);
		editor = sPref.edit();
		if(!sPref.contains(ProfileData.ALL_PROFILES_NAMES)){
			ProfileData.setDefaultProfile();
		}
		
		drawerLayoutSet();
		createLayoutMonthRoll();
		fillLayoutMonthRoll(monthArray[positionInMonthArray]);
		fillListViewMain(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH));
	}
	
	private void fillLayoutMonthRoll(Calendar _cal) {
		if(positionInMonthArray == 0){
			previewB.setClickable(false);
		}
		else previewB.setClickable(true);
		if(positionInMonthArray == monthArray.length-1){
			nextB.setClickable(false);
		}
		else nextB.setClickable(true);
		
		monthRollTV.setText((_cal.get(Calendar.MONTH)+1) + "/" + _cal.get(Calendar.YEAR));
		fillListViewMain(_cal.get(Calendar.YEAR), _cal.get(Calendar.MONTH));
		
	}

	private Calendar[] getMonthArray() {
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		Calendar[] result = dbAdapter.getMonthArray();
		dbAdapter.close();
		return result;
	}
	
	private void createLayoutMonthRoll() {
		nextB = (Button) findViewById(R.id.ButtonMA_next);
		previewB = (Button) findViewById(R.id.ButtonMA_month_preview);
		monthRollTV = (TextView) findViewById(R.id.textViewMA_month);
		
		daysTV = (TextView) findViewById(R.id.TextViewMA_total_type);
		hoursTV = (TextView) findViewById(R.id.TextViewMA_total_end);
		moneyTV = (TextView) findViewById(R.id.TextViewMA_total_many);
		
		nextB.setOnClickListener(onClickListenerMonthRoll);
		previewB.setOnClickListener(onClickListenerMonthRoll);
		monthArray = getMonthArray();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		positionInMonthArray = getPositionInMonthArray(cal);
		
	}

	OnClickListener onClickListenerMonthRoll = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ButtonMA_next:
				if(positionInMonthArray < monthArray.length-1){
					positionInMonthArray++;
					fillLayoutMonthRoll(monthArray[positionInMonthArray]);
				}
				break;
				
			case R.id.ButtonMA_month_preview:
				if(positionInMonthArray > 0){
					positionInMonthArray--;
					fillLayoutMonthRoll(monthArray[positionInMonthArray]);
				}
				break;

			default:
				break;
			}
			
		}
	};
	
	private int getPositionInMonthArray(Calendar _cal) {
		for(int i=0; i<monthArray.length; i++){
			if(monthArray[i].get(Calendar.YEAR) == _cal.get(Calendar.YEAR) 
					&& monthArray[i].get(Calendar.MONTH) == _cal.get(Calendar.MONTH)) return i;
		}
		return -1;
	}
	
	int[] settingsTitles = {R.string.monthly_information, R.string.shift_profiles, R.string.add_shift, R.string.send_to_email};
	
	private void drawerLayoutSet(){
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		listViewDrawer = (ListView) findViewById(R.id.list_slidermenu);
		for(int i =0; i<settingsTitles.length; i++){
			titleForList.add(getString(settingsTitles[i]));
		}
		drawerListViewAdapter = new ListViewAdapterDrawer(mContext, titleForList);
		listViewDrawer.setAdapter(drawerListViewAdapter);
		listViewDrawer.setOnItemClickListener(drawerOniItemClickListener);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.drawable.ic_launcher, R.string.empty, R.string.empty);
		mDrawer.setDrawerListener(mDrawerToggle);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		if (mDrawerToggle != null) mDrawerToggle.syncState();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) return true;
		return super.onOptionsItemSelected(item);
	} 
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (mDrawerToggle != null)mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	OnItemClickListener drawerOniItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent;
			switch (position) {
			case 0:
				intent = new Intent(mContext, MonthInformationActivity.class);
				startActivity(intent);
				mDrawer.closeDrawer(Gravity.LEFT);
				break;

			case 1:
				intent = new Intent(mContext, ProfileActivity.class);
				startActivity(intent);
				mDrawer.closeDrawer(Gravity.LEFT);
				break;

			case 2:
				onClickSelf();
				mDrawer.closeDrawer(Gravity.LEFT);
				break;

			case 3:
				intent = new Intent(mContext, EmailActivity.class);
				startActivity(intent);
				mDrawer.closeDrawer(Gravity.LEFT);
				break;

			default:
				break;
			}

		}
	};
	
	OnClickListener imageButtonoClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (mDrawer == null) return;
			if(mDrawer.isDrawerOpen(Gravity.LEFT)) mDrawer.closeDrawer(Gravity.LEFT);
			else mDrawer.openDrawer(Gravity.LEFT);
			
		}
	};
	
	private void fillListViewMain(int _year, int _month) {
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		wShiftArray = dbAdapter.getAllItems(_year, _month);
		dbAdapter.close();
		currentDate.setTime(new Date());
		if(sPref.getInt(START_YEAR, 0)>0){
			if(monthArray[positionInMonthArray].get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) 
					&& monthArray[positionInMonthArray].get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)){
				WorkeShift[] temp = new WorkeShift[wShiftArray.length+1];
				for(int i=1; i<temp.length; i++){
					temp[i] = wShiftArray[i-1];
				}
				temp[0] = new WorkeShift(mContext,
						sPref.getString(PROFILE_NAME, DEF),
						sPref.getInt(START_YEAR, 0), 
						sPref.getInt(START_MONTH, 0), 
						sPref.getInt(START_DAY, 0), 
						sPref.getInt(START_HOUR, 0), 
						sPref.getInt(START_MINUTE, 0), 
						sPref.getString(TEXT, ""), 
						0);
				wShiftArray = temp;
			}
		}
		listViewMain.setOnItemClickListener(mainOnItemClickListener);
		listViewAdapterMain = new ListViewAdapterMain(mContext, wShiftArray);
		listViewMain.setAdapter(listViewAdapterMain);

	}
	OnItemClickListener mainOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(!sPref.getBoolean(START_ON, false)){
			Intent intentTemp = new Intent(mContext, DayInformationActivity.class);
			intentTemp.putExtra(SHIFT_ID, wShiftArray[position].id);
			startActivity(intentTemp);
			}
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonMA_start:
				if(sPref.getBoolean(START_ON, false)) return;
				startDialog();
				break;
			case R.id.buttonMA_stop:
				if(!sPref.getBoolean(START_ON, false)) return;
				onClickStop();
				break;
			case R.id.buttonMA_self:
				onClickSelf();
				break;
			case R.id.buttonMA_cancel:
				if(!sPref.getBoolean(START_ON, false)) return;
				onClickCancel();
				break;

			default:
				break;
			}
			
		}
	};


	protected void onClickStart() {
		editor.putString(PROFILE_NAME, sPref.getString(PROFILE_NAME, DEF));
		editor.putInt(START_YEAR, wShift.start.get(Calendar.YEAR));
		editor.putInt(START_MONTH, wShift.start.get(Calendar.MONTH));
		editor.putInt(START_DAY, wShift.start.get(Calendar.DAY_OF_MONTH));
		editor.putInt(START_HOUR, wShift.start.get(Calendar.HOUR_OF_DAY));
		editor.putInt(START_MINUTE, wShift.start.get(Calendar.MINUTE));
		editor.commit();
		
		currentDate.setTime(new Date());
		positionInMonthArray = getPositionInMonthArray(currentDate);
		fillLayoutMonthRoll(currentDate);
		if(wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY) 
				|| wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
			if(wShift.dayOrHour == WorkeShift.DAY){
				onClickStop();
				return;
			}
		}
		editor.putBoolean(START_ON, true);
		editor.commit();
		
	}

	protected void onClickCancel() {
			editor.remove(PROFILE_NAME);
			editor.remove(START_YEAR);
			editor.remove(START_MONTH);
			editor.remove(START_DAY);
			editor.remove(START_HOUR);
			editor.remove(START_MINUTE);
			editor.commit();

			currentDate.setTime(new Date());
			positionInMonthArray = getPositionInMonthArray(currentDate);
			fillLayoutMonthRoll(currentDate);
			editor.putBoolean(START_ON, false);
			editor.commit();
	}

	protected void onClickSelf() {
		Intent intentTemp = new Intent(mContext, DayInformationActivity.class);
		intentTemp.putExtra(PROFILE_NAME, NEW_PROFILE);
		startActivity(intentTemp);
		
	}

	protected void onClickStop() {
		wShift = new WorkeShift(mContext);
		ProfileData prof = new ProfileData(mContext);
		wShift.editByProfile( prof.loadProfile(mContext, sPref.getString(PROFILE_NAME, DEF)));
		currentDate.setTime(new Date());
		wShift.start = wShift.setCalendar(
				sPref.getInt(START_YEAR, currentDate.get(Calendar.YEAR)), 
				sPref.getInt(START_MONTH, currentDate.get(Calendar.MONTH)),
				sPref.getInt(START_DAY, currentDate.get(Calendar.DAY_OF_MONTH)), 
				sPref.getInt(START_HOUR, currentDate.get(Calendar.HOUR_OF_DAY)), 
				sPref.getInt(START_MINUTE, currentDate.get(Calendar.MINUTE)));
		wShift.setDate(WorkeShift.E, currentDate);
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		dbAdapter.insertItem(wShift);
		dbAdapter.close();
		onClickCancel();
		
	}
	
	int myWhich = 0;
	boolean isChecked = false;
	CharSequence[] contextMenuItems;
	AlertDialog alert;
	
	public void startDialog(){
		if(sPref.getBoolean(DONT_OPEN_START_DIALOG, true)){
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(getResources().getText(R.string.shift_profile));
			contextMenuItems = ProfileData.getProfilesNamseArray();

			builder.setSingleChoiceItems(contextMenuItems, myWhich, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which){
					myWhich=which;
					if(isChecked){
						editor.putString(MAIN_PROFILE_NAME, contextMenuItems[which].toString());
						editor.putBoolean(DONT_OPEN_START_DIALOG, false);
						
					}
					editor.putString(PROFILE_NAME, contextMenuItems[which].toString());
					editor.commit();
					wShift = new WorkeShift(mContext);
					wShift.editByProfile(new ProfileData(mContext).loadProfile(mContext, contextMenuItems[which].toString()));
					onClickStart();
					alert.cancel();
				}
			});
			CheckBox cBox = new CheckBox(mContext);
			cBox.setText(getResources().getText(R.string.remember_the_choise_and_dont_ask_again));
			cBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean _isChecked) {
					isChecked = _isChecked;

				}
			});
			builder.setView(cBox);
			alert = builder.create();
			alert.show();
		}
		else{
			wShift = new WorkeShift(mContext);
			wShift.editByProfile(new ProfileData(mContext).loadProfile(mContext, sPref.getString(MAIN_PROFILE_NAME, DEF)));
			editor.putString(PROFILE_NAME, sPref.getString(MAIN_PROFILE_NAME, DEF));
			editor.commit();
			onClickStart();
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent(mContext, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
