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
import org.jsoup.select.Elements;

import android.content.Context;

import com.west2.main.database.DbSchedule;
import com.west2.main.entity.CourseEntity;
import com.west2.main.entity.DateEntity;
import com.west2.main.entity.UserEntity;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.FzuHttpUtils;

public class ScheduleService {
	
	public void setDisWay(Context context,String str){
		new DbSchedule(context).setDisWay(str);
	}
	
	public String getDisWay(Context context){
		return new DbSchedule(context).getDisWay();
	}
	
	public String getSchedule(boolean isRefresh){
		int time = 10;
		String res = null;
		if(!isRefresh){
			res = BaseUtils.getInstance().getScheduleJson();
		}
		if(isRefresh || res == null){
			UserEntity user = BaseUtils.getInstance().getUserEntity();
			DateEntity date = BaseUtils.getInstance().getDateEntity();
			String schYear = "";
			String term = "";
			if(date != null){
				schYear = date.getSchoolYear() == null ? "" : date.getSchoolYear().toString();
				term = date.getTerm() == null ? "" : date.getTerm().toString();
			}
			while(time > 0 && (res == null || res.length() < 10)){
				--time;
				res = querySchedule(user,schYear,term);
			}
		}
		if(res != null && res.length() > 10){
			BaseUtils.getInstance().setScheduleJson(res);
		}
		return res;
	}

	
	private JSONObject getPostDataJson(String xnxq,String url){
		JSONObject obj = new JSONObject();
		String res = FzuHttpUtils.getData(url);
		if(res==null||res.length()==0) return null;
		String VIEWSTATE="";
		String EVENTVALIDATION = "";
		Document doc = Jsoup.parse(res);
		Elements es = doc.select("input[name=__VIEWSTATE]");
		if(es.first()!=null)
			VIEWSTATE = es.first().attr("value");
		es = doc.select("input[name=__EVENTVALIDATION]");
		if(es.first()!=null)
			EVENTVALIDATION = es.first().attr("value");	
		try {
			obj.put("ctl00$ContentPlaceHolder1$BT_submit","确定");
			obj.put("ctl00$ContentPlaceHolder1$DDL_xnxq", xnxq);
			obj.put("__EVENTVALIDATION",EVENTVALIDATION);
			obj.put("__VIEWSTATE",VIEWSTATE);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return obj;
	}
	
	// 解析html获取课表
	public String querySchedule(UserEntity user,String xn,String xq){
		String id = user.getLoginId();
		String url = "http://59.77.226.35/student/xkjg/wdxk/xkjg_list.aspx?id="+id;
		JSONObject postData = getPostDataJson(xn+"0"+xq,url);
		String res = FzuHttpUtils.postData(url, postData);
		if(res==null||res.length()<10) return "";
		JSONArray  []weekJsonArray = new JSONArray[25];
		for(int i=0;i<25;i++)
			weekJsonArray[i] = new JSONArray();
		
		Document doc = Jsoup.parse(res);
		Elements eles = doc.select("tr[onmouseout]");
		for(int i=0;i<eles.size();i++){
			
			Elements es = eles.get(i).select("td");
			
			int weekday,length,lesson;
			String name,place,teachername;
			int  startweek,endweek;
			name =es.get(1).text();
			teachername = es.get(7).text();
			String []strs = es.get(8).html().replace("<br />", " ").split(" ");
			for(String s:strs){
				String []mes = s.split("&nbsp;");
				if(mes.length!=3) continue;
				startweek = Integer.parseInt(mes[0].split("-")[0]);
				endweek = Integer.parseInt(mes[0].split("-")[1]);
				place = mes[2];
				String []subMes= mes[1].split(":");
				if(subMes.length!=2) continue;
				weekday = Integer.parseInt(subMes[0].replace("星期", ""));
				int startJie,endJie;
				startJie =Integer.parseInt(subMes[1].replace("节", "")
						               .replace("(单)", "")
						               .replace("(双)", "").split("-")[0]);
				endJie =Integer.parseInt(subMes[1].replace("节", "")
			               .replace("(单)", "")
			               .replace("(双)", "").split("-")[1]);
				lesson = startJie;
				length = endJie-startJie+1;
				
				for(int j=startweek;j<=endweek;j++){
					
					if(subMes[1].contains("单")&&j%2==0) continue;
					if(subMes[1].contains("双")&&j%2!=0) continue;
					JSONObject jsObj = new JSONObject();
					try {
						jsObj.put("weekday", weekday);
						jsObj.put("length", length);
						jsObj.put("lesson", lesson);
						jsObj.put("name", name);
						jsObj.put("place", place);
						jsObj.put("teachername", teachername);
						jsObj.put("startweek", startweek);
						jsObj.put("endweek", endweek);
						weekJsonArray[j-1].put(jsObj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		JSONArray array = new JSONArray();
		for(int i=0;i<22;i++){
			JSONObject obj = new JSONObject();
			try {
				obj.put("week",i+1);
				obj.putOpt("courseArr",weekJsonArray[i]);
				array.put(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return array.toString();
	}
	
	public List<CourseEntity> sort(List<CourseEntity> mList){
		Comparator<CourseEntity> C = new Comparator<CourseEntity>(){
			public int compare(CourseEntity arg0, CourseEntity arg1) {
				// weekday
				if(arg0.getWeekday() < arg1.getWeekday()) return -1;
				if(arg0.getWeekday() > arg1.getWeekday()) return 1;
				// lesson
				if(arg0.getLesson() < arg1.getLesson()) return -1;
				if(arg0.getLesson() > arg1.getLesson()) return 1;
				return -1;
			}
		};
		Collections.sort(mList,C);
		return mList;
	}
	
	public List<CourseEntity> parseWeek(String res,int mWeek){
		try {
			List<CourseEntity> mList = new ArrayList<CourseEntity>();
			JSONArray jsonArr = new JSONArray(res);
			// week
			for(int i=0;i<jsonArr.length();++i){
				JSONObject jsonWeek = jsonArr.getJSONObject(i);
				if(jsonWeek.getInt("week") == mWeek){
					JSONArray arrWeek = jsonWeek.getJSONArray("courseArr");
					// lesson
					for(int j=0;j<arrWeek.length();++j){
						JSONObject courseJson = arrWeek.getJSONObject(j);
						CourseEntity entity = new CourseEntity();
						entity.setName(courseJson.getString("name"));
						entity.setTeacherName(courseJson.getString("teachername"));
						entity.setPlace(courseJson.getString("place"));
						entity.setWeekday(courseJson.getInt("weekday"));
						entity.setLesson(courseJson.getInt("lesson"));
						entity.setLength(courseJson.getInt("length"));
						entity.setStartWeek(courseJson.getInt("startweek"));
						entity.setEndWeek(courseJson.getInt("endweek"));
						mList.add(entity);
					}
					break;
				}
			}
			mList = sort(mList);
			return mList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CourseEntity> parseAll(String res){
		try {
			List<CourseEntity> mList = new ArrayList<CourseEntity>();
			JSONArray jsonArr = new JSONArray(res);
			// week
			for(int i=0;i<jsonArr.length();++i){
				JSONObject jsonWeek = jsonArr.getJSONObject(i);
				JSONArray arrWeek = jsonWeek.getJSONArray("courseArr");
				// lesson
				for(int j=0;j<arrWeek.length();++j){
					JSONObject courseJson = arrWeek.getJSONObject(j);
					CourseEntity entity = new CourseEntity();
					entity.setName(courseJson.getString("name"));
					entity.setTeacherName(courseJson.getString("teachername"));
					entity.setPlace(courseJson.getString("place"));
					entity.setWeekday(courseJson.getInt("weekday"));
					entity.setLesson(courseJson.getInt("lesson"));
					entity.setLength(courseJson.getInt("length"));
					entity.setStartWeek(courseJson.getInt("startweek"));
					entity.setEndWeek(courseJson.getInt("endweek"));
					mList.add(entity);
				}
			}
			return mList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
