package fzu.mcginn.activity;

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
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import fzu.mcginn.R;
import fzu.mcginn.adapter.MenuAdapter;
import fzu.mcginn.database.DbExam;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.fragment.*;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.service.TimeService;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.InfoUtils;

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
		"课表","成绩","考场","二手市场","教务处通知","设置"	,"注销"
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
		
		tvName.setText(userEntity != null && userEntity.getRealname() != null ? userEntity.getRealname() : "王大锤");
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
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.obj.toString().equals(InfoUtils.SR_TIME_SUCCEED)){
				tvWeek.setText("第" + dateEntity.getCurrentWeek() + "周");
				tvDate.setText(dateEntity.getSchoolYear()+"学年"+dateEntity.getTerm()+"学期");
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
		case 3:Toast.makeText(context, "开发中..", Toast.LENGTH_SHORT).show();break;
		case 4:Toast.makeText(context, "开发中..", Toast.LENGTH_SHORT).show();break;
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
                      	.text("是否退出?")
                        .actionLabel("是的")
                        .actionColorResource(R.color.yellow_500)
                        .actionListener(new ActionClickListener() {
                            public void onActionClicked(Snackbar snackbar) {
                            	MainActivity.this.finish();
                            }
                        }));
		return false;
	}
	
}
