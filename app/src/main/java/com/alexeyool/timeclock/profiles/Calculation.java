package com.alexeyool.timeclock.profiles;

import java.util.Calendar;

public class Calculation {

	public static long calculateHours(WorkeShift wShift) {
		long result = timeMinus(wShift.end, wShift.start) - wShift.notPayBreak;
		if(result >= 0) return result;
		else return 0;
	}

	public static float calculateTotalManey(WorkeShift wShift) {
		float baseManey = 0;
		if(wShift.shiftType.equals(ProfileData.SHIFT_TYPE_VACATION)) baseManey =  wShift.payPer*wShift.procentsProcentArray.get(0)/100;
		if(wShift.shiftType.equals(ProfileData.SHIFT_TYPE_SICKDAY)) baseManey =  wShift.payPer*wShift.procentsProcentArray.get(0)/100;
		if(wShift.shiftType.equals(ProfileData.SHIFT_TYPE_HOLIDAY)) baseManey =  wShift.payPer*wShift.procentsProcentArray.get(0)/100;
		if(wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY) ||
				wShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
			if(wShift.dayOrHour == 1) baseManey = wShift.payPer;
			else{
				long time = calculateHours(wShift);
				for(int i=0; i<wShift.procentsProcentArray.size(); i++){
					long hours = wShift.procentsHourArray.get(i);
					if(time > hours && hours != -1){
						time = time - hours;
						baseManey = baseManey + hours*wShift.payPer/60*wShift.procentsProcentArray.get(i)/100;
					}
					else {
						baseManey = baseManey + time*wShift.payPer/60*wShift.procentsProcentArray.get(i)/100;
						i=10;
					}
				}
			}
		}
		baseManey = baseManey + wShift.award + wShift.travelPerDay - wShift.flow;
		return baseManey;
	}

	public static long timeMinus(Calendar a, Calendar b) {
		
		return (a.getTimeInMillis() - b.getTimeInMillis())/60000;
	}

	public static Long numberOfHoursOfProsent(WorkeShift wShift, Integer procent) {
		long time = calculateHours(wShift);
		for(int i=0; i<wShift.procentsProcentArray.size(); i++){
			long hours = wShift.procentsHourArray.get(i);
			if(time>hours && hours != -1){
				time = time - hours;
				if(wShift.procentsProcentArray.get(i) == procent){
					return hours;
				}
			}
			else {
				if(wShift.procentsProcentArray.get(i) == procent) return time;
				else time =0;
				}
		}
		return (long) 0; //return minutes
	}
	
	public static float round(float value, int scale) {
	      return (float) (Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale));
	}

}
