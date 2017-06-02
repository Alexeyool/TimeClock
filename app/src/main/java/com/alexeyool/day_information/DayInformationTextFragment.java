package com.alexeyool.day_information;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.Calculation;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class DayInformationTextFragment extends Fragment{
	private static final String S = "start";
	private static final String E = "end";
	
	private static final int WEEKDAY_DAY = 0;
	private static final int WEEKDAY_HOUR = 1;
	private static final int SICKDAY = 2;
	private static final int VACATION = 3;
	
	View v;
	
	public String[] shiftTypeArray = {"Weekday", "Weekend", "Holiday", "Sickday", "Vacation"};
	
	TableLayout table;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.day_information_profile, container, false);
		openFragment();
		return v;
	}
	
	protected void openFragment() {
		if(DayInformationActivity.wShift.shiftType.equals(shiftTypeArray[0]) || DayInformationActivity.wShift.shiftType.equals(shiftTypeArray[1])){
			if(DayInformationActivity.wShift.dayOrHour == WorkeShift.DAY){
				runFragment(WEEKDAY_DAY);
			}
			else{
				runFragment(WEEKDAY_HOUR);
			}
		}
		else{
			if(DayInformationActivity.wShift.shiftType.equals(shiftTypeArray[3])){
				runFragment(SICKDAY);
			}
			if(DayInformationActivity.wShift.shiftType.equals(shiftTypeArray[4])
					|| DayInformationActivity.wShift.shiftType.equals(shiftTypeArray[2])
					|| DayInformationActivity.wShift.shiftType.equals(shiftTypeArray[1])){
				runFragment(VACATION);
			}
		}
		
	}
	
	private void runFragment(int type) {
		table = new TableLayout(getActivity());
		
		TableRow startTimeTR = (TableRow) v.findViewById(R.id.tableRowDIP_time_start);
		TableRow endTimeTR = (TableRow) v.findViewById(R.id.tableRowDIP_time_end);
		TableRow payPerHourTR = (TableRow) v.findViewById(R.id.tableRowDIP_pay_per_hour);
		TableRow dateTR = (TableRow) v.findViewById(R.id.tableRowDIP_date);
		TableRow payPerDayTR = (TableRow) v.findViewById(R.id.tableRowDIP_pay_per_day);
		TableRow procentTR = (TableRow) v.findViewById(R.id.tableRowDIP_procent);
		TableRow notPayBreakTimeTR = (TableRow) v.findViewById(R.id.tableRowDIP_not_pay_break);
		TableRow awardTR = (TableRow) v.findViewById(R.id.tableRowDIP_award);
		TableRow flowTR = (TableRow) v.findViewById(R.id.tableRowDIP_flow);
		TableRow travelPerDayTR = (TableRow) v.findViewById(R.id.tableRowDIP_travel_per_day);
		
		TextView profileNameTV = (TextView) v.findViewById(R.id.TextViewDIP_profile_name);
		TextView shiftTypeTV = (TextView) v.findViewById(R.id.TextViewDIP_shift_type);
		TextView startTimeTV = (TextView) v.findViewById(R.id.textViewDIP_start_time);
		TextView startDateTV = (TextView) v.findViewById(R.id.textViewDIP_start_date);
		TextView endTimeTV = (TextView) v.findViewById(R.id.textViewDIP_end_time);
		TextView endDateTV = (TextView) v.findViewById(R.id.textViewDIP_end_date);
		TextView payPerHourTV = (TextView) v.findViewById(R.id.textViewDIP_pay_per_hour_money);
		TextView dateTV = (TextView) v.findViewById(R.id.TextViewDIP_date);
		TextView payPerDayTV = (TextView) v.findViewById(R.id.TextViewDIP_pay_per_day_money);
		TextView procentTV = (TextView) v.findViewById(R.id.TextViewDIP_prosent);
		TextView notPayBreakTimeTV = (TextView) v.findViewById(R.id.textViewDIP_not_pay_break_Time);
		TextView awardTV = (TextView) v.findViewById(R.id.textViewDIP_award_money);
		TextView flowTV = (TextView) v.findViewById(R.id.textViewDIP_flow_money);
		TextView travelPerDayTV = (TextView) v.findViewById(R.id.TextViewDIP_travel_per_day);
		TextView totalHoursTV = (TextView) v.findViewById(R.id.textViewDIP_total_hours);
		TextView totalMoneyTV = (TextView) v.findViewById(R.id.textViewDIP_total_money);
		TextView noteTV = (TextView) v.findViewById(R.id.TextViewDIP_note);
		
		profileNameTV.setText(DayInformationActivity.wShift.profileName);
		noteTV.setText(DayInformationActivity.wShift.text);
		
		switch (type) {
		case WEEKDAY_DAY:
			table.setVisibility(View.GONE);
			startTimeTR.setVisibility(View.GONE);
			endTimeTR.setVisibility(View.GONE);
			payPerHourTR.setVisibility(View.GONE);
			dateTR.setVisibility(View.VISIBLE);
			payPerDayTR.setVisibility(View.VISIBLE);
			procentTR.setVisibility(View.GONE);
			notPayBreakTimeTR.setVisibility(View.GONE);
			awardTR.setVisibility(View.VISIBLE);
			flowTR.setVisibility(View.VISIBLE);
			travelPerDayTR.setVisibility(View.VISIBLE);
			
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY))
				shiftTypeTV.setText(getString(R.string.weekday));
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND))
				shiftTypeTV.setText(getString(R.string.weekend));
			dateTV.setText(DayInformationActivity.wShift.getDateString(WorkeShift.S));
			payPerDayTV.setText(""+DayInformationActivity.wShift.payPer);
			travelPerDayTV.setText(""+DayInformationActivity.wShift.travelPerDay);
			awardTV.setText(""+DayInformationActivity.wShift.award);
			flowTV.setText(""+DayInformationActivity.wShift.flow);
			totalMoneyTV.setText(""+Calculation.round(Calculation.calculateTotalManey(DayInformationActivity.wShift), 2));
			
			break;

		case WEEKDAY_HOUR:
			table.setVisibility(View.VISIBLE);
			startTimeTR.setVisibility(View.VISIBLE);
			endTimeTR.setVisibility(View.VISIBLE);
			payPerHourTR.setVisibility(View.VISIBLE);
			dateTR.setVisibility(View.GONE);
			payPerDayTR.setVisibility(View.GONE);
			procentTR.setVisibility(View.GONE);
			notPayBreakTimeTR.setVisibility(View.VISIBLE);
			awardTR.setVisibility(View.VISIBLE);
			flowTR.setVisibility(View.VISIBLE);
			travelPerDayTR.setVisibility(View.VISIBLE);
			
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY))
				shiftTypeTV.setText(getString(R.string.weekday));
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND))
				shiftTypeTV.setText(getString(R.string.weekend));
			startTimeTV.setText(DayInformationActivity.wShift.getTimeString(S));
			startDateTV.setText(DayInformationActivity.wShift.getDateString(S));
			endTimeTV.setText(DayInformationActivity.wShift.getTimeString(E));
			endDateTV.setText(DayInformationActivity.wShift.getDateString(E));
			payPerHourTV.setText(""+DayInformationActivity.wShift.payPer);
			notPayBreakTimeTV.setText(""+DayInformationActivity.wShift.notPayBreak);
			awardTV.setText(""+DayInformationActivity.wShift.award);
			flowTV.setText(""+DayInformationActivity.wShift.flow);
			travelPerDayTV.setText(""+DayInformationActivity.wShift.travelPerDay);
			fillProcents();
			long time = Calculation.calculateHours(DayInformationActivity.wShift);
			totalHoursTV.setText(""+ (time/60) + " : " + (time - time/60*60));
			totalMoneyTV.setText(""+Calculation.round(Calculation.calculateTotalManey(DayInformationActivity.wShift), 2));
			
			
			break;

		case SICKDAY:
			table.setVisibility(View.GONE);
			startTimeTR.setVisibility(View.GONE);
			endTimeTR.setVisibility(View.GONE);
			payPerHourTR.setVisibility(View.GONE);
			dateTR.setVisibility(View.VISIBLE);
			payPerDayTR.setVisibility(View.VISIBLE);
			procentTR.setVisibility(View.VISIBLE);
			notPayBreakTimeTR.setVisibility(View.GONE);
			awardTR.setVisibility(View.GONE);
			flowTR.setVisibility(View.GONE);
			travelPerDayTR.setVisibility(View.GONE);
			
			shiftTypeTV.setText(getString(R.string.sickday));
			dateTV.setText(DayInformationActivity.wShift.getDateString(WorkeShift.S));
			payPerDayTV.setText(""+DayInformationActivity.wShift.payPer);
			procentTV.setText(""+DayInformationActivity.wShift.procentsProcentArray.get(0));
			totalMoneyTV.setText(""+Calculation.calculateTotalManey(DayInformationActivity.wShift));
			
			break;

		case VACATION:
			table.setVisibility(View.GONE);
			startTimeTR.setVisibility(View.GONE);
			endTimeTR.setVisibility(View.GONE);
			payPerHourTR.setVisibility(View.GONE);
			dateTR.setVisibility(View.VISIBLE);
			payPerDayTR.setVisibility(View.VISIBLE);
			procentTR.setVisibility(View.VISIBLE);
			notPayBreakTimeTR.setVisibility(View.GONE);
			awardTR.setVisibility(View.GONE);
			flowTR.setVisibility(View.GONE);
			travelPerDayTR.setVisibility(View.GONE);
			
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_VACATION))
				shiftTypeTV.setText(getString(R.string.vacation));
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_HOLIDAY))
				shiftTypeTV.setText(getString(R.string.holiday));
			dateTV.setText(DayInformationActivity.wShift.getDateString(WorkeShift.S));
			payPerDayTV.setText(""+DayInformationActivity.wShift.payPer);
			procentTV.setText(""+DayInformationActivity.wShift.procentsProcentArray.get(0));
			totalMoneyTV.setText(""+Calculation.calculateTotalManey(DayInformationActivity.wShift));
			
			break;

		default:
			break;
		}

	}
	
	float sum = 0;
	
	private void fillProcents() {
		table = (TableLayout) v.findViewById(R.id.tableLayoutDIP_procents);
		ArrayList<Integer> prosentsProcentArray = DayInformationActivity.wShift.procentsProcentArray;
		ArrayList<Long> prosentsHourArray = DayInformationActivity.wShift.procentsHourArray;
		
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
		
		long time = Calculation.calculateHours(DayInformationActivity.wShift);
		for(int i=0; i<prosentsProcentArray.size(); i++){
			TableRow row = (TableRow) inflater.inflate(R.layout.profile_procent_text_row, null, false);
			TextView prosentTV = (TextView) row.findViewById(R.id.textViewPPTR_prosent);
			TextView hourTV = (TextView) row.findViewById(R.id.textViewPPTR_hour);
			TextView moneyTV = (TextView) row.findViewById(R.id.textViewPPTR_money);
			
			prosentTV.setText(""+prosentsProcentArray.get(i));
			if((time - prosentsHourArray.get(i))>0 && prosentsHourArray.get(i)>0){
				hourTV.setText(""+(prosentsHourArray.get(i)/60)+":"+(prosentsHourArray.get(i) - prosentsHourArray.get(i)/60*60));
				time = time - prosentsHourArray.get(i);
				moneyTV.setText(""+Calculation.round(((prosentsProcentArray.get(i)*DayInformationActivity.wShift.payPer/60/100))*prosentsHourArray.get(i), 2));
				sum = sum + ((prosentsProcentArray.get(i)*DayInformationActivity.wShift.payPer/100))*time/60;
			}
			else{
				hourTV.setText(""+(time/60)+":"+(time - time/60*60));
				moneyTV.setText(""+Calculation.round(((prosentsProcentArray.get(i)*DayInformationActivity.wShift.payPer/60/100))*time, 2));
				sum = sum + ((prosentsProcentArray.get(i)*DayInformationActivity.wShift.payPer/100))*time/60;
				i=10;
			}
			
			table.addView(row);
		}

	}

}
