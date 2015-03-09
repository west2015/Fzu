package fzu.mcginn.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fzu.mcginn.R;
import fzu.mcginn.entity.CourseEntity;

public class ScheduleListAdapter extends BaseAdapter{

	private Context context;
	private ViewHolder holder;

	private Typeface typeBold;
	private Typeface typeRegular;
	private List<CourseEntity> mList;

	public ScheduleListAdapter(Context context,List<CourseEntity> mList){
		this.context = context;
		this.mList = mList;

		typeBold = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_extralight.ttf");
		typeRegular = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_regular.ttf");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_list, null);
			holder.tvLesson = (TextView) convertView.findViewById(R.id.tv_lesson);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvTeacherName = (TextView) convertView.findViewById(R.id.tv_teacher_name);
			holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_place);
			holder.tvWeeks = (TextView) convertView.findViewById(R.id.tv_weeks);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		CourseEntity entity = mList.get(position);
		// LESSON
		int start = entity.getLesson();
		int end = entity.getLesson() + entity.getLength() - 1;
		holder.tvLesson.setText(start + "-" + end);
		// COURSE NAME
		holder.tvName.setText(entity.getName());
//		holder.tvName.setTypeface(typeBold);
		// TEARCHNAME
		holder.tvTeacherName.setText(entity.getTeacherName());
		// PLACE
		holder.tvPlace.setText(entity.getPlace().replace("[", "").replace("]", ""));
		// SCHEDULE
		holder.tvWeeks.setText(entity.getStartWeek() + "-" + entity.getEndWeek());
		
		return convertView;
	}

	public class ViewHolder{
		private TextView tvLesson;
		private TextView tvName;
		private TextView tvTeacherName;
		private TextView tvPlace;
		private TextView tvWeeks;
	}

	public interface onItemClick{
		public void onItem(int position);
	}
}
