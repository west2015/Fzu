package fzu.mcginn.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbSchedule {
	private final String DB_NAME = "db_schedule";
	private final String SCHEDULE_JSON = "db_schedule_json";

	private Context context;
	private SharedPreferences sp;
	
	public DbSchedule(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}

	public void setScheduleJson(String json){
		if(json == null)
			return ;
		Editor ed = sp.edit();
		ed.putString(SCHEDULE_JSON, json);
		ed.commit();
	}
	
	public String getScheduleJson(){
		return sp.getString(SCHEDULE_JSON, null);
	}

}
