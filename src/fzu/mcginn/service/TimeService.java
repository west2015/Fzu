package fzu.mcginn.service;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import fzu.mcginn.database.DbDate;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.HttpUtils;
import fzu.mcginn.utils.InfoUtils;

public class TimeService {
	
	Context context;
	
	public TimeService(Context context){
		this.context = context;
	}
	
	public DateEntity getNetTime(){
		DateEntity resEntity;
		resEntity = getNetTime1();
		if(resEntity == null)
		resEntity = getNetTime2();
		if(resEntity != null){
			new DbDate(context).setDateEntity(resEntity);
			BaseUtils.getInstance().setDateEntity(resEntity);
		}
		return resEntity;
	}
	
	// ����html��ȡʱ��
	public DateEntity getNetTime1(){
		String url = "http://59.77.226.32/tt.asp";
		String res = HttpUtils.getData(url);
		if(res != null){
			Document doc = Jsoup.parse(res);
			Elements eles = doc.select("body");
			String date = eles.text().replace(" ", "");
			DateEntity resEntity = new DateEntity();
			resEntity.setYear(InfoUtils.getNumber(date,4,0));
			resEntity.setMonth(InfoUtils.getNumber(date, 4, 1));
			resEntity.setDay(InfoUtils.getNumber(date, 4, 2));
			resEntity.setSchoolYear(InfoUtils.getNumber(date, 4, 3));
			resEntity.setTerm(InfoUtils.getNumber(date, 4, 4));
			resEntity.setWeek(InfoUtils.getNumber(date, 4, 5));
//			Log.e("!!!!!!!!!!!!",resEntity.getYear()+"��"+resEntity.getMonth()+"��"+resEntity.getDay()+"��"+resEntity.getSchoolYear()+"ѧ��"+resEntity.getTerm()+"ѧ��"+"��"+resEntity.getWeek()+"��");
			return resEntity;
		}
		return null;
	}
	// ���ýӿڻ�ȡʱ��
	public DateEntity getNetTime2(){
		String url = "http://api.west2online.com/fzuhelper/nowdate.php";
		String res = HttpUtils.getData(url);
		if(res != null){
			try{
				JSONObject json = new JSONObject(res);
				Integer week = Integer.parseInt(json.getString("week"));
				String date = json.getString("date");
				DateEntity resEntity = new DateEntity();
				resEntity.setWeek(week);
				resEntity.setYear(InfoUtils.getNumber(date, 4, 0));
				resEntity.setMonth(InfoUtils.getNumber(date, 4, 1));
				resEntity.setDay(InfoUtils.getNumber(date, 4, 2));
				resEntity.setWeekDay(date.substring(date.length()-3, date.length()));
//				Log.e("!!!!!!!!!!!!",resEntity.getYear()+"��"+resEntity.getMonth()+"��"+resEntity.getDay()+"��"+resEntity.getWeekDay()+"��"+resEntity.getWeek()+"��");
				return resEntity;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
