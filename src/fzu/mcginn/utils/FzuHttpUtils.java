package fzu.mcginn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
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
import org.apache.http.util.EntityUtils;

import fzu.mcginn.database.DbUser;
import fzu.mcginn.entity.UserEntity;
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
					
					user.setRealname(res.split("欢迎")[1].split("同学")[0]);
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

//			Log.e("3", response.getStatusLine().getStatusCode() + "");
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
		
		String cookie=FzuHttpUtils.getCookie(BaseUtils.getInstance().getUserEntity());
		
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		HttpGet get = null;
		BufferedReader reader = null;
		try{
			URL mUrl = new URL(url);
			URI uri = new URI(mUrl.getProtocol(), mUrl.getHost(), mUrl.getPath(), mUrl.getQuery(), null);
			get = new HttpGet(uri);
			get.setHeader("Cookie",cookie);
			get.setHeader("Referer","http://59.77.226.33");
			HttpParams params = new BasicHttpParams();
			//设置连接超时
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            //设置请求超时
            HttpConnectionParams.setSoTimeout(params, 10000);
            get.setParams(params);
			HttpResponse response = httpClient.execute(get);
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
				reader = new BufferedReader(new InputStreamReader(is, charset));
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				return result.toString().trim();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				reader.close();
				is.close();
				get.abort();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
