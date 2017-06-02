package com.alexeyool.timeclock.profiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.timeclock.main.R;

public class WriteText {
	private static final String LINE_DOWN = "\n";
	private static final int WIDTH_PROFILE_NAME = 10;
	private static final int WIDTH_DAY = 5;
	private static final int WIDTH_DAY_OF_WEEK = 5;
	private static final int WIDTH_SHIFT_TYPE = 10;
	private static final int WIDTH_TIME = 7;
	private static final int WIDTH_BREAK = 6;
	private static final int WIDTH_HOURS = 10;
	private static final int WIDTH_HOURS_OF_PROSENT = 10;
	private static final int WIDTH_AWARD = 6;
	private static final int WIDTH_FLOW = 6;
	private static final int WIDTH_MONEY = 10;
	private static final int WIDTH_TOTAL_TITLE = 15;
	private static final int WIDTH_TOTAL_DAYS = 10;
	private static final int WIDTH_TOTAL_HOURS = 10;
	private static final int WIDTH_TOTAL_MONEY = 10;
	
	static Context mContext;

	Calendar[] monthArray;
	static Calendar cal;
	int positionInMonthArray;
	int vacationNumTotal, sickdayNumTotal, workdayNumTotal, holidayNumTotal;
	float vacationMoneyTotal, sickdayMoneyTotal, holidayMoneyTotal;
	float money, workdayMoneyTotal;
	long hours, hoursTotal;
	
	String mainString;
	
	ArrayList<String> profileNames;
	ArrayList<Integer> procentProsent;
	ArrayList<Long> procentHour;
	ArrayList<Float> procentMoney;
	
	private String inputFile;
	static File file;
	
	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void write() throws Exception{
		file = new File(inputFile);
		calculetData(cal);
		createLabel();
		addShiftsList();
		addTotal();
		BufferedWriter output = null;

		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(mainString);
		} catch (IOException ex) {
			// report
		} 
		finally {
			try {
				output.close();
			} catch (Exception ex) {}
		}

	}

 	private void createLabel(){
		addCaption(mContext.getString(R.string.profile), WIDTH_PROFILE_NAME);
		addCaption(mContext.getString(R.string.date), WIDTH_DAY);
		addCaption(mContext.getString(R.string.day), WIDTH_DAY_OF_WEEK); //day of week
		addCaption(mContext.getString(R.string.type), WIDTH_SHIFT_TYPE);
		addCaption(mContext.getString(R.string.start), WIDTH_TIME);
		addCaption(mContext.getString(R.string.end), WIDTH_TIME);
		addCaption(mContext.getString(R.string.not_pay_braek), WIDTH_BREAK);
		addCaption(mContext.getString(R.string.hours), WIDTH_HOURS);
		int z = 0;
		for(z=0; z<procentProsent.size(); z++){
			addCaption(""+procentProsent.get(z)+"%", WIDTH_HOURS_OF_PROSENT);
		}
		addCaption(mContext.getString(R.string.award),WIDTH_AWARD);
		addCaption(mContext.getString(R.string.flow), WIDTH_FLOW);
		addCaption(mContext.getString(R.string.money), WIDTH_MONEY);
		mainString = mainString + LINE_DOWN;
	}

	private boolean calculetData(Calendar cal){
		workdayNumTotal = 0;
		hours = 0;
		money = 0;
		procentHour = new ArrayList<Long>();
		procentMoney = new ArrayList<Float>();
		procentProsent = new ArrayList<Integer>();
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		WorkeShift[] wShiftArray = dbAdapter.getAllItems(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		dbAdapter.close();
		if(wShiftArray.length > 0){
			for(int i=0; i<wShiftArray.length; i++){
				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_VACATION)){
					vacationNumTotal++;
					vacationMoneyTotal = vacationMoneyTotal + Calculation.calculateTotalManey(wShiftArray[i]);
					
				}

				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_SICKDAY)){
					sickdayNumTotal++;
					sickdayMoneyTotal = sickdayMoneyTotal + Calculation.calculateTotalManey(wShiftArray[i]);
				}
				
				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_HOLIDAY)){
					holidayNumTotal++;
					holidayMoneyTotal = holidayMoneyTotal + Calculation.calculateTotalManey(wShiftArray[i]);
				}


				if(wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)
						|| wShiftArray[i].shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
					workdayNumTotal++;
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
								if(Calculation.numberOfHoursOfProsent(wShiftArray[i], wShiftArray[i].procentsProcentArray.get(j)) < wShiftArray[i].procentsHourArray.get(j)) j=1000;
							}
							else{
								procentProsent.add(wShiftArray[i].procentsProcentArray.get(j));
								procentHour.add(Calculation.numberOfHoursOfProsent(wShiftArray[i], procentProsent.get(procentProsent.size()-1)));
								procentMoney.add(Calculation.round(procentHour.get(procentProsent.size()-1)*(wShiftArray[i].payPer/60*procentProsent.get(procentProsent.size()-1)/100), 2));
								if(Calculation.numberOfHoursOfProsent(wShiftArray[i], wShiftArray[i].procentsProcentArray.get(j)) < wShiftArray[i].procentsHourArray.get(j)) j=1000;
							}
						}
					}
					hours = hours +Calculation.calculateHours(wShiftArray[i]);
					money = Calculation.round(money + Calculation.calculateTotalManey(wShiftArray[i]), 2);
				}

			}
			sortProsent(procentProsent, procentHour, procentMoney);
			hoursTotal = hoursTotal + hours;
			workdayMoneyTotal = workdayMoneyTotal + money;
			mainString = mainString + LINE_DOWN;
			return true;
		}
		return false;

	}
	
	private void sortProsent(ArrayList<Integer> p, ArrayList<Long> h, ArrayList<Float> m) {
		procentProsent = new ArrayList<Integer>();
		procentHour = new ArrayList<Long>();
		procentMoney = new ArrayList<Float>();
		int temp = 0;
		int a = p.size();
		for(int j=0; j<a; j++){
			for(int i=0; i<p.size(); i++){
				if(p.get(temp)>p.get(i)){
					temp = i;
				}
			}
			procentProsent.add(p.get(temp));
			procentHour.add(h.get(temp));
			procentMoney.add(m.get(temp));
			p.remove(temp);
			temp = 0;
		}
	}
	
	private void addShiftsList(){
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		WorkeShift[] wShiftArray = dbAdapter.getAllItems(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		dbAdapter.close();
		if(wShiftArray.length > 0){
			for(int i=0; i<wShiftArray.length; i++){
				addRow(wShiftArray[i]);
				mainString = mainString + LINE_DOWN;
			}
		}
		mainString = mainString + LINE_DOWN;
	}

	private void addRow(WorkeShift workeShift){
		float moneyTemp = 0;
//		mainString = mainString + String.format("%-10.9s%-10.9s%s",workeShift.profileName,workeShift.getDayOfWeek(WorkeShift.S),workeShift.shiftType);
		addLabel(workeShift.profileName, WIDTH_PROFILE_NAME);
		addInteger(workeShift.getDay(WorkeShift.S), WIDTH_DAY);
		addLabel(workeShift.getDayOfWeek(WorkeShift.S), WIDTH_DAY_OF_WEEK); //day of week
		addLabel(workeShift.shiftType, WIDTH_SHIFT_TYPE);
		int z = procentProsent.size();
		if(workeShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)
				|| workeShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
			if(workeShift.dayOrHour == WorkeShift.HOUR){
				addLabel(workeShift.getTimeString(WorkeShift.S), WIDTH_TIME);
				addLabel(workeShift.getTimeString(WorkeShift.E), WIDTH_TIME);
				addLong(workeShift.notPayBreak, WIDTH_BREAK);
				long temp = Calculation.calculateHours(workeShift);
				addLabel("" + temp/60 + ":" + (temp-(temp/60*60)), WIDTH_HOURS);
				for(z=0; z<procentProsent.size(); z++){
					int p = workeShift.procentsProcentArray.indexOf(procentProsent.get(z));
					if(p>-1){
						temp = Calculation.numberOfHoursOfProsent(workeShift, workeShift.procentsProcentArray.get(p));
						addLabel("" + temp/60 + ":" + (temp-(temp/60*60)), WIDTH_HOURS_OF_PROSENT);
					}
				}
			}
			else addLabel("", WIDTH_TIME + WIDTH_TIME + WIDTH_BREAK + WIDTH_HOURS + WIDTH_HOURS_OF_PROSENT);
		}
		addFloat(workeShift.award, WIDTH_AWARD);
		addFloat(workeShift.flow, WIDTH_FLOW);
		addFloat(moneyTemp, WIDTH_MONEY);
		
	}
	
	private void addTotal(){
		addLabel("", WIDTH_TOTAL_TITLE);
		addLabel(mContext.getString(R.string.days), WIDTH_TOTAL_DAYS);
		addLabel(mContext.getString(R.string.hours), WIDTH_TOTAL_HOURS);
		addLabel(mContext.getString(R.string.money), WIDTH_TOTAL_MONEY);
		mainString = mainString + LINE_DOWN;
		long temp = hoursTotal;
		addLabel(mContext.getString(R.string.work_days), WIDTH_TOTAL_TITLE);
		addInteger(workdayNumTotal, WIDTH_TOTAL_DAYS);
		addLabel("" + temp/60 + ":" + (temp-(temp/60*60)), WIDTH_TOTAL_HOURS);
		addFloat(workdayMoneyTotal, WIDTH_TOTAL_MONEY);
		mainString = mainString + LINE_DOWN;
		addLabel( mContext.getString(R.string.vacation), WIDTH_TOTAL_TITLE);
		addInteger(vacationNumTotal, WIDTH_TOTAL_DAYS);
		addLabel("", WIDTH_TOTAL_HOURS);
		addFloat(vacationMoneyTotal, WIDTH_TOTAL_MONEY);
		mainString = mainString + LINE_DOWN;
		addLabel(mContext.getString(R.string.sick_days), WIDTH_TOTAL_TITLE);
		addInteger(sickdayNumTotal, WIDTH_TOTAL_DAYS);
		addLabel("", WIDTH_TOTAL_HOURS);
		addFloat(sickdayMoneyTotal, WIDTH_TOTAL_MONEY);
		mainString = mainString + LINE_DOWN;
		addLabel(mContext.getString(R.string.holiday), WIDTH_TOTAL_TITLE);
		addInteger(holidayNumTotal, WIDTH_TOTAL_DAYS);
		addLabel("", WIDTH_TOTAL_HOURS);
		addFloat(holidayMoneyTotal, WIDTH_TOTAL_MONEY);
		mainString = mainString + LINE_DOWN;
		mainString = mainString + LINE_DOWN;
		
		addLabel(mContext.getString(R.string.total), WIDTH_TOTAL_TITLE);
		addInteger(workdayNumTotal + vacationNumTotal + sickdayNumTotal + holidayNumTotal, WIDTH_TOTAL_DAYS);
		addLabel("" + temp/60 + ":" + (temp-(temp/60*60)), WIDTH_TOTAL_HOURS);
		addFloat(workdayMoneyTotal + vacationMoneyTotal + sickdayMoneyTotal + holidayMoneyTotal, WIDTH_TOTAL_MONEY);//MMMMMMMM
	}

	private void addCaption(String s, int _cellWidth){
		String[] temp = s.split("");
		int len = 0;
		String end = "";
		if(temp.length > _cellWidth){ 
			len = _cellWidth -3;
			end = "...";
		}
		else{
			len = _cellWidth;
			end = "";
		}
		String ss = "";
		for(int i=0; i<len; i++){
			if(i<temp.length) ss = ss + temp[i];
			else ss = ss + " ";
		}
		ss = ss + end;
		mainString = mainString + String.format("%-"+ _cellWidth + "." + (_cellWidth-2) +"s", ss);
		
	}
	
	private void addFloat(float f, int _cellWidth){
		String temp = "" + f;
		int len = 0;
		String end = "";
		if(temp.length() > _cellWidth){ 
			len = _cellWidth -3;
			end = "...";
		}
		else{
			len = _cellWidth;
			end = "";
		}
		String ss = temp;
		for(int i=temp.length()-1; i<len; i++){
			ss = ss + " ";
		}
		ss = ss + end;
		mainString = mainString + String.format("%-"+ _cellWidth + "." + (_cellWidth-2) +"s", ss);
	}
	
	private void addLong(long l, int _cellWidth){
		String temp = "" + l;
		int len = 0;
		String end = "";
		if(temp.length() > _cellWidth){ 
			len = _cellWidth -3;
			end = "...";
		}
		else{
			len = _cellWidth;
			end = "";
		}
		String ss = temp;
		for(int i=temp.length()-1; i<len; i++){
			ss = ss + " ";
		}
		ss = ss + end;
		mainString = mainString + String.format("%-"+ _cellWidth + "." + (_cellWidth-2) +"s", ss);
	}

	private void addInteger(int integer, int _cellWidth){
		String temp = "" + integer;
		int len = 0;
		String end = "";
		if(temp.length() > _cellWidth){ 
			len = _cellWidth -3;
			end = "...";
		}
		else{
			len = _cellWidth;
			end = "";
		}
		String ss = temp;
		for(int i=temp.length()-1; i<len; i++){
			ss = ss + " ";
		}
		ss = ss + end;
		mainString = mainString + String.format("%-"+ _cellWidth + "." + (_cellWidth-2) +"s", ss);
	}

	private void addLabel(String s, int _cellWidth){
		String[] temp = s.split("");
		int len = 0;
		String end = "";
		if(temp.length > _cellWidth){ 
			len = _cellWidth -3;
			end = "...";
		}
		else{
			len = _cellWidth;
			end = "";
		}
		String ss = "";
		for(int i=0; i<len; i++){
			if(i<temp.length) ss = ss + temp[i];
			else ss = ss + " ";
		}
		ss = ss + end;
		mainString = mainString + String.format("%-"+ _cellWidth + "." + (_cellWidth-2) +"s", ss);
		
	}

	public static File main(Calendar _cal, Context context) throws Exception{
		mContext = context;
		cal = _cal;
		String direction;
		File root = android.os.Environment.getExternalStorageDirectory(); 
		File myDirectory = new File(root.getAbsolutePath(), "TimeClock");
		try{
			if(!myDirectory.exists()) {
				myDirectory.mkdirs();
			}
		}catch(Exception e){
		  e.printStackTrace();
		}
		direction = root.getAbsolutePath()
				+ "/TimeClock/" + (cal.get(Calendar.MONTH)+1) + "_" + cal.get(Calendar.YEAR)+ "_text" +".doc";
		WriteText test = new WriteText();
		test.setOutputFile(direction);
		try {
			test.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	} 
