package com.alexeyool.day_information;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.Checkup;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class EditSickdayFragmentDayInformation extends Fragment{
	
View v;
	
	EditText sickPayET, dateET, procentET, noteET;
	TextView moneyTV;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.day_information_edit_sickday, container, false);
		fillLayout();
		return v;
	}
	
	private void fillLayout() {
		dateET = (EditText) v.findViewById(R.id.editTextDIES_date);
		sickPayET = (EditText) v.findViewById(R.id.editTextDIES_pay_per_day);
		procentET = (EditText) v.findViewById(R.id.editTextDIES_prosent);
		noteET = (EditText) v.findViewById(R.id.editTextDIES_note);

		moneyTV = (TextView) v.findViewById(R.id.textViewDIES_total_money);

		dateET.setText(""+DayInformationActivity.wShift.getDateString(WorkeShift.S));
		sickPayET.setText(""+DayInformationActivity.wShift.payPer);
		procentET.setText(""+DayInformationActivity.wShift.procentsProcentArray.get(0));
		noteET.setText(DayInformationActivity.wShift.text);
		
		dateET.setOnClickListener(onClickListener);
		dateET.setOnFocusChangeListener(onFocusChangeListener);
		sickPayET.setOnFocusChangeListener(onFocusChangeListener);
		procentET.setOnFocusChangeListener(onFocusChangeListener);

		moneyTV.setText(""+ (DayInformationActivity.wShift.payPer * DayInformationActivity.wShift.procentsProcentArray.get(0)/100 ));

	}
	
	public void collectData(){
		DayInformationActivity.wShift.addTimeAndDate(WorkeShift.S, "00:00", dateET.getText().toString());
		DayInformationActivity.wShift.payPer = Float.valueOf(sickPayET.getText().toString());
		DayInformationActivity.wShift.procentsProcentArray.set(0, Integer.valueOf(procentET.getText().toString()));
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
				dateET.setText(String.format("%02d/%02d/%2d", dayOfMonth , monthOfYear+1, year));

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
				m.addTimeAndDate(WorkeShift.S, "00:00", dateET.getText().toString());
				switch (v.getId()) {
				case R.id.editTextDIES_date:
					if(!Checkup.dateCheck(dateET.getText().toString()))
						tempET.setText(tempString);
					break;
				case R.id.editTextDIES_pay_per_day:
					if(sickPayET.getText().toString().equals("")) tempET.setText(tempString);
					break;
				case R.id.editTextDIES_prosent:
					if(procentET.getText().toString().equals("")) tempET.setText(tempString);
					break;

				default:
					break;
				}
			}

		}
	};
}
