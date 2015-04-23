package com.west2.main.adapter;

import java.util.List;

import com.west2.main.R;
import com.west2.main.entity.JWCNoticeEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JwcNoticeAdapter extends BaseAdapter{
	private Context context;
	private List<JWCNoticeEntity> mList;
	private ViewHolder holder;

	public JwcNoticeAdapter(Context context,List<JWCNoticeEntity> mList){
		this.context = context;
		this.mList = mList;
	}
	
	public void update(List<JWCNoticeEntity> mList){
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_jwc_notice, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		JWCNoticeEntity entity = mList.get(position);
		holder.tvTime.setText(entity.getTime());
		holder.tvTitle.setText(entity.getTitle());
		
		if(entity.isRed()){
			holder.tvTitle.setTextColor(context.getResources().getColor(R.color.red_400));
			holder.tvTime.setTextColor(context.getResources().getColor(R.color.red_400));
		}
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tvTitle;
		private TextView tvTime;
	}

}
