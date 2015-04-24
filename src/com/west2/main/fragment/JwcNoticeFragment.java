package com.west2.main.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.activity.JwcNoticeDetatilActivity;
import com.west2.main.adapter.JwcNoticeAdapter;
import com.west2.main.entity.JWCNoticeEntity;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.service.JWCNoticeService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

public class JwcNoticeFragment extends Fragment {

	
	private final int REFRESH_SUCCEED = 1001;
	private final int REFRESH_FAIL = 1002;
	
	private Context context;
	private Activity activity;
	private MessageInterface mListener;

	
	
	private List<JWCNoticeEntity> mList;
	private JwcNoticeAdapter mAdapter;
	private boolean isFirstRefresh = true;
	private boolean toRefresh = false;
	private boolean isRefreshing = false;
	private ListView lv;
	private SwipeRefreshLayout srl;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		activity = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			context.setTheme(R.style.DarkTheme);
		}
		else{
			context.setTheme(R.style.LightTheme);
		}
		
		View view = inflater.inflate(R.layout.fragment_jwc_notice, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		
		lv = (ListView) view.findViewById(R.id.lv);
		srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
		
		mList = new ArrayList<JWCNoticeEntity>();
		mAdapter = new JwcNoticeAdapter(context, mList);
		lv.setAdapter(mAdapter);
		
		// ��ȡ����
		srl.setProgressViewOffset(false, -120, 100);
		srl.setRefreshing(true);
		new Thread(getJwcNoticeRun).start();
		
	}

	private void setListener(View view){
		// ����ˢ��
		srl.setOnRefreshListener(new OnRefreshListener(){public void onRefresh() {
			if(!isRefreshing){
				toRefresh = true;
				new Thread(getJwcNoticeRun).start();
			}
		}});
		view.findViewById(R.id.av).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			mListener.Message(InfoUtils.OPEN_DRAWER);
		}});
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putSerializable("entity", mList.get(position));
				Intent intent = new Intent(context, JwcNoticeDetatilActivity.class);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
		
	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == REFRESH_SUCCEED || msg.what == REFRESH_FAIL){
				isRefreshing = false;
				srl.setRefreshing(false);
//				if(isFirstRefresh){
//					isFirstRefresh = false;
//					srl.setProgressViewOffset(true, -50, 100);
//				}
			}
		
			switch(msg.what){
			case REFRESH_SUCCEED:
				if(toRefresh){
					SnackbarManager.show(Snackbar.with(context).text("ˢ�³ɹ�"));
				}
				mAdapter.update(mList);
				break;
			case REFRESH_FAIL:
				SnackbarManager.show(Snackbar.with(context)
                      	.text("����������")
                        .actionLabel("����")
                        .actionColorResource(R.color.yellow_500)
                        .actionListener(new ActionClickListener() {
                            public void onActionClicked(Snackbar snackbar) {
                            	if(!isRefreshing){
                            		toRefresh = true;
                            		new Thread(getJwcNoticeRun).start();
                            	}
                            }
                        }));
				break;
			}
		}
	};

	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

	/*
	 * ��ȡ����֪ͨ
	 */
	Runnable getJwcNoticeRun = new Runnable(){public void run() {
		try {
			isRefreshing = true;
			Thread.sleep(1500);
			mList = JWCNoticeService.getJwcNotice(context, toRefresh);
			if(mList != null){
				sendMessage(REFRESH_SUCCEED, null, 0);
			}
			else{
				sendMessage(REFRESH_FAIL, null, 0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}};
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("JwcNoticeFragment"); //ͳ��ҳ��(����Activity��Ӧ����SDK�Զ����ã�����Ҫ����д)
	    MobclickAgent.onResume(getActivity());;          //ͳ��ʱ��
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("JwcNoticeFragment"); // ������Activity��Ӧ����SDK�Զ����ã�����Ҫ����д����֤ onPageEnd ��onPause ֮ǰ����,��Ϊ onPause �лᱣ����Ϣ 
	    MobclickAgent.onPause(getActivity());
	}
}
