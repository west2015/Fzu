package com.west2.main.fragment;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.adapter.ScheduleListAdapter;
import com.west2.main.entity.CourseEntity;
import com.west2.main.service.ScheduleService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DayScheduleFragment extends Fragment{

	private static final String ARG_WEEK = "week";
	private static final String ARG_WEEKDAY = "weekday";
	private static final String ARG_SCHEDULE_JSON = "schedule_json";

	private TextView tv;
	private ListView lv;

	private String json;
	private int week;
	private int weekday;
	private List<CourseEntity> mList;
	
	public static DayScheduleFragment newInstance(int weekday){
		DayScheduleFragment f = new DayScheduleFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_WEEKDAY, weekday);
		f.setArguments(bundle);
		return f;
	}
	
	public static DayScheduleFragment newInstance(int weekday,int week,String json){
		DayScheduleFragment f = new DayScheduleFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_WEEK, week);
		bundle.putInt(ARG_WEEKDAY, weekday);
		bundle.putString(ARG_SCHEDULE_JSON, json);
		f.setArguments(bundle);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		week = getArguments().getInt(ARG_WEEK);
		weekday = getArguments().getInt(ARG_WEEKDAY);
		json =  getArguments().getString(ARG_SCHEDULE_JSON);

		mList = new ArrayList<CourseEntity>();
		if(json != null && json.length() > 10){
			List<CourseEntity> list = new ScheduleService().parseWeek(json, week);
			for(int i=0;i<list.size();++i)
			if(list.get(i).getWeekday() == weekday){
				mList.add(list.get(i));
			}
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schedule_fragment_day, null);
		findView(view);
		if(mList.size() > 0){
			lv.setVisibility(View.VISIBLE);
			lv.setAdapter(new ScheduleListAdapter(container.getContext(), mList));
		}
		else{
			lv.setVisibility(View.GONE);
			tv.setText("没有课呦");
		}
		return view;
	}
	
	private void findView(View view){
		tv = (TextView) view.findViewById(R.id.tv_schedule_day);
		lv = (ListView) view.findViewById(R.id.lv_schedule);
	}
	
	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("DayScheduleFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(getActivity());;          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("DayScheduleFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(getActivity());
	}
}	
