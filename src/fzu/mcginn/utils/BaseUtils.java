package fzu.mcginn.utils;

import fzu.mcginn.database.*;
import fzu.mcginn.entity.UserEntity;
import android.app.Application;
import android.content.Context;
/*
 * 保存一些常用数据
 */
public class BaseUtils extends Application{

	private static BaseUtils instance;
	private static Context context;
	
	private UserEntity userEntity;
	
	public static BaseUtils getInstance(){
		if(instance == null){
			instance = new BaseUtils();
		}
		return instance;
	}
	
	public void onCreate(){
		super.onCreate();
		instance = this;
		context = this;

		userEntity = new DbUser(context).getUserEnitty();

	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
}
