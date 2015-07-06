package com.west2.main.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.west2.main.database.DbUser;
import com.west2.main.entity.UserEntity;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.FzuHttpUtils;
import com.west2.main.utils.HttpUtils;
import com.west2.main.utils.InfoUtils;

import android.content.Context;
import android.util.Base64;

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
	
	public String loginByAPI(UserEntity user){
		
		
		// ÅÐ¶Ï·Ç¿Õ
		boolean isUsernameNull = false, isPasswordNull = false;
		if (user.getUsername() == null || user.getUsername().equals("")) {
			isUsernameNull = true;
		}
		if (user.getPassword() == null || user.getPassword().equals("")) {
			isPasswordNull = true;
		}
		if (isUsernameNull) {
			if (isPasswordNull) {
				return InfoUtils.SR_LOGIN_NULL;
			} else {
				return InfoUtils.SR_LOGIN_NULL_USERNAME;
			}
		} else if (isPasswordNull) {
			return InfoUtils.SR_LOGIN_NULL_PASSWORD;
		}

		String url="http://219.229.132.35/api/api.php/Jwch/login.html";
		JSONObject post = new JSONObject();
		try {
			post.put("stunum", user.getUsername());
			post.put("passwd", Base64.encode(user.getPassword().getBytes(), Base64.DEFAULT));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String res = HttpUtils.postData(url, post);
		if(res==null||res.length()==0)
			return InfoUtils.SR_LOGIN_NETERROR;
		try {
			JSONObject obj = new JSONObject(res);
			if(!obj.getBoolean("status"))
				return InfoUtils.SR_LOGIN_WRONG;
			JSONObject o = obj.getJSONObject("data");
			user.setRealname(o.getString("stuname"));
			BaseUtils.getInstance().setUserEntity(user);
			BaseUtils.getInstance().setToken(o.getString("token"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return InfoUtils.SR_LOGIN_SUCCEED;
		
	}
	
}
