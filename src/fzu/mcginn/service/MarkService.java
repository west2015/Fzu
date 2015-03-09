package fzu.mcginn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import fzu.mcginn.entity.MarkEntity;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.utils.FzuHttpUtils;
import android.app.Activity;

public class MarkService {
	private Activity activity;
	public MarkService(Activity activity){
		this.activity = activity;
	}

	
	
	public Map<String,List<MarkEntity>> queryMark(){
		
		String url="http://59.77.226.34/xszy/wdcj/cjyl/zhcx_xs.asp?xn=&xq=&zylb=%B1%BE%D7%A8%D2%B5&px=kcmc&change=yes&subs=%B3%C9%BC%A8%B2%E9%D1%AF";
		String res = FzuHttpUtils.getData(url);
		if(res==null) return null;
		List<String> timeList = new ArrayList<String>();
		Map<String,List<MarkEntity>> map = new TreeMap<String,List<MarkEntity>>();
		try{

			Document doc = Jsoup.parse(res);
			Elements ele = doc.select("tr[bgcolor=ffffff]");
			if (ele == null || ele.size() == 0)
				return map;
			for (int i = 0; i < ele.size(); i++) {
				Element e = ele.get(i);
				Elements es =e.getElementsByTag("td");
				if(es==null) return map;
				MarkEntity mark = new MarkEntity();
				mark.setCourseName(es.get(1).text());
				mark.setScore(es.get(3).text());
				mark.setGradePoint(es.get(4).text());
				mark.setGradeCridit(es.get(2).text());
				String time = es.get(0).text();
				if(map.containsKey(time)){
					List<MarkEntity> list = map.get(time);
					list.add(mark);
				}
				else{
					timeList.add(time);
					List<MarkEntity> list = new ArrayList<MarkEntity>();
					list.add(mark);
					map.put(time, list);
				}
			}
			
			ele = doc.select("tr[bgcolor=ffcccc]");
			if (ele != null && ele.size() != 0){
				for (int i = 0; i < ele.size(); i++) {
					Element e = ele.get(i);
					Elements es =e.getElementsByTag("td");
					if(es==null) return map;
					MarkEntity mark = new MarkEntity();
					mark.setCourseName(es.get(1).text());
					mark.setScore(es.get(3).getAllElements().get(0).text());
					mark.setGradePoint(es.get(4).text());
					mark.setGradeCridit(es.get(2).text());
					String time = es.get(0).text();
					if(map.containsKey(time)){
						List<MarkEntity> list = map.get(time);
						list.add(mark);
					}
					else{
						timeList.add(time);
						List<MarkEntity> list = new ArrayList<MarkEntity>();
						list.add(mark);
						map.put(time, list);
					}
				}
			}
			return map;
		}
		catch(Exception e){
			e.printStackTrace();
			return map;
		}
	}
	
	
	
	
	
	
}