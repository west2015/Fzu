package fzu.mcginn.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
				resEntity = getTime();
			}
			if(resEntity != null){
				// 获取成功
				BaseUtils.getInstance().setDateEntity(resEntity);
			}
		}
		return resEntity;
	}

	// 获取星期几	
	public int getWeekDay(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int res = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		return res;
	}
	
	// 解析html获取时间
	public DateEntity getTime(){
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
			Log.e("!!!!!!!!!!!!",resEntity.getYear()+"年"+resEntity.getMonth()+"月"+resEntity.getDay()+"日"+resEntity.getSchoolYear()+"学年"+resEntity.getTerm()+"学期"+"第"+resEntity.getCurrentWeek()+"周");
			return resEntity;
		}
		return null;
	}

}
