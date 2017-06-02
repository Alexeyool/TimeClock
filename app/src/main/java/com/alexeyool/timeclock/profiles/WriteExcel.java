package com.alexeyool.timeclock.profiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;

import com.alexeyool.database.DataBaseAdapter;
import com.alexeyool.timeclock.main.R;

public class WriteExcel {

	static Context mContext;

	Calendar[] monthArray;
	static Calendar cal;
	int positionInMonthArray;
	int vacationNumTotal, sickdayNumTotal, workdayNumTotal, holidayNumTotal;
	float vacationMoneyTotal, sickdayMoneyTotal, holidayMoneyTotal;
	float money, workdayMoneyTotal;
	long hours, hoursTotal;
	ArrayList<String> profileNames;
	ArrayList<Integer> procentProsent;
	ArrayList<Long> procentHour;
	ArrayList<Float> procentMoney;

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
	int rowPosition;
	static File file;

	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void write() throws IOException, WriteException {
		file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createLabel(excelSheet);

		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet)
			throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);
		
		calculetData(cal);
		rowPosition = 0;
		addCaption(sheet, 1, rowPosition, String.format("%02d/%02d", cal.get(Calendar.MONTH),  cal.get(Calendar.YEAR)));
		rowPosition = 1;
		addCaption(sheet, 0, rowPosition, mContext.getString(R.string.shift_profile));
		addCaption(sheet, 1, rowPosition, mContext.getString(R.string.date));
		addCaption(sheet, 2, rowPosition, mContext.getString(R.string.day)); //day of week
		addCaption(sheet, 3, rowPosition, mContext.getString(R.string.shift_type));
		addCaption(sheet, 4, rowPosition, mContext.getString(R.string.start));
		addCaption(sheet, 5, rowPosition, mContext.getString(R.string.end));
		addCaption(sheet, 6, rowPosition, mContext.getString(R.string.not_pay_braek) + " ("+mContext.getString(R.string.minutes)+")");
		addCaption(sheet, 7, rowPosition, mContext.getString(R.string.hours));
		int z = 0;
		for(z=0; z<procentProsent.size(); z++){
			addCaption(sheet, z+8, 0, ""+procentProsent.get(z)+"%");
		}
		addCaption(sheet, z+8, rowPosition, mContext.getString(R.string.award));
		addCaption(sheet, z+9, rowPosition, mContext.getString(R.string.flow));
		addCaption(sheet, z+10, rowPosition, mContext.getString(R.string.money));
		rowPosition++;
		addShiftsList(sheet);
		addTootal(sheet);
	}

	private boolean calculetData(Calendar cal) throws RowsExceededException, WriteException{
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

	private void addShiftsList(WritableSheet sheet) throws RowsExceededException, WriteException {
		DataBaseAdapter dbAdapter = new DataBaseAdapter(mContext);
		dbAdapter.open();
		WorkeShift[] wShiftArray = dbAdapter.getAllItems(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		dbAdapter.close();
		if(wShiftArray.length > 0){
			for(int i=0; i<wShiftArray.length; i++){
				addRow(wShiftArray[i], rowPosition, sheet);
				rowPosition = rowPosition + 1;
			}
		}
		rowPosition = rowPosition + 1;
	}


	private void addRow(WorkeShift workeShift, int _rowPosition, WritableSheet sheet) throws RowsExceededException, WriteException {
		addLabel(sheet, 0, _rowPosition, workeShift.profileName);
		addInteger(sheet, 1, _rowPosition, workeShift.getDay(WorkeShift.S));
		addLabel(sheet, 2, _rowPosition, workeShift.getDayOfWeek(WorkeShift.S)); //day of week
		addLabel(sheet, 3, _rowPosition, workeShift.shiftType);
		int z = procentProsent.size();
		if(workeShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKDAY)
				|| workeShift.shiftType.equals(ProfileData.SHIFT_TYPE_WEEKEND)){
			if(workeShift.dayOrHour == WorkeShift.HOUR){
				addLabel(sheet, 4, _rowPosition, workeShift.getTimeString(WorkeShift.S));
				addLabel(sheet, 5, _rowPosition, workeShift.getTimeString(WorkeShift.E));
				addLong(sheet, 6, _rowPosition, workeShift.notPayBreak);
				long temp = Calculation.calculateHours(workeShift);
				addLabel(sheet, 7, _rowPosition, "" + temp/60 + ":" + (temp-(temp/60*60)));
				for(z=0; z<procentProsent.size(); z++){
					int p = workeShift.procentsProcentArray.indexOf(procentProsent.get(z));
					if(p>-1){
						temp = Calculation.numberOfHoursOfProsent(workeShift, workeShift.procentsProcentArray.get(p));
						addLabel(sheet, z+8, _rowPosition, "" + temp/60 + ":" + (temp-(temp/60*60)));
					}
				}
			}
		}
		
		addFloat(sheet, z+8, _rowPosition, workeShift.award);
		addFloat(sheet, z+9, _rowPosition, workeShift.flow);
		addFloat(sheet, z+10, _rowPosition, Calculation.round(Calculation.calculateTotalManey(workeShift), 2));
		
		
	}
	
	private void addTootal(WritableSheet sheet) throws RowsExceededException, WriteException{
		addLabel(sheet, 1, rowPosition, mContext.getString(R.string.days));
		addLabel(sheet, 2, rowPosition, mContext.getString(R.string.hours));
		addLabel(sheet, 3, rowPosition, mContext.getString(R.string.money));
		rowPosition++;
		long temp = hoursTotal;
		addLabel(sheet, 0, rowPosition, mContext.getString(R.string.work_days));
		addInteger(sheet, 1, rowPosition, workdayNumTotal);
		addLabel(sheet, 2, rowPosition, "" + temp/60 + ":" + (temp-(temp/60*60)));
		addFloat(sheet, 3, rowPosition, workdayMoneyTotal);
		rowPosition++;
		addLabel(sheet, 0, rowPosition, mContext.getString(R.string.vacation));
		addInteger(sheet, 1, rowPosition, vacationNumTotal);
		addFloat(sheet, 3, rowPosition, vacationMoneyTotal);
		rowPosition++;
		addLabel(sheet, 0, rowPosition, mContext.getString(R.string.sick_days));
		addInteger(sheet, 1, rowPosition, sickdayNumTotal);
		addFloat(sheet, 3, rowPosition, sickdayMoneyTotal);
		rowPosition++;
		addLabel(sheet, 0, rowPosition, mContext.getString(R.string.holiday));
		addInteger(sheet, 1, rowPosition, holidayNumTotal);
		addFloat(sheet, 3, rowPosition, holidayMoneyTotal);
		rowPosition++;
		rowPosition++;
		
		addLabel(sheet, 0, rowPosition, mContext.getString(R.string.total));
		addInteger(sheet, 1, rowPosition, workdayNumTotal + vacationNumTotal + sickdayNumTotal + holidayNumTotal);
		addLabel(sheet, 2, rowPosition, "" + temp/60 + ":" + (temp-(temp/60*60)));
		addFloat(sheet, 3, rowPosition, workdayMoneyTotal + vacationMoneyTotal + sickdayMoneyTotal + holidayMoneyTotal);//MMMMMMMM
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}
	
	private void addFloat(WritableSheet sheet, int column, int row,
			float f) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, f, times);
		sheet.addCell(number);
	}
	
	private void addLong(WritableSheet sheet, int column, int row,
			long l) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, l, times);
		sheet.addCell(number);
	}

	private void addInteger(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	public static File main(Calendar _cal, Context context){
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
				+ "/TimeClock/" + cal.get(Calendar.MONTH)+ "_" + cal.get(Calendar.YEAR)+ "_excel" +".xls";
		WriteExcel test = new WriteExcel();
		test.setOutputFile(direction);
		try {
			test.write();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	} 