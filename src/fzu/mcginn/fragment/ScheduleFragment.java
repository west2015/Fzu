package fzu.mcginn.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.material.widget.ActionView;
import com.material.widget.CheckBox;
import com.material.widget.FloatingActionButton;
import com.material.widget.PagerSlidingTabStrip;
import com.material.widget.RaisedButton;
import com.material.widget.RevealColorView;
import com.material.widget.action.Action;
import com.material.widget.action.BackAction;
import com.material.widget.action.DrawerAction;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.ActionSwipeListener;

import fzu.mcginn.R;
import fzu.mcginn.adapter.ScheduleAdapter;
import fzu.mcginn.adapter.ScheduleListAdapter;
import fzu.mcginn.adapter.SimpleAdapter;
import fzu.mcginn.adapter.ViewPagerAdapter;
import fzu.mcginn.entity.CourseEntity;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.service.ScheduleService;
import fzu.mcginn.service.TimeService;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.InfoUtils;
import fzu.mcginn.utils.MetricsConverter;
import fzu.mcginn.utils.ViewUtils;

public class ScheduleFragment extends Fragment implements SimpleAdapter.onItemClick{

	private final int OPEN_WEEK_PICKER = 1000;
	private final int CLOSE_WEEK_PICKER = 1001;
	private final int MOVE_FAB = 1002;
	private final int SHOW_FAB = 1003;
	private final int SHOW_RCV = 1004;
	private final int HIDE_RCV = 1005;
	private final int SHOW_ACB = 1006;
	private final int HIDE_ACB = 1007;
	private final int SET_AV = 1008;
	private final int CHANGE = 1009;
	private final int OPEN_MENU = 1010;
	private final int CLOSE_MENU = 1011;
	private final int SET_WEEK = 1012;
	private final int SET_WEEKDAY = 1013;
	
	private final int[] llid = {
		R.id.ll_c1,R.id.ll_c2,R.id.ll_c3,R.id.ll_c4,R.id.ll_c5,R.id.ll_c6,R.id.ll_c7
	};
	
	private final int[] weekdayId = {
		R.id.tv_monday,R.id.tv_tuesday,R.id.tv_wednesday,R.id.tv_thursday,R.id.tv_friday,R.id.tv_saturday,R.id.tv_sunday
	};

	private final int[] bg = {
		R.drawable.course_bg1 ,R.drawable.course_bg2 ,R.drawable.course_bg3 ,R.drawable.course_bg4 ,
		R.drawable.course_bg5 ,R.drawable.course_bg6 ,R.drawable.course_bg7 ,R.drawable.course_bg8 ,
		R.drawable.course_bg9 ,R.drawable.course_bg10,R.drawable.course_bg11,R.drawable.course_bg12,
		R.drawable.course_bg13,R.drawable.course_bg14,R.drawable.course_bg15,R.drawable.course_bg16,
	};
	
	private final String[] arrWeek = {
			"第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周","第10周",
			"第11周","第12周","第13周","第14周","第15周","第16周","第17周","第18周","第19周","第20周",
			"第21周","第22周","第23周","第24周","第25周"};

	private final String[] TAB_TITLE = {"   周一   ","   周二   ","   周三   ","   周四   ","   周五   ","   周六   ","   周日   "};
	
	private Activity activity;
	private Context context;
	private FragmentManager fm;
	private MessageInterface mListener;

	private Point p;
	private boolean isWeekView;
	private boolean isChanging;

	private ActionView av;
	private View viewCenter;

	private Button btnHide;
	// MENU
	private boolean isMenuView;
	private Button btnAdd;
	private Button btnDel;
	private Button btnRefresh;
	private Button btnSetting;
	private LinearLayout llMenu;

	// MENU_REFRESH
	private boolean toRefresh;			// 是否刷新
	private boolean isRefreshing;		// 刷新状态
	private RelativeLayout rlRefresh;

	private RelativeLayout rlHide;
	// MENU_ADD
	private boolean isAddWeekDay;
	private boolean isAddLesson;
	private ListView lvWeekDayPicker;
	private ListView lvLessonPicker;

	// MENU_SETTING
	private String disWay;
	private boolean isSetTerm;
	private boolean isSetWeek;
	private RaisedButton btnSetTerm;
	private RaisedButton btnSetWeek;
	private ListView lvTermPicker;
	private ListView lvWeekPicker;
	private CheckBox cbAll;
	private CheckBox cbWeek;
	private EditText etSchoolYear;

	
	// WEEK PICKER
	private boolean isWeekPicker;
	private ListView lvWeek;
	private RaisedButton btnWeek;
	
	// WEEK SCHEDULE
	private TextView[] tvWeekday = new TextView[7];
	private String scheduleJson;
	private RevealColorView rcv;
	private FloatingActionButton fab;
	private RelativeLayout rlSchedule14;
	private RelativeLayout rlSchedule58;
	private RelativeLayout rlSchedule9;
	private RelativeLayout[] llColumn;
	private HashMap<String,Integer> map;
	private int colorIndex;

	// DAY SCHEDULE
	private boolean tabHasSetView;
	private PagerSlidingTabStrip tab;
	private ViewPager viewPager;
	private RelativeLayout rl_tab;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		activity = this.getActivity();
		context = this.getActivity();
		mListener = (MessageInterface) context;
		fm = this.getActivity().getSupportFragmentManager();
		
		isWeekView = true;
		isChanging = false;
		isRefreshing = toRefresh = false;
		isWeekPicker = isMenuView = false;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_schedule, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		av = (ActionView) view.findViewById(R.id.av);
		btnWeek = (RaisedButton) view.findViewById(R.id.btn_week);
		lvWeek  = (ListView) view.findViewById(R.id.lv_week);
		btnHide = (Button) view.findViewById(R.id.btn_hide_week_picker);
		fab = (FloatingActionButton) view.findViewById(R.id.fab_schedule);
		viewCenter = view.findViewById(R.id.view_center);
		rcv = (RevealColorView) view.findViewById(R.id.rcv_schedule);
		rl_tab = (RelativeLayout) view.findViewById(R.id.rl_tab);
		tab = (PagerSlidingTabStrip) view.findViewById(R.id.tab_schedule);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager_schedule);
		llMenu = (LinearLayout) view.findViewById(R.id.ll_menu);
		btnAdd = (Button) view.findViewById(R.id.btn_add);
		btnDel = (Button) view.findViewById(R.id.btn_del);
		btnRefresh = (Button) view.findViewById(R.id.btn_refresh);
		btnSetting = (Button) view.findViewById(R.id.btn_setting);
		rlRefresh = (RelativeLayout) view.findViewById(R.id.rl_refresh);
		rlSchedule14 = (RelativeLayout) view.findViewById(R.id.rl_schedule_1_4);
		rlSchedule58 = (RelativeLayout) view.findViewById(R.id.rl_schedule_5_8);
		rlSchedule9 = (RelativeLayout) view.findViewById(R.id.rl_schedule_9);
		for(int i=0;i<weekdayId.length;++i){
			tvWeekday[i] = (TextView) view.findViewById(weekdayId[i]);
		}

		// SCHEDULE WEEK
		sendMessage(SET_WEEKDAY);
		lvWeek.setAdapter(new SimpleAdapter(context,arrWeek,this));
		// SCHEDULE DAY
		tabHasSetView = false;
		tab.setBackgroundColor(context.getResources().getColor(R.color.blue_500));
		// NET WORK
		rlRefresh.setVisibility(View.VISIBLE);
		new Thread(getScheduleRun).start();
		new Thread(getTimeRun).start();
	}
	/*
	 * 获取时间、日期、周数
	 */
	Runnable getTimeRun = new Runnable(){
		@Override
		public void run() {
			DateEntity dateEntity = new TimeService().getTime(false);
			if(dateEntity != null && dateEntity.getCurrentWeek() != null &&
			1<=dateEntity.getCurrentWeek() && dateEntity.getCurrentWeek()<=25){
				sendMessageDelay(SET_WEEK,arrWeek[dateEntity.getCurrentWeek() - 1],0L);
			}
		}
	};
	/*
	 * 获取课表
	 */
	Runnable getScheduleRun = new Runnable(){
		public void run() {
			// 延迟防止界面卡顿
			try {
				isRefreshing = true;
				Thread.sleep(500);
				String res = new ScheduleService().getSchedule(toRefresh);
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				if(res != null && res.length() > 10){
					scheduleJson = res;
					msg.obj = InfoUtils.SR_SCHEDULE_SUCCEED;
				}
				else{
					msg.obj = InfoUtils.SR_SCHEDULE_FAILED;
				}
				mHandler.sendMessageDelayed(msg, 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	/*
	 * 处理课表界面
	 */
	private void display(){
		if(scheduleJson == null || scheduleJson.length() < 10){
			return ;
		}
		String disWay = new ScheduleService().getDisWay(context);
		int mWeek = InfoUtils.getNumber(btnWeek.getText().toString());
		List<CourseEntity> mList = new ScheduleService().parseWeek(scheduleJson, mWeek);
		// 显示全部课程
		if(disWay.equals(InfoUtils.SR_SCHEDULE_DIS_ALL)){
			boolean[][] hasClass = new boolean[13][8];
			for(int i=0;i<13;++i)
			for(int j=0;j<8 ;++j) hasClass[i][j] = false;
			for(int i=0;i<mList.size();++i){
				CourseEntity entity = mList.get(i);
				int p = entity.getLesson() - 1;
				for(int j=0;j<entity.getLength();++j,++p){
					hasClass[p][entity.getWeekday() - 1] = true;
				}
			}
			
			List<CourseEntity> xList = new ScheduleService().parseAll(scheduleJson);
			for(int i=0;i<xList.size();++i)
			if(!(xList.get(i).getStartWeek()<=mWeek && mWeek<=xList.get(i).getEndWeek())){
				CourseEntity entity = xList.get(i);
				boolean hasConflict = false;
				int p = entity.getLesson() - 1;
				for(int j=0;j<entity.getLength();++j,++p)
				if(hasClass[p][entity.getWeekday() - 1]){
					hasConflict = true;
					break;
				}
				if(!hasConflict){
					mList.add(entity);
					p = entity.getLesson() - 1;
					for(int j=0;j<entity.getLength();++j,++p){
						hasClass[p][entity.getWeekday() - 1] = true;
					}
				}
			}

		}
		colorIndex = 0;
		map = new HashMap<String,Integer>();
		// 1 - 4
		rlSchedule14.removeAllViews();
		rlSchedule14.addView(getScheduleView(mList,mWeek,1,4));
		// 5 - 8
		rlSchedule58.removeAllViews();
		rlSchedule58.addView(getScheduleView(mList,mWeek,5,8));
		// 9 - 12
		rlSchedule9 .removeAllViews();
		rlSchedule9 .addView(getScheduleView(mList,mWeek,9,12));

	}

	private View getScheduleView(List<CourseEntity> mList,int curWeek,int startLesson,int endLesson){
		int height = (int)MetricsConverter.dpToPx(context, 48);
		CourseEntity entity;
		LayoutInflater flater = LayoutInflater.from(context);
		View view = flater.inflate(R.layout.layout_line_schedule, null);
		llColumn = new RelativeLayout[7];
		for(int i=0;i<7;++i){
			llColumn[i] = (RelativeLayout) view.findViewById(llid[i]);
		}
		for(int i=0;i<mList.size();++i){
			entity = mList.get(i);
			if(!(entity.getLesson()>endLesson || entity.getLesson()+entity.getLength()-1<startLesson)){
				int start = entity.getLesson() >= startLesson ? entity.getLesson() : startLesson;
				int end = entity.getLesson()+entity.getLength()-1 <= endLesson ? entity.getLesson()+entity.getLength()-1 : endLesson;
				int length = end - start + 1;
				int margin = start - startLesson;
				if(1<=entity.getWeekday() && entity.getWeekday()<=7)
				if(entity.getStartWeek()<=curWeek && curWeek<=entity.getEndWeek())
					llColumn[entity.getWeekday() - 1].addView(newTextView(entity,height,length,margin,true));
				else
					llColumn[entity.getWeekday() - 1].addView(newTextView(entity,height,length,margin,false));
			}
		}
		for(int i=0;i<7;++i)
		if(llColumn[i].getChildCount() == 0){
			llColumn[i].addView(newTextView(null,height,1,0,false));
		}
		return view;
	}
	
	private TextView newTextView(CourseEntity entity,int height,int length,int margin,boolean inWeek){
		TextView text = new TextView(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.height = height * length + length - 2;
		params.setMargins(1, 1 + margin + margin*height, 1, 1);
		text.setLayoutParams(params);
		text.setBackgroundColor(getResources().getColor(R.color.green_500));
		text.setGravity(Gravity.CENTER);
		if(entity != null){
			String mName = entity.getName().length() <= 4*length ? entity.getName() : entity.getName().substring(0, 4*length) + "...";
			String mPlace = entity.getPlace();
			if(inWeek){
				int mNameColor = getResources().getColor(R.color.white_text);
				int mPlaceColor = getResources().getColor(R.color.white);
				SpannableString str = new SpannableString(mName + "\n" + mPlace);
				str.setSpan(new ForegroundColorSpan(mNameColor),0, mName.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				str.setSpan(new ForegroundColorSpan(mPlaceColor),mName.length(), mName.length()+mPlace.length()+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				text.setText(str);
				text.setTextSize(13);
				if(!map.containsKey(entity.getName())){
					map.put(entity.getName(), colorIndex++);
				}
				text.setBackgroundResource(bg[map.get(entity.getName())]);
			}
			else{
				int mNameColor = getResources().getColor(R.color.black_dividers);
				int mPlaceColor = getResources().getColor(R.color.black_dividers);
				SpannableString str = new SpannableString(mName + "\n" + mPlace);
				str.setSpan(new ForegroundColorSpan(mNameColor),0, mName.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				str.setSpan(new ForegroundColorSpan(mPlaceColor),mName.length(), mName.length()+mPlace.length()+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				text.setText(str);
				text.setTextSize(13);
				text.setBackgroundResource(R.drawable.course_bg0);
			}
			final String temp = text.getText().toString();
		}
		else{
			text.setText("                ");
			text.setBackgroundColor(Color.TRANSPARENT);
		}
		return text;
	}

	private void setListener(View view){
		// 切换单日视图
		fab.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(isChanging) return ;
			changePage();
		}});
		// 隐藏Picker
		btnHide.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(isWeekPicker)
				sendMessage(CLOSE_WEEK_PICKER);
			if(isMenuView)
				sendMessage(CLOSE_MENU);
		}});
		// 打开菜单、返回周视图
		av.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(isChanging) return ;
			if(isWeekView){
				mListener.Message(InfoUtils.OPEN_DRAWER);
			} else{
				changePage();
			}
		}});
		// week picker 
		btnWeek.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isWeekView || isRefreshing) return ;
			if(isMenuView){
				sendMessage(CLOSE_MENU);
				return ;
			}
			sendMessageDelay(OPEN_WEEK_PICKER,null,200L);
		}});
		// menu picker
		view.findViewById(R.id.av_menu).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isWeekView || isRefreshing) return ;
			if(isWeekPicker){
				sendMessage(CLOSE_WEEK_PICKER);
				return ;
			}
			sendMessage(OPEN_MENU);
		}});
		btnAdd.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isWeekView || !isMenuView) return ;
			showAdd();
			sendMessage(CLOSE_MENU);
		}});
		btnDel.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isWeekView || !isMenuView) return ;
			showDel();
			sendMessage(CLOSE_MENU);
		}});
		btnRefresh.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isWeekView || !isMenuView) return ;
			toRefresh = true;
			rlRefresh.setVisibility(View.VISIBLE);
			sendMessage(CLOSE_MENU);
			new Thread(getScheduleRun).start();
		}});
		btnSetting.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(!isWeekView || !isMenuView) return ;
			showSetting();
			sendMessage(CLOSE_MENU);
		}});			
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg == null) return ;
			if(msg.what == 1){
				isRefreshing = false;
				rlRefresh.setVisibility(View.GONE);
				if(msg.obj.toString().equals(InfoUtils.SR_SCHEDULE_SUCCEED)){
					if(toRefresh){
						SnackbarManager.show(Snackbar.with(context).text("获取成功"));
					}
					display();
				}
				else
				if(msg.obj.toString().equals(InfoUtils.SR_SCHEDULE_FAILED)){
					SnackbarManager.show(
	                        Snackbar.with(context)
	                              	.text("遇到错误啦")
	                                .actionLabel("重试")
	                                .actionColorResource(R.color.yellow_500)
	                                .actionListener(new ActionClickListener() {
	                                    @Override
	                                    public void onActionClicked(Snackbar snackbar) {
	                                    	if(!isRefreshing){
	                                    		rlRefresh.setVisibility(View.VISIBLE);
	                                    		new Thread(getScheduleRun).start();
	                                    	}
	                                    }
	                                }));
				}
			}
			else
			switch(msg.what){
			case OPEN_WEEK_PICKER: openWeekPicker();
				break;
			case CLOSE_WEEK_PICKER: closeWeekPicker();
				break;
			case MOVE_FAB:
				if(msg.obj != null) moveFab((long) msg.obj);
				else moveFab(300);
				break;
			case SHOW_FAB:
				if(msg.obj != null) showFab((long) msg.obj);
				else showFab(300);
				break;
			case SHOW_RCV:
				if(msg.obj != null) showRcv((int) msg.obj);
				else showRcv(context.getResources().getColor(R.color.blue_400));
				break;
			case HIDE_RCV:
				if(msg.obj != null) hideRcv((int) msg.obj);
				else hideRcv(context.getResources().getColor(R.color.blue_400));
				break;
			case SHOW_ACB: showAcb();
				break;
			case HIDE_ACB: hideAcb();
				break;
			case SET_AV: if(msg.obj != null) av.setAction((Action) msg.obj);
				break;
			case OPEN_MENU: openMenu();
				break;
			case CLOSE_MENU: closeMenu();
				break;
			case CHANGE: isChanging = false;
				isWeekView = !isWeekView;
				break;
			case SET_WEEK:
				if(msg.obj != null){
					btnWeek.setText((String) msg.obj);
					display();
					closeWeekPicker();
				}
				break;
			case SET_WEEKDAY:
				int cntWeekday = new TimeService().getWeekDay();
				for(int i=0;i<weekdayId.length;++i)
				if(cntWeekday == i){
					tvWeekday[i].setTextColor(getResources().getColor(R.color.black_text));
				}
				else{
					tvWeekday[i].setTextColor(getResources().getColor(R.color.black_disabled));
				}
				break;
			}
		}
	};

	private void showAdd(){
		isAddWeekDay = isAddLesson = false;
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_schedule_add,null));
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_schedule_add);
		// findView
		lvWeekDayPicker = (ListView) w.findViewById(R.id.lv_weekday_picker);
		lvLessonPicker = (ListView) w.findViewById(R.id.lv_lesson_picker);
		// setListener
		w.findViewById(R.id.btn_weekday).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				if(isAddLesson)
					return ;
				isAddWeekDay = true;
				lvWeekDayPicker.setVisibility(View.VISIBLE);
				ScaleAnimation animationS = new ScaleAnimation(
						1.0f, 1.0f, 0.2f, 1f, 
						Animation.RELATIVE_TO_SELF, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.1f);
				animationS.setDuration(300);
				animationS.setInterpolator(new DecelerateInterpolator());
				lvWeekDayPicker.startAnimation(animationS);
			}
		});
		w.findViewById(R.id.btn_lesson).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				if(isAddWeekDay)
					return ;
				isAddLesson = true;
				lvLessonPicker.setVisibility(View.VISIBLE);
				ScaleAnimation animationS = new ScaleAnimation(
						1.0f, 1.0f, 0.2f, 1f, 
						Animation.RELATIVE_TO_SELF, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.1f);
				animationS.setDuration(300);
				animationS.setInterpolator(new DecelerateInterpolator());
				lvLessonPicker.startAnimation(animationS);
			}
		});
		w.findViewById(R.id.rl_hide).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				ScaleAnimation animationS = new ScaleAnimation(
						1.0f, 1.0f, 1.0f, 0f, 
						Animation.RELATIVE_TO_SELF, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.1f);
				animationS.setDuration(150);
				animationS.setInterpolator(new AccelerateInterpolator());
				if(isAddWeekDay)
					lvWeekDayPicker.startAnimation(animationS);
				if(isAddLesson)
					lvLessonPicker.startAnimation(animationS);
				animationS.setAnimationListener(new AnimationListener(){
					public void onAnimationStart(Animation animation) {}
					public void onAnimationRepeat(Animation animation) {}
					public void onAnimationEnd(Animation animation){
						lvWeekDayPicker.setVisibility(View.GONE);
						lvLessonPicker.setVisibility(View.GONE);
						isAddWeekDay = isAddLesson = false;
					}
				});
				// hide input method
				View decorView = dialog.getWindow().getDecorView();
				Context mContext = dialog.getWindow().getContext();
				if(decorView != null){
					InputMethodManager mInputMethodManager = (InputMethodManager) 
							mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
				}
			}
		});
		w.findViewById(R.id.btn_add).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			Toast.makeText(context, "ADD", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}});
		w.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			dialog.dismiss();
		}});
	}

	private void showDel(){
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_schedule_del);
		// findView
		// setListener
		w.findViewById(R.id.btn_del).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			Toast.makeText(context, "DEL", 1).show();
			dialog.dismiss();
		}});
		w.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			dialog.dismiss();
		}});
	}
	
	private void showSetting(){
		isSetTerm = isSetWeek = false;
		disWay = new ScheduleService().getDisWay(context);
		DateEntity date = BaseUtils.getInstance().getDateEntity();
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_schedule_setting,null));
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_schedule_setting);
		// findView
		rlHide = (RelativeLayout) w.findViewById(R.id.rl_hide);
		btnSetTerm = (RaisedButton) w.findViewById(R.id.btn_term);
		btnSetWeek = (RaisedButton) w.findViewById(R.id.btn_week);
		lvTermPicker = (ListView) w.findViewById(R.id.lv_term_picker);
		lvWeekPicker = (ListView) w.findViewById(R.id.lv_week_picker);
		etSchoolYear = (EditText) w.findViewById(R.id.et_school_year);
		cbAll = (CheckBox) w.findViewById(R.id.cb_all);
		cbWeek = (CheckBox) w.findViewById(R.id.cb_week);
		if(disWay.equals(InfoUtils.SR_SCHEDULE_DIS_ALL)){
			cbAll.setChecked(true);
			cbWeek.setChecked(false);
		}
		else{
			cbAll.setChecked(false);
			cbWeek.setChecked(true);
		}
		if(date!=null){
			 if(date.getSchoolYear()!=null) etSchoolYear.setText(date.getSchoolYear().toString());
			 if(date.getTerm()!=null) btnSetTerm.setText("0" + date.getTerm() + "学期");
			 if(date.getCurrentWeek()!=null) btnSetWeek.setText("第"+date.getCurrentWeek()+"周");
		}
		lvTermPicker.setAdapter(new ArrayAdapter<String>(context, R.layout.item_text, new String[]{"01学期","02学期"}));
		lvWeekPicker.setAdapter(new ArrayAdapter<String>(context, R.layout.item_text, arrWeek));
		// setListener
		cbAll.setOnClickListener(new OnClickListener(){public void onClick(View arg0) {
			if(disWay.equals(InfoUtils.SR_SCHEDULE_DIS_ALL)){
				cbAll.setChecked(true);
				return ;
			}
			disWay = InfoUtils.SR_SCHEDULE_DIS_ALL;
			cbAll.setChecked(true);
			cbWeek.setChecked(false);
		}});
		cbWeek.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(disWay.equals(InfoUtils.SR_SCHEDULE_DIS_WEEK)){
				cbWeek.setChecked(true);
				return ;
			}
			disWay = InfoUtils.SR_SCHEDULE_DIS_WEEK;
			cbAll.setChecked(false);
			cbWeek.setChecked(true);
		}});
		lvTermPicker.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(position == 0)
					btnSetTerm.setText("01学期");
				else if(position == 1)
					btnSetTerm.setText("02学期");
				hideSettingPicker(0);
			}
		});
		lvWeekPicker.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				btnSetWeek.setText(arrWeek[position]);
				hideSettingPicker(1);
			}
		});
		btnSetTerm.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(isSetWeek) return;
			showSettingPicker(0);
		}});
		btnSetWeek.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(isSetTerm) return ;
			showSettingPicker(1);
		}});
		rlHide.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean hasFocus) {
			rlHide.performClick();
		}});
		rlHide.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			hideSettingPicker(0);
			hideSettingPicker(1);
			// hide input method
			View decorView = dialog.getWindow().getDecorView();
			Context mContext = dialog.getWindow().getContext();
			if (decorView != null) {
				InputMethodManager mInputMethodManager = (InputMethodManager) mContext
						.getSystemService(mContext.INPUT_METHOD_SERVICE);
				mInputMethodManager.hideSoftInputFromWindow(
						decorView.getWindowToken(), 0);
			}
		}});
		// 保存
		w.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			int schYear = InfoUtils.getNumber(etSchoolYear.getText().toString());
			int term = InfoUtils.getNumber(btnSetTerm.getText().toString());
			int week = InfoUtils.getNumber(btnSetWeek.getText().toString());
			applySetting(schYear,term,week);
			dialog.dismiss();
		}});
		// 取消
		w.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			dialog.dismiss();
		}});
	}
	
	private void applySetting(int schoolYear,int term,int week){
		DateEntity date = BaseUtils.getInstance().getDateEntity();
		toRefresh = false;
		if(date == null || date.getSchoolYear() == null || schoolYear != date.getSchoolYear())
			toRefresh = true;
		if(date == null || date.getTerm() == null || term != date.getTerm())
			toRefresh = true;
		if(!disWay.equals(new ScheduleService().getDisWay(context))){
			new ScheduleService().setDisWay(context, disWay);
		}
		date.setSchoolYear(schoolYear);
		date.setTerm(term);
		date.setCurrentWeek(week);
		BaseUtils.getInstance().setDateEntity(date);
		mListener.Message(InfoUtils.REFRESH_TIME);
		btnWeek.setText("第"+week+"周");
		rlRefresh.setVisibility(View.VISIBLE);
		new Thread(getScheduleRun).start();
	}

	private void showSettingPicker(final int which){
		ScaleAnimation animationS = new ScaleAnimation(
				1.0f, 1.0f, 0.2f, 1f, 
				Animation.RELATIVE_TO_SELF, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.1f);
		animationS.setDuration(300);
		animationS.setInterpolator(new DecelerateInterpolator());
		if(which == 0){
			if(lvTermPicker.isShown()) return ;
			isSetTerm = true;
			lvTermPicker.setVisibility(View.VISIBLE);
			lvTermPicker.startAnimation(animationS);
		}
		else if(which == 1){
			if(lvWeekPicker.isShown()) return ;
			isSetWeek = true;
			lvWeekPicker.setVisibility(View.VISIBLE);
			lvWeekPicker.startAnimation(animationS);
		}
	}
	
	private void hideSettingPicker(final int which){
		ScaleAnimation animationS = new ScaleAnimation(
				1.0f, 1.0f, 1.0f, 0f, 
				Animation.RELATIVE_TO_SELF, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.1f);
		animationS.setDuration(150);
		animationS.setInterpolator(new AccelerateInterpolator());
		if(which == 0){
			if(!lvTermPicker.isShown()) return ;
			lvTermPicker.startAnimation(animationS);
		}
		else if(which == 1){
			if(!lvWeekPicker.isShown()) return ;
			lvWeekPicker.startAnimation(animationS);
		}
		animationS.setAnimationListener(new AnimationListener(){
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				if(which == 0){
					lvTermPicker.setVisibility(View.GONE);
					isSetTerm = false;
				}
				else
				if(which == 1){
					lvWeekPicker.setVisibility(View.GONE);
					isSetWeek = false;
				}
			}
		});
	}

	/*
	 * 刷新单日视图
	 */
	private void refreshViewPager(){
		List<View> views = new ArrayList<View>();
		List<CourseEntity> mList = new ScheduleService().parseWeek(scheduleJson, InfoUtils.getNumber(btnWeek.getText().toString()));
		for(int i=1;i<=7;++i){
			ListView listView = new ListView(context);
			List<CourseEntity> xList = new ArrayList<CourseEntity>();
			for(int j=0;j<mList.size();++j)
			if(mList.get(j).getWeekday() == i){
				xList.add(mList.get(j));
			}
			listView.setAdapter(new ScheduleListAdapter(context,xList));
			listView.setDivider(context.getResources().getDrawable(R.color.black_dividers));
			listView.setDividerHeight(1);
			listView.setVerticalScrollBarEnabled(false);
			views.add(listView);
		}
		viewPager.setAdapter(new ViewPagerAdapter(views,TAB_TITLE));
		int position = new TimeService().getWeekDay();
		if(!(0<=position && position<7)){
			position = 0;
		}
		viewPager.setCurrentItem(position);
		if(!tabHasSetView){
			tabHasSetView = true;
			tab.setViewPager(viewPager);
		}
	}

	private void changePage(){
		isChanging = true;
		if(isWeekView){
			// 设置单日视图
			refreshViewPager();
			sendMessage(MOVE_FAB,300L);
			sendMessageDelay(SHOW_RCV,context.getResources().getColor(R.color.blue_400),200);
			sendMessageDelay(SHOW_ACB,null,550L);
			sendMessageDelay(SET_AV,new BackAction(),700L);
		}
		else {
			sendMessageDelay(SET_AV,new DrawerAction(),0L);
			sendMessageDelay(HIDE_ACB,null,200L);
			sendMessageDelay(HIDE_RCV,Color.TRANSPARENT,500L);
			sendMessageDelay(SHOW_FAB,300L,500L);
		}
		sendMessageDelay(CHANGE,null,700L);
	}
	
	private void moveFab(long duration){
		p = ViewUtils.getRelativePoint(fab,viewCenter);
		TranslateAnimation animationX = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, p.x/(1.0f*fab.getWidth()) + 0.5f * p.x / Math.abs(p.x),
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f);
		TranslateAnimation animationY = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, p.y/(1.0f*fab.getHeight()) + 0.5f * p.y / Math.abs(p.y));
		animationX.setDuration(duration);
		animationX.setInterpolator(new DecelerateInterpolator());
		animationY.setDuration(duration);
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(animationX);
		set.addAnimation(animationY);
		fab.startAnimation(set);
	}
	
	private void showFab(long duration){
		fab.show();
		fab.setVisibility(View.VISIBLE);
	}
	
	private void showRcv(int color){
		fab.setVisibility(View.INVISIBLE);
		p = ViewUtils.getRelativePoint(rcv,viewCenter);
		rcv.reveal(p.x, p.y, color, null);
	}
	
	private void hideRcv(int color){
		p = ViewUtils.getRelativePoint(rcv,fab);
		rcv.hide(p.x, p.y, color, null);
	}

	private void showAcb(){
		viewPager.setVisibility(View.VISIBLE);
		AlphaAnimation animationA = new AlphaAnimation(0f, 1f);
		animationA.setDuration(1000);
		animationA.setInterpolator(new DecelerateInterpolator());
		viewPager.startAnimation(animationA);
		TranslateAnimation animationY = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, -1f,
				Animation.RELATIVE_TO_SELF, 0f);
		animationY.setDuration(300);
		animationY.setInterpolator(new DecelerateInterpolator());
		rl_tab.startAnimation(animationY);
		rl_tab.setVisibility(View.VISIBLE);
		tab.setVisibility(View.VISIBLE);
	}
	
	private void hideAcb(){
		TranslateAnimation animationY = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, -1f);
		animationY.setDuration(300);
		animationY.setInterpolator(new AccelerateInterpolator());
		rl_tab.startAnimation(animationY);
		animationY.setAnimationListener(new AnimationListener(){
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				rl_tab.setVisibility(View.GONE);
				tab.setVisibility(View.GONE);
			}
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		AlphaAnimation animationA = new AlphaAnimation(1f, 0f);
		animationA.setDuration(300);
		animationA.setInterpolator(new AccelerateInterpolator());
		viewPager.startAnimation(animationA);
		animationA.setAnimationListener(new AnimationListener(){
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				viewPager.setVisibility(View.GONE);
			}
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}
	
	private void openWeekPicker(){
		isWeekPicker = true;

		lvWeek.setVisibility(View.VISIBLE);
		btnHide.setVisibility(View.VISIBLE);
		ScaleAnimation animationS = new ScaleAnimation(
				1.0f, 1.0f, 0f, 1f, 
				Animation.RELATIVE_TO_SELF, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.2f);
		animationS.setDuration(180);
		animationS.setInterpolator(new DecelerateInterpolator());
		lvWeek.startAnimation(animationS);
		
		int position = InfoUtils.getNumber(btnWeek.getText().toString());
		if(1<=position && position<=25){
			lvWeek.setSelection(position - 1);
		}
	}
	
	private void closeWeekPicker(){
		isWeekPicker = false;
		
		ScaleAnimation animationS = new ScaleAnimation(
				1.0f, 1.0f, 1f, 0f, 
				Animation.RELATIVE_TO_SELF, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.1f);
		animationS.setDuration(150);
		animationS.setInterpolator(new DecelerateInterpolator());
		lvWeek.startAnimation(animationS);

		animationS.setAnimationListener(new AnimationListener(){
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				lvWeek.setVisibility(View.GONE);
				btnHide.setVisibility(View.GONE);
			}
		});
	}
	
	private void openMenu(){
		isMenuView = true;
		llMenu.setVisibility(View.VISIBLE);
		btnHide.setVisibility(View.VISIBLE);
		ScaleAnimation animationSX = new ScaleAnimation(
				0f, 1.0f, 1.0f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		animationSX.setDuration(300);
		animationSX.setInterpolator(new DecelerateInterpolator());
		ScaleAnimation animationSY = new ScaleAnimation(
				1.0f, 1.0f, 0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0f);
		animationSY.setDuration(300);
		
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(animationSX);
		set.addAnimation(animationSY);

		llMenu.startAnimation(set);
	}
	
	private void closeMenu(){
		isMenuView = false;
		ScaleAnimation animationSX = new ScaleAnimation(
				1.0f, 0.0f, 1.0f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		animationSX.setDuration(150);
		ScaleAnimation animationSY = new ScaleAnimation(
				1.0f, 1.0f, 1.0f, 0f,
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0f);
		animationSY.setDuration(150);
		animationSY.setInterpolator(new DecelerateInterpolator());
		
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(animationSX);
		set.addAnimation(animationSY);
		llMenu.startAnimation(set);
		
		set.setAnimationListener(new AnimationListener(){
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				llMenu.setVisibility(View.GONE);
				btnHide.setVisibility(View.GONE);
			}
		});
	}
	
	// week picker click
	public void itemClick(int position) {
		sendMessageDelay(SET_WEEK,arrWeek[position],50L);
	}
	
	private void sendMessage(int message){
		sendMessageDelay(message,null,0);
	}

	private void sendMessage(int message,Object obj){
		sendMessageDelay(message,obj,0);
	}

	private void sendMessageDelay(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

}
