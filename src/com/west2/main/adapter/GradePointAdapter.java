package com.west2.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.west2.main.R;
import com.west2.main.entity.MarkEntity;
import com.west2.main.utils.InfoUtils;

public class GradePointAdapter extends BaseAdapter{
	
	private Context context;
	private ViewHolder holder;
	private List<GradePoint> mList;

	public GradePointAdapter(Context context,List<MarkEntity> xList){
		this.context = context;
		calcGPA(xList);
	}
	
	/*
	 * 计算学期绩点
	 */
	private void calcGPA(List<MarkEntity> xList){
		mList = new ArrayList<GradePoint>();
		for(int i=0;i<xList.size();++i){
			boolean hasAllScore = true;
			int start = i, end = i;
			String term = xList.get(i).getTerm();
			while(end<xList.size() && xList.get(end).getTerm().equals(term)){
				String score = xList.get(end).getScore();
				if(score == null || score.contains("未录入")){
					hasAllScore = false;
				}
				++end;
			}
			if(hasAllScore){
				int j;
				double totalCredit=0, gradePoint=0;
				for(j=start;j<end;++j){
					double credit=InfoUtils.getDouble(xList.get(j).getGradeCredit());
					totalCredit += credit;
				}
				if(totalCredit != 0){
					for(j=start;j<end;++j){
						double credit=InfoUtils.getDouble(xList.get(j).getGradeCredit());
						double grade =InfoUtils.getDouble(xList.get(j).getGradePoint());
						gradePoint += (grade*credit)/totalCredit;
					}
					gradePoint = Math.round(gradePoint * 1000000.0) / 1000000.0;
					mList.add(new GradePoint(term, gradePoint));
				}
			}
			i = end-1;
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_gradepoint, null);
			holder.tvTerm = (TextView) convertView.findViewById(R.id.tv_term);
			holder.tvGradePoint = (TextView) convertView.findViewById(R.id.tv_gradepoint);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		GradePoint gp = mList.get(position);
		holder.tvTerm.setText(gp.getTerm());
		holder.tvGradePoint.setText(gp.getGp()+"");
		return convertView;
	}

	public class GradePoint {
		private String term;
		private double gp;
		public GradePoint(String t,double g){
			term = t;
			gp = g;
		}
		public String getTerm(){
			return term;
		}
		public double getGp(){
			return gp;
		}
	}
	
	public class ViewHolder{
		private TextView tvTerm;
		private TextView tvGradePoint;
	}
}
