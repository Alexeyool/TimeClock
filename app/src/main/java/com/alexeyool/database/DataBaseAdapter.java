package com.alexeyool.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.alexeyool.timeclock.profiles.WorkeShift;

public class DataBaseAdapter {
	  private SQLiteDatabase db;
	  private final Context context;
	  private DataBase dbHelper;
	  
	  private final static String TABLE_NAME = DataBase.TABLE_SHIFT_TIME;
	  
	  public DataBaseAdapter(Context _context) {
	    this.context = _context;
	    dbHelper = new DataBase(context);
	  }
	  
	  public void close() {
	    db.close();
	  }
	  
	  public void open() throws SQLiteException {  
	    try {
	      db = dbHelper.getWritableDatabase();
	    } catch (SQLiteException ex) {
	      db = dbHelper.getReadableDatabase();
	    }
	  }
	  
	  public int getCount(){
		  Cursor cursor = db.query(TABLE_NAME, new String[] { DataBase.UID}, null, null, null, null, null);
		  int a = cursor.getCount();
		  return a;
	  }
	  
	  public WorkeShift getItemById(long id){
		  Cursor cursor = db.query(TABLE_NAME, null, DataBase.UID + "=" + id, null, null, null, null);
		  if((cursor.getCount() == 0) || !cursor.moveToFirst()){
			  return null;
		  }
		  
		  String profileName = cursor.getString(cursor.getColumnIndex(DataBase.PROFILE_NAME));
		  String type = cursor.getString(cursor.getColumnIndex(DataBase.TYPE));
		  Calendar start = Calendar.getInstance();
		  start.set(cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.START_MONTH)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.START_DAY)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.START_HOUR)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.START_MINUTE)));
		  Calendar end = Calendar.getInstance();
		  end.set(cursor.getInt(cursor.getColumnIndex(DataBase.END_YEAR)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.END_MONTH)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.END_DAY)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.END_HOUR)), 
				  cursor.getInt(cursor.getColumnIndex(DataBase.END_MINUTE)));
		  String text = cursor.getString(cursor.getColumnIndex(DataBase.TEXT));
		  float payPer = cursor.getFloat(cursor.getColumnIndex(DataBase.PAY_PER));
		  float award = cursor.getFloat(cursor.getColumnIndex(DataBase.AWARD));
		  float flow = cursor.getFloat(cursor.getColumnIndex(DataBase.FLOW));
		  long notPayBreak = cursor.getLong(cursor.getColumnIndex(DataBase.NOT_PAY_BREAK));
		  float travelPerDay = cursor.getFloat(cursor.getColumnIndex(DataBase.TRAVEL_PER_DAY));
		  int dayOrHour = cursor.getInt(cursor.getColumnIndex(DataBase.DAY_OR_HOUR));
		  String prosentsProcent = cursor.getString(cursor.getColumnIndex(DataBase.PROCENT_PROCENT));
		  String prosentsHour = cursor.getString(cursor.getColumnIndex(DataBase.PROCENT_HOUR));
		  
		    WorkeShift result = new WorkeShift(context, id, type, profileName, start, end, text,
					  payPer, award, flow, notPayBreak, travelPerDay, dayOrHour,
					  stringToArrayListInteger(prosentsProcent), stringToArrayListLong(prosentsHour));
		    
		    return result;
	  }
	  
	  
	  public WorkeShift[] getAllItems(int _year, int _month){
		  Cursor cursor = db.query(TABLE_NAME, null, DataBase.START_MONTH + "=" + _month  , null, null, null, DataBase.START_DAY + " DESC" + " , " + DataBase.START_HOUR + " DESC" + " , " + DataBase.START_MINUTE + " DESC");
		  if((cursor.getCount() == 0) || !cursor.moveToFirst()){
			  return new WorkeShift[0];
		  }
		  WorkeShift[] result = new WorkeShift[cursor.getCount()];
		  int i = 0;
		  do {
			  if(cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)) == _year){
				  long id = cursor.getLong(cursor.getColumnIndex(DataBase.UID));
				  String profileName = cursor.getString(cursor.getColumnIndex(DataBase.PROFILE_NAME));
				  String type = cursor.getString(cursor.getColumnIndex(DataBase.TYPE));
				  Calendar start = Calendar.getInstance();
				  start.set(cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.START_MONTH)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.START_DAY)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.START_HOUR)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.START_MINUTE)));
				  Calendar end = Calendar.getInstance();
				  end.set(cursor.getInt(cursor.getColumnIndex(DataBase.END_YEAR)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.END_MONTH)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.END_DAY)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.END_HOUR)), 
						  cursor.getInt(cursor.getColumnIndex(DataBase.END_MINUTE)));
				  String text = cursor.getString(cursor.getColumnIndex(DataBase.TEXT));
				  float payPer = cursor.getFloat(cursor.getColumnIndex(DataBase.PAY_PER));
				  float award = cursor.getFloat(cursor.getColumnIndex(DataBase.AWARD));
				  float flow = cursor.getFloat(cursor.getColumnIndex(DataBase.FLOW));
				  long notPayBreak = cursor.getLong(cursor.getColumnIndex(DataBase.NOT_PAY_BREAK));
				  float travelPerDay = cursor.getFloat(cursor.getColumnIndex(DataBase.TRAVEL_PER_DAY));
				  int dayOrHour = cursor.getInt(cursor.getColumnIndex(DataBase.DAY_OR_HOUR));
				  String prosentsProcent = cursor.getString(cursor.getColumnIndex(DataBase.PROCENT_PROCENT));
				  String prosentsHour = cursor.getString(cursor.getColumnIndex(DataBase.PROCENT_HOUR));

				  result[i] = new WorkeShift(context, id, type, profileName, start, end, text,
						  payPer, award, flow, notPayBreak, travelPerDay, dayOrHour,
						  stringToArrayListInteger(prosentsProcent), stringToArrayListLong(prosentsHour));
				  i++;
			  }
		  } while(cursor.moveToNext());
		  return result;
	  }
	  
	  public WorkeShift[] getAllItems(String _profileName, int _year, int _month){
		  Cursor cursor = db.query(TABLE_NAME, null, DataBase.START_MONTH + "=" + _month  , null, null, null, DataBase.START_DAY + " DESC" + " , " + DataBase.START_HOUR + " DESC" + " , " + DataBase.START_MINUTE + " DESC");
		  if((cursor.getCount() == 0) || !cursor.moveToFirst()){
			  return new WorkeShift[0];
		  }
		  ArrayList<WorkeShift> result = new ArrayList<WorkeShift>();
		  do {
			  if(cursor.getString(cursor.getColumnIndex(DataBase.PROFILE_NAME)).equals(_profileName)){
				  if(cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)) == _year){
					  long id = cursor.getLong(cursor.getColumnIndex(DataBase.UID));
					  String profileName = cursor.getString(cursor.getColumnIndex(DataBase.PROFILE_NAME));
					  String type = cursor.getString(cursor.getColumnIndex(DataBase.TYPE));
					  Calendar start = Calendar.getInstance();
					  start.set(cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.START_MONTH)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.START_DAY)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.START_HOUR)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.START_MINUTE)));
					  Calendar end = Calendar.getInstance();
					  end.set(cursor.getInt(cursor.getColumnIndex(DataBase.END_YEAR)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.END_MONTH)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.END_DAY)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.END_HOUR)), 
							  cursor.getInt(cursor.getColumnIndex(DataBase.END_MINUTE)));
					  String text = cursor.getString(cursor.getColumnIndex(DataBase.TEXT));
					  float payPer = cursor.getFloat(cursor.getColumnIndex(DataBase.PAY_PER));
					  float award = cursor.getFloat(cursor.getColumnIndex(DataBase.AWARD));
					  float flow = cursor.getFloat(cursor.getColumnIndex(DataBase.FLOW));
					  long notPayBreak = cursor.getLong(cursor.getColumnIndex(DataBase.NOT_PAY_BREAK));
					  float travelPerDay = cursor.getFloat(cursor.getColumnIndex(DataBase.TRAVEL_PER_DAY));
					  int dayOrHour = cursor.getInt(cursor.getColumnIndex(DataBase.DAY_OR_HOUR));
					  String prosentsProcent = cursor.getString(cursor.getColumnIndex(DataBase.PROCENT_PROCENT));
					  String prosentsHour = cursor.getString(cursor.getColumnIndex(DataBase.PROCENT_HOUR));

					  result.add(new WorkeShift(context, id, type, profileName, start, end, text,
							  payPer, award, flow, notPayBreak, travelPerDay, dayOrHour,
							  stringToArrayListInteger(prosentsProcent), stringToArrayListLong(prosentsHour)));
				  }
			  }
		  } while(cursor.moveToNext());
		  
		  WorkeShift[] result2 = new WorkeShift[result.size()];
		  for(int i=0; i < result.size(); i++){
			  result2[i] = result.get(i);
		  }
		  return result2;

	  }
	  
	  //Insert a new task
	  public long insertItem(WorkeShift _wShift) {
	    ContentValues newItemValues = new ContentValues();

	    newItemValues.put(DataBase.PROFILE_NAME, _wShift.profileName);
	    
	    newItemValues.put(DataBase.START_YEAR,   _wShift.start.get(Calendar.YEAR));
	    newItemValues.put(DataBase.START_MONTH,  _wShift.start.get(Calendar.MONTH));
	    newItemValues.put(DataBase.START_DAY,    _wShift.start.get(Calendar.DAY_OF_MONTH));
	    newItemValues.put(DataBase.START_HOUR,   _wShift.start.get(Calendar.HOUR_OF_DAY));
	    newItemValues.put(DataBase.START_MINUTE, _wShift.start.get(Calendar.MINUTE));
	    
	    newItemValues.put(DataBase.END_YEAR,   _wShift.end.get(Calendar.YEAR));
	    newItemValues.put(DataBase.END_MONTH,  _wShift.end.get(Calendar.MONTH));
	    newItemValues.put(DataBase.END_DAY,    _wShift.end.get(Calendar.DAY_OF_MONTH));
	    newItemValues.put(DataBase.END_HOUR,   _wShift.end.get(Calendar.HOUR_OF_DAY));
	    newItemValues.put(DataBase.END_MINUTE, _wShift.end.get(Calendar.MINUTE));
	    
	    newItemValues.put(DataBase.TEXT, _wShift.text);
	    newItemValues.put(DataBase.TYPE, _wShift.shiftType);
	    newItemValues.put(DataBase.PAY_PER, _wShift.payPer);
	    newItemValues.put(DataBase.AWARD, _wShift.award);
	    newItemValues.put(DataBase.FLOW, _wShift.flow);
	    newItemValues.put(DataBase.NOT_PAY_BREAK, _wShift.notPayBreak);
	    newItemValues.put(DataBase.TRAVEL_PER_DAY, _wShift.travelPerDay);
	    newItemValues.put(DataBase.DAY_OR_HOUR, _wShift.dayOrHour);
	    newItemValues.put(DataBase.PROCENT_PROCENT, arrayListIntegerToString(_wShift.procentsProcentArray));
	    newItemValues.put(DataBase.PROCENT_HOUR, arrayListLongToString(_wShift.procentsHourArray));

	    return db.insert(TABLE_NAME, null, newItemValues);
	  }

	// Remove a task based on its index
	  public boolean removeItem(long position) {
	    return db.delete(TABLE_NAME, DataBase.UID + "=" + position, null) > 0;
	  }

	  // Update a task
	  public long updateItem(WorkeShift _wShift) {
		 removeItem(_wShift.id);
		 return insertItem(_wShift);
	  }
	  
	  private String arrayListIntegerToString(ArrayList<Integer> array) {
		  String result = "";
		  for(int i=0; i<array.size(); i++){
			  if(i == 0)result = result +array.get(i);
			  else result = result + "_" +array.get(i);
		  }
		  return result;
	  }

	  private String arrayListLongToString(ArrayList<Long> array) {
		  String result = "";
		  for(int i=0; i<array.size(); i++){
			  if(i == 0)result = result +array.get(i);
			  else result = result + "_" +array.get(i);
		  }
		  return result;
	  }
	  
	  private ArrayList<Long> stringToArrayListLong(String temp){
		  StringTokenizer st = new StringTokenizer(temp, "_");
			if(st.countTokens() == 0) return null;
			ArrayList<Long> result = new ArrayList<Long>();
			while (st.hasMoreTokens()){
				result.add(Long.valueOf(st.nextToken()));
			
			}
			return result;
	  }
	  
	  private ArrayList<Integer> stringToArrayListInteger(String temp){
		  StringTokenizer st = new StringTokenizer(temp, "_");
			if(st.countTokens() == 0) return null;
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (st.hasMoreTokens()){
				result.add(Integer.valueOf(st.nextToken()));
			
			}
			return result;
	  }

	public Calendar[] getMonthArray() {
		Cursor cursor = db.query(TABLE_NAME, null, null  , null, null, null, DataBase.START_YEAR + " DESC" + " , " + DataBase.START_MONTH + " DESC");
		  if((cursor.getCount() == 0) || !cursor.moveToFirst()){
			  Calendar[] cal = new Calendar[1];
				cal[0] = Calendar.getInstance();
			  return cal;
		  }
		  ArrayList<Calendar> resultTemp = new ArrayList<Calendar>();
		  ArrayList<String> temp = new ArrayList<String>();

		  do {
			  if(!temp.contains(""+cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)) 
					  + cursor.getInt(cursor.getColumnIndex(DataBase.START_MONTH)))){		  
				  temp.add(""+cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)) 
					  + cursor.getInt(cursor.getColumnIndex(DataBase.START_MONTH)));
				  Calendar a = Calendar.getInstance();
				  a.set(cursor.getInt(cursor.getColumnIndex(DataBase.START_YEAR)), cursor.getInt(cursor.getColumnIndex(DataBase.START_MONTH)), 1);
				  resultTemp.add(a);
			  }
		  } while(cursor.moveToNext());
		  resultTemp = addCurrentData(resultTemp);
		  Calendar[] result = new Calendar[resultTemp.size()];
		  for(int i=0; i<resultTemp.size(); i++){
			  result[resultTemp.size()-1-i] = resultTemp.get(i);
		  }
		  return result;
	}

	private ArrayList<Calendar> addCurrentData(ArrayList<Calendar> _resultTemp) {
		Calendar current =  Calendar.getInstance();
		int y = current.get(Calendar.YEAR);
		int m = current.get(Calendar.MONTH);
		if(_resultTemp.isEmpty()) return _resultTemp;
		
		for(int i=0; i<_resultTemp.size(); i++){
			int Y = _resultTemp.get(i).get(Calendar.YEAR);
			int M = _resultTemp.get(i).get(Calendar.MONTH);
			if(Y > y){}
			else{
				if(Y < y){
					_resultTemp.add(i, current);
					return _resultTemp;
				}
				else{
					if(M > m){}
					else{
						if(M < m){
							_resultTemp.add(i, current);
							return _resultTemp;
						}
						else return _resultTemp;
					}
				}
			}
		}
		_resultTemp.add(current);
		return _resultTemp;
	}
	  
}