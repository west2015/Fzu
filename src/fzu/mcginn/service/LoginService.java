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
import fzu.mcginn.utils.HttpUtils;
import fzu.mcginn.utils.InfoUtils;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

public class LoginService {
	
	private Context context;
	
	public LoginService(Context context){
		this.context = context;
	}
	
	public String login(String username,String password){
		boolean isUsernameNull = false,isPasswordNull = false;
		if(username == null || username.equals("")){
			isUsernameNull = true;
		}
		if(password == null || password.equals("")){
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

		String res = getCookie(username,password);
		if(res.equals(InfoUtils.SR_LOGIN_SUCCEED)){
			UserEntity entity = new UserEntity(username,password,"王大锤");
			new DbUser(context).setUserEntity(entity);
			BaseUtils.getInstance().setUserEntity(entity);
		}
		return res;

//		String mUsername = username;
//		String mPassword = password;
//		mPassword = new String(Base64.encode(mPassword.getBytes(), Base64.DEFAULT));
//		String url = "http://api.west2online.com/fzuhelper/fzulogin.php?"
//				+ "num=" + mUsername + "&pass=" + mPassword;
//		String result = HttpUtils.getData(url);
//		if(result != null){
//			try{
//				JSONObject json = new JSONObject(result);
//				if(json != null){
//					// 解析
//					String state = json.getString("state");
//					// 处理
//					if(state == null || state.equals("false")){
//						return InfoUtils.SR_LOGIN_NETERROR;
//					}
//					else{
//						String realname = json.getString("userName");
//						UserEntity entity = new UserEntity(username,password,realname);
//						new DbUser(context).setUserEntity(entity);
//						BaseUtils.getInstance().setUserEntity(entity);
//						return InfoUtils.SR_LOGIN_SUCCEED;
//					}
//				}
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		return InfoUtils.SR_LOGIN_NETERROR;
	}
	
	public String getCookie(String username,String password) {
		String cookie = null;
		// 从网络取cookie保存到本地
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		InputStream is = null;
		BufferedReader reader = null;
		String url = "http://59.77.226.32/logincheck.asp";
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Referer", "http://jwch.fzu.edu.cn/");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("muser", username));
			nameValuePairs.add(new BasicNameValuePair("passwd", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				if (entity != null) {
					// 获得响应的字符集编码信息
					String charset = EntityUtils.getContentCharSet(entity);
					if (charset == null) {
						charset = "GB2312";
					}
					is = entity.getContent();
					StringBuffer result = new StringBuffer();
					String line = null;
					reader = new BufferedReader(new InputStreamReader(is, charset));
					while ((line = reader.readLine()) != null) {
						result.append(line);
					}

					String res= result.toString().trim();
					if(res!=null && res.contains("密码错误")){
						return InfoUtils.SR_LOGIN_WRONG;
					}
					if(res==null||res.length()==0){
						return InfoUtils.SR_LOGIN_NETERROR;
					}
				}
				// 获得响应
				List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					Cookie cookie1 = cookies.get(i);
					String cookieName = cookie1.getName();
					String cookieValue = cookie1.getValue();
					if (!TextUtils.isEmpty(cookieName) && !TextUtils.isEmpty(cookieValue)) {
						sb.append(cookieName + "=");
						sb.append(cookieValue + ";");
					}
				}
				return InfoUtils.SR_LOGIN_SUCCEED;
			} else {
				return response.getStatusLine().getStatusCode() + "";
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			httppost.abort();
			httpClient.getConnectionManager().shutdown();
		}
		return InfoUtils.SR_LOGIN_NETERROR;
	}
}
