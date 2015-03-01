package fzu.mcginn.utils;

import fzu.mcginn.entity.UserEntity;

public class MsgUtils {
	// constant 
	public static final String OPEN_DRAWER = "open_drawer";
	public static final String CLOSE_DRAWER = "close_drawer";
	
	public static final String SCHEDULE_ADD = "schedule_add";
	public static final String SCHEDULE_SETTING = "schedule_setting";
	
	// static
	private static UserEntity user = null;

	public static UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
