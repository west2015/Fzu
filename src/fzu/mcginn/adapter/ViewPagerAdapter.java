package fzu.mcginn.adapter;

import java.util.List;

import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter{

	private List<View> views;
	private String[] TITLE;
	
	public ViewPagerAdapter(List<View> views,String[] TITLE){
		this.views = views;
		this.TITLE = TITLE;
	}
	
	public ViewPagerAdapter(List<View> views,List<String> titles){
		this.views = views;
		TITLE = new String[titles.size()];
		TITLE = titles.toArray(TITLE);
	}
	
	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(View container, int position){
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
	
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

}
