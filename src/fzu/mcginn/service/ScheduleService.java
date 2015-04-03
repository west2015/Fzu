package fzu.mcginn.service;

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

import android.content.Context;
import fzu.mcginn.database.DbSchedule;
import fzu.mcginn.entity.CourseEntity;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.FzuHttpUtils;

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

	// 解析html获取课表
	public String querySchedule(UserEntity user,String xn,String xq){
		JSONArray jsonArr = new JSONArray();
		String url="http://59.77.226.34/xszy/wsxk/wdkb/kb_xs.asp?menu_no="
									+"&xh="+user.getUsername()
									+"&xn="+xn
									+"&xq=0"+xq;
		String res = FzuHttpUtils.getData(url);
		if(res == null || res.length() == 0){
			return null;
		}
		Document doc = Jsoup.parse(res);
		Elements eles = doc.select("table[bordercolor=#111111]");
		
		for(int week=1;week<=eles.size();week++){
			List<CourseEntity> weekCourses = new ArrayList<CourseEntity>();
			JSONArray courseArr = new JSONArray();
			Element ele = eles.get(week-1);  //第week周
			Elements es = ele.select("tr[id]");
			
			for(int jie=1;jie<=es.size();jie+=2){
				Element jieEle = es.get(jie-1);
				Elements weekEles = jieEle.select("td[id]");
				for(int j=0;j<weekEles.size();j++){
					Element e = weekEles.get(j);
					int weekday;
					int length=1;
					if(e.attr("rowspan")!=null && e.attr("rowspan").length()!=0){
						length = Integer.parseInt(e.attr("rowspan"));
					}
					weekday = Integer.parseInt(e.attr("id").substring(1));
					if(weekday%2 == 0)
						weekday /= 2;
					else
						weekday = weekday/2 + 1;
					String text = e.text();
					if(text == null) continue;
					String mes[] = text.split(" ");
					if(mes.length != 4) continue;
					
					JSONObject jsObj = new JSONObject();
					try {
						jsObj.put("weekday", weekday);
						jsObj.put("length", length);
						jsObj.put("lesson", jie);
						jsObj.put("name", mes[0]);
						jsObj.put("place", mes[1]);
						jsObj.put("teachername", mes[2]);
						jsObj.put("startweek", Integer.parseInt(mes[3].split("-")[0]));
						jsObj.put("endweek", Integer.parseInt(mes[3].split("-")[1]));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					courseArr.put(jsObj);
				}
			}
			JSONObject obj = new JSONObject();
			try {
				obj.put("week",week);
				obj.putOpt("courseArr",courseArr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonArr.put(obj);
		}
		return jsonArr.toString();
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
