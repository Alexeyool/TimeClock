package com.alexeyool.timeclock.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.timeclock.profiles.WriteExcel;
import com.alexeyool.timeclock.profiles.WriteText;

public class EmailActivity extends Activity {

	public static final String YEAR = "year";
	public static final String MONTH = "month";
	private static final String TEXT_CHECKED = "text";
	private static final String EXCEL_CHECKED = "excel";
	private static final String EMAIL_SET = "email_set";
	private static final String EMAIL_LAST = "email_last";

	Context mContext;

	TextView titleActionBar;
	ImageButton homeActionBar;

	EditText emailET;
	Button sandB, viewB, saveB, getB;
	Spinner monthS, yearS;
	CheckBox textCB, excelCB;
	
	Calendar[] dateArray;
	Calendar dateSelection;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);

		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(R.string.email);
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
		
		setDateSelection();
		createLayout();
		fillLayout();
	}

	private void setDateSelection() {
		dateSelection = Calendar.getInstance();
		Intent intent = getIntent();
		int m = intent.getIntExtra(MONTH, -1);
		int y = intent.getIntExtra(YEAR, -1);
		if(m!= -1 && y!=-1){
			dateSelection.set(Calendar.YEAR, y);
			dateSelection.set(Calendar.MONTH, m);
		}
		
	}

	private void createLayout() {
		emailET = (EditText) findViewById(R.id.editTextAE_email);

		sandB = (Button) findViewById(R.id.buttonAE_send);
		getB = (Button) findViewById(R.id.buttonAE_get);
		saveB = (Button) findViewById(R.id.buttonAE_save);
		viewB = (Button) findViewById(R.id.buttonAE_cancel);
		sandB.setOnClickListener(onClickListener);
		getB.setOnClickListener(onClickListener);
		saveB.setOnClickListener(onClickListener);
		viewB.setOnClickListener(onClickListener);
		
		textCB = (CheckBox) findViewById(R.id.checkBoxAE_text);
		excelCB = (CheckBox) findViewById(R.id.checkBoxAE_excel);
		textCB.setOnCheckedChangeListener(onCheckedChangeListener);
		excelCB.setOnCheckedChangeListener(onCheckedChangeListener);
		
		dateArray = getDateArray();
		spinnerActivationMonth();
		spinnerActivationYear();
	}
	
	OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.checkBoxAE_text:
				setIsTextchecked(isChecked);
				break;
				
			case R.id.checkBoxAE_excel:
				setIsExcelchecked(isChecked);
				break;

			default:
				break;
			}
			
		}
	};

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonAE_send:
				try {
					sendEmail();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case R.id.buttonAE_get:
				getAddress();
				break;

			case R.id.buttonAE_save:
				saveAddress(emailET.getText().toString());
				break;

			case R.id.buttonAE_cancel:
				openMonthView();
				break;

			default:
				break;
			}
		}
	};

	private void fillLayout() {
		emailET.setText(getLastAddress());
		String[] temp = getMonthArray(getYearSelected());
		for(int i=0; i<temp.length; i++){
			if((""+getMonthSelected()).equals(temp[i])){
				monthS.setSelection(i);
			}
		}
		temp = getYearArray();
		for(int i=0; i<temp.length; i++){
			if((""+getYearSelected()).equals(temp)){
				yearS.setSelection(i);
			}
		}
		textCB.setChecked(isTextCheked());
		excelCB.setChecked(isExcelCheked());

	}

	private void spinnerActivationMonth() {
		monthS = (Spinner) findViewById(R.id.spinnerAE_month);
		String[] t = getMonthArray(dateSelection.get(Calendar.YEAR));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_item, t);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthS.setOnItemSelectedListener(onItemSelectedListenerMonth);
		monthS.setAdapter(adapter);
		Calendar cal = Calendar.getInstance();
		for(int i=0;i<t.length;i++){
			if(t[i].equals("" + cal.get(Calendar.MONTH))){
				monthS.setSelection(i);
			}
		}
	}

	OnItemSelectedListener onItemSelectedListenerMonth = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			setMonthSelection(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private void spinnerActivationYear() {
		yearS = (Spinner) findViewById(R.id.spinnerAE_year);
		String[] t = getYearArray();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, 
				android.R.layout.simple_spinner_item, t);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearS.setOnItemSelectedListener(onItemSelectedListenerTravel);
		yearS.setAdapter(adapter);
		Calendar cal = Calendar.getInstance();
		for(int i=0;i<t.length;i++){
			if(t[i].equals("" + cal.get(Calendar.YEAR)))yearS.setSelection(i);
		}
		
	}

	OnItemSelectedListener onItemSelectedListenerTravel = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			setYearSelection(position);
			spinnerActivationMonth();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};
	
	private Calendar[] getDateArray() {
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		Calendar[] result = dbAdapter.getMonthArray();
		dbAdapter.close();
		return result;
	}

	private String[] getYearArray() {
		ArrayList<Integer> temp = new ArrayList<Integer>(); 
		for(int i=0; i<dateArray.length; i++){
			if(!temp.contains(dateArray[i].get(Calendar.YEAR))){
				temp.add(dateArray[i].get(Calendar.YEAR));
			}
		}
		String[] result = new String[temp.size()];
		for(int i=0; i<temp.size(); i++){
			result[i] = ""+temp.get(i);
		}
		return result;
	}
	
	private String[] getMonthArray(int year) {
		ArrayList<Integer> temp = new ArrayList<Integer>(); 
		for(int i=0; i<dateArray.length; i++){
			if(!temp.contains(dateArray[i].get(Calendar.MONTH)+1) 
					&& dateArray[i].get(Calendar.YEAR) == year){
				temp.add(dateArray[i].get(Calendar.MONTH)+1);
			}
		}
		String[] result = new String[temp.size()];
		for(int i=0; i<temp.size(); i++){
			result[i] = ""+temp.get(i);
		}
		return result;
		
	}

	protected void setYearSelection(int position) {
		dateSelection.set(Calendar.YEAR, Integer.valueOf(getYearArray()[position]));
	}

	protected void setMonthSelection(int position) {
		dateSelection.set(Calendar.MONTH, Integer.valueOf(getMonthArray(getYearSelected())[position])-1);

	}
	
	private int getYearSelected() {
		return dateSelection.get(Calendar.YEAR);
	}

	private int getMonthSelected() {
		return dateSelection.get(Calendar.MONTH);
	}

	protected void openMonthView() {
		Intent intent = new Intent(mContext, MonthInformationActivity.class);
		intent.putExtra(YEAR, getYearSelected());
		intent.putExtra(MONTH, getMonthSelected());
		startActivity(intent);
	}

	protected void saveAddress(String address) {
		Set<String> emailSet = MainActivity.sPref.getStringSet(EMAIL_SET, new HashSet<String>());
		emailSet.add(address);
		Editor editor = MainActivity.sPref.edit();
		editor.putStringSet(EMAIL_SET, emailSet);
		editor.commit();
		Toast toast = Toast.makeText(mContext, 
				   getString(R.string.address_saved), Toast.LENGTH_SHORT);
		toast.show();

	}
	
	String[] contextMenuItems;
	boolean[] checkedItems;
	String emailsForSend;
	boolean deleteYes;
	AlertDialog alert;
	
	protected void getAddress() {   	
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle(getResources().getText(R.string.email_address));
		contextMenuItems = getEmailAddressesArray();
		checkedItems = new boolean[contextMenuItems.length];
		
		builder.setMultiChoiceItems(contextMenuItems, checkedItems, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				checkedItems[which] = isChecked;
				
			}
		});

		builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Editor editor = MainActivity.sPref.edit();
				HashSet<String> temp = new HashSet<String>();
				emailsForSend = "";
				for(int i=0; i<contextMenuItems.length; i++){
					if(checkedItems[i]){
						temp.add(contextMenuItems[i]);
						if(i < contextMenuItems.length - 1)emailsForSend = emailsForSend + contextMenuItems[i] + ", ";
						else emailsForSend = emailsForSend + contextMenuItems[i];
					}
				}
				editor.putStringSet(EMAIL_LAST, temp);
				editor.commit();
				emailET.setText(emailsForSend);
			}
		});

		builder.setNeutralButton(getResources().getText(R.string.delete), new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteDialog();
				if(deleteYes){
					alert.cancel();
					getAddress();
				}

			}

		});

		builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alert = builder.create();
		alert.show();
	}

	protected void deleteDialog(){   	
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle(getResources().getText(R.string.warning));
		builder.setMessage(getResources().getText(R.string.delete) +" " + emailsForSend + "?");

		builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteEmail();
				deleteYes = true;
			}
		});

		builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteYes = false;
			}
		});
		builder.show();
	}

	protected void deleteEmail() {
		Editor editor = MainActivity.sPref.edit();
		ArrayList<String> array = getEmailAddressesArrayList();
		emailET.setText("");
		editor.remove(EMAIL_LAST);
		int z = 0;
		for(int i=0; i<array.size(); i++){
			if(checkedItems[i]){
				array.remove(i-z);
				z++;
			}
		}
		HashSet<String> result = new HashSet<String>();
		for(int i=0; i<array.size();i++){
			result.add(array.get(i).toString());
		}
		editor.putStringSet(EMAIL_SET, result);
		editor.commit();

	}

	public static ArrayList<String> getEmailAddressesArrayList() {
		ArrayList<String> array = new ArrayList<String>();
		array.addAll(MainActivity.sPref.getStringSet(EMAIL_SET, new HashSet<String>()));
		return array;
	}

	public static String[] getEmailAddressesArray() {
		ArrayList<String> array = getEmailAddressesArrayList();
		String[] result = new String[array.size()];
		for(int i=0; i<array.size();i++){
			result[i]  = array.get(i).toString();
		}
		return result;
	}

	protected void sendEmail() throws Exception {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		emailIntent.setType("plain/text");
		ArrayList<String> array = new ArrayList<String>();
		array.addAll(MainActivity.sPref.getStringSet(EMAIL_LAST, new HashSet<String>()));
		String[] emailAddresses = new String[array.size()];
		for(int i=0; i<emailAddresses.length; i++){
			emailAddresses[i] = array.get(i);
		}
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailAddresses);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
		ArrayList<Uri> uris = new ArrayList<Uri>();
		if(textCB.isChecked()){
			File file = WriteText.main(dateSelection, mContext);
			String fileDirection = file.getPath();
			uris.add(Uri.parse("file://" + fileDirection));
		}
		if(excelCB.isChecked()){
			File file = WriteExcel.main(dateSelection, mContext);
			String fileDirection = file.getAbsolutePath();
			uris.add(Uri.parse("file://" + fileDirection));
		}
	     emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//	     startActivity(emailIntent);
		startActivity(Intent.createChooser(emailIntent, "send"));
	}

	private boolean isExcelCheked() {
		return MainActivity.sPref.getBoolean(EXCEL_CHECKED, false);
	}

	private boolean isTextCheked() {
		return MainActivity.sPref.getBoolean(TEXT_CHECKED, false);
	}
	
	private void setIsExcelchecked(boolean isChecked) {
		Editor editor = MainActivity.sPref.edit();
		editor.putBoolean(EXCEL_CHECKED, isChecked);
		editor.commit();
		
	}

	private void setIsTextchecked(boolean isChecked) {
		Editor editor = MainActivity.sPref.edit();
		editor.putBoolean(TEXT_CHECKED, isChecked);
		editor.commit();
		
	}

	private String getLastAddress() {
		Set<String> temp = MainActivity.sPref.getStringSet(EMAIL_LAST, new HashSet<String>());
		String s = "";
		String[] array = new String[temp.size()];
		temp.toArray(array);
		for(int i=0; i<array.length; i++){
			if(i<array.length-1) s = s + array[i] + ", ";
			else s = s + array[i];
		}
		return s;
	}

	public void generateNoteOnSD(String sFileName, String sBody){
		try
		{
			File root = new File(Environment.getExternalStorageDirectory(), "Notes");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
			Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
