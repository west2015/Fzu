package fzu.mcginn.activity;


import fzu.mcginn.R;
import fzu.mcginn.adapter.MenuAdapter;
import fzu.mcginn.fragment.*;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.utils.MsgUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ListView;

public class MainActivity extends FragmentActivity
						  implements MenuAdapter.onItemClick,
						  			 MessageInterface{
	
	private int[] iconId = {
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black,
			R.drawable.ic_menu_black
	};
	private String[] text = {
		"课表","成绩","考场","二手市场","教务处通知","设置"	
	};

	private Context context;
	private Fragment fm;
	private FragmentTransaction ft;

	private DrawerLayout drawer;
	private ListView menuList;
	
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
		
		context = this;

		menuList.setDividerHeight(0);
		menuList.setAdapter(new MenuAdapter(context,iconId,text));
		
		setFragment(new ScheduleFragment());
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
		if(Msg.equals(MsgUtils.OPEN_DRAWER)){
			drawer.openDrawer(Gravity.LEFT);
		}
		if(Msg.equals(MsgUtils.CLOSE_DRAWER)){
			drawer.closeDrawer(Gravity.LEFT);
		}
	}
	
}
