package com.west2.main.service;

import com.west2.main.database.DbUser;
import com.west2.main.entity.UserEntity;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.FzuHttpUtils;
import com.west2.main.utils.InfoUtils;

import android.content.Context;

public class LoginService {
	
	public String login(UserEntity user){
		// ÅÐ¶Ï·Ç¿Õ
		boolean isUsernameNull = false,isPasswordNull = false;
		if(user.getUsername() == null || user.getUsername().equals("")){
			isUsernameNull = true;
		}
		if(user.getPassword() == null || user.getPassword().equals("")){
			isPasswordNull = true;
		}
		if(isUsernameNull){
			if(isPasswordNull){
				return InfoUtils.SR_LOGIN_NULL;
			}
			else{
				return InfoUtils.SR_LOGIN_NULL_USERNAME;
			}
		}
		else if(isPasswordNull){
			return InfoUtils.SR_LOGIN_NULL_PASSWORD;
		}
		// ³¢ÊÔµÇÂ¼
		String res = FzuHttpUtils.login(user);
		if(res.equals(InfoUtils.SR_LOGIN_SUCCEED)){
			BaseUtils.getInstance().setUserEntity(user);
		}
		return res;
	}
	
}
