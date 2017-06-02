package com.alexeyool.day_information;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class DayInformationActivity extends FragmentActivity {

	protected static final String EDIT = "edit";
	protected static final String TEXT = "text";
	public static final int EDIT_QUICK_FRAGMENT = 0;
	public static final int EDIT_WEEKDAY_DAY_FRAGMENT = 1;
	public static final int EDIT_WEEKDAY_HOUR_FRAGMENT = 2;
	public static final int EDIT_SICKDAY_FRAGMENT = 3;
	public static final int TEXT_FRAGMENT = 4;
	
	public static int fragmentIsOnNow;
	Context mContext;
	boolean quick;

	TextView titleActionBar;
	ImageButton homeActionBar;

	Button edit, delete;
	View mCustomView;
	String layoutKind;
	
	Spinner profileNameET;
	
	long shiftId;
	public static WorkeShift wShift;
	
	DayInformationEditFragment fragmentEdit;
	DayInformationTextFragment fragmentText;
	DayInformationEditQuickFragment fragmentEditQuick;
	FragmentTransaction fTrans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_information);

		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(R.string.shift);
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

		edit = (Button) findViewById(R.id.buttonADI_edit);
		delete = (Button) findViewById(R.id.buttonADI_delete);
		edit.setOnClickListener(onClickListener);
		delete.setOnClickListener(onClickListener);
		fragmentEdit = new DayInformationEditFragment();
		fragmentText = new DayInformationTextFragment();
		fragmentEditQuick = new DayInformationEditQuickFragment();
		
		Intent intent = getIntent();
		shiftId = intent.getLongExtra(MainActivity.SHIFT_ID, -1);
		if(shiftId == -1){
			layoutKind = EDIT;
			quick = true;
			edit.setText(R.string.save);
			delete.setText(R.string.cancel);
			wShift = new WorkeShift(mContext);
			insertView(layoutKind, wShift);
		}
		else{
			layoutKind = TEXT;
			edit.setText(R.string.edit);
			delete.setText(R.string.delete);
			wShift = new WorkeShift(mContext, shiftId);
			insertView(layoutKind, wShift);
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonADI_edit:
				if(layoutKind.equals(TEXT)){
					layoutKind = EDIT;
					edit.setText(R.string.save);
					delete.setText(R.string.cancel);
					insertView(layoutKind, wShift);
				}
				else{
					if(fragmentIsOnNow == EDIT_QUICK_FRAGMENT) {
						if(!fragmentEditQuick.collectShift()) return;
					}
					else{
						if(!fragmentEdit.collectShift()) return;
					}
					shiftId = saveShift(wShift);
					wShift.id = shiftId;
					layoutKind = TEXT;
					edit.setText(R.string.edit);
					delete.setText(R.string.delete);
					if(fragmentIsOnNow == EDIT_QUICK_FRAGMENT) {
						Intent intent = new Intent(mContext, MainActivity.class);
						startActivity(intent);
						finish();
					}
					else insertView(layoutKind, wShift);
				}
				break;

			case R.id.buttonADI_delete:
				if(layoutKind.equals(TEXT)){
					deleteConfirmation();
				}
				else{
					if(shiftId == -1){
						Intent intentTemp = new Intent(mContext, MainActivity.class);
						startActivity(intentTemp);
						finish();
					}
					else{
						layoutKind = TEXT;
						edit.setText(R.string.edit);
						delete.setText(R.string.delete);
						wShift = new WorkeShift(mContext, shiftId);
						insertView(layoutKind, wShift);
					}
				}
				break;

			default:
				break;
			}

		}
	};

	private void insertView(String _layoutKind, WorkeShift _wShift){	 
		if(_layoutKind.equals(TEXT)){
			fragmentIsOnNow = TEXT_FRAGMENT;
				fTrans = getFragmentManager().beginTransaction();	
				fTrans.replace(R.id.fragment_container, fragmentText, "fragmentEdit212wwe");
				fTrans.commit();
		}
		else{
			if(quick){
				fragmentIsOnNow = EDIT_QUICK_FRAGMENT;
				fTrans = getFragmentManager().beginTransaction();	
				fTrans.replace(R.id.fragment_container, fragmentEditQuick, "fragmentEdit212wwe");
				fTrans.commit();
				quick = false;
			}
			else{
				fTrans = getFragmentManager().beginTransaction();	
				fTrans.replace(R.id.fragment_container, fragmentEdit, "fragmentEdit212wwe");
				fTrans.commit();
			}
		}

	}
	
	protected void deleteConfirmation() {  	
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(getResources().getText(R.string.warning));
			builder.setMessage(getResources().getText(R.string.delete) +" " + getString(R.string.shift1) + "?");

			builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
					dbAdapter.open();
					dbAdapter.removeItem(shiftId);
					dbAdapter.close();
					Intent intentTemp = new Intent(mContext, MainActivity.class);
					startActivity(intentTemp);
					finish();
				}
			});

			builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.show();
		}

	protected long saveShift(WorkeShift collectedShift) {
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		long result = dbAdapter.updateItem(collectedShift);
		dbAdapter.close();
		return result;
		
	}

}
