package fzu.mcginn.adapter;

import fzu.mcginn.fragment.DayScheduleFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ScheduleAdapter extends FragmentPagerAdapter {
	
	private final String[] TITLE = {"   周一   ","   周二   ","   周三   ","   周四   ","   周五   ","   周六   ","   周日   "};

	public ScheduleAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return DayScheduleFragment.newInstance(position);
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
