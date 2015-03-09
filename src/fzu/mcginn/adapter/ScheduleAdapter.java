package fzu.mcginn.adapter;

import fzu.mcginn.fragment.DayScheduleFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ScheduleAdapter extends FragmentPagerAdapter {
	
	private final String[] TITLE = {"   ��һ   ","   �ܶ�   ","   ����   ","   ����   ","   ����   ","   ����   ","   ����   "};

	private int week = 0;
	private String json = null;

	public ScheduleAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public ScheduleAdapter(FragmentManager fm,int week,String json) {
		super(fm);
		this.week = week;
		this.json = json;
	}

	public void update(int week,String json){
		this.week = week;
		this.json = json;
		notifyDataSetChanged();
	}
	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return DayScheduleFragment.newInstance(position+1,week,json);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TITLE.length;
	}
	
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

}
