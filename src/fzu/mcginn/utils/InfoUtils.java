package fzu.mcginn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fzu.mcginn.entity.UserEntity;

/*
 * 保存字符串常量以及处理字符串的函数
 */
public class InfoUtils {
	public static final String OPEN_DRAWER = "open_drawer";
	public static final String CLOSE_DRAWER = "close_drawer";
	
	public static final String SCHEDULE_ADD = "schedule_add";
	public static final String SCHEDULE_SETTING = "schedule_setting";
	
	/* SERVICE*/
	public static final String SR_LOGIN_SUCCEED = "succeed";
	public static final String SR_LOGIN_NETERROR = "neterror";
	public static final String SR_LOGIN_WRONG = "wrong_username_password";
	public static final String SR_LOGIN_NULL = "null";
	public static final String SR_LOGIN_NULL_USERNAME = "null_username";
	public static final String SR_LOGIN_NULL_PASSWORD = "null_password";

	
	/* DATABASE*/
	
	// USER INFOMATION
		
	// 获取字符串中数字
	public static int getNumber(String str){
		return getNumber(str,4);
	}
	public static int getNumber(String str,int length){
		return getNumber(str,4,0);
	}
	public static int getNumber(String str,int length,int position){
		String reg = "[0-9]{1," + length + "}";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		if(m.find() && position<m.groupCount()){
			return Integer.parseInt(m.group(position).toString());
		}
		return -1;
	}
}
