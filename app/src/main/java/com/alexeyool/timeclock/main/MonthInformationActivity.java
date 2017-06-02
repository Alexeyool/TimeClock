package com.alexeyool.timeclock.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.timeclock.profiles.Calculation;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class MonthInformationActivity extends Activity {
	
	Context mContext;
	
	TextView titleActionBar;
	ImageButton homeActionBar;
	
	TextView profileNameTV, workDaysTV, vacationDaysTV, sickDaysTV, totalDaysTV, totalHoursTV, totalMoneyTV;
	TextView monthRollTV;
	
	Button nextB, previewB, emailSend;
	
	Calendar[] monthArray;
	int positionInMonthArray;
	int vacationNum, sickdayNum, workdayNum, vacationNumTotal, sickdayNumTotal, workdayNumTotal;
	float money, moneyTotal;
	long hours, hoursTotal;
	int profilesFilledCaunter;
	ArrayList<String> profileNames;
	ArrayList<Integer> procentProsent;
	ArrayList<Long> procentHour;
	ArrayList<Float> procentMoney;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_information);
		
		mContext = this;

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_bar_secondary, null);
		titleActionBar = (TextView) mCustomView.findViewById(R.id.TextViewABS_title);
		homeActionBar = (ImageButton) mCustomView.findViewById(R.id.imageButtonABS_home);
		
		titleActionBar.setText(R.string.monthly_information);
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
		
		profileNames = ProfileData.getProfilesNamseArrayList();
		createLayoutMonthRoll();
		
		fillLayoutMonthRoll(monthArray[positionInMonthArray]);
	}

	private void fillLayoutMonthRoll(Calendar _cal) {
		emailSend = (Button) findViewById(R.id.buttonAMI_mail_send);
		emailSend.setOnClickListener(onClickListener);
		if(positionInMonthArray == 0) previewB.setClickable(false);
		else previewB.setClickable(true);
		if(positionInMonthArray == monthArray.length-1) nextB.setClickable(false);
		else nextB.setClickable(true);
		
		monthRollTV.setText(_cal.get(Calendar.MONTH)+1 + "/" + _cal.get(Calendar.YEAR));
		fillLayoutProfiles(_cal);
		if(profilesFilledCaunter > 1) fillLayoutTotal();
		else{
			LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutAMI_total);
			layout.setVisibility(View.GONE);
		}
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonAMI_mail_send:
				Intent intent = new Intent(mContext, EmailActivity.class);
				intent.putExtra(EmailActivity.YEAR, monthArray[positionInMonthArray].get(Calendar.YEAR));
				intent.putExtra(EmailActivity.MONTH, monthArray[positionInMonthArray].get(Calendar.MONTH));
				startActivity(intent);
				break;

			default:
				break;
			}
			
		}
	};

	private Calendar[] getMonthArray() {
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		Calendar[] result = dbAdapter.getMonthArray();
		dbAdapter.close();
		return result;
	}
	
	private void createLayoutMonthRoll() {
		nextB = (Button) findViewById(R.id.ButtonAMI_next);
		previewB = (Button) findViewById(R.id.ButtonAMI_month_preview);
		monthRollTV = (TextView) findViewById(R.id.textViewAMI_month);
		
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
			case R.id.ButtonAMI_next:
				if(positionInMonthArray < monthArray.length-1){
					positionInMonthArray++;
					fillLayoutMonthRoll(monthArray[positionInMonthArray]);
				}
				break;
				
			case R.id.ButtonAMI_month_preview:
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

	private void fillLayoutTotal() {
		TextView vacationTotalTV = (TextView) findViewById(R.id.textViewAMI_vacation);
		TextView sickdayTotalTV = (TextView) findViewById(R.id.textViewAMI_sick);
		TextView workdayTotalTV = (TextView) findViewById(R.id.textViewAMI_days_work);
		TextView daysTV = (TextView) findViewById(R.id.textViewAMI_total_days);
		TextView hoursTV = (TextView) findViewById(R.id.textViewAMI_total_hours);
		TextView moneyTV = (TextView) findViewById(R.id.textViewAMI_total_money);
		
		vacationTotalTV.setText(""+vacationNumTotal);
		sickdayTotalTV.setText(""+sickdayNumTotal);
		workdayTotalTV.setText(""+workdayNumTotal);
		daysTV.setText(""+(vacationNumTotal + sickdayNumTotal + workdayNumTotal));
		hoursTV.setText(""+(hoursTotal/60) + ":" + (hoursTotal - hoursTotal/60*60));
		moneyTV.setText(""+Calculation.round(moneyTotal, 2));
		
	}

	private void fillLayoutProfiles(Calendar cal) {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutAMI_profiles);
		linearLayout.removeAllViews();
		for(int i=0; i<profileNames.size(); i++){
			if(calculetData(profileNames.get(i), cal)){
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
				View v = inflater.inflate(R.layout.month_tables, null, false);
				profileNameTV = (TextView) v.findViewById(R.id.textViewMT_profile_name);
				workDaysTV = (TextView) v.findViewById(R.id.textViewMT_days_work);
				vacationDaysTV = (TextView) v.findViewById(R.id.textViewMT_vacation);
				sickDaysTV = (TextView) v.findViewById(R.id.textViewMT_sick);
				totalDaysTV = (TextView) v.findViewById(R.id.textViewMT_total_days);
				totalHoursTV = (TextView) v.findViewById(R.id.textViewMT_total_hours);
				totalMoneyTV = (TextView) v.findViewById(R.id.textViewMT_total_money);

				profileNameTV.setText(profileNames.get(i));
				workDaysTV.setText(""+workdayNum);
				vacationDaysTV.setText(""+vacationNum);
				sickDaysTV.setText(""+sickdayNum);
				totalDaysTV.setText(""+(workdayNum + vacationNum + sickdayNum));
				totalHoursTV.setText(""+(hours/60) + ":" + (hours - hours/60*60));
				totalMoneyTV.setText(""+Calculation.round(money, 2));
				profileNames = ProfileData.getProfilesNamseArrayList();
				fillProcents(profileNames.get(i), v);
				linearLayout.addView(v);
				profilesFilledCaunter++;
			}
		}

	}
	
	private void fillProcents(String _profileName, View v) {
		TableLayout table = (TableLayout) v.findViewById(R.id.TableLayoutMT_hours);
		
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
		
		for(int i=0; i<procentProsent.size(); i++){
			TableRow row = (TableRow) inflater.inflate(R.layout.profile_procent_text_row, null, false);
			TextView prosentTV = (TextView) row.findViewById(R.id.textViewPPTR_prosent);
			TextView hourTV = (TextView) row.findViewById(R.id.textViewPPTR_hour);
			TextView moneyTV = (TextView) row.findViewById(R.id.textViewPPTR_money);
			
			prosentTV.setText(""+procentProsent.get(i));
			hourTV.setText(""+(procentHour.get(i)/60) +":"+ (procentHour.get(i) - procentHour.get(i)/60*60));
			moneyTV.setText(""+Calculation.round(procentMoney.get(i), 2));
			
			table.addView(row);
		}
	}
	
	private boolean calculetData(String _profileName, Calendar cal){
		vacationNum = 0;
		sickdayNum = 0;
		workdayNum = 0;
		hours = 0;
		money = 0;
		procentHour = new ArrayList<Long>();
		procentMoney = new ArrayList<Float>();
		procentProsent = new ArrayList<Integer>();
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		WorkeShift[] wShiftArray = dbAdapter.getAllItems(_profileName, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		dbAdapter.close();
		if(wShiftArray == null || wShiftArray.length==0) return false;
		if(wShiftArray.length > 0){
			for(int i=0; i<wShiftArray.length; i++){
				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_VACATION)){
					vacationNum++;
					money = money + Calculation.calculateTotalManey(wShiftArray[i]);
				}

				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_SICKDAY)){
					sickdayNum++;
					money = money + Calculation.calculateTotalManey(wShiftArray[i]);
				}

				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)
						|| wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
					workdayNum++;
					if(wShiftArray[i].dayOrHour == WorkeShift.HOUR){
						for(int j=0; j<wShiftArray[i].procentsProcentArray.size(); j++){
							if(procentProsent.contains(wShiftArray[i].procentsProcentArray.get(j))){
								int index = procentProsent.indexOf(wShiftArray[i].procentsProcentArray.get(j));
								long temph = procentHour.get(index);
								float tempM = procentMoney.get(index);
								procentHour.remove(index);
								procentHour.add(index, temph+Calculation.numberOfHoursOfProsent(wShiftArray[i], procentProsent.get(index)));
								procentMoney.remove(index);
								procentMoney.add(index, Calculation.round(tempM + procentHour.get(index)*(wShiftArray[i].payPer/60*procentProsent.get(index)/100), 2));
								if(Calculation.numberOfHoursOfProsent(wShiftArray[i], procentProsent.get(index)) < wShiftArray[i].procentsHourArray.get(index)) j=1000;
							}
							else{
								procentProsent.add(wShiftArray[i].procentsProcentArray.get(j));
								procentHour.add(Calculation.numberOfHoursOfProsent(wShiftArray[i], procentProsent.get(procentProsent.size()-1)));
								procentMoney.add(Calculation.round(procentHour.get(procentProsent.size()-1)*(wShiftArray[i].payPer/60*procentProsent.get(procentProsent.size()-1)/100), 2));
								if(Calculation.numberOfHoursOfProsent(wShiftArray[i], procentProsent.get(procentProsent.size()-1)) < wShiftArray[i].procentsHourArray.get(procentProsent.size()-1)) j=1000;
							}
						}
					}
					hours = hours +Calculation.calculateHours(wShiftArray[i]);
					money = Calculation.round(money + Calculation.calculateTotalManey(wShiftArray[i]), 2);
				}

			}
			hoursTotal = hoursTotal + hours;
			moneyTotal = moneyTotal + money;
			vacationNumTotal = vacationNumTotal + vacationNum;
			sickdayNumTotal = sickdayNumTotal + sickdayNum;
			workdayNumTotal = workdayNumTotal + workdayNum;
			return true;
		}
		return false;

	}


}
