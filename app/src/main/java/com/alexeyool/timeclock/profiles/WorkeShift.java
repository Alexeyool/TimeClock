package com.alexeyool.timeclock.profiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.content.Context;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.timeclock.main.MainActivity;
import com.alexeyool.timeclock.main.R;

public class WorkeShift {

	public static final String S = "start";
	public static final String E = "end";
	private static final String DEF = "def";
	public static final int HOUR = 0;
	public static final int DAY = 1;
	
	Context mContext;
	
	public long id;
	public String profileName;
	public String shiftType;
	public Calendar start;
	public Calendar end;
	public String text;
	
	public float payPer, award, flow, travelPerDay;
	public int dayOrHour;
	public long notPayBreak;
	public ArrayList<Integer> procentsProcentArray;
	public ArrayList<Long> procentsHourArray;
	
	public WorkeShift(Context context){
		mContext = context;
		id = 0;
		shiftType = ProfileData.SHIFT_TYPE_WEEKDAY;
		profileName = MainActivity.sPref.getString(MainActivity.MAIN_PROFILE_NAME, DEF);
		start = Calendar.getInstance();
		start.setTime(new Date());
		end = Calendar.getInstance();
		end.setTime(new Date());
		dayOrHour = HOUR;
		payPer = MainActivity.sPref.getFloat(ProfileData.PAY_PER_ + profileName, 0);
		dayOrHour = MainActivity.sPref.getInt(ProfileData.PAY_HOUR_DAY_ + profileName, 0);
		award = 0;
		flow = 0;
		notPayBreak = MainActivity.sPref.getLong(ProfileData.NOT_PAY_BREAK_ + profileName, 0);
		if(MainActivity.sPref.getInt(ProfileData.TRAVEL_PER_DAY_MONTH_ + profileName, 1) == 0)
		travelPerDay = MainActivity.sPref.getFloat(ProfileData.TRAVEL_PER_ + profileName, 0);
		else travelPerDay = 0;
		procentsProcentArray = new ArrayList<Integer>();
		procentsHourArray = new ArrayList<Long>();
		for(int i=0; i<4; i++){
			if(shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)){
				if(-111 != MainActivity.sPref.getInt(ProfileData.PROCENT_PROCENT_+ProfileData.SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, -111)){
                                                    	//				editor.putInt(PROCENT_PROCENT_+SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName,
					procentsProcentArray.add(MainActivity.sPref.getInt(ProfileData.PROCENT_PROCENT_+ProfileData.SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, -111));
					procentsHourArray.add(MainActivity.sPref.getLong(ProfileData.PROCENT_HOUR_+ProfileData.SHIFT_TYPE_WEEKDAY+"_"+i+"_"+profileName, -111));
				}
			}
			if(shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND ) || shiftType.equals(ProfileData.SHIFT_TYPE_HOLIDAY)){
				if(-111 != MainActivity.sPref.getInt(ProfileData.PROCENT_PROCENT_+ProfileData.SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, -111)){
					procentsProcentArray.add(MainActivity.sPref.getInt(ProfileData.PROCENT_PROCENT_+ProfileData.SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, -111));
					procentsHourArray.add(MainActivity.sPref.getLong(ProfileData.PROCENT_HOUR_+ProfileData.SHIFT_TYPE_WEEKEND+"_"+i+"_"+profileName, -111));

				}
			}
		}
		text = "";
		
	}
	
	public WorkeShift(Context context, String _profileName, int _startYear, int _startMonth, int _startDay, int _startHour, int _startMinute, String _text, float _bonus){
		WorkeShift wShift = new WorkeShift(context);
		mContext = context;
		id = 0;
		profileName = _profileName;
		shiftType = DEF;
		start = setCalendar(_startYear, _startMonth, _startDay, _startHour, _startMinute);
		end = null;
		text = _text;
		payPer = wShift.payPer;
		award = wShift.award;
		flow = wShift.flow;
		notPayBreak = wShift.notPayBreak;
		travelPerDay = wShift.travelPerDay;
		procentsProcentArray = wShift.procentsProcentArray;
		procentsHourArray = wShift.procentsHourArray;
		
	}
	
	public WorkeShift(Context context, long _id, String _type, String _profileName, Calendar _start, Calendar _end, 
			String _text, float _payPer, float _award, float _flow, long _notPayBreak, float _travelPerDay, 
			int _dayOrHour, ArrayList<Integer> _procentPracent, ArrayList<Long> _procentHour) {
		mContext = context;
		id = _id;
		shiftType = _type;
		profileName = _profileName;
		start = _start;
		end = _end;
		text = _text;
		payPer = _payPer;
		award = _award;
		flow = _flow;
		notPayBreak = _notPayBreak;
		dayOrHour = _dayOrHour;
		travelPerDay = _travelPerDay;
		procentsProcentArray = _procentPracent;
		procentsHourArray = _procentHour;
	}

	public WorkeShift(Context context, long shiftId) {
		mContext = context;
		DataBaseAdapter dbAdapter = new DataBaseAdapter(context);
		dbAdapter.open();
		WorkeShift wShift = dbAdapter.getItemById(shiftId);
		dbAdapter.close();
		id = shiftId;
		shiftType = wShift.shiftType;
		profileName = wShift.profileName;
		start = wShift.start;
		end = wShift.end;
		text = wShift.text;
		payPer = wShift.payPer;
		award = wShift.award;
		flow = wShift.flow;
		notPayBreak = wShift.notPayBreak;
		dayOrHour = wShift.dayOrHour;
		travelPerDay = wShift.travelPerDay;
		procentsProcentArray = wShift.procentsProcentArray;
		procentsHourArray = wShift.procentsHourArray;
		
	}

	public Calendar setCalendar(int year, int month, int day, int hourOfDay, int minute){
		Calendar temp = Calendar.getInstance();
		temp.set(year, month, day, hourOfDay, minute);
		return temp;
	}

	public String getDayOfWeek(String _type) {
		int day = 0;
		if(_type.equals(S)) day = start.get(Calendar.DAY_OF_WEEK);
		if(_type.equals(E)) day = end.get(Calendar.DAY_OF_WEEK);
			switch (day) {
			case 1: return mContext.getString(R.string.sun);
			case 2: return mContext.getString(R.string.mon);
			case 3: return mContext.getString(R.string.tue);
			case 4: return mContext.getString(R.string.wed);
			case 5: return mContext.getString(R.string.thu);
			case 6: return mContext.getString(R.string.fri);
			case 7: return mContext.getString(R.string.sat);

			default:
				return null;
			}
	}

	public int getDay(String _type) {
		int day = 0;
		if(_type.equals(S)) day = start.get(Calendar.DAY_OF_MONTH);
		if(_type.equals(E)) day = end.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public int getHour(String _type) {
		int hour = 0;
		if(_type.equals(S)) hour = start.get(Calendar.HOUR_OF_DAY);
		if(_type.equals(E)) hour = end.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	public int getMinute(String _type) {
		int minute = 0;
		if(_type.equals(S)) minute = start.get(Calendar.MINUTE);
		if(_type.equals(E)) minute = end.get(Calendar.MINUTE);
		return minute;
	}

	public int getHoursRegular() {
		if(end != null) {
			return 0;
		}
		return 0;
	}

	public int getHoursPlus() {
		if(end != null) return 0;
		return 0;
	}

	public int getMany() {
		if(end != null) return 0;
		return 0;
	}

	public void setDate(String _type, Calendar newDate) {
		if(_type.equals(S)) start = newDate;
		if(_type.equals(E)) end = newDate;
		
	}

	public String getTimeString(String _type) {
		if(_type.equals(S)){
		return String.format("%02d:%02d", 
				start.get(Calendar.HOUR_OF_DAY), 
				start.get(Calendar.MINUTE));
		}
		else{
			return String.format("%02d:%02d", 
					end.get(Calendar.HOUR_OF_DAY),
					end.get(Calendar.MINUTE));
		}
	}

	public String getDateString(String _type) {
		if(_type.equals(S)){
			return String.format("%02d/%02d/%02d", 
					start.get(Calendar.DAY_OF_MONTH), 
					start.get(Calendar.MONTH) + 1,
					start.get(Calendar.YEAR));
			}
			else{
				return String.format("%02d/%02d/%02d", 
						end.get(Calendar.DAY_OF_MONTH), 
						end.get(Calendar.MONTH) + 1,
						end.get(Calendar.YEAR));
			}
	}

	public void addTimeAndDate(String _type, String _time, String _Date) {
		StringTokenizer st = new StringTokenizer(_time, ":");
		if(st.countTokens() == 0) return;
		int[] resultTime = new int[2];
		int i = 0;
		while (st.hasMoreTokens()){
			resultTime[i] = Integer.valueOf(st.nextToken());
			i++;
		}
		
		st = new StringTokenizer(_Date, "/");
		if(st.countTokens() == 0) return;
		int[] resultDate = new int[3];
		i = 0;
		while (st.hasMoreTokens()){
			resultDate[i] = Integer.valueOf(st.nextToken());
			i++;
		}
		if(_type.equals(S)){
			start.set(resultDate[2], resultDate[1] - 1, resultDate[0], resultTime[0], resultTime[1]);
		}
		else{
			end.set(resultDate[2], resultDate[1] - 1, resultDate[0], resultTime[0], resultTime[1]);
		}
	}

	public void editByProfile(ProfileData profile) {
		profileName = profile.profileName;
		payPer = profile.payPer;
		notPayBreak = profile.notPayBreak;
		dayOrHour = profile.payPerHourOrDay;
		if(MainActivity.sPref.getInt(ProfileData.TRAVEL_PER_DAY_MONTH_ + profileName, 1) == 0)
			travelPerDay = MainActivity.sPref.getFloat(ProfileData.TRAVEL_PER_ + profileName, 0);
			else travelPerDay = 0;
		procentsProcentArray = profile.prosentsProcentWeekdayArray;
		procentsHourArray = profile.prosentsHourWeekdayArray;
		
	}

}
