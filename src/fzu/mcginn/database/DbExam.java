package fzu.mcginn.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DbExam {
	private final String DB_NAME = "db_exam";
	private final String Exam_JSON = "db_exam_json";

	private Context context;
	private SharedPreferences sp;
	
	public DbExam(Context context){
		this.context = context;
		sp = context.getSharedPreferences(DB_NAME, 0);
	}

	public String getExamJson(){
		return sp.getString(Exam_JSON, null);
	}

	public void setExamJson(String json){
		Editor ed = sp.edit();
		ed.putString(Exam_JSON, json);
		ed.commit();
	}
	
}
