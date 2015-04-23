package com.west2.main.adapter;

import java.util.List;

import com.material.widget.RaisedButton;

import com.west2.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SimpleAdapter extends BaseAdapter{
	
	public interface onItemClick{
		public void itemClick(int position);
	}

	private Context context;
	private String[] array;
	private ViewHolder holder;
	private onItemClick mItemClickListener;
	
	public SimpleAdapter(Context context,List<String> list,Object listener){
		this.context = context;
		array = (String[]) list.toArray();
		mItemClickListener = (onItemClick) listener;
	}
	
	public SimpleAdapter(Context context,String[] array,Object listener){
		this.context = context;
		this.array = array;
		mItemClickListener = (onItemClick) listener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return array[position];
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_simple, null);
			holder = new ViewHolder();
			holder.rb = (RaisedButton) convertView.findViewById(R.id.btn);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.rb.setText(array[position]);
		final int mPosition = position;
		holder.rb.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mItemClickListener.itemClick(mPosition);
			}
		});
		return convertView;
	}
	
	public class ViewHolder{
		private RaisedButton rb;
	}
}
