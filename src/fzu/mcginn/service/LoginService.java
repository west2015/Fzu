package fzu.mcginn.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import fzu.mcginn.database.DbUser;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.FzuHttpUtils;
import fzu.mcginn.utils.HttpUtils;
import fzu.mcginn.utils.InfoUtils;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class LoginService {
	
	private Context context;
	
	public LoginService(Context context){
		this.context = context;
	}
	
	public String login(UserEntity user){
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

		String res = FzuHttpUtils.login(user);
		if(res.equals(InfoUtils.SR_LOGIN_NETERROR)){
			
		}
		if(res.equals(InfoUtils.SR_LOGIN_SUCCEED)){
//			UserEntity entity = new UserEntity(user.getUsername(),user.getPassword(),"Íõ´ó´¸");
			Log.e("YAO",user.getRealname());
			new DbUser(context).setUserEntity(user);
			BaseUtils.getInstance().setUserEntity(user);
		}
		return res;


	}
	
}
