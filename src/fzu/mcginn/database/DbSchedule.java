package fzu.mcginn.database;

import fzu.mcginn.utils.InfoUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbSchedule {
	private final String DB_NAME = "db_schedule";
	private final String SCHEDULE_JSON = "db_schedule_json";
	private final String DIS_COURSES = "db_dis_courses";

	private Context context;
	private SharedPreferences sp;
	
	public DbSchedule(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}

	public void setScheduleJson(String json){
		Editor ed = sp.edit();
		ed.putString(SCHEDULE_JSON, json);
		ed.commit();
	}
	
	public String getScheduleJson(){
		return sp.getString(SCHEDULE_JSON, null);
	}

	public void setDisWay(String str){
		if(str == null) return ;
		Editor ed = sp.edit();
		ed.putString(DIS_COURSES, str);
		ed.commit();
	}

	public String getDisWay(){
		return sp.getString(DIS_COURSES, InfoUtils.SR_SCHEDULE_DIS_ALL);
	}
}
