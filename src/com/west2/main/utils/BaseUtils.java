package com.west2.main.utils;

import com.west2.main.database.*;
import com.west2.main.entity.DateEntity;
import com.west2.main.entity.UserEntity;

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
	private String markJson;
	private String cookie;
	
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
		scheduleJson = new DbSchedule(context).getScheduleJson();
		markJson = new DbMark(context).getMarkJson();
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

	public String getMarkJson() {
		return markJson;
	}
	
	public void setMarkJson(String markJson){
		new DbMark(context).setMarkJson(markJson);
		this.markJson = markJson;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
