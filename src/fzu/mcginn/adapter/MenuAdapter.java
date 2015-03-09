package fzu.mcginn.adapter;

import com.material.widget.RaisedButton;

import fzu.mcginn.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MenuAdapter extends BaseAdapter{

	private int[] iconId;
	private String[] text;
	
	private Context context;
	private ViewHolder holder;
	private onItemClick mListener;
	
	private int curItem;
	Typeface typeFace;

	public MenuAdapter(Context context,int[] iconId,String[] text){
		this.context = context;
		this.iconId = iconId;
		this.text = text;

		mListener = (onItemClick) context;
		curItem = 0;
		typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_extralight.ttf");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(iconId.length != text.length) return 0;
		return text.length;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_drawer_menu, null);

			holder.imgIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
			holder.btn =  (RaisedButton) convertView.findViewById(R.id.menu_btn);
			holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		// initValue
		holder.btn.setTypeface(typeFace);
		holder.btn.setText(text[position]);
		if(position == curItem){
			holder.btn.setTextColor(context.getResources().getColor(R.color.blue_500));
			holder.imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_blue));
			holder.rlItem.setBackgroundColor(context.getResources().getColor(R.color.grey_200));
		}
		else{
			holder.btn.setTextColor(context.getResources().getColor(R.color.black_text));
			holder.imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_black));
			holder.rlItem.setBackgroundColor(Color.TRANSPARENT);
		}
		
		// setListener
		final int mPosition = position;
		holder.btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mListener != null){
					curItem = mPosition;
					notifyDataSetChanged();
					mListener.onItem(mPosition);
				}
			}
		});
		
		return convertView;
	}

	public class ViewHolder{
		private ImageView imgIcon;
		private RaisedButton btn;
		private RelativeLayout rlItem;
	}

	public interface onItemClick{
		public void onItem(int position);
	}
}
