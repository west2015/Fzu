package com.west2.main.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.west2.main.R;
import com.west2.main.entity.BookEntity;

public class SearchAdapter extends BaseAdapter{
	
	private Context context;
	private List<BookEntity> mList;
	private ViewHolder holder;

	public SearchAdapter(Context context,List<BookEntity> mList){
		this.context = context;
		this.mList = mList;
	}
	
	public void updateDate(List<BookEntity> mList){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
			holder.tvPublisher = (TextView) convertView.findViewById(R.id.tv_publisher);
			holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_place);
			holder.tvStore = (TextView) convertView.findViewById(R.id.tv_store);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		BookEntity be = mList.get(position);
		holder.tvName.setText(be.getName());
		holder.tvAuthor.setText(be.getAuthor());
		holder.tvPublisher.setText(be.getPublisher());
		holder.tvPlace.setText(be.getPlace());
		holder.tvStore.setText(be.getStore()+"");
		holder.tvNumber.setText(be.getCntAmount()+"");
		return convertView;
	}

	private class ViewHolder{
		private TextView tvName;
		private TextView tvAuthor;
		private TextView tvPublisher;
		private TextView tvPlace;
		private TextView tvStore;
		private TextView tvNumber;
	}
}
