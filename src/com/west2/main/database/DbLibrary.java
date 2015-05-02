package com.west2.main.database;

import com.west2.main.entity.UserEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbLibrary {
	private final String DB_NAME = "db_library";
	private final String USERNAME = "db_username";
	private final String PASSWORD = "db_password";
	private final String DB_JSON = "db_library_json";
	
	private Context context;
	private SharedPreferences sp;
	
	public DbLibrary(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}

	public void setUserEntity(UserEntity entity){
		Editor ed = sp.edit();
		if(entity == null){
			ed.clear();
		}
		else{
			ed.putString(USERNAME, entity.getUsername());
			ed.putString(PASSWORD, entity.getPassword());
		}
		ed.commit();
	}
	
	public UserEntity getUserEnitty(){
		if(sp.getString(USERNAME, null) != null){
			UserEntity entity = new UserEntity();
			entity.setUsername(sp.getString(USERNAME, ""));
			entity.setPassword(sp.getString(PASSWORD, ""));
			return entity;
		}
		return null;
	}
	
	public void setLibraryJson(String json){
		Editor ed = sp.edit();
		ed.putString(DB_JSON, json);
		ed.commit();
	}
	
	public String getLibraryJson(){
		return sp.getString(DB_JSON, null);
	}

}
