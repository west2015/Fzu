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
					// �����Ӧ���ַ���������Ϣ
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
					if(res!=null && res.contains("�������")){
						return InfoUtils.SR_LOGIN_WRONG;
					}
					if(res==null||res.length()==0){
						return InfoUtils.SR_LOGIN_NETERROR;
					}
					if(res.contains("��ӭ")){
						user.setRealname(res.split("��ӭ")[1].split("ͬѧ")[0]);
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
	 *		�����url��ַ
	 * @return 
	 */
	public static String getData(String url) {
		
		String cookie=FzuHttpUtils.getCookie(BaseUtils.getInstance().getUserEntity());
		
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		HttpGet get = null;
		BufferedReader reader = null;
		HttpResponse response;
		HttpGet httpget = new HttpGet(url);
//		Log.e("1", "cookie:"+cookie);
		
		httpget.setHeader("Cookie",cookie);
//		Log.e("YAO","cookie"+cookie);
		httpget.setHeader("Referer", "http://59.77.226.34/");
		
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();

//			Log.e("3",response.getStatusLine().getStatusCode()+""); 
			if (entity != null) {
				// �����Ӧ���ַ���������Ϣ
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
//					Log.e("login2", line);
				}
				
				
				return result.toString().trim();
				// return IOUtils.toString(is, charset);
			}
			
			
			
//			Log.e("1", "location "+response.getEntity().getContent().toString());


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
}
