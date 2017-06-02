package com.alexeyool.day_information;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.Calculation;
import com.alexeyool.timeclock.profiles.Checkup;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class DayInformationEditQuickFragment extends Fragment{
	View v;
	Spinner profileNameSpinner;
	
	String[] profileNameArray;
	public String[] shiftTypeArray = {"Weekday", "Weekend", "Holiday", "Sickday", "Vacation"};
	
	EditText startTimeET, startDateET, endTimeET, endDateET, dateET;
	Button edit;
	TextView error;
	String profileName;
	String shiftType;
	int dayOhHourEQF;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.day_information_edit_quick, container, false);
		spinnerActivationProfile();
		fillTime(true);
		return v;
	}
	
	private void fillTime(boolean first) {
		DayInformationActivity.wShift.editByProfile(
				new ProfileData(getActivity()).loadProfile(getActivity(), profileNameSpinner.getSelectedItem().toString()));
		if(!first){
			if(dayOhHourEQF == DayInformationActivity.wShift.dayOrHour) return;
		}
		dayOhHourEQF = DayInformationActivity.wShift.dayOrHour;
		TableRow startTR = (TableRow) v.findViewById(R.id.tableRowDIEQ_time_start);
		TableRow endTR = (TableRow) v.findViewById(R.id.tableRowDIEQ_time_end);
		TableRow dateTR = (TableRow) v.findViewById(R.id.tableRowDIEQ_date);
		if(DayInformationActivity.wShift.dayOrHour == WorkeShift.DAY){
			startTR.setVisibility(View.GONE);
			endTR.setVisibility(View.GONE);
			dateTR.setVisibility(View.VISIBLE);
			
			dateET = (EditText) v.findViewById(R.id.editTextDIEQ_date);
			dateET.setText(DayInformationActivity.wShift.getDateString(WorkeShift.S));
			dateET.setLongClickable(true);
			dateET.setOnClickListener(onClickListener);
		}
		else{
			startTR.setVisibility(View.VISIBLE);
			endTR.setVisibility(View.VISIBLE);
			dateTR.setVisibility(View.GONE);

			startTimeET = (EditText) v.findViewById(R.id.EditTextDIEQ_start_time);
			startDateET = (EditText) v.findViewById(R.id.EditTextDIEQ_start_date);
			endTimeET = (EditText) v.findViewById(R.id.EditTextDIEQ_end_time);
			endDateET = (EditText) v.findViewById(R.id.EditTextDIEQ_end_date);

			startTimeET.setText(DayInformationActivity.wShift.getTimeString(WorkeShift.S));
			startDateET.setText(DayInformationActivity.wShift.getDateString(WorkeShift.S));
			endTimeET.setText(DayInformationActivity.wShift.getTimeString(WorkeShift.E));
			endDateET.setText(DayInformationActivity.wShift.getDateString(WorkeShift.E));

			startTimeET.setLongClickable(true);
			startTimeET.setOnClickListener(onClickListener);
			//		startTimeET.setOnFocusChangeListener(onFocusChangeListener);
			startDateET.setLongClickable(true);
			startDateET.setOnClickListener(onClickListener);
			//		startDateET.setOnFocusChangeListener(onFocusChangeListener);
			endTimeET.setLongClickable(true);
			endTimeET.setOnClickListener(onClickListener);
			//		endTimeET.setOnFocusChangeListener(onFocusChangeListener);
			endDateET.setLongClickable(true);
			endDateET.setOnClickListener(onClickListener);
			//		endDateET.setOnFocusChangeListener(onFocusChangeListener);
		}
		edit = (Button) v.findViewById(R.id.buttonDIEQ_full_edit);
		error = (TextView) v.findViewById(R.id.textViewDIEQ_error);

		error.setText("");

		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DayInformationEditFragment fragmentEdit = new DayInformationEditFragment();
				FragmentTransaction fTrans = getFragmentManager().beginTransaction();	
				fTrans.replace(R.id.fragment_container, fragmentEdit, "fragmentEdit212wwe");
				fTrans.commit();
				getFragmentManager().executePendingTransactions();

			}
		});
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			WorkeShift temp = new WorkeShift(getActivity());
			switch (v.getId()) {
			case R.id.EditTextDIEQ_start_time:
				temp.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
				timeSetDialog(temp.start, WorkeShift.S);
				break;
			case R.id.EditTextDIEQ_end_time:
				temp.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), endDateET.getText().toString());
				timeSetDialog(temp.end, WorkeShift.E);
				break;
			case R.id.EditTextDIEQ_start_date:
				temp.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
				dateSetDialog(temp.start, WorkeShift.S);
				break;
			case R.id.EditTextDIEQ_end_date:
				temp.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), endDateET.getText().toString());
				dateSetDialog(temp.end, WorkeShift.E);
				break;
			case R.id.editTextDIEQ_date:
				temp.addTimeAndDate(WorkeShift.S, "00:00", dateET.getText().toString());
				dateSetDialog(temp.start, WorkeShift.S);
				break;

			default:
				break;
			}
		}
	};

	private void spinnerActivationProfile() {
		profileNameSpinner = (Spinner) v.findViewById(R.id.SpinnerDIEQ_profile_name);
		profileNameArray = ProfileData.getProfilesNamseArray();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_spinner_item, profileNameArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		profileNameSpinner.setOnItemSelectedListener(onItemSelectedListener);
		profileNameSpinner.setAdapter(adapter);
		profileNameSpinner.setSelection(ProfileData.getChoisenBoxPosition());
	}
	
	OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
				profileName = ProfileData.getProfilesNamseArrayList().get(position);
				fillTime(false);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	public boolean collectShift() {
		if(DayInformationActivity.wShift.dayOrHour == WorkeShift.DAY){
			if(!Checkup.dateCheck(dateET.getText().toString())){
				error.setText(getString(R.string.start_date_format_error));
				return false;
				}
			DayInformationActivity.wShift.profileName = ProfileData.getProfilesNamseArrayList().get(profileNameSpinner.getSelectedItemPosition());
			DayInformationActivity.wShift.addTimeAndDate(WorkeShift.S, "00:00", dateET.getText().toString());
			return true;
		}
		else{
			if(!checkDateCollectionForErrors(startTimeET.getText().toString(),
					endTimeET.getText().toString(),
					startDateET.getText().toString(),
					endDateET.getText().toString())){
				return false;
			}
			DayInformationActivity.wShift.profileName = ProfileData.getProfilesNamseArrayList().get(profileNameSpinner.getSelectedItemPosition());
			DayInformationActivity.wShift.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
			DayInformationActivity.wShift.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), endDateET.getText().toString());
			return true;
		}
	}
	
	public boolean checkDateCollectionForErrors(String startTime, String endTime, String startDate, String endDate){
		if(!Checkup.timeCheck(startTime)){
			error.setText(getString(R.string.start_time_format_error));
			return false;
		}
		if(!Checkup.timeCheck(endTime)){
			error.setText(getString(R.string.end_time_format_error));
			return false;
		}
		if(!Checkup.dateCheck(startDate)){
			error.setText(getString(R.string.start_date_format_error));
			return false;
		}
		if(!Checkup.dateCheck(endDate)){
			error.setText(getString(R.string.end_date_format_error));
			return false;
		}
		if(!Checkup.startMoreThanEnd(startTime, endTime, startDate, endDate)){
			error.setText(getString(R.string.start_more_than_end));
			return false;
		}
		if(Checkup.amountOfHoursMoreThan24(startTime, endTime, startDate, endDate)){
			amountOfHoursMoreThan24Dialog();
			return yes24;
		}
		return true;
	}
	
	boolean yes24;
	
	private void amountOfHoursMoreThan24Dialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle(getResources().getText(R.string.warning));
		builder.setMessage(getResources().getText(R.string.amount_of_hours_more_than_24) + "\n" + getString(R.string.save) + "?");

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
		
	}

	String startOrEnd;
	
	public void timeSetDialog(Calendar cal, String _startOrEnd){
		startOrEnd = _startOrEnd;
		TimePickerDialog dialog = new TimePickerDialog(getActivity(), onTimeSetListener, 
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
		dialog.show();

	}
	
	WorkeShift n;
	
	OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			changeTimeInEditText(hourOfDay, minute);

		}
	};

	public void dateSetDialog(Calendar cal, String _startOrEnd){
		startOrEnd = _startOrEnd;
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener, 
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	OnDateSetListener onDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
		changeDateInEditText(year, monthOfYear, dayOfMonth);
		}
	};
	
	String tempString = "";
	EditText tempET;
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				tempET = (EditText) v;
				tempString = tempET.getText().toString();
			}
			else{
				WorkeShift m = new WorkeShift(getActivity());
				m.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
				m.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), startDateET.getText().toString());
				switch (v.getId()) {
				case R.id.EditTextDIEQ_start_time:
					if(Checkup.timeCheck(startTimeET.getText().toString())){
						startOrEnd = WorkeShift.S;
						n = null;
						changeTimeInEditText(m.start.get(Calendar.HOUR_OF_DAY), m.start.get(Calendar.MINUTE));
					}
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEQ_end_time:
					if(Checkup.timeCheck(endTimeET.getText().toString())){
						startOrEnd = WorkeShift.E;
						n =null;
						changeTimeInEditText(m.end.get(Calendar.HOUR_OF_DAY), m.end.get(Calendar.MINUTE));
					}
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEQ_start_date:
					if(Checkup.dateCheck(startDateET.getText().toString())){
						startOrEnd = WorkeShift.S;
						n =null;
						changeDateInEditText(m.start.get(Calendar.YEAR), m.start.get(Calendar.MONTH), m.start.get(Calendar.DAY_OF_MONTH));
					}
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEQ_end_date:
					if(Checkup.dateCheck(endDateET.getText().toString())){
						startOrEnd = WorkeShift.E;
						n = null;
						changeDateInEditText(m.end.get(Calendar.YEAR), m.end.get(Calendar.MONTH), m.end.get(Calendar.DAY_OF_MONTH));
					
					}
					else tempET.setText(tempString);
					break;

				default:
					break;
				}
			}

		}
	};
	
	private void changeTimeInEditText(int hourOfDay, int minute){
		if(startOrEnd.equals(WorkeShift.S)) startTimeET.setText(String.format("%02d:%02d", hourOfDay, minute));
		else endTimeET.setText(String.format("%02d:%02d", hourOfDay, minute));
		if(n==null){
			n = new WorkeShift(getActivity());
			n.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
			n.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), startDateET.getText().toString());
			if(Calculation.timeMinus(n.end, n.start)<0){
				n.end = n.start;
				n.end.add(Calendar.DAY_OF_MONTH, 1);
				endDateET.setText(String.format("%02d/%02d/%2d",  n.end.get(Calendar.DAY_OF_MONTH), n.end.get(Calendar.MONTH) + 1, n.end.get(Calendar.YEAR)));
			}
			else{
				n.end = n.start;
				endDateET.setText(String.format("%02d/%02d/%2d",  n.end.get(Calendar.DAY_OF_MONTH), n.end.get(Calendar.MONTH) + 1, n.end.get(Calendar.YEAR)));
			}
		}
		else n=null;

	}
	
	private void changeDateInEditText(int year, int monthOfYear, int dayOfMonth){
		if(DayInformationActivity.wShift.dayOrHour == WorkeShift.DAY){
			dateET.setText(String.format("%02d/%02d/%4d", dayOfMonth, monthOfYear + 1, year));
		}
		else{
			if(startOrEnd.equals(WorkeShift.S)){
				startDateET.setText(String.format("%02d/%02d/%4d", dayOfMonth, monthOfYear +1, year));
				if(n==null){
					n = new WorkeShift(getActivity());
					n.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
					n.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), startDateET.getText().toString());
					if(Calculation.timeMinus(n.end, n.start)<0){
						n.end = n.start;
						n.end.add(Calendar.DAY_OF_MONTH, 1);
						endDateET.setText(String.format("%02d/%02d/%2d",  n.end.get(Calendar.DAY_OF_MONTH), n.end.get(Calendar.MONTH) +1, n.end.get(Calendar.YEAR)));
					}
					else{
						n.end = n.start;
						endDateET.setText(String.format("%02d/%02d/%2d",  n.end.get(Calendar.DAY_OF_MONTH), n.end.get(Calendar.MONTH) +1, n.end.get(Calendar.YEAR)));
					}
				}
				else n=null;
			}
			else endDateET.setText(String.format("%02d/%02d/%2d",  dayOfMonth, monthOfYear +1, year));
		}
	}
}
