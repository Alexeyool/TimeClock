package com.alexeyool.day_information;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableRow;

import com.alexeyool.timeclock.main.R;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class DayInformationEditFragment extends Fragment{
	
	private static final int WEEKDAY_DAY = 0;
	private static final int WEEKDAY_HOUR = 1;
	private static final int SICKDAY = 2;
	private static final int VACATION = 3;
//	private static final int HOLIDAY = 4;
	
	View v;
	static Spinner profileNameSpinnerDEF;
	public static Spinner shiftTypeSpinnerDEF;
	static Spinner dayOrHourSpinnerDEF;
	
	String[] profileNameArray;
	public String[] shiftTypeArray = {"Weekday", "Weekend", "Holiday", "Sickday", "Vacation"};
	String[] dayOrHourArray = {"Pay per hour", "Pay per day"};
	
	static EditWeekdayDayFragmentDayInformation weekdayDayFragmentDEF;
	static EditWeekdayHourFragmentDayInformation weekdayHourFragmentDEF;
	static EditSickdayFragmentDayInformation sickdayFragmentDEF;
	FragmentTransaction fTrans;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.day_information_edit, container, false);
		spinnerActivationProfile();
		spinnerActivationShiftType();
		spinnerActivationDayOrHour();
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
		TableRow tb = (TableRow) v.findViewById(R.id.tableRowDIE_day_or_hour);
		switch (type) {
		case WEEKDAY_DAY:
			DayInformationActivity.fragmentIsOnNow = DayInformationActivity.EDIT_WEEKDAY_DAY_FRAGMENT;
			tb.setVisibility(View.VISIBLE);
			dayOrHourSpinnerDEF.setSelection(WorkeShift.DAY, false);
			weekdayDayFragmentDEF = new EditWeekdayDayFragmentDayInformation();
			fTrans = getFragmentManager().beginTransaction();	
			fTrans.replace(R.id.frameLayoutDIE, weekdayDayFragmentDEF, "fragmentEditrtg333");
			fTrans.commit();
			
			break;

		case WEEKDAY_HOUR:
			DayInformationActivity.fragmentIsOnNow = DayInformationActivity.EDIT_WEEKDAY_HOUR_FRAGMENT;
			tb.setVisibility(View.VISIBLE);
			dayOrHourSpinnerDEF.setSelection(WorkeShift.HOUR, false);
			weekdayHourFragmentDEF = new EditWeekdayHourFragmentDayInformation();
			fTrans = getFragmentManager().beginTransaction();	
			fTrans.replace(R.id.frameLayoutDIE, weekdayHourFragmentDEF, "fragmentEditrtg333");
			fTrans.commit();
			
			break;

		case SICKDAY:
			DayInformationActivity.fragmentIsOnNow = DayInformationActivity.EDIT_SICKDAY_FRAGMENT;
			tb.setVisibility(View.GONE);
			sickdayFragmentDEF = new EditSickdayFragmentDayInformation();
			fTrans = getFragmentManager().beginTransaction();	
			fTrans.replace(R.id.frameLayoutDIE, sickdayFragmentDEF, "fragmentEditrtg333");
			fTrans.commit();
			
			break;

		case VACATION:
			DayInformationActivity.fragmentIsOnNow = DayInformationActivity.EDIT_SICKDAY_FRAGMENT;
			tb.setVisibility(View.GONE);
			sickdayFragmentDEF = new EditSickdayFragmentDayInformation();
			fTrans = getFragmentManager().beginTransaction();	
			fTrans.replace(R.id.frameLayoutDIE, sickdayFragmentDEF, "fragmentEditrtg333");
			fTrans.commit();
			
			break;

		default:
			break;
		}

	}

	private void spinnerActivationProfile() {
		profileNameSpinnerDEF = (Spinner) v.findViewById(R.id.SpinnerDIE_profile_name);
		profileNameArray = ProfileData.getProfilesNamseArray();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_spinner_item, profileNameArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		profileNameSpinnerDEF.setAdapter(adapter);
		profileNameSpinnerDEF.setSelection(ProfileData.getProfilesNamseArrayList().indexOf(DayInformationActivity.wShift.profileName), false);
		profileNameSpinnerDEF.setOnItemSelectedListener(onItemSelectedListenerProfileName);
	}
	
	private void spinnerActivationShiftType() {
		String[] shiftTypeArrayString = {getString(R.string.weekday),getString(R.string.weekend), getString(R.string.holiday), getString(R.string.sickday), getString(R.string.vacation)};
		shiftTypeSpinnerDEF = (Spinner) v.findViewById(R.id.spinnerDIE_type_of_shift);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_spinner_item, shiftTypeArrayString);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shiftTypeSpinnerDEF.setAdapter(adapter);
		shiftTypeSpinnerDEF.setSelection(findInShiftTypeArray(DayInformationActivity.wShift.shiftType), false);
		shiftTypeSpinnerDEF.setOnItemSelectedListener(onItemSelectedListenerShiftType);
	}

	private void spinnerActivationDayOrHour() {
		String[] dayOrHourArrayString = {getString(R.string.pay_per_hour), getString(R.string.pay_per_day)};
		dayOrHourSpinnerDEF = (Spinner) v.findViewById(R.id.spinnerDIE_day_or_hour);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_spinner_item, dayOrHourArrayString);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dayOrHourSpinnerDEF.setAdapter(adapter);
		dayOrHourSpinnerDEF.setSelection(DayInformationActivity.wShift.dayOrHour, false);
		dayOrHourSpinnerDEF.setOnItemSelectedListener(onItemSelectedListenerDayOrHour);
	}
	
	OnItemSelectedListener onItemSelectedListenerProfileName = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ProfileData profailTemp = new ProfileData(getActivity());
				if(!profileNameArray[position].equals(DayInformationActivity.wShift.profileName)){
				DayInformationActivity.wShift.editByProfile(profailTemp.loadProfile(getActivity(), ProfileData.getProfilesNamseArray()[position]));
				openFragment();
				}
		}	

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	OnItemSelectedListener onItemSelectedListenerShiftType = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			DayInformationActivity.wShift.shiftType = shiftTypeArray[position];
			ProfileData tempProfile =  new ProfileData(getActivity())
			.loadProfile(getActivity(), profileNameSpinnerDEF.getSelectedItem().toString());
			DayInformationActivity.wShift.payPer = tempProfile.getPayPer(DayInformationActivity.wShift.shiftType);
			if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)){
				DayInformationActivity.wShift.procentsProcentArray = tempProfile.prosentsProcentWeekdayArray;
				DayInformationActivity.wShift.procentsHourArray = tempProfile.prosentsHourWeekdayArray;
			}
			else{
				if(DayInformationActivity.wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
					DayInformationActivity.wShift.procentsProcentArray = tempProfile.prosentsProcentWeekendArray;
					DayInformationActivity.wShift.procentsHourArray = tempProfile.prosentsHourWeekendArray;
				}
				else{
					DayInformationActivity.wShift.procentsProcentArray.add(0, 100);
				}
			}
			openFragment();
		}	

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	OnItemSelectedListener onItemSelectedListenerDayOrHour = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				DayInformationActivity.wShift.dayOrHour = position;
				ProfileData tempProfile = new ProfileData(getActivity())
				.loadProfile(getActivity(), profileNameSpinnerDEF.getSelectedItem().toString());
				if(tempProfile.payPerHourOrDay != DayInformationActivity.wShift.dayOrHour){
					DayInformationActivity.wShift.payPer = 0;
				}
				else DayInformationActivity.wShift.payPer = tempProfile.payPer;
				openFragment();
		}	

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	private int findInShiftTypeArray(String _shiftType) {
		for(int i=0; i<shiftTypeArray.length; i++){
			if(shiftTypeArray[i].equals(_shiftType)) return i;
		}
		return -1;
	}
	
	public boolean collectShift() {
		switch (DayInformationActivity.fragmentIsOnNow) {
		case DayInformationActivity.EDIT_WEEKDAY_DAY_FRAGMENT:
			weekdayDayFragmentDEF.collectData();
			break;
			
		case DayInformationActivity.EDIT_WEEKDAY_HOUR_FRAGMENT:
			if(!weekdayHourFragmentDEF.collectData()) return false;
			break;
			
		case DayInformationActivity.EDIT_SICKDAY_FRAGMENT:
			sickdayFragmentDEF.collectData();
			break;


		default:
			break;
		}
		
		DayInformationActivity.wShift.profileName = profileNameSpinnerDEF.getSelectedItem().toString();
		DayInformationActivity.wShift.shiftType =  shiftTypeArray[(int)shiftTypeSpinnerDEF.getSelectedItemId()];
		DayInformationActivity.wShift.dayOrHour = (int) dayOrHourSpinnerDEF.getSelectedItemId();
		
		return true;
	}
		
}
