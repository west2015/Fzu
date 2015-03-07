package fzu.mcginn.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import fzu.mcginn.entity.CourseEntity;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.utils.FzuHttpUtils;

public class ScheduleService {

	public String querySchedule(UserEntity user,Integer xn,Integer xq){
		JSONArray jsonArr = new JSONArray();
		String url="http://59.77.226.33/xszy/wsxk/wdkb/kb_xs.asp?menu_no="
									+"xh="+user.getUsername()
									+"xn="+xn
									+"xq="+xq;
		String res = FzuHttpUtils.getData(url);
		if(res == null || res.length() == 0) return null;
		Document doc =Jsoup.parse(res);
		Elements eles = doc.select("table[bordercolor=#111111]");
		
		for(int week=1;week<=eles.size();week++){
			List<CourseEntity> weekCourses = new ArrayList<CourseEntity>();
			JSONArray courseArr = new JSONArray();
			Element ele = eles.get(week-1);  //µÚweekÖÜ
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
					if(text==null) continue;
					String mes[] = text.split(" ");
					if(mes.length != 4) continue;

//					for(String s:mes){
//						System.out.println(s);
//					}
//					
//					CourseEntity entity = new CourseEntity();
//					entity.setWeekday(weekday);
//					entity.setLength(length);
//					entity.setLesson(jie);
//					entity.setName(mes[0]);
//					entity.setPlace(mes[1]);
//					entity.setTeacherName(mes[2]);
//					entity.setStartWeek(Integer.parseInt(mes[3].split("-")[0]));
//					entity.setEndWeek(Integer.parseInt(mes[3].split("-")[1]));
//					courses.add(entity);
//					weekCourses.add(entity);
					
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
				obj.putOpt("courseArr",courseArr);
				obj.put("week",week);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonArr.put(obj);
		}
		return jsonArr.toString();
	}
}
