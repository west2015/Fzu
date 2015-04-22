package com.west2.main.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbMark {
	private final String DB_NAME = "db_mark";
	private final String MARK_JSON = "db_mark_json";

	private Context context;
	private SharedPreferences sp;
	
	public DbMark(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}

	public String getMarkJson(){
		return sp.getString(MARK_JSON, null);
	}

	public void setMarkJson(String json){
		Editor ed = sp.edit();
		ed.putString(MARK_JSON, json);
		ed.commit();
	}
	
}
