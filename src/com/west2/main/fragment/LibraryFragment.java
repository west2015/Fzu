package com.west2.main.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.material.widget.FloatingActionButton;
import com.material.widget.InputText;
import com.material.widget.RaisedButton;
import com.material.widget.ScrollableLayout;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.activity.BindingActivity;
import com.west2.main.adapter.BookAdapter;
import com.west2.main.adapter.SearchAdapter;
import com.west2.main.entity.BookEntity;
import com.west2.main.entity.UserEntity;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.service.LibraryService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;
import com.west2.main.utils.MetricsConverter;

public class LibraryFragment extends Fragment implements BookAdapter.onItemClickListener{

	private final int HIDE = 1000;
	private final int REFRESH_SEARCH_SUCCEED = 1001;
	private final int REFRESH_SEARCH_FAIL = 1002;
	private final int REFRESH_BOOK_SUCCEED = 1003;
	private final int REFRESH_BOOK_FAIL = 1004;
	private final int SHOW_FIRST = 1005;
	private final int SHOW_SECOND = 1006;
	private final int SHOW_SEARCH = 1007;
	private final int SHOW_SEARCH_LIST = 1008;
	private final int SHOW_BOOK_LIST = 1009;
	private final int RENEW_SUCCEED = 1010;
	private final int RENEW_FAIL = 1011;
	private final int RENEW_HAD = 1012;
	private final int SHOW_NO_BOOK = 1013;
	
	private final int SEARCH_PAGE = 2000;
	private final int SEARCH_LIST_PAGE = 2001;
	private final int BOOK_LIST_PAGE = 2002;
	
	private Context context;
	private Activity activity;
	private MessageInterface mListener;
	private SearchAdapter searchAdapter = null;
	private BookAdapter bookAdapter = null;
	
	private TextView tvFirst,tvSecond;
	private PullToRefreshListView lv;
	private FloatingActionButton fabSearch;
	private RelativeLayout rlRefresh;
	private RelativeLayout rlSearch;
	private InputText inputKey;
	private RaisedButton btnSearch;
	private ScrollableLayout sl;
	private ProgressBar pb;
	private SwipeRefreshLayout srl;
	private ListView lvBook;
	private TextView tvBook;
	private TextView tvNoBook;
	
	private int curPage;
	private int page;
	private String key;
	private BookEntity renewBook;
	
	private boolean isRefreshing;
	private boolean isRenewing;
	private boolean showRefreshResult;
	private List<BookEntity> mList = null;
	private List<BookEntity> bList = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		activity = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			context.setTheme(R.style.DarkTheme);
		}
		else{
			context.setTheme(R.style.LightTheme);
		}
		
		View view = inflater.inflate(R.layout.fragment_library, null);
		findView(view);
		setListener(view);
		return view;
	}

	private void findView(View view){
		sl = (ScrollableLayout) view.findViewById(R.id.sl);
		tvFirst = (TextView) view.findViewById(R.id.tv_first);
		tvSecond = (TextView) view.findViewById(R.id.tv_second);
		lv = (PullToRefreshListView) view.findViewById(R.id.lv);
		fabSearch = (FloatingActionButton) view.findViewById(R.id.fab_search);
		rlRefresh = (RelativeLayout) view.findViewById(R.id.rl_refresh);
		rlSearch = (RelativeLayout) view.findViewById(R.id.rl_search);
		inputKey = (InputText) view.findViewById(R.id.input_key);
		btnSearch = (RaisedButton) view.findViewById(R.id.btn_search);
		pb = (ProgressBar) view.findViewById(R.id.pb);
		srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
		lvBook = (ListView) view.findViewById(R.id.lv_book);
		tvNoBook = (TextView) view.findViewById(R.id.tv_no_book);

		isRefreshing = false;
		isRenewing = false;
		sl.setMaxScrollY(0);
		lv.setMode(Mode.PULL_FROM_END);

		mList = new ArrayList<BookEntity>();
		searchAdapter = new SearchAdapter(context,mList);
		lv.setAdapter(searchAdapter);
		
		bList = new ArrayList<BookEntity>();
		bookAdapter = new BookAdapter(this,context,bList);
		lvBook.setAdapter(bookAdapter);
		
		sendMessage(HIDE,null,0);
		showSearchPage();
	}

	@SuppressWarnings("unchecked")
	private void setListener(View view){
		// 我的图书
		view.findViewById(R.id.btn_book).setOnClickListener(new OnClickListener(){public void onClick(View arg0) {
			if(isRefreshing) return ;
			UserEntity user = BaseUtils.getInstance().getLibraryUser();
			if(user==null){
//				SnackbarManager.show(Snackbar.with(context).text("在'设置－绑定账号'绑定图书馆账号"));
				Intent intent = new Intent(activity,BindingActivity.class);
				activity.startActivity(intent);
			}
			else{
				showBookList(false);
			}
		}});
		// 搜索页面
		fabSearch.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			if(curPage == SEARCH_PAGE || isRefreshing) return ;
			showSearchPage();
		}});
		// 检索
		btnSearch.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(isRefreshing) return ;
				key = inputKey.getText().toString();
				if(key == null || key.equals("")){
					inputKey.setError("输入为空!");
					return ;
				}
				inputKey.setError(null);
				pb.setVisibility(View.VISIBLE);
				mList.clear();
				searchAdapter.updateDate(mList);
				lv.getRefreshableView().smoothScrollToPosition(0);
				page=1;
				new Thread(getSearchRun).start();
			}
		});
		// 刷新我的图书
		srl.setOnRefreshListener(new OnRefreshListener(){
			public void onRefresh() {
				if(curPage == BOOK_LIST_PAGE && isRefreshing){
					return ;
				}
				showRefreshResult=true;
				new Thread(getBookRun).start();
			}
		});
		// 加载更多
		lv.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener(){

			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				++page;
				new Thread(getSearchRun).start();
			}
		});
		// 菜单
		view.findViewById(R.id.av).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			mListener.Message(InfoUtils.OPEN_DRAWER);
		}});
	}

	@Override
	public void renew(final BookEntity book) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_renew);
		tvBook = (TextView) w.findViewById(R.id.tv_text);
		String start = "是否续借";
		String end = "?";
		String name = book.getName();
		SpannableString sp = new SpannableString(start+name+end);
		int blackTextColor = context.getResources().getColor(R.color.black_text);
		int specialTextColor = context.getResources().getColor(R.color.blue_500);
		sp.setSpan(new ForegroundColorSpan(blackTextColor), 0, start.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(specialTextColor), start.length(), start.length()+name.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(blackTextColor), start.length()+name.length(), start.length()+name.length()+end.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvBook.setText(sp);
		w.findViewById(R.id.btn_sure).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(isRenewing) return ;
				renewBook = book;
				new Thread(getRenewRun).start();
				dialog.dismiss();
			}
		});
		w.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}


	private void showWisdom(int msg){
		ScaleAnimation animationS = new ScaleAnimation(
				0.2f, 1.0f, 0.2f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 1.0f);
		animationS.setDuration(500);
		animationS.setInterpolator(new DecelerateInterpolator());
		switch(msg){
		case SHOW_FIRST:
			tvFirst.setVisibility(View.VISIBLE);
			tvFirst.startAnimation(animationS);
			break;
		case SHOW_SECOND:
			tvSecond.setVisibility(View.VISIBLE);
			tvSecond.startAnimation(animationS);
			break;
		}
	}
	
	private void showSearchPage(){
		curPage = SEARCH_PAGE;
		sl.scrollTo(0, 0);
		sendMessage(HIDE,null,0);
		sendMessage(SHOW_SEARCH,null,10);
	}
	
	private void showSearchList(){
		curPage = SEARCH_LIST_PAGE;
		searchAdapter.updateDate(mList);
		sendMessage(HIDE,null,0);
		sendMessage(SHOW_SEARCH_LIST,null,10);
	}
	
	private void showBookList(boolean fromNet){
		curPage = BOOK_LIST_PAGE;
		if(!fromNet){
			String json = BaseUtils.getInstance().getLibraryJson();
			bList = new LibraryService().parse2BookList(json);
		}
		bookAdapter.updateDate(bList);
		sendMessage(HIDE,null,0);
		if(bList.size() != 0){
			sendMessage(SHOW_BOOK_LIST,null,10);			
		}
		else{
			sendMessage(SHOW_NO_BOOK,null,10);			
		}
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==REFRESH_SEARCH_SUCCEED || msg.what==REFRESH_SEARCH_FAIL){
				if(lv.isRefreshing()) lv.onRefreshComplete();
				isRefreshing = false;
				pb.setVisibility(View.GONE);
			}
			if(msg.what==REFRESH_BOOK_SUCCEED || msg.what==REFRESH_BOOK_FAIL){
				isRefreshing = false;
				srl.setRefreshing(false);
			}
			switch(msg.what){
			case REFRESH_SEARCH_SUCCEED:
				showSearchList();
				break;
			case REFRESH_SEARCH_FAIL:
				SnackbarManager.show(Snackbar.with(context)
                .text("遇到错误啦")
                .actionLabel("重试")
                .actionColorResource(R.color.yellow_500)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                    	if(!isRefreshing){ 
                    		new Thread(getSearchRun).start();
                    	}
                    }
                }));
				break;
			case REFRESH_BOOK_SUCCEED:
				if(showRefreshResult){
					SnackbarManager.show(Snackbar.with(context).text("刷新成功"));
				}
				showBookList(true);
				break;
			case REFRESH_BOOK_FAIL:
				SnackbarManager.show(Snackbar.with(context).text("遇到错误啦"));
				break;
			case RENEW_SUCCEED:
				isRenewing = false;
				SnackbarManager.show(Snackbar.with(context).text("续借成功"));
				// 刷新图书列表
				showRefreshResult=false;
				new Thread(getBookRun).start();
				break;
			case RENEW_FAIL:
				isRenewing = false;
				SnackbarManager.show(Snackbar.with(context)
		                .text("遇到错误啦")
		                .actionLabel("重试")
		                .actionColorResource(R.color.yellow_500)
		                .actionListener(new ActionClickListener() {
		                    @Override
		                    public void onActionClicked(Snackbar snackbar) {
		                    	if(!isRenewing){ 
		                    		new Thread(getRenewRun).start();
		                    	}
		                    }
		                }));
				break;
			case RENEW_HAD:
				isRenewing = false;
				SnackbarManager.show(Snackbar.with(context).text("已经续借过"));
				break;
			case SHOW_FIRST:
			case SHOW_SECOND:
				showWisdom(msg.what);
				break;
			case SHOW_SEARCH:
				rlSearch.setVisibility(View.VISIBLE);
				inputKey.setVisibility(View.VISIBLE);
				btnSearch.setVisibility(View.VISIBLE);
				break;
			case SHOW_SEARCH_LIST:
				lv.setVisibility(View.VISIBLE);
				sl.setMaxScrollY((int)MetricsConverter.dpToPx(context,130));
				break;
			case SHOW_BOOK_LIST:
				srl.setVisibility(View.VISIBLE);
				lvBook.setVisibility(View.VISIBLE);
				sl.setMaxScrollY((int)MetricsConverter.dpToPx(context,130));
				break;
			case SHOW_NO_BOOK:
				tvNoBook.setVisibility(View.VISIBLE);
				sl.scrollTo(0,0);
				sl.setMaxScrollY(0);
				break;
			case HIDE:
				sl.setMaxScrollY(0);
				srl.setRefreshing(false);
				if(lv.isShown()) lv.setVisibility(View.GONE);
				if(rlSearch.isShown()) rlSearch.setVisibility(View.GONE);
				if(inputKey.isShown()) inputKey.setVisibility(View.GONE);
				if(btnSearch.isShown()) btnSearch.setVisibility(View.GONE);
				if(pb.isShown()) pb.setVisibility(View.GONE);
				if(tvFirst.isShown()) tvFirst.setVisibility(View.GONE);
				if(tvSecond.isShown()) tvSecond.setVisibility(View.GONE);
				if(srl.isShown()) srl.setVisibility(View.GONE);
				if(lvBook.isShown()) lvBook.setVisibility(View.GONE);
				if(tvNoBook.isShown()) tvNoBook.setVisibility(View.GONE);
				break;
			}
		}
	};
	/*
	 * 检索图书
	 */
	Runnable getSearchRun = new Runnable(){
		public void run() {
			isRefreshing = true;
			List<BookEntity> xList = new LibraryService().search(key, page);
			if(xList==null || xList.size()==0){
				sendMessage(REFRESH_SEARCH_FAIL,null,0);
			}
			else{
				for(int i=0;i<xList.size();++i){
					BookEntity e = xList.get(i);
					if(!e.getName().equals("")){
						mList.add(e);
					}
				}
				sendMessage(REFRESH_SEARCH_SUCCEED,null,0);
			}
		}
	};
	
	/*
	 * 获取我的图书
	 */
	Runnable getBookRun = new Runnable(){
		public void run() {
			isRefreshing = true;
			List<BookEntity> xList = new LibraryService().getBook();
			if(xList == null){
				sendMessage(REFRESH_BOOK_FAIL,null,0);
			}
			else{
				bList = xList;
				sendMessage(REFRESH_BOOK_SUCCEED,null,0);
			}
		}
	};
	
	/*
	 * 续借图书
	 */
	Runnable getRenewRun = new Runnable(){
		public void run() {
			isRenewing = true;
			String res = new LibraryService().renewBook(renewBook);
			if(res.equals(InfoUtils.SR_RENEW_SUCCEED)){
				sendMessage(RENEW_SUCCEED,null,0);
			}
			else
			if(res.equals(InfoUtils.SR_RENEW_HAD)){
				sendMessage(RENEW_HAD,null,0);
			}
			else
			if(res.equals(InfoUtils.SR_RENEW_FAIL)){
				sendMessage(RENEW_FAIL,null,0);
			}
		}
	};

	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("LibraryFragment"); 	//统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(getActivity());;         //统计时长
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("LibraryFragment"); 	// （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(getActivity());
	}
}
