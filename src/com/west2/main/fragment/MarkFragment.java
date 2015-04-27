package com.west2.main.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ClipboardManager;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.material.widget.ActionView;
import com.material.widget.BounceListView;
import com.material.widget.PagerSlidingTabStrip;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.util.Log;
import com.west2.main.R;
import com.west2.main.adapter.GradePointAdapter;
import com.west2.main.adapter.MarkDetailAdapter;
import com.west2.main.adapter.ViewPagerAdapter;
import com.west2.main.entity.MarkEntity;
import com.west2.main.entity.UserEntity;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.service.MarkService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

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
	private List<String> titles;

	private ActionView avDrawer;
	private SwipeRefreshLayout srl;
	private PagerSlidingTabStrip tab;
	private ViewPager viewPager;
	private ImageView imgRefresh;
	
	// GRADE POINT
	private ListView lvGp;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		fm = this.getActivity().getSupportFragmentManager();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			context.setTheme(R.style.DarkTheme);
		}
		else{
			context.setTheme(R.style.LightTheme);
		}
		
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
		imgRefresh = (ImageView) view.findViewById(R.id.img_refresh);
		
		// 获取数据
		Animation animationR = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
		animationR.setInterpolator(new DecelerateInterpolator());
		imgRefresh.clearAnimation();
		imgRefresh.startAnimation(animationR);
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
				showGpDialog();
			}
		}});
		// 复制成绩
		view.findViewById(R.id.btn_copy).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isRefreshing){
				StringBuilder str = new StringBuilder();
				UserEntity user = BaseUtils.getInstance().getUserEntity();
				str.append(user.getRealname()+" "+user.getUsername()+"\n");
				int position = viewPager.getCurrentItem();
				String term = titles.get(position);
				double totalCredit = 0, gradePoint = 0;
				for(int i=0;i<mList.size();++i){
					MarkEntity e = mList.get(i);
					if(e.getTerm().equals(term)){
						str.append(e.getCourseName() + " " + e.getScore() + "\n");
						double credit = InfoUtils.getDouble(e.getGradeCredit());
						// 排除校选课
						String type = e.getCourseType();
//						if(!type.equals("自然科学类") && !type.equals("人文科学类") && !type.equals("经济管理类")
//						&& !type.equals("公共艺术类") && !type.equals("工程技术类")){
						if(!type.contains("类")){
							totalCredit += credit;
						}
					}
				}
				if(totalCredit != 0){
					for(int i=0;i<mList.size();++i){
						MarkEntity e = mList.get(i);
						if(e.getTerm().equals(term)){
							// 排除校选课
							String type = e.getCourseType();
//							if(!type.equals("自然科学类") && !type.equals("人文科学类") && !type.equals("经济管理类")
//							&& !type.equals("公共艺术类") && !type.equals("工程技术类")){
							if(!type.contains("类")){
								double credit = InfoUtils.getDouble(e.getGradeCredit());
								double grade = InfoUtils.getDouble(e.getGradePoint());
								gradePoint += (grade*credit)/totalCredit;
							}
						}
					}
					gradePoint = Math.round(gradePoint * 1000000.0) / 1000000.0;
					str.append("学期绩点 " + gradePoint);
				}
				ClipboardManager c = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
				c.setText(str.toString());
				SnackbarManager.show(Snackbar.with(context).text(titles.get(position) + "成绩已复制到剪贴板"));

			}
		}});
		// 刷新
		view.findViewById(R.id.btn_refresh).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isRefreshing){
				Animation animationR = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
				animationR.setInterpolator(new DecelerateInterpolator());
				imgRefresh.clearAnimation();
				imgRefresh.startAnimation(animationR);
				toRefresh = true;
				new Thread(getMarkRun).start();
			}
		}});
	}

	/*
	 * 显示绩点
	 */
	private void showGpDialog(){
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_gradepoint,null));
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_gradepoint);
		// findView
		lvGp = (ListView) w.findViewById(R.id.lv_gradepoint);
		// initValue
		lvGp.setAdapter(new GradePointAdapter(context,mList));
		// setListener
		w.findViewById(R.id.btn_close).setOnClickListener(new OnClickListener(){public void onClick(View arg0) {
			dialog.dismiss();
		}});
	}
	
	/*
	 * 刷新成绩界面
	 */
	private void makeMarkView() {
		List<View> views = new ArrayList<View>();
		titles = new ArrayList<String>();
		List<MarkEntity> xList;
		// ALL
		for(int i=0;i<mList.size();++i){
			xList = new ArrayList<MarkEntity>();
			int start = i, end = i;
			String term = mList.get(i).getTerm();
			while(end < mList.size() && mList.get(end).getTerm().equals(term)){
				xList.add(mList.get(end++));
			}
			i = end - 1;
			BounceListView lv = new BounceListView(context);
			lv.setAdapter(new MarkDetailAdapter(context,xList));
			lv.setDividerHeight(0);
			lv.setVerticalScrollBarEnabled(false);
			lv.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
			titles.add(term);
			views.add(lv);
		}
		viewPager.setAdapter(new ViewPagerAdapter(views,titles));
		if(!tabHasSetView){
			tabHasSetView = true;
			tab.setViewPager(viewPager);
		}
	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case REFRESH_SUCCEED:
				isRefreshing = false;
				imgRefresh.clearAnimation();
				if(toRefresh){
					SnackbarManager.show(Snackbar.with(context).text("刷新成功"));
				}
				makeMarkView();
				break;
			case REFRESH_FAIL:
				isRefreshing = false;
				imgRefresh.clearAnimation();
				SnackbarManager.show(Snackbar.with(context)
                              	.text("遇到错误啦")
                                .actionLabel("重试")
                                .actionColorResource(R.color.yellow_500)
                                .actionListener(new ActionClickListener() {
                                    public void onActionClicked(Snackbar snackbar) {
                                    	if(!isRefreshing){
                                    		toRefresh = true;
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
			if(toRefresh){
				Thread.sleep(700);
			}
			else{
				Thread.sleep(200);
			}
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

	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MarkFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(getActivity());;          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MarkFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(getActivity());
	}

}
