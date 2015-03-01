package fzu.mcginn.fragment;

import fzu.mcginn.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DayScheduleFragment extends Fragment{
	
	private static final String ARG_POSITION = "position";
	
	private TextView tv;
	private int position;
	
	public static DayScheduleFragment newInstance(int position){
		DayScheduleFragment f = new DayScheduleFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_POSITION, position);
		f.setArguments(bundle);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schedule_fragment_day, null);
		findView(view);
		tv.setText("Card " + position);
		return view;
	}
	
	private void findView(View view){
		tv = (TextView) view.findViewById(R.id.tv_schedule_day);
	}
}	
