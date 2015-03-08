package fzu.mcginn.utils;

import fzu.mcginn.database.*;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.entity.UserEntity;
import android.app.Application;
import android.content.Context;
import android.view.WindowManager;
/*
 * 保存一些常用数据
 */
public class BaseUtils extends Application{

	private static BaseUtils instance;
	private static Context context;
	private static WindowManager wm;
	
	private UserEntity userEntity;
	private DateEntity dateEntity;
	private String scheduleJson;
	
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
//		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		userEntity = new DbUser(context).getUserEnitty();
		dateEntity = new DbDate(context).getDateEntity();
		scheduleJson = new DbSchedule(context).getScheduleJson();
	}

	@SuppressWarnings("deprecation")
	public int getWidth(){
		if(wm == null){
			wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);			
		}
		return wm.getDefaultDisplay().getWidth();
	}
	
	@SuppressWarnings("deprecation")
	public int getHeight(){
		if(wm == null){
			wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);			
		}
		return wm.getDefaultDisplay().getHeight();
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

	public String getScheduleJson() {
		return scheduleJson;
	}

	public void setScheduleJson(String scheduleJson) {
		new DbSchedule(context).setScheduleJson(scheduleJson);
		this.scheduleJson = scheduleJson;
	}

}
