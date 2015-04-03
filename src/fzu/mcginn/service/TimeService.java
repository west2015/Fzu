package fzu.mcginn.service;

import java.util.Calendar;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.text.format.Time;
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
		if(resEntity != null){
			int week = getCurWeek();
			DateEntity date = new DateEntity();
			date.setSchoolYear(resEntity.getSchoolYear());
			date.setTerm(resEntity.getTerm());
			date.setYear(resEntity.getYear());
			date.setMonth(resEntity.getMonth());
			date.setDay(resEntity.getDay());
			date.setweekDay(resEntity.getweekDay());
			date.setCurrentWeek(week);
			return date;
		}
		else{
			return null;
		}
	}
	
//	// 刷新周数
//	public int refreshCurWeek(){
//		Log.e("!!!!!!!!!!!", "shua xin zhou shu ");
//		int res = getCurWeek();
//		Log.e("CurrentWeek", "CurrentWeek = " +  res);
//		if(res >= 1){
//			DateEntity d = BaseUtils.getInstance().getDateEntity();
//			d.setCurrentWeek(res);
//			BaseUtils.getInstance().setDateEntity(d);
//			return res;
//		}
//		return 1;
//	}
	
	// 获取本周是第几周
	public int getCurWeek(){
		DateEntity e = BaseUtils.getInstance().getDateEntity();
//		Test
//		if(e == null){
//			Log.e("", "DateEntity is null");
//		}
//		else{
//			Log.e("", "DateEntity is not null");
//			if(e.getYear() == null) Log.e("", "year is null"); else Log.e("", "year is not null");
//			if(e.getMonth() == null) Log.e("", "month is null"); else Log.e("", "month is not null");
//			if(e.getDay() == null) Log.e("", "day is null"); else Log.e("", "day is not null");
//			if(e.getweekDay() == null) Log.e("", "weekday is null"); else Log.e("", "weekday is not null");
//		}
		if(e != null && e.getYear() != null && e.getMonth() != null && e.getDay() != null && e.getweekDay() != null){
			int y1 = e.getYear(), m1 = e.getMonth(), d1 = e.getDay(), w1 = e.getweekDay();
//			Log.e("", "y1 = " + y1 + ", m1 = " + m1 + ", d1 = " + d1 + ", w1 = " + w1 + ", weekday = " + e.getCurrentWeek());
			Time t = new Time("GMT+8");
			t.setToNow();
			int y2 = t.year, m2 = t.month + 1, d2 = t.monthDay, w2 = getWeekDay();
//			Log.e("", "y2 = " + y2 + ", m2 = " + m2 + ", d2 = " + d2 + ", w2 = " + w2);
			int t1 = parseToDay(y1, m1, d1) - w1;
			int t2 = parseToDay(y2, m2, d2) - w2;
			int res = (t2 - t1 + 1) / 7;
			return res + e.getCurrentWeek();
		}
		return 0;
	}

	// 日期转为该年第几天
	public int parseToDay(int year, int month, int day){
		int res = day;
		int m[] = {31,28,31,30,31,30,31,31,30,31,30,31};
		int extra = 0; // 判断是否是闰年
		if((year % 400 == 0) || (year % 100 != 0 && year % 4 == 0)){
			extra = 1;
		}
		if(month > 2){
			res += extra;
		}
		for(int i=0;i<month;++i){
			res += m[i];
		}
		return res;
	}

	// 获取星期几	
	public int getWeekDay(){
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
			resEntity.setweekDay(parseWeekDay(date));
			Log.e("!!!!!!!!!!!!",resEntity.getYear()+"年"+resEntity.getMonth()+"月"+resEntity.getDay()+"日"+"星期"+resEntity.getweekDay()+" "+resEntity.getSchoolYear()+"学年"+resEntity.getTerm()+"学期"+"第"+resEntity.getCurrentWeek()+"周");
			return resEntity;
		}
		return null;
	}
	
	public int parseWeekDay(String str){
		if(str.contains("一")) return 0;
		if(str.contains("二")) return 1;
		if(str.contains("三")) return 2;
		if(str.contains("四")) return 3;
		if(str.contains("五")) return 4;
		if(str.contains("六")) return 5;
		return 6;
	}

}
