package com.west2.main.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.west2.main.R;
import com.west2.main.entity.MarkEntity;

public class MarkAdapter extends BaseAdapter{
	
	private Context context;
	private List<MarkEntity> mList;
	private ViewHolder holder;

	public MarkAdapter(Context context,List<MarkEntity> mList){
		this.context = context;
		this.mList = mList;
	}
	
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mark, null);
			holder.tvSubtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
			holder.tvCredit = (TextView) convertView.findViewById(R.id.tv_credit);
			holder.tvGradePoint = (TextView) convertView.findViewById(R.id.tv_gradepoint);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		MarkEntity mark = mList.get(position);
		if(position == 0 || !mark.getTerm().equals(mList.get(position - 1).getTerm())){
			holder.tvSubtitle.setText(mark.getTerm());
			holder.tvSubtitle.setVisibility(View.VISIBLE);
		}
		else{
			holder.tvSubtitle.setVisibility(View.GONE);
		}
		holder.tvName.setText(mark.getCourseName());
		holder.tvScore.setText(mark.getScore());
		holder.tvCredit.setText(mark.getGradeCredit());
		holder.tvGradePoint.setText(mark.getGradePoint());
		return convertView;
	}

	public class ViewHolder{
		private TextView tvSubtitle;
		private TextView tvName;
		private TextView tvScore;
		private TextView tvCredit;
		private TextView tvGradePoint;
	}
}
