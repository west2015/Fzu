package com.west2.main.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpUtils {
	/**
	 * @param url
	 *		请求的url地址
	 * @return 
	 */
	public static String getData(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		HttpGet get = null;
		BufferedReader reader = null;
		try{
			URL mUrl = new URL(url);
			URI uri = new URI(mUrl.getProtocol(), mUrl.getHost(), mUrl.getPath(), mUrl.getQuery(), null);
			get = new HttpGet(uri);
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

	/**
	 * post方法
	 * 
	 * @param httpClient
	 * @param url
	 *            请求的url地址
	 * @param jsonObject
	 *            封装好的JSON数据
	 * @return
	 */
	public static String postData(String url, JSONObject jsonObject) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = null;
		HttpEntity mEntity = null;
		HttpResponse mResponse = null;
		BufferedReader reader = null;
		InputStream is = null;
		try {
			post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Iterator<String>  keyIter = jsonObject.keys();
			while(keyIter.hasNext()){
				String key =keyIter.next();
				params.add(new BasicNameValuePair(key+"", jsonObject.getString(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.ISO_8859_1));
//			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=gb2312");
			mResponse = httpClient.execute(post);
			mEntity = mResponse.getEntity();
			if(mEntity==null) return null;
			
			
			// 获得响应的字符集编码信息
			String charset = EntityUtils.getContentCharSet(mEntity);
			if (charset == null) {
				charset = "gb2312";
			}
			is = mEntity.getContent();
			StringBuffer result = new StringBuffer();
			String line = null;
			reader = new BufferedReader(new InputStreamReader(is, charset));
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			return result.toString().trim();
			

		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				is.close();
				post.abort();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
