package com.west2.main.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.west2.main.database.DbExam;
import com.west2.main.entity.ExamEntity;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.FzuHttpUtils;

import android.content.Context;
import android.util.Log;

public class ExamService {
	
	private static final String URL_EXAM="http://59.77.226.35/student/xkjg/examination/exam_list.aspx?id=";
	
	public List<ExamEntity> getExam(Context context, boolean toRefresh){
		int time = 5;
		List<ExamEntity> mList = null;
		if(!toRefresh){
			String examJson = new DbExam(context).getExamJson();
			mList = getExamList(examJson);
		}
		if(toRefresh || mList == null){
			String examJson = null;
			while(time > 0 || examJson == null || examJson.length() < 10){
				--time;
				examJson = getExamJson();
			}
			if(examJson != null && examJson.length() > 10){
				Log.e("", examJson);
				mList = getExamList(examJson);
				// 刷新成功
				new DbExam(context).setExamJson(examJson);
			}
			else{
//				Log.e("", "考场为空");
			}
		}
		return mList;
	}
	
	public List<ExamEntity> getExamList(String examJson) {
		if (examJson == null)
			return null;
		List<ExamEntity> exams = new ArrayList<ExamEntity>();
		try {
			JSONArray array = new JSONArray(examJson);
			for (int i = 0; i < array.length(); i++) {
				ExamEntity exam = new ExamEntity();
				JSONObject obj = array.getJSONObject(i);
				exam.setCourseName(obj.getString("courseName"));
				exam.setExamPlace(obj.getString("examPlace"));
				exam.setTeacher(obj.getString("teacher"));
				exam.setExamTime(obj.getString("examTime"));
				exams.add(exam);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return exams;
	}

	
	
	
	
	public String getExamJson() {
		String id = BaseUtils.getInstance().getUserEntity().getLoginId();
		String res = FzuHttpUtils.getData(URL_EXAM+id);
		if (res == null || res.length() == 0)
			return "";
		JSONArray jsonArray = new JSONArray();
		Document doc = Jsoup.parse(res);
		Elements eles = doc.select("tr[onmouseout]");
		for (int i = 0; i < eles.size() ; i++) {
			Elements es = eles.get(i).select("td");
			JSONObject obj = new JSONObject();
			try {
				obj.put("courseName", es.get(0).text());
				obj.put("teacher", es.get(2).text());
				String str = es.get(3).html();
				String r[] = str.split("&nbsp;&nbsp;&nbsp;&nbsp;");
				// r[0]: 2015年01月06日
				// r[1]: 09:00-11:00
				// r[2]: 文2-405
				if (r.length == 3) {
					obj.put("examPlace", r[2]);
					obj.put("examTime", r[0] + r[1]);
				}else{
					obj.put("examPlace", "");
					obj.put("examTime", "未录入");
				}
				jsonArray.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray.toString();
	}

}
