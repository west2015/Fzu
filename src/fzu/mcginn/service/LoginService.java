package fzu.mcginn.service;

import android.content.Context;
import fzu.mcginn.database.DbUser;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.FzuHttpUtils;
import fzu.mcginn.utils.InfoUtils;

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
