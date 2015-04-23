package fzu.mcginn.database;

import fzu.mcginn.entity.UserEntity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbUser {
	private final String DB_NAME = "user";
	
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String REALNAME = "realname";

	private Context context;
	private SharedPreferences sp;
	
	public DbUser(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}
	
	public void setUserEntity(UserEntity entity){
		if(entity == null)
			return ;
		Editor ed = sp.edit();
		ed.putString(USERNAME, entity.getUsername());
		ed.putString(PASSWORD, entity.getPassword());
		ed.putString(REALNAME, entity.getRealname());
		ed.commit();
	}
	
	public UserEntity getUserEnitty(){
		if(sp.getString(USERNAME, null) != null){
			UserEntity entity = new UserEntity();
			entity.setUsername(sp.getString(USERNAME, ""));
			entity.setPassword(sp.getString(PASSWORD, ""));
			entity.setRealname(sp.getString(REALNAME, ""));
			return entity;
		}
		return null;
	}

}
