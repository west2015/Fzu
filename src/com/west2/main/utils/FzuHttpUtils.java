package com.west2.main.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.west2.main.database.DbUser;
import com.west2.main.entity.UserEntity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


public class FzuHttpUtils {

	
	
	public static String login(UserEntity user){
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		InputStream is = null;
		BufferedReader reader = null;
		HttpPost httppost = new HttpPost("http://59.77.226.32/logincheck.asp");
		httppost.setHeader("Referer", "http://jwch.fzu.edu.cn/");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("muser", user.getUsername()));

			nameValuePairs.add(new BasicNameValuePair("passwd", user.getPassword()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
	
			if (response.getStatusLine().getStatusCode() == 200) {
				
				
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
				BaseUtils.getInstance().setCookie(sb.toString());
				
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
					if(res.contains("top.aspx?id=")){
						String id=res.split("id=")[1].split("\"")[0];
						user.setLoginId(id);
						String str =FzuHttpUtils.getData("http://59.77.226.35/top.aspx?id="+id);
//						Log.e("H", res.split("当前用户：")[1]+"!!!");
						user.setRealname(str.split("当前用户：")[1].split("&nbsp;")[0]);
						BaseUtils.getInstance().setUserEntity(user);
					}
					else
						return InfoUtils.SR_LOGIN_NETERROR;
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
	
	public static String getCookie(UserEntity user) {
		String cookie = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		InputStream is = null;
		BufferedReader reader = null;
		HttpPost httppost = new HttpPost("http://59.77.226.32/logincheck.asp");
		httppost.setHeader("Referer", "http://jwch.fzu.edu.cn/");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("muser", user.getUsername()));

			nameValuePairs.add(new BasicNameValuePair("passwd",user.getPassword()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (response.getStatusLine().getStatusCode() == 200) {

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
				BaseUtils.getInstance().setCookie(sb.toString());
				
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
					if(res.contains("top.aspx?id=")){
						user.setLoginId(res.split("id=")[1].split("\"")[0]);
						BaseUtils.getInstance().setUserEntity(user);
					}
					else
						return InfoUtils.SR_LOGIN_NETERROR;
				}
				
				
				return sb.toString();
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
	
	

	/**
	 * @param url
	 *		请求的url地址
	 * @return 
	 */
	public static String getData(String url) {
		String cookie;
		if(BaseUtils.getInstance().getCookie()==null){
			cookie=FzuHttpUtils.getCookie(BaseUtils.getInstance().getUserEntity());
			url = url.split("id=")[0]+"id="+BaseUtils.getInstance().getUserEntity().getLoginId();
		}
		else{
			cookie=BaseUtils.getInstance().getCookie();
		}
		
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		HttpGet get = null;
		BufferedReader reader = null;
		HttpResponse response;
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Cookie",cookie);
		httpget.setHeader("Referer", "http://59.77.226.35/");
		
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// 获得响应的字符集编码信息
				String charset = EntityUtils.getContentCharSet(entity);
				if (charset == null) {
					charset = "GB2312";
				}
				is = entity.getContent();
				StringBuffer result = new StringBuffer();
				String line = null;
				reader= new BufferedReader(new InputStreamReader(is, charset));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				return result.toString().trim();
			}
			
			
			


		} catch (ClientProtocolException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			httpget.abort();
			httpClient.getConnectionManager().shutdown();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			httpget.abort();
			httpClient.getConnectionManager().shutdown();
		}
		
		return null;
	}
	
	
	
	/**
	 * post方法，该方法在此客户端默认发送表单为：{data：JSON数据}
	 * 
	 * @param httpClient
	 * @param url
	 *            请求的url地址
	 * @param jsonObject
	 *            封装好的JSON数据
	 * @return
	 */
	public static String postData(String url, JSONObject jsonObject) {
		String cookie="";
		if(BaseUtils.getInstance().getCookie()==null){
			cookie=FzuHttpUtils.getCookie(BaseUtils.getInstance().getUserEntity());
			url = url.split("id=")[0]+"id="+BaseUtils.getInstance().getUserEntity().getLoginId();
		}
		else{
			cookie=BaseUtils.getInstance().getCookie();
		}
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = null;
		HttpEntity mEntity = null;
		HttpResponse mResponse = null;
		BufferedReader reader = null;
		InputStream inStream = null;
		try {
			post = new HttpPost(url);
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("data", jsonObject.toString()));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			post.setHeader("Cookie",cookie);
			post.setHeader("Referer", "http://59.77.226.35/");
			mResponse = httpClient.execute(post);
			mEntity = mResponse.getEntity();
			if(mEntity==null) return null;
			inStream = mEntity.getContent();
			StringBuffer res = new StringBuffer();
			String line = null;
			reader = new BufferedReader(new InputStreamReader(inStream));
			while ((line = reader.readLine()) != null)
				res.append(line);
			return res.toString().trim();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				inStream.close();
				post.abort();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
