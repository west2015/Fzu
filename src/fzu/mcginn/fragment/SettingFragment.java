package fzu.mcginn.fragment;

import com.material.widget.DampScrollView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import fzu.mcginn.R;
import fzu.mcginn.adapter.GradePointAdapter;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.utils.InfoUtils;

public class SettingFragment extends Fragment{

	private Context context;
	private MessageInterface mListener;

	private DampScrollView dsv;
	private RelativeLayout toolbarAll;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_setting, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		dsv = (DampScrollView) view.findViewById(R.id.dsv);
		toolbarAll = (RelativeLayout) view.findViewById(R.id.toolbar_all);

	}

	private void setListener(View view){
		view.findViewById(R.id.btn_push).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			Toast.makeText(context, "ÍÆËÍ", Toast.LENGTH_SHORT).show();
			showPushDialog();
		}});
		view.findViewById(R.id.btn_feedback).setOnClickListener(new OnClickListener(){public void onClick(View arg0) {
			Toast.makeText(context, "·´À¡", Toast.LENGTH_SHORT).show();
		}});
		view.findViewById(R.id.av).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			mListener.Message(InfoUtils.OPEN_DRAWER);
		}});
	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
		}
	};
	


	private void showPushDialog(){
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_push,null));
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_push);
		// findView
		// initValue
		// setListener
		w.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			dialog.dismiss();
		}});
		w.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			dialog.dismiss();
		}});
	}
	
	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

}
