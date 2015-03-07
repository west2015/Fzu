package fzu.mcginn.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.utils.HttpUtils;
import android.content.Context;
import android.util.Log;

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
		return resEntity;
	}
	
	// 解析html获取时间
	public DateEntity getNetTime1(){
		String url = "http://59.77.226.32/tt.asp";
		String res = HttpUtils.getData(url);
		if(res != null){
			Document doc = Jsoup.parse(res);
			Elements eles = doc.select("body");
			Log.e("!!!!!!!!!!", eles.text());
		}
		else{
			Log.e("!!!!!!!", "time is null");
		}
		return null;
	}
	// 调用接口获取时间
	public DateEntity getNetTime2(){
		String url = "http://api.west2online.com/fzuhelper/nowdate.php";
		String res = HttpUtils.getData(url);
		if(res != null){
			
		}
		return null;
	}
	
}
