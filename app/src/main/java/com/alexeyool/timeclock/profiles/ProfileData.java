package com.alexeyool.timeclock.profiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;

public class ProfileData {

	public static final String PROFILE_NAME_ = "profile_name_";
	public static final String PAY_PER_ = "pay_per_hour_";
	public static final String PAY_HOUR_DAY_ = "pay_hour_day";
	public static final String SICK_PAY_ = "sick_pay_";
	public static final String VACATION_ = "vacation_";
	public static final String HOLIDAY_ = "holiday_";
	public static final String NOT_PAY_BREAK_ = "not_pay_break_";
	public static final String PROCENT_PROCENT_ = "procent_procent_";
	public static final String PROCENT_HOUR_ = "procent_hour_";
	public static final String TRAVEL_PER_ = "travel_per_";
	public static final String TRAVEL_PER_DAY_MONTH_ = "travel_per_day_or_month_";
	public static final String DEFAULT_PROFILE_NAME = "Shift";
	public static final String ALL_PROFILES_NAMES = "all_profiles_names";
	public static final String SHIFT_TYPE_WEEKEND = "Weekend";
	public static final String SHIFT_TYPE_WEEKDAY = "Weekday";
	public static final String SHIFT_TYPE_HOLIDAY = "Holiday";
	public static final String SHIFT_TYPE_SICKDAY = "Sickday";
	public static final String SHIFT_TYPE_VACATION = "Vacation";
	
	public static final int TRAVEL_DAY = 0;
	public static final int TRAVEL_MONTH = 1;
	
	public static final int PAY_HOUR = 0;
	public static final int PAY_DAY = 1;

	Context mContext;
	public String profileName;
	public float payPer, sickPay, vacation, holiday, travelPer;
	public long notPayBreak;
	public int payPerHourOrDay, travelPerDayOrMonth;
	public ArrayList<Integer> prosentsProcentWeekdayArray;
	public ArrayList<Long> prosentsHourWeekdayArray;
	public ArrayList<Integer> prosentsProcentWeekendArray;
	public ArrayList<Long> prosentsHourWeekendArray;

	public ProfileData(Context context){
		mContext = context;
	}

	public ProfileData(Context context, String _profileName) {
		profileName = _profileName;
		payPer = 0;
		sickPay = 0;
		vacation = 0;
		holiday = 0;
		notPayBreak = 0;
		payPerHourOrDay = 0;
		travelPerDayOrMonth = 1;
		prosentsProcentWeekdayArray = new ArrayList<Integer>();
		prosentsHourWeekdayArray = new ArrayList<Long>();
		prosentsProcentWeekendArray = new ArrayList<Integer>();
		prosentsHourWeekendArray = new ArrayList<Long>();
	}

	public static void setDefaultProfile(){
		Editor editor = MainActivity.sPref.edit();
		String profName = DEFAULT_PROFILE_NAME;
		editor.putString(PROFILE_NAME_ + profName, DEFAULT_PROFILE_NAME);
		editor.putString(MainActivity.MAIN_PROFILE_NAME, DEFAULT_PROFILE_NAME);
		editor.putInt(PAY_HOUR_DAY_ + profName, PAY_HOUR);
		editor.putFloat(PAY_PER_ + profName, 0);
		editor.putFloat(SICK_PAY_ + profName, 0);
		editor.putFloat(VACATION_ + profName, 0);
		editor.putFloat(HOLIDAY_ + profName, 0);
		editor.putLong(NOT_PAY_BREAK_ + profName, 0);
		editor.putInt(TRAVEL_PER_DAY_MONTH_ + profName, TRAVEL_MONTH);
		editor.putFloat(TRAVEL_PER_ + profName, 0);

			editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+0+"_"+profName, 100);
			editor.putLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKDAY+"_"+0+"_"+profName, 8*60);
			
			editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+1+"_"+profName, 125);
			editor.putLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKDAY+"_"+1+"_"+profName, 2*60);
			
			editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+2+"_"+profName, 150);
			editor.putLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKDAY+"_"+2+"_"+profName, -1);
			
			editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKEND+"_"+0+"_"+profName, 150);
			editor.putLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKEND+"_"+0+"_"+profName, -1);
		
		Set<String> set = MainActivity.sPref.getStringSet(ALL_PROFILES_NAMES, new HashSet<String>());
		if(!set.contains(profName)) set.add(profName);
		editor.putStringSet(ALL_PROFILES_NAMES, set);
		editor.commit();
	}

	public void saveProfile(ProfileData _prof, String priweuProfileName){
		Editor editor = MainActivity.sPref.edit();
		String temp = _prof.profileName;
		if(!priweuProfileName.equals(_prof.profileName)){
			removeProfile(priweuProfileName);
		}
		profileName = temp;
		editor.putString(PROFILE_NAME_ + profileName, _prof.profileName);
		editor.putInt(PAY_HOUR_DAY_ + profileName, _prof.payPerHourOrDay);
		editor.putFloat(PAY_PER_ + profileName, _prof.payPer);
		editor.putFloat(SICK_PAY_ + profileName, _prof.sickPay);
		editor.putFloat(VACATION_ + profileName, _prof.vacation);
		editor.putFloat(HOLIDAY_ + profileName, _prof.holiday);
		editor.putLong(NOT_PAY_BREAK_ + profileName, _prof.notPayBreak);
		editor.putInt(TRAVEL_PER_DAY_MONTH_ + profileName, _prof.travelPerDayOrMonth);
		editor.putFloat(TRAVEL_PER_ + profileName, _prof.travelPer);


		for(int i=0; i<_prof.prosentsProcentWeekdayArray.size(); i++){
			editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, _prof.prosentsProcentWeekdayArray.get(i));
			editor.putLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, _prof.prosentsHourWeekdayArray.get(i));
		}
		for(int i=0; i<_prof.prosentsProcentWeekendArray.size(); i++){
			editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, _prof.prosentsProcentWeekendArray.get(i));
			editor.putLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, _prof.prosentsHourWeekendArray.get(i));
		}

		Set<String> set = MainActivity.sPref.getStringSet(ALL_PROFILES_NAMES, new HashSet<String>());
		if(!set.contains(profileName)) set.add(profileName);
		editor.putStringSet(ALL_PROFILES_NAMES, set);
		editor.commit();

	}

	public ProfileData loadProfile(Context context, String _profName){
		ProfileData prof = new ProfileData(context);
		profileName = _profName;
		prof.profileName = MainActivity.sPref.getString(PROFILE_NAME_ + profileName, mContext.getString(R.string.shift));
		prof.payPer = MainActivity.sPref.getFloat(PAY_PER_ + profileName, 0);
		prof.payPerHourOrDay = MainActivity.sPref.getInt(PAY_HOUR_DAY_ + profileName, 0);
		prof.sickPay = MainActivity.sPref.getFloat(SICK_PAY_ + profileName, 0);
		prof.vacation = MainActivity.sPref.getFloat(VACATION_ + profileName, 0);
		prof.holiday = MainActivity.sPref.getFloat(HOLIDAY_ + profileName, 0);
		prof.notPayBreak = MainActivity.sPref.getLong(NOT_PAY_BREAK_ + profileName, 0);
		prof.travelPerDayOrMonth = MainActivity.sPref.getInt(TRAVEL_PER_DAY_MONTH_ + profileName, TRAVEL_MONTH);
		prof.travelPer = MainActivity.sPref.getFloat(TRAVEL_PER_ + profileName, 0);

		prof.prosentsProcentWeekdayArray = new ArrayList<Integer>();
		prof.prosentsHourWeekdayArray = new ArrayList<Long>();
		prof.prosentsProcentWeekendArray = new ArrayList<Integer>();
		prof.prosentsHourWeekendArray = new ArrayList<Long>();
		for(int i=0; i<4; i++){
			if(-111 != MainActivity.sPref.getInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, -111)){
				prof.prosentsProcentWeekdayArray.add(MainActivity.sPref.getInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, -111));
				prof.prosentsHourWeekdayArray.add(MainActivity.sPref.getLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, -111));
			}
			if(-111 != MainActivity.sPref.getInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, -111)){
				prof.prosentsProcentWeekendArray.add(MainActivity.sPref.getInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, -111));
				prof.prosentsHourWeekendArray.add(MainActivity.sPref.getLong(PROCENT_HOUR_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, -111));
			
			}
		}
		if(prof.prosentsProcentWeekdayArray.isEmpty()){
			prof.prosentsProcentWeekdayArray.add(100);
			prof.prosentsHourWeekdayArray.add((long) 0);

		}
		if(prof.prosentsProcentWeekendArray.isEmpty()){
			prof.prosentsProcentWeekendArray.add(100);
			prof.prosentsHourWeekendArray.add((long) 0);

		}

		return prof;
	}

	public void removeProfile(String _profName){
		profileName = _profName;
		Editor editor = MainActivity.sPref.edit();
		editor.remove(PROFILE_NAME_ + profileName);
		editor.remove(PAY_PER_ + profileName);
		editor.remove(PAY_HOUR_DAY_ + profileName);
		editor.remove(SICK_PAY_ + profileName);
		editor.remove(VACATION_ + profileName);
		editor.remove(HOLIDAY_ + profileName);
		editor.remove(NOT_PAY_BREAK_ + profileName);
		editor.remove(TRAVEL_PER_DAY_MONTH_ + profileName);
		editor.remove(TRAVEL_PER_ + profileName);
		
		if(MainActivity.sPref.getString(MainActivity.MAIN_PROFILE_NAME, DEFAULT_PROFILE_NAME).equals(profileName)){
			editor.putString(MainActivity.MAIN_PROFILE_NAME, DEFAULT_PROFILE_NAME);
		}
		Set<String> set = MainActivity.sPref.getStringSet(ALL_PROFILES_NAMES, new HashSet<String>());
		set.remove(profileName);
		for(int i=0; i<4; i++){
			editor.remove(PROCENT_HOUR_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName);
			editor.remove(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName);
			editor.remove(PROCENT_PROCENT_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName);
			editor.remove(PROCENT_HOUR_+SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName);
		}
		editor.putStringSet(ALL_PROFILES_NAMES, set);
		editor.commit();
	}

	public static ArrayList<String> getProfilesNamseArrayList() {
		ArrayList<String> array = new ArrayList<String>();
		array.addAll(MainActivity.sPref.getStringSet(ALL_PROFILES_NAMES, new HashSet<String>()));
		return array;
	}

	public static String[] getProfilesNamseArray() {
		ArrayList<String> array = getProfilesNamseArrayList();
		String[] result = new String[array.size()];
		for(int i=0; i<array.size();i++){
			result[i]  = array.get(i).toString();
		}
		return result;
	}

	public static int getChoisenBoxPosition(){
		return getProfilesNamseArrayList().indexOf(MainActivity.sPref.getString(MainActivity.MAIN_PROFILE_NAME, DEFAULT_PROFILE_NAME));
	}

	public static String getMainProfileName() {
		return MainActivity.sPref.getString(MainActivity.MAIN_PROFILE_NAME, DEFAULT_PROFILE_NAME);
	}

	public  float getPayPer(String shiftType) {
		if(shiftType.equals(SHIFT_TYPE_WEEKDAY)) return payPer;
		if(shiftType.equals(SHIFT_TYPE_WEEKEND)) return payPer;
		if(shiftType.equals(SHIFT_TYPE_VACATION)) return vacation;
		if(shiftType.equals(SHIFT_TYPE_HOLIDAY)) return holiday;
		if(shiftType.equals(SHIFT_TYPE_SICKDAY)) return sickPay;
		return 0;
	}

}
