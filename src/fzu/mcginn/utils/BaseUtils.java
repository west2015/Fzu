package fzu.mcginn.utils;

import fzu.mcginn.database.*;
import fzu.mcginn.entity.DateEntity;
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
	private DateEntity dateEntity;
	
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
		dateEntity = new DbDate(context).getDateEntity();
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		new DbUser(context).setUserEntity(userEntity);
		this.userEntity = userEntity;
	}

	public DateEntity getDateEntity() {
		return dateEntity;
	}

	public void setDateEntity(DateEntity dateEntity) {
		new DbDate(context).setDateEntity(dateEntity);
		this.dateEntity = dateEntity;
	}

}
