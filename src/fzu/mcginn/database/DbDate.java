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
	private final String DB_YEAR = "db_year";
	private final String DB_MONTH = "db_month";
	private final String DB_DAY = "db_day";
	private final String DB_WEEKDAY = "db_weekday";

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
			ed.putInt(DB_YEAR, -1);
			ed.putInt(DB_MONTH, -1);
			ed.putInt(DB_DAY, -1);
			ed.putInt(DB_WEEKDAY, -1);
		}
		else{
			ed.putInt(DB_WEEK, entity.getCurrentWeek() != null ? entity.getCurrentWeek() : -1);
			ed.putInt(DB_SCHOOL_YEAR, entity.getSchoolYear() != null ? entity.getSchoolYear() : -1);
			ed.putInt(DB_TERM, entity.getTerm() != null ? entity.getTerm() : -1);
			ed.putInt(DB_YEAR, entity.getYear() != null ? entity.getYear() : -1);
			ed.putInt(DB_MONTH, entity.getMonth() != null ? entity.getMonth() : -1);
			ed.putInt(DB_DAY, entity.getDay() != null ? entity.getDay() : -1);
			ed.putInt(DB_WEEKDAY, entity.getweekDay() != null ? entity.getweekDay() : -1);
		}
		ed.commit();
	}

	public DateEntity getDateEntity(){
		if(sp.getInt(DB_WEEK, -1) != -1){
			DateEntity entity = new DateEntity();
			entity.setCurrentWeek(sp.getInt(DB_WEEK, 0));
			entity.setSchoolYear(sp.getInt(DB_SCHOOL_YEAR, 0));
			entity.setTerm(sp.getInt(DB_TERM, 0));
			entity.setYear(sp.getInt(DB_YEAR, 0));
			entity.setMonth(sp.getInt(DB_MONTH, 0));
			entity.setDay(sp.getInt(DB_DAY, 0));
			entity.setweekDay(sp.getInt(DB_WEEKDAY, 0));
			return entity;
		}
		return null;
	}
}
