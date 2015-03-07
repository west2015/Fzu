package fzu.mcginn.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import fzu.mcginn.entity.CourseEntity;
import fzu.mcginn.utils.HttpUtils;
import fzu.mcginn.utils.InfoUtils;

public class NoScheduleService {

	private Context context;
	
	public NoScheduleService(Context context){
		this.context = context;
	}

	private List<CourseEntity> sortData(List<CourseEntity> mList){
		if(mList == null || mList.size() <= 0) return null;
		// sort
		Comparator<CourseEntity> C = new Comparator<CourseEntity>(){
			public int compare(CourseEntity arg0, CourseEntity arg1) {
				// name
				if(arg0.getName().compareTo(arg1.getName()) < 0) return -1;
				if(arg0.getName().compareTo(arg1.getName()) > 0) return 1;
				// week
				if(arg0.getStartWeek() < arg1.getStartWeek()) return -1;
				if(arg0.getStartWeek() > arg1.getStartWeek()) return 1;
				return -1;
			}
		};
		Collections.sort(mList,C);
		// simplify
		List<CourseEntity> resList = new ArrayList<CourseEntity>();
		for(int i=0;i<mList.size();++i)
		if(!mList.get(i).getName().equals("")){
			CourseEntity arg0 = mList.get(i);
			int j = i + 1;
			while(j < mList.size()){
				if(mList.get(j).getName().equals(arg0.getName())){
					++j;
				}
				else{
					break;
				}
			}
			int startWeek = arg0.getStartWeek();
			int endWeek = mList.get(j - 1).getStartWeek();
			for(int k=i;k<j;++k){
				CourseEntity ce = mList.get(k);
				ce.setStartWeek(startWeek);
				ce.setEndWeek(endWeek);
				resList.add(ce);
			}
		}
		return resList;
	}
	
	public List<CourseEntity> query(){
// 处理		
		String url = "http://api.west2online.com/fzuhelper/timeTable.php?" ;
// 处理	
		String result = HttpUtils.getData(url);
		if(result != null){ // 获取成功
			try{
				JSONObject json = new JSONObject(result);
				if(json != null){
					// result list
					List<CourseEntity> mList = new ArrayList<CourseEntity>();
					
					JSONArray arrAllCourses = json.getJSONArray("WeekArr");
					// week
					for(int i=0;i<arrAllCourses.length();++i){
						JSONObject jsonWeek = arrAllCourses.getJSONObject(i);
						int week = InfoUtils.getNumber(jsonWeek.getString("week"));
						JSONObject arrWeekCourses = jsonWeek.getJSONObject("courseArr");
						// weekday
						for(int j=0;j<7;++j){
							JSONObject arrWeekdayCourses = arrWeekCourses.getJSONObject((j+1)+"");
							// lesson
							for(int k=0;k<5;++k){
								JSONObject jsonLesson = arrWeekdayCourses.getJSONObject((k+1)+"");
								CourseEntity ce = new CourseEntity();
								String name = jsonLesson.getString("courseName");
								ce.setName(jsonLesson.getString("courseName"));
								ce.setPlace(jsonLesson.getString("place"));
								ce.setTeacherName(jsonLesson.getString("teacherName"));
								ce.setWeekday(j);
								ce.setLesson(k);
								ce.setStartWeek(week);
								ce.setEndWeek(week);
								mList.add(ce);
							}
						}
					}
					return mList;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
}
