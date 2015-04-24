package com.west2.main.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.west2.main.entity.UserEntity;

import android.util.Log;

/*
 * �����ַ��������Լ������ַ����ĺ���
 */
public class InfoUtils {
	public static final String OPEN_DRAWER = "open_drawer";
	public static final String CLOSE_DRAWER = "close_drawer";

	public static final String REFRESH_TIME = "refresh_time";
	public static final String RELOAD = "reload";
	
	public static final String SCHEDULE_ADD = "schedule_add";
	public static final String SCHEDULE_SETTING = "schedule_setting";

	/* SERVICE*/
	public static final String SR_LOGIN_SUCCEED = "succeed";
	public static final String SR_LOGIN_NETERROR = "neterror";
	public static final String SR_LOGIN_WRONG = "wrong_username_password";
	public static final String SR_LOGIN_NULL = "null";
	public static final String SR_LOGIN_NULL_USERNAME = "null_username";
	public static final String SR_LOGIN_NULL_PASSWORD = "null_password";

	public static final String SR_TIME_SUCCEED = "succeed";
	
	public static final String SR_SCHEDULE_FAILED = "failed";
	public static final String SR_SCHEDULE_SUCCEED = "secceed";
	public static final String SR_SCHEDULE_DIS_ALL = "all_courses";
	public static final String SR_SCHEDULE_DIS_WEEK = "single_week_courses";

	public static final String SR_SETTING_THEME_WHITE = "white";
	public static final String SR_SETTING_THEME_BLACK = "black";
	
	// ��ȡ�ַ���������
	public static int getNumber(String str){
		return getNumber(str,4);
	}

	public static int getNumber(String str,int maxLength){
		return getNumber(str,maxLength,0);
	}

	public static int getNumber(String str,int maxLength,int position){
		String reg = "[0-9]{1," + maxLength + "}";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		while(m.find()){
			if(position == 0){
				return Integer.parseInt(m.group().toString());
			}
			--position;
		}
		return -1;
	}

	// ��ȡ�ַ����еĺ���
	public static boolean hasChCharacter(String str){
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher m = p.matcher(str);
		if(m.find())
			return true;
		return false;
	}
	
	// ��ȡ������
	public static double getDouble(String str){
		Pattern p = Pattern.compile("[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)([eE][+-]?[0-9]+)?$");
		Matcher m = p.matcher(str);
		if(m.find()){
			return Double.parseDouble(m.group().toString());
		}
		return 0;
	}

}
