package fzu.mcginn.service;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.HttpUtils;
import fzu.mcginn.utils.InfoUtils;

public class TimeService {
	
	public DateEntity getTime(boolean isRefresh){
		int time = 10;
		DateEntity resEntity = null;
		if(!isRefresh){
			resEntity = BaseUtils.getInstance().getDateEntity();
		}
		if(isRefresh || resEntity == null){
			while(time > 0 && resEntity == null){
				--time;
				resEntity = getTime1();
			}
			if(resEntity != null){
				// ��ȡ�ɹ�
				BaseUtils.getInstance().setDateEntity(resEntity);
			}
		}
		if(resEntity == null){
			resEntity = BaseUtils.getInstance().getDateEntity();
		}
		return resEntity;
	}
	
	// ����html��ȡʱ��
	public DateEntity getTime1(){
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
			resEntity.setCurrentWeek(InfoUtils.getNumber(date, 4, 5));
			Log.e("!!!!!!!!!!!!",resEntity.getYear()+"��"+resEntity.getMonth()+"��"+resEntity.getDay()+"��"+resEntity.getSchoolYear()+"ѧ��"+resEntity.getTerm()+"ѧ��"+"��"+resEntity.getCurrentWeek()+"��");
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
				resEntity.setCurrentWeek(week);
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
