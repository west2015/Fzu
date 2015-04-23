package com.west2.main.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.west2.main.R;
import com.west2.main.entity.ExamEntity;

public class ExamAdapter extends BaseAdapter{
	
	private Context context;
	private List<ExamEntity> mList;
	private ViewHolder holder;

	public ExamAdapter(Context context,List<ExamEntity> mList){
		this.context = context;
		this.mList = mList;
	}
	
	public void updateDate(List<ExamEntity> mList){
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_exam, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_place);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		ExamEntity ee = mList.get(position);
		holder.tvName.setText(ee.getCourseName());
		if(ee.getExamPlace() != null && !ee.getExamPlace().equals("")){
			holder.tvPlace.setVisibility(View.VISIBLE);
			holder.tvPlace.setText(ee.getExamPlace());
		}
		else{
			holder.tvPlace.setVisibility(View.GONE);
		}
		if(ee.getExamTime() != null && !ee.getExamTime().equals("")){
			holder.tvTime.setVisibility(View.VISIBLE);
			holder.tvTime.setText(ee.getExamTime());
		}
		else{
			holder.tvTime.setVisibility(View.GONE);
		}
		return convertView;
	}

	public class ViewHolder{
		private TextView tvName;
		private TextView tvPlace;
		private TextView tvTime;
	}
}
