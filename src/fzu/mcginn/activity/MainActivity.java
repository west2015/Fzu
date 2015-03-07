package fzu.mcginn.activity;


import fzu.mcginn.R;
import fzu.mcginn.adapter.MenuAdapter;
import fzu.mcginn.entity.UserEntity;
import fzu.mcginn.fragment.*;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.service.TimeService;
import fzu.mcginn.utils.BaseUtils;
import fzu.mcginn.utils.InfoUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;

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

	private DrawerLayout drawer;
	private ListView menuList;
	private TextView tvName;
	
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
		
		context = this;
		userEntity = BaseUtils.getInstance().getUserEntity();

		tvName.setText(userEntity != null && userEntity.getRealname() != null ? userEntity.getRealname() : "王大锤");
		menuList.setDividerHeight(0);
		menuList.setAdapter(new MenuAdapter(context,iconId,text));
		
		setFragment(new ScheduleFragment());
		new TimeService(this).getNetTime();
	}

	private void setListener(){
		
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
