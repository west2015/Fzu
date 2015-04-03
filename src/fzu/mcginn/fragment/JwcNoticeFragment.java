package fzu.mcginn.fragment;

import com.material.widget.ObservableListView;
import com.material.widget.scroll.ObservableScrollViewCallbacks;
import com.material.widget.scroll.ScrollState;
import com.material.widget.scroll.ScrollUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fzu.mcginn.R;

public class JwcNoticeFragment extends Fragment implements ObservableScrollViewCallbacks{

	private Context context;
	private Activity activity;
	
	private int mBaseTranslationY;
	
	private View mHeaderView;
	private View mToolbarView;
	private ObservableListView olv;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		activity = this.getActivity();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_jwc_notice, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		mHeaderView = view.findViewById(R.id.view_header);
		mToolbarView = view.findViewById(R.id.toolbar);
		olv = (ObservableListView) view.findViewById(R.id.olv);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		// toolbar
		olv.addHeaderView(inflater.inflate(R.layout.padding, olv, false));
		// head
		olv.addHeaderView(inflater.inflate(R.layout.padding, olv, false));
		
	}

	private void setListener(View view){
	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			}
		}
	};

	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll,
			boolean dragging) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void onDownMotionEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		// TODO Auto-generated method stub
		
	}

}
