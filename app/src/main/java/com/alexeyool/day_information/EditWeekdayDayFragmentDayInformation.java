package com.alexeyool.day_information;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class EditWeekdayDayFragmentDayInformation extends Fragment {
	View v;
	
	EditText payPerHourET, travelPerDayET, awardET, flowET, noteET, dateET;
	TextView moneyTV;
	
	float sum = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.day_information_edit_workday_per_day, container, false);
		fillLayout();
		return v;
	}
	
	private void fillLayout() {
		dateET = (EditText) v.findViewById(R.id.editTextDIEWPD_date);
		payPerHourET = (EditText) v.findViewById(R.id.editTextDIEWPD_pay_per_day_money);
		travelPerDayET = (EditText) v.findViewById(R.id.editTextDIEWPD_travel_per_day);
		awardET = (EditText) v.findViewById(R.id.EditTextDIEWPD_award_money);
		flowET = (EditText) v.findViewById(R.id.EditTextDIEWPD_flow_money);
		noteET = (EditText) v.findViewById(R.id.editTextDIEWPD_note);
		
		moneyTV = (TextView) v.findViewById(R.id.textViewDIEWPD_total_money);
		
		dateET.setText(""+DayInformationActivity.wShift.getDateString(WorkeShift.S));
		payPerHourET.setText(""+DayInformationActivity.wShift.payPer);
		travelPerDayET.setText(""+DayInformationActivity.wShift.travelPerDay);
		awardET.setText(""+DayInformationActivity.wShift.award);
		flowET.setText(""+DayInformationActivity.wShift.flow);
		noteET.setText(DayInformationActivity.wShift.text);
		
		moneyTV.setText(""+ (DayInformationActivity.wShift.payPer + DayInformationActivity.wShift.travelPerDay 
				+ DayInformationActivity.wShift.award 
				-DayInformationActivity.wShift.flow));
		
		dateET.setOnClickListener(onClickListener);
	}
	
	public void collectData(){
		DayInformationActivity.wShift.addTimeAndDate(WorkeShift.S, "00:00", dateET.getText().toString());
		DayInformationActivity.wShift.payPer = Float.valueOf(payPerHourET.getText().toString());
		DayInformationActivity.wShift.travelPerDay = Float.valueOf(travelPerDayET.getText().toString());
		DayInformationActivity.wShift.award = Float.valueOf(awardET.getText().toString());
		DayInformationActivity.wShift.flow = Float.valueOf(flowET.getText().toString());
		DayInformationActivity.wShift.text = noteET.getText().toString();
	}
	
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			WorkeShift temp = new WorkeShift(getActivity());
				temp.addTimeAndDate(WorkeShift.S, "00:00", dateET.getText().toString());
				dateSetDialog(temp.start, WorkeShift.S);
		}
	};
	
	String startOrEnd;
	
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
				dateET.setText(String.format("%02d/%02d/%2d", dayOfMonth, monthOfYear +1, year));

		}
	};

}
