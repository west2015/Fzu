package com.west2.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.adapter.MenuAdapter;
import com.west2.main.database.DbExam;
import com.west2.main.entity.DateEntity;
import com.west2.main.entity.UserEntity;
import com.west2.main.fragment.ExamFragment;
import com.west2.main.fragment.JwcNoticeFragment;
import com.west2.main.fragment.MarkFragment;
import com.west2.main.fragment.MarketFragment;
import com.west2.main.fragment.ScheduleFragment;
import com.west2.main.fragment.SettingFragment;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.service.TimeService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

public class MainActivity extends FragmentActivity
						  implements MenuAdapter.onItemClick,
						  			 MessageInterface{

	private final int[] iconId = {
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black
	};

	private final String[] text = {
		"�α�","�ɼ�","����","�����г�","����֪ͨ","����"	,"ע��"
	};

	private Context context;
	private int curPosition = -1;
	private Fragment fm;
	private FragmentTransaction ft;
	private UserEntity userEntity;
	private DateEntity dateEntity;

	private DrawerLayout drawer;
	private ListView menuList;
	private TextView tvName;
	private TextView tvWeek;
	private TextView tvDate;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findView();
		setListener();
	}

	private void findView(){
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		menuList = (ListView) findViewById(R.id.menu_listView);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvWeek = (TextView) findViewById(R.id.tv_week);
		tvDate = (TextView) findViewById(R.id.tv_date);

		context = this;
		userEntity = BaseUtils.getInstance().getUserEntity();
		
		tvName.setText(userEntity != null && userEntity.getRealname() != null ? userEntity.getRealname() : "����");
		menuList.setDividerHeight(0);
		menuList.setAdapter(new MenuAdapter(context,iconId,text));
		
		curPosition = 0;
		setFragment(new ScheduleFragment());
		new Thread(getNetTimeRun).start();
	}

	private void setListener(){
	}

	Runnable getNetTimeRun = new Runnable(){
		public void run() {
			dateEntity = new TimeService().getTime(false);
			if(dateEntity != null){
				Message msg = mHandler.obtainMessage();
				msg.obj = InfoUtils.SR_TIME_SUCCEED;
				mHandler.sendMessage(msg);
			}
		}
	};

	private void setCustomTheme(){
		String theme = BaseUtils.getInstance().getCustomTheme();
		if(theme.equals(InfoUtils.SR_SETTING_THEME_WHITE)){
			menuList.setBackgroundResource(R.color.white);
		}
		else
		if(theme.equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			menuList.setBackgroundResource(R.color.black_bg);				
		}
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.obj.toString().equals(InfoUtils.SR_TIME_SUCCEED)){
				tvWeek.setText("��" + dateEntity.getCurrentWeek() + "��");
				tvDate.setText(dateEntity.getSchoolYear()+"ѧ��"+dateEntity.getTerm()+"ѧ��");
			}
		}
	};
	
	@Override
	public void onItem(int position) {
		if(position != 6)
			Message(InfoUtils.CLOSE_DRAWER);
		if(position == curPosition){
			return ;
		}
		Fragment mFragment = null;
		switch(position){
		case 0:mFragment = new ScheduleFragment();break;
		case 1:mFragment = new MarkFragment();break;
		case 2:mFragment = new ExamFragment();break;
		case 3:mFragment = new MarketFragment();break;
		case 4:mFragment = new JwcNoticeFragment();break;
		case 5:mFragment = new SettingFragment();break;
		case 6:
			BaseUtils.getInstance().setUserEntity(null);
			BaseUtils.getInstance().setDateEntity(null);
			BaseUtils.getInstance().setScheduleJson(null);
			BaseUtils.getInstance().setMarkJson(null);
			new DbExam(context).setExamJson(null);;
			skip2Activity(LoginActivity.class,true);
			break;
		}
		curPosition = position;
		setFragment(mFragment);
	}
	
	private void setFragment(Fragment mFragment){
		if(mFragment == null){
			return ;
		}
		fm = mFragment;
		ft = getSupportFragmentManager().beginTransaction();
    	ft.replace(R.id.fragment_layout, fm);
    	ft.commit();
	}
	
	@Override
	public void Message(String msg) {
		if(msg.equals(InfoUtils.OPEN_DRAWER)){
			drawer.openDrawer(Gravity.LEFT);
		} else
		if(msg.equals(InfoUtils.CLOSE_DRAWER)){
			drawer.closeDrawer(Gravity.LEFT);
		} else
		if(msg.equals(InfoUtils.REFRESH_TIME)){
			Message mMsg = mHandler.obtainMessage();
			mMsg.obj = InfoUtils.SR_TIME_SUCCEED;
			mHandler.sendMessage(mMsg);
		}
	}
	
	private void skip2Activity(Class mClass,boolean isFinish){
		startActivity(new Intent(this,mClass));
		if(isFinish){
			finish();
		}
	}

	public boolean onKeyDown(int KeyCode,KeyEvent event){
		SnackbarManager.show(
                Snackbar.with(context)
                      	.text("�Ƿ��˳�?")
                        .actionLabel("�ǵ�")
                        .actionColorResource(R.color.yellow_500)
                        .actionListener(new ActionClickListener() {
                            public void onActionClicked(Snackbar snackbar) {
                            	MainActivity.this.finish();
                            }
                        }));
		return false;
	}
	
	
	
	//����ͳ��
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
