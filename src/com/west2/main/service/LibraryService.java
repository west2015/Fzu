package com.west2.main.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.west2.main.entity.BookEntity;
import com.west2.main.entity.UserEntity;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.HttpUtils;
import com.west2.main.utils.InfoUtils;

public class LibraryService {
	private final static String tag = "LibraryService";

	/*
	 * 续借图书
	 */
	public String renewBook(BookEntity book){
		String base = "http://fzuapi.west2online.com/api/api.php/FzuHelper/bookDelay?";
		if(book != null){
			String url = base + "book_barcode="+book.getBarCode();
			String res = HttpUtils.getData(url);
			Log.e(tag, url);
			Log.e(tag, res);
			if(res != null){
				try {
					JSONObject json = new JSONObject(res);
					if(json.getBoolean("status")){
						return InfoUtils.SR_RENEW_SUCCEED;
					}
					else{
						String errMsg = json.getString("errMsg");
						if(errMsg.contains("借过一次")){
							return InfoUtils.SR_RENEW_HAD;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return InfoUtils.SR_RENEW_FAIL;
	}
	
	/*
	 * 获取已借图书(整合登陆功能)
	 */
	public boolean Login(UserEntity user){
		BaseUtils.getInstance().setLibraryUser(user);
		List<BookEntity> mList = getBook();
		if(mList!=null){
//			for(int i=0;i<mList.size();++i){
//				BookEntity e = mList.get(i);
//				Log.e("LibraryService", e.getName());
//			}
			return true;
		}
//		Log.e("LibraryService", "图书馆获取失败");
		BaseUtils.getInstance().setLibraryUser(null);
		return false;
	}
	
	public List<BookEntity> getBook(){
		String base = "http://fzuapi.west2online.com/api/api.php/FzuHelper/myBook?";
		List<BookEntity> resList = null;
		UserEntity user = BaseUtils.getInstance().getLibraryUser();
		if(user != null){
			String url = base+"user="+user.getUsername()+"&psw="+user.getPassword();
			String res = HttpUtils.getData(url);
			Log.e(tag, url);
			Log.e(tag, res);
			resList = parse2BookList(res);
			if(resList != null){
				BaseUtils.getInstance().setLibraryUser(user);
				BaseUtils.getInstance().setLibraryJson(res);
			}
		}
		return resList;
	}	
	/*
	 * 解析已借图书json
	 */
	public List<BookEntity> parse2BookList(String res){
		List<BookEntity> mList = null;
		if(res!=null){
			try {
				JSONObject json = new JSONObject(res);
				boolean status = json.getBoolean("status");
				if(status){
					mList = new ArrayList<BookEntity>();
					JSONArray arr = json.getJSONArray("data");
					for(int index=0;index<arr.length();++index){
						JSONObject item = arr.getJSONObject(index);
						BookEntity book = new BookEntity();
						book.setName(item.getString("bookName"));
						book.setBarCode(item.getString("barcode"));
						book.setReturnDate(item.getString("returnTime"));
						mList.add(book);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mList = null;
			}			
		}
		return mList;
	}
	
	
	/*
	 * 检索图书
	 */
	public List<BookEntity> search(String key, int page){
		int time1=0, time2=5;
		List<BookEntity> resList = null;
		while(time1>0 && (resList == null || resList.size()==0)){
			resList = searchBook1(key,page);
			--time1;
		}
		while(time2>0 && (resList == null || resList.size()==0)){
			resList = searchBook2(key,page);
			--time2;
		}
		if(resList==null || resList.size()==0){
			Log.e(tag, "resList("+key+", "+page+") is null");
		}
		else{
			Log.e(tag, "resList("+key+", "+page+") is not null");
		}
		return resList;
	}
	
	private static final String URL_LIBRARYSEARCH="http://218.193.121.100/opac_two/search2/searchout.jsp";
	public static List<BookEntity> searchBook1(String key, int page){
		List<BookEntity> bookList= new ArrayList<BookEntity>();
		JSONObject postData = new JSONObject();
		try {
			postData.put("suchen_word", key);
			postData.put("curpage", page+"");
			postData.put("orderby", "pubdate_date");
			postData.put("ordersc", "desc");
			postData.put("pagesize", 20);
			postData.put("library_id", "all");
			postData.put("recordtype", "all");
			postData.put("kind", "simple");
			postData.put("suchen_match", "qx");
			postData.put("suchen_type", "1");
			postData.put("show_type", "wenzi");
			postData.put("snumber_type", "Y");
			postData.put("search_no_type", "Y");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String res = HttpUtils.postData(URL_LIBRARYSEARCH, postData);
		if(res==null||res.length()==0) return bookList;
		try {
			res = new String (res.replaceAll("&nbsp;", "").getBytes("ISO8859-1"),"gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Document doc =Jsoup.parse(res);
		Elements elements = doc.select("tr[class^=td_color_]");
		for(int i=0;i<elements.size();++i){
			BookEntity book = new BookEntity();
			Elements es = elements.get(i).select("td");
			book.setName(es.get(1).text());
			book.setAuthor(es.get(2).text());
			book.setPublisher(es.get(3).text());
			book.setId(es.get(4).text());
			book.setYear(es.get(5).text());
			book.setPlace(es.get(6).text());
			Pattern p = Pattern.compile("(\\d+)");
			Matcher m = p.matcher(es.get(7).text());
			if(m.find())
				book.setStore(Integer.parseInt(m.group()));
			if(m.find())
				book.setCntAmount(Integer.parseInt(m.group()));
			String str = es.get(1).select("a").attr("href");
			book.setAppointment_id(str.split("=")[1]);
			bookList.add(book);
		}
		return bookList;
	}
	
	public static List<BookEntity> searchBook2(String key, int page){
		String base = "http://fzuapi.west2online.com/api/api.php/FzuHelper/bookSearch?";
		String url = base+"key="+key+"&page="+page;
		String res = HttpUtils.getData(url);
		List<BookEntity> resList = null;
		if(res != null){
			try {
				List<BookEntity> mList = new ArrayList<BookEntity>();
				JSONObject json = new JSONObject(res);
				JSONArray arr = json.getJSONArray("data");
				for(int i=0;i<arr.length();++i){
					JSONObject item = arr.getJSONObject(i);
					BookEntity book = new BookEntity();
					book.setName(item.getString("name"));
					book.setAuthor(item.getString("author"));
					book.setPublisher(item.getString("publisher"));
					book.setYear(item.getString("year"));
					book.setPlace(item.getString("place"));
					String amount = item.getString("amount");
					book.setStore(InfoUtils.getNumber(amount, 2, 0));
					book.setCntAmount(InfoUtils.getNumber(amount, 2, 1));
					mList.add(book);
				}
				resList = mList;
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e(tag, "Error in SearchBook2");
			}
		}
		return resList;
	}
}
