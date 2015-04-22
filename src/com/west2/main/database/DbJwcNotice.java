package com.west2.main.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbJwcNotice {
	private final String DB_NAME = "db_jwc_notice";
	private final String JWCNOTICE_JSON = "db_jwc_notice_json";
	
	private Context context;
	private SharedPreferences sp;
	public DbJwcNotice(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}

	public String getJwcNoticeJson(){
		return sp.getString(JWCNOTICE_JSON, null);
	}

	public void setJwcNoticeJson(String json){
		Editor ed = sp.edit();
		ed.putString(JWCNOTICE_JSON, json);
		ed.commit();
	}
}
