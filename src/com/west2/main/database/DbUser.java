package com.west2.main.database;

import com.west2.main.entity.UserEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbUser {
	private final String DB_NAME = "db_user";
	private final String USERNAME = "db_username";
	private final String PASSWORD = "db_password";
	private final String REALNAME = "db_realname";
	private final String LOGINID = "db_loginid";
	private Context context;
	private SharedPreferences sp;
	
	public DbUser(Context context){
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
			ed.putString(REALNAME, entity.getRealname());
			ed.putString(LOGINID , entity.getLoginId());
		}
		ed.commit();
	}
	
	public UserEntity getUserEnitty(){
		if(sp.getString(USERNAME, null) != null){
			UserEntity entity = new UserEntity();
			entity.setUsername(sp.getString(USERNAME, ""));
			entity.setPassword(sp.getString(PASSWORD, ""));
			entity.setRealname(sp.getString(REALNAME, ""));
			entity.setLoginId(sp.getString(LOGINID, ""));
			return entity;
		}
		return null;
	}

}
