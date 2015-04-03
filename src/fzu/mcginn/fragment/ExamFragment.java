package fzu.mcginn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import fzu.mcginn.R;
import fzu.mcginn.adapter.ExamAdapter;
import fzu.mcginn.entity.ExamEntity;
import fzu.mcginn.interfaces.MessageInterface;
import fzu.mcginn.service.ExamService;
import fzu.mcginn.utils.InfoUtils;

public class ExamFragment extends Fragment{
	private final int REFRESH_SUCCEED = 1001;
	private final int REFRESH_FAIL = 1002;

	private Context context;
	private MessageInterface mListener;
	private ExamAdapter mAdapter;

	private boolean isFirstRefresh = true;
	private boolean toRefresh = false;
	private boolean isRefreshing = false;
	private List<ExamEntity> mList;

	private ListView lv;
	private SwipeRefreshLayout srl;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_exam, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		lv = (ListView) view.findViewById(R.id.lv);
		srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);

		// 初始化
		mList = new ArrayList<ExamEntity>();
		mAdapter = new ExamAdapter(context, mList);
		lv.setAdapter(mAdapter);
		// 获取数据
		srl.setProgressViewOffset(false, -120, 100);
		srl.setRefreshing(true);
		new Thread(getExamRun).start();
	}

	private void setListener(View view){
		// 下拉刷新
		srl.setOnRefreshListener(new OnRefreshListener(){public void onRefresh() {
			if(!isRefreshing){
				toRefresh = true;
				new Thread(getExamRun).start();
			}
		}});
		view.findViewById(R.id.av).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			mListener.Message(InfoUtils.OPEN_DRAWER);
		}});
	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == REFRESH_SUCCEED || msg.what == REFRESH_FAIL){
				isRefreshing = false;
				srl.setRefreshing(false);
//				if(isFirstRefresh){
//					isFirstRefresh = false;
//					srl.setProgressViewOffset(true, -50, 100);
//				}
			}
		
			switch(msg.what){
			case REFRESH_SUCCEED:
				if(toRefresh){
					SnackbarManager.show(Snackbar.with(context).text("刷新成功"));
				}
				mAdapter.updateDate(mList);
				break;
			case REFRESH_FAIL:
				SnackbarManager.show(Snackbar.with(context)
                      	.text("遇到错误啦")
                        .actionLabel("重试")
                        .actionColorResource(R.color.yellow_500)
                        .actionListener(new ActionClickListener() {
                            public void onActionClicked(Snackbar snackbar) {
                            	if(!isRefreshing){
                            		toRefresh = true;
                            		new Thread(getExamRun).start();
                            	}
                            }
                        }));
				break;
			}
		}
	};

	/*
	 * 获取考试线程
	 */
	Runnable getExamRun = new Runnable(){public void run() {
		try {
			isRefreshing = true;
			Thread.sleep(1500);
			mList = new ExamService().getExam(context, toRefresh);
			if(mList != null){
				sendMessage(REFRESH_SUCCEED, null, 0);
			}
			else{
				sendMessage(REFRESH_FAIL, null, 0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}};
	
	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

}
