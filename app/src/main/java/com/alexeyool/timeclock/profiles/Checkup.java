package com.alexeyool.timeclock.profiles;

import java.util.Calendar;
import java.util.StringTokenizer;

public class Checkup {
	
	public static boolean timeCheck(String timeString){
		if(timeString.length()<3 || timeString.length()>5)return false;
		try {
			String[] t = timeString.split(":");
			if(t.length!=2) return false;
			if( Integer.parseInt(t[0])<0 || Integer.parseInt(t[0])>23 ) return false;
			if( Integer.parseInt(t[1])<0 || Integer.parseInt(t[1])>59 ) return false;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean dateCheck(String dateString){
		if(dateString.length()<8 || dateString.length()>10)return false;
		try {
			String[] t = dateString.split("/");
			if(t.length!=3) return false;
			if( Integer.parseInt(t[0])<1 || Integer.parseInt(t[0])>31 ) return false;
			if( Integer.parseInt(t[1])<1 || Integer.parseInt(t[1])>12 ) return false;
			if( t[2].length()!=4 || Integer.parseInt(t[2])<1900 ) return false;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean startMoreThanEnd(String startTime, String endTime,
			String startDate, String endDate) {
		Calendar s = setCalendar(startTime, startDate);
		Calendar e = setCalendar(endTime, endDate);
		if(e.getTimeInMillis() < s.getTimeInMillis()) return false;
		return true;
	}
	
	public static boolean amountOfHoursMoreThan24(String startTime,
			String endTime, String startDate, String endDate) {
		Calendar s = setCalendar(startTime, startDate);
		Calendar e = setCalendar(endTime, endDate);
		if((e.getTimeInMillis() - s.getTimeInMillis())>60000*60*24) return true;
		return false;
	}
	
	public static Calendar setCalendar(String _time, String _date){
		Calendar result = Calendar.getInstance();
		StringTokenizer st = new StringTokenizer(_time, ":");
		if(st.countTokens() == 0) return null;
		int[] resultTime = new int[2];
		int i = 0;
		while (st.hasMoreTokens()){
			resultTime[i] = Integer.valueOf(st.nextToken());
			i++;
		}

		st = new StringTokenizer(_date, "/");
		if(st.countTokens() == 0) return null;
		int[] resultDate = new int[3];
		i = 0;
		while (st.hasMoreTokens()){
			resultDate[i] = Integer.valueOf(st.nextToken());
			i++;
		}

		result.set(resultDate[2], resultDate[1], resultDate[0], resultTime[0], resultTime[1]);
		return result;
	}

	public static boolean containProfileName(String _profileName) {
		return ProfileData.getProfilesNamseArrayList().contains(_profileName);
	}

	public static boolean editTextCheck(String _payPerHour) {
		if(_payPerHour.equals("")) return false;
		return true;
	}

	public static boolean isProcentsDataFillIncorrectWay(String p0,String p1, String p2, String p3,
			String h0, String h1, String h2, String h3) {
		if(h0.equals("0")) h0 = "";
		if(h1.equals("0")) h1 = "";
		if(h2.equals("0")) h2 = "";
		if(h3.equals("0")) h3 = "";
		
		if( !p0.equals("") && p1.equals("") && p2.equals("") && p3.equals("")
		  && h0.equals("") && h1.equals("") && h2.equals("") && h3.equals("")){
			return true;
		}
		
		if( !p0.equals("") && !p1.equals("") && p2.equals("") && p3.equals("")
		 && !h0.equals("") &&  h1.equals("") && h2.equals("") && h3.equals("")){
			return true;
		}
		
		if( !p0.equals("") && !p1.equals("") && !p2.equals("") && p3.equals("")
		 && !h0.equals("") && !h1.equals("") &&  h2.equals("") && h3.equals("")){
			return true;
		}
		
		if( !p0.equals("") && !p1.equals("") && !p2.equals("") && !p3.equals("")
		 && !h0.equals("") && !h1.equals("") && !h2.equals("") &&  h3.equals("")){
			return true;
		}
		return false;
	}

}
