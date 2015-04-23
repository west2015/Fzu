package com.west2.main.database;

import com.west2.main.utils.InfoUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbSetting {
	private final String DB_NAME = "db_setting";
	private final String DB_THEME = "db_theme";

	private Context context;
	private SharedPreferences sp;

	public DbSetting(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}
	
	public void setTheme(String theme){
		Editor ed = sp.edit();
		ed.putString(DB_THEME, theme);
		ed.commit();
	}
	
	public String getTheme(){
		return sp.getString(DB_THEME, InfoUtils.SR_SETTING_THEME_WHITE);
	}
	
}
