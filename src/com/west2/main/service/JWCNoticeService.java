package com.west2.main.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.west2.main.database.DbJwcNotice;
import com.west2.main.entity.JWCNoticeEntity;
import com.west2.main.utils.HttpUtils;

import android.content.Context;
import android.util.Log;

public class JWCNoticeService {
	private static final String JWCURL = "http://jwch.fzu.edu.cn/html/jwtz/index.html";
	private static final String HOST = "http://jwch.fzu.edu.cn";

	
	public static List<JWCNoticeEntity> getJwcNotice(Context context, boolean toRefresh){
		int time = 5;
		List<JWCNoticeEntity> mList = null;
		if(!toRefresh){
			String jwcJson = new DbJwcNotice(context).getJwcNoticeJson();
			mList = getNoticeListFormJSON(jwcJson);
		}
		if(toRefresh || mList == null){
			String jwcJson = null;
			while(time > 0 || jwcJson == null || jwcJson.length() < 10){
				--time;
				jwcJson = getNoticeJSON();
			}
			if(jwcJson != null && jwcJson.length() > 10){
				mList = getNoticeListFormJSON(jwcJson);
				// Ë¢ÐÂ³É¹¦
				new DbJwcNotice(context).setJwcNoticeJson(jwcJson);
			}
			else{
			}
		}
		return mList;
	}
	
	
	public static String getNoticeJSON() {

		String res = HttpUtils.getData(JWCURL);
		if (res == null || res.length() == 0)
			return null;

		Document doc = Jsoup.parse(res);
		JSONArray jsonArray = new JSONArray();
		if (doc != null) {
			Elements eles = doc.select("ul[class=Tlist list]");
			Elements es = eles.select("li");
			for (int i = 0; i < es.size(); i++) {
				Element ele = es.get(i);
				JSONObject obj = new JSONObject();
				try {
					Element e;
					e = ele.select("span[class=date]").first();
					if (e != null) {
						obj.put("time", e.text());
						if (e.children() != null && e.children().size() != 0)
							obj.put("isred", true);
						else
							obj.put("isred", false);
					}
					e = ele.select("a").get(1);
					if (e != null) {
						obj.put("title", e.text());
						obj.put("url", HOST + e.attr("href"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jsonArray.put(obj);

			}
		}

		return jsonArray.toString();
	}

	public static List<JWCNoticeEntity> getNoticeListFormJSON(String json) {

		if (json == null || json.length() == 0)
			return null;

		List<JWCNoticeEntity> mList = new ArrayList<JWCNoticeEntity>();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(json);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				JWCNoticeEntity entity = new JWCNoticeEntity();
				entity.setRed(obj.getBoolean("isred"));
				entity.setTime(obj.getString("time"));
				entity.setTitle(obj.getString("title"));
				entity.setUrl(obj.getString("url"));
				mList.add(entity);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mList;

	}
	
	public static String getDetatilHtml(String url){
		
		String res =HttpUtils.getData(url);
		if(res==null||res.length()==0) return "";
		Document doc = Jsoup.parse(res);
		Elements eles = doc.select("div[id=artbody]");
		
		if(eles==null)  return "";
		
		return eles.html();
		
	}

}
