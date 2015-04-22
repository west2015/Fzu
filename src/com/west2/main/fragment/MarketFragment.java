package com.west2.main.fragment;

import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.material.widget.ActionView;
import com.material.widget.RaisedButton;
import com.material.widget.action.Action;
import com.material.widget.action.BackAction;
import com.material.widget.action.DrawerAction;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.service.MarketService;
import com.west2.main.utils.InfoUtils;

public class MarketFragment extends Fragment{
	
	
	private Context context;
	private Activity activity;
	private MessageInterface mListener;
	
	
	private WebView mWebView;
	private ActionView mActionView;
	private RaisedButton nextPageButton;
	private RaisedButton prePageButton;
	
	
	private String nextPageUrl;
	private String prePageUrl;
	private String curURL;
	private String preURL;
	
	private String curHtml;
	private boolean isIndex =true;
	

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		activity = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_market, null);
		findView(view);
		setListener(view);
		init();
		return view;
	}


	private void findView(View view) {
		// TODO Auto-generated method stub
		mWebView = (WebView) view.findViewById(R.id.webview);
		nextPageButton = (RaisedButton) view.findViewById(R.id.btn_nextpage);
		prePageButton = (RaisedButton) view.findViewById(R.id.btn_prepage);
		mActionView = (ActionView) view.findViewById(R.id.av);
	}
	private void setListener(View view) {
		// TODO Auto-generated method stub
		
		
		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("login")) {

				}
				//相册 不解析
				if (url.contains("album")) {
					super.shouldOverrideUrlLoading(view, url);
					return false;
				}
				curURL = url;
				view.loadDataWithBaseURL(MarketService.URL_MARKET_HOST, curHtml,
						"text/html", "utf-8", "");
				new LoadHtmlTask().execute();
				return false;
			}

		});
		
		nextPageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(nextPageUrl!=null)
					curURL = nextPageUrl;
				new LoadHtmlTask().execute();
				
			}
		});
		
		prePageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(prePageUrl!=null)
				curURL = prePageUrl;
				new LoadHtmlTask().execute();
			}
		});
		
		mActionView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isIndex){
					mListener.Message(InfoUtils.OPEN_DRAWER);
				}else{
					if(preURL!=null)
						curURL = preURL;
					new LoadHtmlTask().execute();
				}
			}
		});
		
	}
	
	private void init(){
		WebSettings settings= mWebView.getSettings(); 
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		curURL = MarketService.URL_MARKET;
		new LoadHtmlTask().execute();
	}

	
	
	private class LoadHtmlTask extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
		//	Log.e("Market", curURL);
			if(curURL!=null&&curURL.equals("http://bbs.fzu.edu.cn/forum.php")){
				curURL=MarketService.URL_MARKET;
				isIndex = true;
			}else if(curURL.contains("http://bbs.fzu.edu.cn/forum.php?mod=forumdisplay"))
				isIndex = true;
			else
				isIndex = false;
			if(!curURL.contains("mobile=2"))
				curURL+="?mobile=2";
			String res =MarketService.getData(context,curURL);
			if(res==null || res.length()<10) return "";
			
			Document doc = Jsoup.parse(res);
			
			if(isIndex){
				//是否有上下一页
				Elements e ;
				e= doc.select("a[class=prev]");
				if(e!=null&&e.first()!=null){
					prePageUrl = MarketService.URL_MARKET_HOST+ e.first().attr("href");
				}
				e=doc.select("a[class=nxt]");
				if(e!=null&&e.first()!=null){
					nextPageUrl = MarketService.URL_MARKET_HOST+ e.first().attr("href");
				}
			}
			Elements eles =doc.select("a[class=z]");
			if(eles.first()!=null){
				preURL="http://bbs.fzu.edu.cn/"+eles.first().attr("href");
				Log.e("preUrl", preURL+"!!!!");
			}else{
			//	preURL = MarketService.URL_MARKET;
			}
			
			doc.select("form[id=fastpostform]").remove();
			res=doc.html();
			res = res.replace("<div class=\"pg\">", "<!--");
			res = res.replace("<!-- main threadlist end", "");
			res = res.replace("header start -->", "");
			res = res.replace("<!-- header end", "");
			res = res.replace("<div class=\"footer\">", "<!--");
			res = res.replace("</body>", "--> </body>");
			curHtml = res;
			return res;
		}
		@Override
		protected void onPostExecute(String result) {
			
			if(isIndex){
				mActionView.setAction(new DrawerAction(), ActionView.ROTATE_CLOCKWISE);
				nextPageButton.setVisibility(View.VISIBLE);
				prePageButton.setVisibility(View.VISIBLE);
			}else{
				mActionView.setAction(new BackAction(), ActionView.ROTATE_CLOCKWISE);
				nextPageButton.setVisibility(View.GONE);
				prePageButton.setVisibility(View.GONE);
			}
			
			if(result==null||result.length()<10){
				nextPageButton.setVisibility(View.GONE);
				prePageButton.setVisibility(View.GONE);
				SnackbarManager.show(Snackbar.with(context)
                      	.text("遇到错误啦")
                        .actionLabel("重试")
                        .actionColorResource(R.color.yellow_500)
                        .actionListener(new ActionClickListener() {
                            public void onActionClicked(Snackbar snackbar) {
                            	new LoadHtmlTask().execute();
                            }
                        }));
			}
			mWebView.loadDataWithBaseURL(MarketService.URL_MARKET_HOST, result, "text/html", "utf-8", "");
			super.onPostExecute(result);
		}
	}
	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MarketFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(getActivity());;          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MarketFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(getActivity());
	}

}
