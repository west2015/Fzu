package fzu.mcginn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.material.widget.ActionView;
import com.material.widget.PagerSlidingTabStrip;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import fzu.mcginn.R;
import fzu.mcginn.adapter.MarkDetailAdapter;
import fzu.mcginn.entity.MarkEntity;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.service.MarkService;
import fzu.mcginn.utils.InfoUtils;

public class MarkFragment extends Fragment{
	private final int REFRESH_SUCCEED = 1001;
	private final int REFRESH_FAIL = 1002;

	private Context context;
	private FragmentManager fm;
	private MessageInterface mListener;

	private boolean toRefresh = false;
	private boolean isRefreshing = false;
	private boolean tabHasSetView = false;
	private List<MarkEntity> mList;

	private ActionView avDrawer;
	private SwipeRefreshLayout srl;
	private PagerSlidingTabStrip tab;
	private ViewPager viewPager;
	
	// GRADE POINT
	private ListView lvGrade;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		fm = this.getActivity().getSupportFragmentManager();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_mark, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		avDrawer = (ActionView) view.findViewById(R.id.av_drawer);
		srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
		tab = (PagerSlidingTabStrip) view.findViewById(R.id.tab_mark_report);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager_mark_report);

		// 获取数据
		new Thread(getMarkRun).start();
	}

	private void setListener(View view){
		srl.setOnRefreshListener(new OnRefreshListener(){public void onRefresh() {
			toRefresh = true;
			new Thread(getMarkRun).start();
		}});
		avDrawer.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isRefreshing && mListener != null){
				mListener.Message(InfoUtils.OPEN_DRAWER);
			}
		}});
		// 显示绩点
		view.findViewById(R.id.btn_credit).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isRefreshing){
				Toast.makeText(context, "显示绩点", Toast.LENGTH_SHORT).show();
			}
		}});
		// 复制成绩
		view.findViewById(R.id.btn_copy).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isRefreshing){
				Toast.makeText(context, "复制成绩", Toast.LENGTH_SHORT).show();
			}
		}});
	}

	/*
	 * 刷新成绩界面
	 */
	private void makeMarkView() {
		List<View> views = new ArrayList<View>();
		List<String> titles = new ArrayList<String>();
		List<MarkEntity> xList;
		// ALL
		for(int i=0;i<mList.size();++i){
			xList = new ArrayList<MarkEntity>();
			int start = i, end = i;
			String term = mList.get(i).getTerm();
			while(end < mList.size() && mList.get(end).getTerm().equals(term)){
				xList.add(mList.get(end++));
			}
			ListView lv = new ListView(context);
			lv.setAdapter(new MarkDetailAdapter(context,xList));
			lv.setDividerHeight(0);
		}
	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case REFRESH_SUCCEED:
				isRefreshing = false;
				srl.setRefreshing(false);
				if(toRefresh){
					SnackbarManager.show(Snackbar.with(context).text("刷新成功"));
				}
				break;
			case REFRESH_FAIL:
				isRefreshing = false;
				srl.setRefreshing(false);
				SnackbarManager.show(Snackbar.with(context)
                              	.text("遇到错误啦")
                                .actionLabel("重试")
                                .actionColorResource(R.color.yellow_500)
                                .actionListener(new ActionClickListener() {
                                    public void onActionClicked(Snackbar snackbar) {
                                    	if(!isRefreshing){
                                    		new Thread(getMarkRun).start();
                                    	}
                                    }
                                }));
				break;
			}
		}
	};

	/*
	 * 获取成绩线程
	 */
	Runnable getMarkRun = new Runnable(){public void run() {
		try {
			isRefreshing = true;
			Thread.sleep(500);
			mList = new MarkService().getMark(toRefresh);
			if(mList != null && mList.size() > 0){
				sendMessage(REFRESH_SUCCEED,null,0);
			}
			else{
				sendMessage(REFRESH_FAIL,null,0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}};
	
	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

}
