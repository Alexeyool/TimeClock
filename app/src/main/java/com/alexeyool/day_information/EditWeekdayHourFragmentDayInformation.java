package com.alexeyool.day_information;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.Calculation;
import com.alexeyool.timeclock.profiles.Checkup;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class EditWeekdayHourFragmentDayInformation extends Fragment{
	
	View v;
	
	EditText startTimeET, startDateET, endTimeET, endDateET;
	EditText payPerHourET, travelPerDayET, notPayBreakET, awardET, flowET, noteET;
	TextView hoursTV, moneyTV, errorTime, errorMisc;
	
	float sum = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.day_information_edit_workday_per_hour, container, false);
		fillLayout();
		return v;
	}
	
	private void fillLayout() {
		startTimeET = (EditText) v.findViewById(R.id.EditTextDIEW_start_time);
		startDateET = (EditText) v.findViewById(R.id.EditTextDIEW_start_date);
		endTimeET = (EditText) v.findViewById(R.id.EditTextDIEW_end_time);
		endDateET = (EditText) v.findViewById(R.id.EditTextDIEW_end_date);
		payPerHourET = (EditText) v.findViewById(R.id.editTextDIEW_pay_per_hour_money);
		travelPerDayET = (EditText) v.findViewById(R.id.editTextDIEW_travel_per_day);
		notPayBreakET = (EditText) v.findViewById(R.id.EditTextDIEW_not_pay_break_Time);
		awardET = (EditText) v.findViewById(R.id.EditTextDIEW_award_money);
		flowET = (EditText) v.findViewById(R.id.EditTextDIEW_flow_money);
		noteET = (EditText) v.findViewById(R.id.editTextDIEW_note);
		
		hoursTV = (TextView) v.findViewById(R.id.textViewDIEW_total_hours);
		moneyTV = (TextView) v.findViewById(R.id.textViewDIEW_total_money);
		errorTime = (TextView) v.findViewById(R.id.textViewDIEW_time_error);
		errorMisc = (TextView) v.findViewById(R.id.textViewDIEW_misc_error);
		
		startTimeET.setText(DayInformationActivity.wShift.getTimeString(WorkeShift.S));
		startDateET.setText(DayInformationActivity.wShift.getDateString(WorkeShift.S));
		endTimeET.setText(DayInformationActivity.wShift.getTimeString(WorkeShift.E));
		endDateET.setText(DayInformationActivity.wShift.getDateString(WorkeShift.E));
		payPerHourET.setText(""+DayInformationActivity.wShift.payPer);
		travelPerDayET.setText(""+DayInformationActivity.wShift.travelPerDay);
		notPayBreakET.setText(""+DayInformationActivity.wShift.notPayBreak);
		awardET.setText(""+DayInformationActivity.wShift.award);
		flowET.setText(""+DayInformationActivity.wShift.flow);
		noteET.setText(DayInformationActivity.wShift.text);
		fillProcents();
		long time = Calculation.calculateHours(DayInformationActivity.wShift);
		hoursTV.setText(""+ (time/60) + " : " + (time - time/60*60));
		moneyTV.setText(""+ Calculation.round(Calculation.calculateTotalManey(DayInformationActivity.wShift), 2));
		
		startTimeET.setClickable(true);
		startTimeET.setOnClickListener(onClickListener);
//		startTimeET.setOnFocusChangeListener(onFocusChangeListener);
		startDateET.setClickable(true);
		startDateET.setOnClickListener(onClickListener);
//		startDateET.setOnFocusChangeListener(onFocusChangeListener);
		endTimeET.setClickable(true);
		endTimeET.setOnClickListener(onClickListener);
//		endTimeET.setOnFocusChangeListener(onFocusChangeListener);
		endDateET.setClickable(true);
		endDateET.setOnClickListener(onClickListener);
//		endDateET.setOnFocusChangeListener(onFocusChangeListener);
		
//		payPerHourET.setOnFocusChangeListener(onFocusChangeListener);
//		travelPerDayET.setOnFocusChangeListener(onFocusChangeListener);
//		notPayBreakET.setOnFocusChangeListener(onFocusChangeListener);
//		awardET.setOnFocusChangeListener(onFocusChangeListener);
//		flowET.setOnFocusChangeListener(onFocusChangeListener);
		
	}
	
	private void fillProcents() {
		String[] shiftTypeArrayString = {ProfileData.SHIFT_TYPE_WEEKDAY, ProfileData.SHIFT_TYPE_WEEKEND, ProfileData.SHIFT_TYPE_HOLIDAY, ProfileData.SHIFT_TYPE_SICKDAY, ProfileData.SHIFT_TYPE_VACATION};
		TableLayout table = new TableLayout(getActivity());
		ArrayList<Integer> prosentsProcentArray = new ArrayList<Integer>();
		ArrayList<Long> prosentsHourArray = new ArrayList<Long>();
		table = (TableLayout) v.findViewById(R.id.tableLayoutDIEW_procents);
		if(shiftTypeArrayString[DayInformationEditFragment.shiftTypeSpinnerDEF.getSelectedItemPosition()].equals(DayInformationActivity.wShift.shiftType)){
			prosentsProcentArray = DayInformationActivity.wShift.procentsProcentArray;
			prosentsHourArray = DayInformationActivity.wShift.procentsHourArray;
		}
		else{
			if(shiftTypeArrayString[DayInformationEditFragment.shiftTypeSpinnerDEF.getSelectedItemPosition()].equals(ProfileData.SHIFT_TYPE_WEEKDAY)){
				for(int j=0; j<4; j++){
					prosentsProcentArray.add(MainActivity.sPref.getInt(ProfileData.PROCENT_PROCENT_+ProfileData.SHIFT_TYPE_WEEKDAY+"_"+0+"_"+DayInformationActivity.wShift.profileName, 0));
					prosentsHourArray.add(MainActivity.sPref.getLong(ProfileData.PROCENT_HOUR_+ProfileData.SHIFT_TYPE_WEEKDAY+"_"+0+"_"+DayInformationActivity.wShift.profileName, 0));
				}
			}
			if(shiftTypeArrayString[DayInformationEditFragment.shiftTypeSpinnerDEF.getSelectedItemPosition()].equals(ProfileData.SHIFT_TYPE_WEEKEND)){
				for(int j=0; j<4; j++){
					prosentsProcentArray.add(MainActivity.sPref.getInt(ProfileData.PROCENT_PROCENT_+ProfileData.SHIFT_TYPE_WEEKEND+"_"+0+"_"+DayInformationActivity.wShift.profileName, 0));
					prosentsHourArray.add(MainActivity.sPref.getLong(ProfileData.PROCENT_HOUR_+ProfileData.SHIFT_TYPE_WEEKEND+"_"+0+"_"+DayInformationActivity.wShift.profileName, 0));
				}
			}
		}

		table.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		TableRow rowTitle = (TableRow) inflater.inflate(R.layout.profile_procent_text_row, null, false);
		TextView prosentTitleTV = (TextView) rowTitle.findViewById(R.id.textViewPPTR_prosent);
		TextView hourTitleTV = (TextView) rowTitle.findViewById(R.id.textViewPPTR_hour);
		TextView moneyTitleTV = (TextView) rowTitle.findViewById(R.id.textViewPPTR_money);

		prosentTitleTV.setText(getResources().getText(R.string.prosent));
		hourTitleTV.setText(getResources().getText(R.string.hours));
		moneyTitleTV.setText(getResources().getText(R.string.money));
		table.addView(rowTitle);
		
		WorkeShift m = new WorkeShift(getActivity());
		m.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
		m.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), startDateET.getText().toString());
		m.notPayBreak = Long.valueOf(notPayBreakET.getText().toString());
		
//		long time = Calculation.calculateHours(m);
		for(int i=0; i<prosentsProcentArray.size(); i++){
			TableRow row = (TableRow) inflater.inflate(R.layout.profile_procent_text_row, null, false);
			TextView prosentTV = (TextView) row.findViewById(R.id.textViewPPTR_prosent);
			TextView hourTV = (TextView) row.findViewById(R.id.textViewPPTR_hour);
//			TextView moneyTV = (TextView) row.findViewById(R.id.textViewPPTR_money);

			prosentTV.setText(""+prosentsProcentArray.get(i));
			hourTV.setText(""+prosentsHourArray.get(i));
//			float payPer = Float.valueOf(payPerHourET.getText().toString());
//			if((time - prosentsHourArray.get(i))>0 && prosentsHourArray.get(i)>0){
//				hourTV.setText(""+(prosentsHourArray.get(i)/60)+":"+(prosentsHourArray.get(i) - prosentsHourArray.get(i)/60*60));
//				time = time - prosentsHourArray.get(i);
//				moneyTV.setText(""+Calculation.round(((prosentsProcentArray.get(i)*payPer/60/100))*prosentsHourArray.get(i), 2));
//				sum = sum + ((prosentsProcentArray.get(i)*payPer/100))*time/60;
//			}
//			else{
//				hourTV.setText(""+(time/60)+":"+(time - time/60*60));
//				moneyTV.setText(""+Calculation.round(((prosentsProcentArray.get(i)*payPer/60/100))*time, 2));
//				sum = sum + ((prosentsProcentArray.get(i)*payPer/100))*time/60;
//				i=10;
//			}

			table.addView(row);
		}

	}
	
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			WorkeShift temp = new WorkeShift(getActivity());
			switch (v.getId()) {
			case R.id.EditTextDIEW_start_time:
				temp.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
				timeSetDialog(temp.start, WorkeShift.S);
				break;
			case R.id.EditTextDIEW_end_time:
				temp.addTimeAndDate(WorkeShift.E, startTimeET.getText().toString(), startDateET.getText().toString());
				timeSetDialog(temp.start, WorkeShift.E);
				break;
			case R.id.EditTextDIEW_start_date:
				temp.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
				dateSetDialog(temp.start, WorkeShift.S);
				break;
			case R.id.EditTextDIEW_end_date:
				temp.addTimeAndDate(WorkeShift.E, startTimeET.getText().toString(), startDateET.getText().toString());
				dateSetDialog(temp.start, WorkeShift.E);
				break;

			default:
				break;
			}
		}
	};

	
	public boolean collectData(){
		if(!checkDateCollectionForErrors(startTimeET.getText().toString(),
				endTimeET.getText().toString(),
				startDateET.getText().toString(),
				endDateET.getText().toString())){
			return false;
		}
		
		if(!checkMiscCollectionForErrors(payPerHourET.getText().toString(),
				travelPerDayET.getText().toString(),
				notPayBreakET.getText().toString(),
				awardET.getText().toString(),
				flowET.getText().toString(),
				noteET.getText().toString())){
			return false;
		}
		
		DayInformationActivity.wShift.addTimeAndDate(WorkeShift.S, startTimeET.getText().toString(), startDateET.getText().toString());
		DayInformationActivity.wShift.addTimeAndDate(WorkeShift.E, endTimeET.getText().toString(), endDateET.getText().toString());
		DayInformationActivity.wShift.payPer = Calculation.round(Float.valueOf(payPerHourET.getText().toString()), 2);
		DayInformationActivity.wShift.travelPerDay = Float.valueOf(travelPerDayET.getText().toString());
		DayInformationActivity.wShift.notPayBreak = Integer.valueOf(notPayBreakET.getText().toString());
		DayInformationActivity.wShift.award = Float.valueOf(awardET.getText().toString());
		DayInformationActivity.wShift.flow = Float.valueOf(flowET.getText().toString());
		DayInformationActivity.wShift.text = noteET.getText().toString();
		
		return true;
	}

	private boolean checkMiscCollectionForErrors(String _payPerHour, String _travelPerDay,
			String _notPayBreak, String _award, String _flow, String _note) {
		if(!Checkup.editTextCheck(_payPerHour)){
			payPerHourET.setText(""+DayInformationActivity.wShift.payPer);
		}
		if(!Checkup.editTextCheck(_travelPerDay)){
			travelPerDayET.setText(""+DayInformationActivity.wShift.travelPerDay);
		}
		if(!Checkup.editTextCheck(_notPayBreak)){
			notPayBreakET.setText(""+DayInformationActivity.wShift.notPayBreak);
		}
		if(!Checkup.editTextCheck(_award)){
			awardET.setText(""+DayInformationActivity.wShift.award);
		}
		if(!Checkup.editTextCheck(_flow)){
			flowET.setText(""+DayInformationActivity.wShift.flow);
		}
		return true;
	}

	public boolean checkDateCollectionForErrors(String startTime, String endTime, String startDate, String endDate){
		if(!Checkup.timeCheck(startTime)){
			errorTime.setText(getString(R.string.start_time_format_error));
			return false;
		}
		if(!Checkup.timeCheck(endTime)){
			errorTime.setText(getString(R.string.end_time_format_error));
			return false;
		}
		if(!Checkup.dateCheck(startDate)){
			errorTime.setText(getString(R.string.start_date_format_error));
			return false;
		}
		if(!Checkup.dateCheck(endDate)){
			errorTime.setText(getString(R.string.end_date_format_error));
			return false;
		}
		if(!Checkup.startMoreThanEnd(startTime, endTime, startDate, endDate)){
			errorTime.setText(getString(R.string.start_more_than_end));
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
		fillProcents();
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
				case R.id.EditTextDIEW_start_time:
					if(Checkup.timeCheck(startTimeET.getText().toString())){
						startOrEnd = WorkeShift.S;
						n = null;
						changeTimeInEditText(m.start.get(Calendar.HOUR_OF_DAY), m.start.get(Calendar.MINUTE));
						fillProcents();
					}
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEW_end_time:
					if(Checkup.timeCheck(endTimeET.getText().toString())){
						startOrEnd = WorkeShift.E;
						n =null;
						changeTimeInEditText(m.end.get(Calendar.HOUR_OF_DAY), m.end.get(Calendar.MINUTE));
						fillProcents();
					}
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEW_start_date:
					if(Checkup.dateCheck(startDateET.getText().toString())){
						startOrEnd = WorkeShift.S;
						n =null;
						changeDateInEditText(m.start.get(Calendar.YEAR), m.start.get(Calendar.MONTH), m.start.get(Calendar.DAY_OF_MONTH));
						fillProcents();
					}
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEW_end_date:
					if(Checkup.dateCheck(endDateET.getText().toString())){
						startOrEnd = WorkeShift.E;
						n = null;
						changeDateInEditText(m.end.get(Calendar.YEAR), m.end.get(Calendar.MONTH), m.end.get(Calendar.DAY_OF_MONTH));
						fillProcents();
					}
					else tempET.setText(tempString);
					break;
				case R.id.editTextDIEW_pay_per_hour_money:
					if(!payPerHourET.getText().toString().equals("")) fillProcents();
					else tempET.setText(tempString);
					break;
				case R.id.EditTextDIEW_award_money:
					if(awardET.getText().toString().equals("")) tempET.setText(tempString);
					break;
				case R.id.EditTextDIEW_flow_money:
					if(flowET.getText().toString().equals("")) tempET.setText(tempString);
					break;
				case R.id.editTextDIEW_travel_per_day:
					if(travelPerDayET.getText().toString().equals("")) tempET.setText(tempString);
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
				endDateET.setText(String.format("%02d/%02d/%2d",  n.end.get(Calendar.DAY_OF_MONTH), n.end.get(Calendar.MONTH)+1, n.end.get(Calendar.YEAR)));
			}
			else{
				n.end = n.start;
				endDateET.setText(String.format("%02d/%02d/%2d",  n.end.get(Calendar.DAY_OF_MONTH), n.end.get(Calendar.MONTH)+1, n.end.get(Calendar.YEAR)));
			}
		}
		else n=null;

	}
	
	private void changeDateInEditText(int year, int monthOfYear, int dayOfMonth){
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
