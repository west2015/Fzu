package fzu.mcginn.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import fzu.mcginn.entity.DateEntity;

public class DbDate {
	private final String DB_NAME = "db_date";
	private final String DB_WEEK = "db_week";
	private final String DB_SCHOOL_YEAR = "db_school_year";
	private final String DB_TERM = "db_term";
	
	private Context context;
	private SharedPreferences sp;

	public DbDate(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}
	
	public void setDateEntity(DateEntity entity){
		Editor ed = sp.edit();
		if(entity == null){
			ed.putInt(DB_WEEK, -1);
			ed.putInt(DB_SCHOOL_YEAR, -1);
			ed.putInt(DB_TERM, -1);
		}
		else{
			ed.putInt(DB_WEEK, entity.getCurrentWeek() != null ? entity.getCurrentWeek() : -1);
			ed.putInt(DB_SCHOOL_YEAR, entity.getSchoolYear() != null ? entity.getSchoolYear() : -1);
			ed.putInt(DB_TERM, entity.getTerm() != null ? entity.getTerm() : -1);
		}
		ed.commit();
	}
	
	public DateEntity getDateEntity(){
		if(sp.getInt(DB_WEEK, -1) != -1){
			DateEntity entity = new DateEntity();
			entity.setCurrentWeek(sp.getInt(DB_WEEK, 0));
			entity.setSchoolYear(sp.getInt(DB_SCHOOL_YEAR, 0));
			entity.setTerm(sp.getInt(DB_TERM, 0));
			return entity;
		}
		return null;
	}
}
