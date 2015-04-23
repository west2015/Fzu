package com.west2.main.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.west2.main.entity.MarkEntity;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.FzuHttpUtils;

import android.util.Log;

public class MarkService {

	/*
	 * 按学期排序
	 */
	public List<MarkEntity> sort(List<MarkEntity> mList){
		if(mList == null)
			return mList;
		Comparator<MarkEntity> C = new Comparator<MarkEntity>(){
			public int compare(MarkEntity arg0, MarkEntity arg1) {
				return -arg0.getTerm().compareTo(arg1.getTerm());
			}
		};
		Collections.sort(mList,C);
		return mList;
	}

	/*
	 * 获取成绩
	 */
	public List<MarkEntity> getMark(boolean isRefresh){
		int time = 10;
		String markJson = null;
		if(!isRefresh){
			markJson = BaseUtils.getInstance().getMarkJson();
		}
		if(isRefresh || markJson == null || markJson.length() < 10){
			while(time>0 && (markJson == null || markJson.length() < 10)){
				--time;
				markJson = getMarkJson("","");
			}
			if(markJson != null && markJson.length() > 10){
				BaseUtils.getInstance().setMarkJson(markJson);
			}
		}
		return sort(getMarkList(markJson));
	}
	
	
	
	
	/*
	 * @xn 学年
	 * @xq 学期
	 */
	public String getMarkJson(String xn,String xq){
		
		
		String id =BaseUtils.getInstance().getUserEntity().getLoginId();
		String url = "http://59.77.226.35/student/xyzk/cjyl/score_sheet.aspx?id="+id;
		JSONObject object = new JSONObject();
		try {
			object.put("ctl00$ContentPlaceHolder1$DDL_xnxq", xn+xq);
			object.put("ctl00$ContentPlaceHolder1$BT_submit", "确定");
			object.put("ctl00$ContentPlaceHolder1$zylbdpl", "<-全部->");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res = FzuHttpUtils.postData(url, object);
		
		
		if(res==null || res.length()==0) return "";
		Document doc = Jsoup.parse(res);
		Elements ele = doc.select("tr[bgcolor]");
		if (ele == null || ele.size() == 0)
			return "";
		JSONArray markArray = new JSONArray();
		for (int i = 0; i < ele.size(); i++) {
			Element e = ele.get(i);
			Elements es =e.getElementsByTag("td");
			if(es==null) return "";
			JSONObject obj  = new JSONObject();
			try {
				obj.put("term", es.get(1).text());
				obj.put("coursename",es.get(2).text());
				obj.put("gradecridit", es.get(3).text());
				obj.put("score", es.get(4).text());
				obj.put("gradepoint", es.get(5).text());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			markArray.put(obj);
		}

		return markArray.toString();
	}
	
	public List<MarkEntity> getMarkList(String markJson){
		List<MarkEntity> marks = new ArrayList<MarkEntity>();
		if(markJson==null || markJson.length()==0)
			return null;
		try {
			JSONArray markArray = new JSONArray(markJson);
			if(markArray==null||markArray.length()==0)  return marks;
			for(int i=0;i<markArray.length();i++){
				MarkEntity mark = new MarkEntity();
				JSONObject obj = markArray.getJSONObject(i);
				mark.setCourseName(obj.getString("coursename"));
				mark.setScore(obj.getString("score"));
				mark.setGradePoint(obj.getString("gradepoint"));
				mark.setGradeCredit(obj.getString("gradecridit"));
				mark.setTerm(obj.getString("term"));
				marks.add(mark);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return marks;
	}
	
}