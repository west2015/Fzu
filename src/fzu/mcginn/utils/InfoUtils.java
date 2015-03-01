package fzu.mcginn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fzu.mcginn.R.integer;
import fzu.mcginn.entity.UserEntity;

public class InfoUtils {
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
	
	// method
	public static int getNumber(String str){
		Pattern p = Pattern.compile("[0-9]{1,2}");
		Matcher m = p.matcher(str);
		if(m.find())
			return Integer.parseInt(m.group().toString());
		return -1;
	}
}
