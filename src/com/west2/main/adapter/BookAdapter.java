package com.west2.main.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.west2.main.R;
import com.west2.main.entity.BookEntity;

public class BookAdapter extends BaseAdapter{
	
	private Context context;
	private List<BookEntity> mList;
	private ViewHolder holder;
	private onItemClickListener mListener;
	
	public interface onItemClickListener{
		public void renew(BookEntity book);			
	}

	public BookAdapter(Context context,List<BookEntity> mList){
		this.context = context;
		this.mList = mList;
	}
	
	public BookAdapter(Fragment fm,Context context,List<BookEntity> mList){
		this.mListener = (onItemClickListener) fm;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_book, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvCollection = (TextView) convertView.findViewById(R.id.tv_collection);
			holder.tvReturnDate = (TextView) convertView.findViewById(R.id.tv_returndate);
			holder.btnRenew = (Button) convertView.findViewById(R.id.btn_renew);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		final BookEntity be = mList.get(position);
		holder.tvName.setText(be.getName());
		holder.tvCollection.setText(be.getBarCode());
		holder.tvReturnDate.setText(be.getReturnDate());
		holder.btnRenew.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				mListener.renew(be);
			}
		});
		return convertView;
	}

	private class ViewHolder{
		private TextView tvName;
		private TextView tvCollection;
		private TextView tvReturnDate;
		private Button btnRenew;
	}
}
