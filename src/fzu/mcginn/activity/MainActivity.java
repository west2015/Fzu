package fzu.mcginn.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;
import fzu.mcginn.R;
import fzu.mcginn.adapter.MenuAdapter;
import fzu.mcginn.entity.DateEntity;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.fragment.ScheduleFragment;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.service.ScheduleService;
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
			R.drawable.ic_menu_black
	};
	private final String[] text = {
		"课表","成绩","考场","二手市场","教务处通知","设置"	
	};

	private Context context;
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
		
		setFragment(new ScheduleFragment());
		new Thread(getNetTimeRun).start();
	}

	private void setListener(){
		
	}
	
//	Runnable getScheduleRun = new Runnable(){
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			UserEntity userEntity = BaseUtils.getInstance().getUserEntity();
//			DateEntity dateEntity = BaseUtils.getInstance().getDateEntity();
//			Log.e("!!!!!!!", userEntity.getUsername()+" "+dateEntity.getSchoolYear()+" "+dateEntity.getTerm());
//			String res = new ScheduleService().querySchedule(userEntity, dateEntity.getSchoolYear(), dateEntity.getTerm());
//			if(res != null) Log.e("!!!!!!!", res);
//		}
//	};

	Runnable getNetTimeRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dateEntity = new TimeService().getTime(true);
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
	public void onItem(int position) {
		// TODO Auto-generated method stub
		Fragment mFragment = null;
		switch(position){
		case 0:mFragment = new ScheduleFragment();break;
		case 1:break;
		case 2:break;
		case 3:break;
		case 4:break;
		case 5:break;
		}
		setFragment(mFragment);
		drawer.closeDrawer(Gravity.LEFT);
	}

	@Override
	public void Message(String Msg) {
		// TODO Auto-generated method stub
		if(Msg.equals(InfoUtils.OPEN_DRAWER)){
			drawer.openDrawer(Gravity.LEFT);
		}
		if(Msg.equals(InfoUtils.CLOSE_DRAWER)){
			drawer.closeDrawer(Gravity.LEFT);
		}
	}
	
}
