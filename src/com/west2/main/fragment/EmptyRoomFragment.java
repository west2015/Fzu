package com.west2.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

public class EmptyRoomFragment extends Fragment{

	private Context context;
	private WebView mWebView;
	private MessageInterface mListener;


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			context.setTheme(R.style.DarkTheme);
		}
		else{
			context.setTheme(R.style.LightTheme);
		}

		
		View view = inflater.inflate(R.layout.fragment_empty, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		mWebView = (WebView) view.findViewById(R.id.webview);
		mWebView.loadUrl("http://3w.west2online.com/lyc/empty.html");
	}

	private void setListener(View view){
		view.findViewById(R.id.av).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.Message(InfoUtils.OPEN_DRAWER);
			}
		});
	}

	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("ExamFragment"); //ͳ��ҳ��(����Activity��Ӧ����SDK�Զ����ã�����Ҫ����д)
	    MobclickAgent.onResume(getActivity());;          //ͳ��ʱ��
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("ExamFragment"); // ������Activity��Ӧ����SDK�Զ����ã�����Ҫ����д����֤ onPageEnd ��onPause ֮ǰ����,��Ϊ onPause �лᱣ����Ϣ 
	    MobclickAgent.onPause(getActivity());
	}

}
