package fzu.mcginn.adapter;

import fzu.mcginn.fragment.DayScheduleFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ScheduleAdapter extends FragmentPagerAdapter {
	
	private final String[] TITLE = {"   周一   ","   周二   ","   周三   ","   周四   ","   周五   ","   周六   ","   周日   "};

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
	
	public Fragment getItem(int position) {
		return DayScheduleFragment.newInstance(position+1,week,json);
	}

	public int getCount() {
		return TITLE.length;
	}
	
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

}
