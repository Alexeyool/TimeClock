package com.alexeyool.timeclock.main;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alexeyool.timeclock.profiles.Calculation;
import com.alexeyool.timeclock.profiles.ProfileData;
import com.alexeyool.timeclock.profiles.WorkeShift;

public class ListViewAdapterMain extends BaseAdapter{
	private static final String S = "start";

	private static final String E = "end";

	ListViewHolderMain holder;
	
	Context mContext;
	WorkeShift[] wShift;
	
	int days;
	long hours;
	float money;

	public ListViewAdapterMain(Context context, WorkeShift[] _wShift){
		mContext = context;
		wShift = sortByDataAndTime(_wShift);
		days = 0;
		hours = 0;
		money = 0;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getCount() {
		return wShift.length;
	
	}
	
	@Override
	public Object getItem(int position) {
		return wShift[position];
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
				convertView = fillListView(position, convertView, parent);
		} 
		else {
			holder = (ListViewHolderMain) convertView.getTag();
		}
		if(getCount()-1 == position){
			MainActivity.daysTV.setText(""+days);
			MainActivity.hoursTV.setText(String.format("%02d:%02d", (hours/60), hours-hours/60*60));
			MainActivity.moneyTV.setText(""+Calculation.round(money, 2));
		}

		return convertView;

	}
	
	private  View fillListView(int position, View convertView, ViewGroup parent){
		holder = new ListViewHolderMain();
		WorkeShift temp = wShift[position];

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		convertView = inflater.inflate(R.layout.cell_list_main, parent, false);

		holder.tvDay = (TextView) convertView.findViewById(R.id.TextViewMLC_day);
		holder.tvDate = (TextView) convertView.findViewById(R.id.TextViewMLC_date);
		holder.tvProfile = (TextView) convertView.findViewById(R.id.TextViewMLC_profile);
		holder.tvStart = (TextView) convertView.findViewById(R.id.TextViewMLC_start);
		holder.tvEnd = (TextView) convertView.findViewById(R.id.TextViewMLC_end);
		holder.tv100 = (TextView) convertView.findViewById(R.id.TextViewMLC_100);
		holder.tvMany = (TextView) convertView.findViewById(R.id.TextViewMLC_many);
		
		holder.tvDay.setText(temp.getDayOfWeek(S));
		holder.tvDate.setText(""+temp.getDay(S));
		holder.tvProfile.setText(temp.profileName);
		
		if(temp.shiftType.equals(ProfileData.SHIFT_TYPE_HOLIDAY) 
				|| temp.shiftType.equals(ProfileData.SHIFT_TYPE_VACATION) 
				|| temp.shiftType.equals(ProfileData.SHIFT_TYPE_SICKDAY)
				|| temp.dayOrHour == WorkeShift.DAY){
			
			if(temp.shiftType.equals(ProfileData.SHIFT_TYPE_HOLIDAY)){
				holder.tvStart.setText(mContext.getText(R.string.holiday));
			}
			if(temp.shiftType.equals(ProfileData.SHIFT_TYPE_VACATION)){
				holder.tvStart.setText(mContext.getText(R.string.vacation));
				}
			if(temp.shiftType.equals(ProfileData.SHIFT_TYPE_SICKDAY)){
				holder.tvStart.setText(mContext.getText(R.string.sickday));
			}
			if(temp.dayOrHour == WorkeShift.DAY){
			}
			holder.tvMany.setText(""+Calculation.calculateTotalManey(temp));
			money = money + Calculation.calculateTotalManey(temp);
			days++;
		}
		else{
			holder.tvStart.setText(String.format("%02d:%02d", temp.getHour(S), temp.getMinute(S)));

			if(temp.end != null){
				days++;
				holder.tvEnd.setText(String.format("%02d:%02d", temp.getHour(E), temp.getMinute(E)));
				long t = Calculation.calculateHours(temp);
				hours = hours + t;
				holder.tv100.setText(String.format("%02d:%02d", (t/60), t-t/60*60));
				float m = Calculation.calculateTotalManey(temp);
				money = money + m;
				holder.tvMany.setText(""+Calculation.round(m, 2));
			}
			else{
				holder.tvEnd.setText("-:-");		
				holder.tv100.setText("-");
				holder.tvMany.setText("-");
			}
		}
		convertView.setTag(holder);
		return convertView;
	}

	private WorkeShift[] sortByDataAndTime(WorkeShift[] _workeShift) {
		ArrayList<WorkeShift> tempList = new ArrayList<WorkeShift>();
		for(int i=0; i< _workeShift.length; i++){
			tempList.add(_workeShift[i]);
		}
		int temp = 0;
		int a = tempList.size();
		for(int j=a-1; j>-1; j--){
			for(int i=0; i<tempList.size(); i++){
				if(tempList.get(temp).start.get(Calendar.DAY_OF_MONTH) > tempList.get(i).start.get(Calendar.DAY_OF_MONTH)){
					temp = i;
				}
				else{
					if(tempList.get(temp).start.get(Calendar.DAY_OF_MONTH) == tempList.get(i).start.get(Calendar.DAY_OF_MONTH)){
						if(tempList.get(temp).start.get(Calendar.HOUR_OF_DAY) > tempList.get(i).start.get(Calendar.HOUR_OF_DAY)){
							temp = i;
						}
						else{
							if(tempList.get(temp).start.get(Calendar.HOUR_OF_DAY) == tempList.get(i).start.get(Calendar.HOUR_OF_DAY)){
								if(tempList.get(temp).start.get(Calendar.MINUTE) > tempList.get(i).start.get(Calendar.MINUTE)){
									temp = i;
								}
							}
						}
					}
				}
			}
			_workeShift[j] = tempList.get(temp);
			tempList.remove(temp);
			temp = 0;
		}
		return _workeShift;
	}

}

class ListViewHolderMain {
	TextView tvDay;
	TextView tvDate;
	TextView tvProfile;
	TextView tvStart;
	TextView tvEnd;
	TextView tv100;
	TextView tvMany;


}
