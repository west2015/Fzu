package com.west2.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.material.widget.InputText;
import com.material.widget.RaisedButton;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.ActionSwipeListener;
import com.west2.main.R;
import com.west2.main.entity.UserEntity;
import com.west2.main.service.LibraryService;
import com.west2.main.service.LoginService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

public class BindingActivity extends Activity{
	private final int LOGIN_SUCCEED = 1000;
	private final int LOGIN_FAIL = 1001;
	
	private Context context;
	
	private RelativeLayout rlHide;
	private InputText etUsername,etPassword;
	private ProgressBar pb;
	private TextView bindUser;
	private RaisedButton btnLogOut;
	
	private View decorView;
	private boolean isLogining;

	public void onCreate(Bundle savedInstanceState){
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			this.setTheme(R.style.DarkTheme);
		}
		else{
			this.setTheme(R.style.LightTheme);
		}

		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_binding);
		
		findView();
		setListener();
	}

	private void findView(){
		rlHide = (RelativeLayout) findViewById(R.id.rl_hide);
		pb = (ProgressBar) findViewById(R.id.pb);
		etUsername = (InputText) findViewById(R.id.et_username);
		etPassword = (InputText) findViewById(R.id.et_password);
		bindUser = (TextView) findViewById(R.id.bind_user);
		btnLogOut = (RaisedButton) findViewById(R.id.btn_log_out);

		context = this;
		decorView = getWindow().getDecorView();
		isLogining = false;		
		
		UserEntity user =  BaseUtils.getInstance().getLibraryUser();
		if(user == null){
			bindUser.setText("未绑定账号");
			btnLogOut.setTextColor(context.getResources().getColor(R.color.white_hint));
		}
		else{
			bindUser.setText("已绑定:"+user.getUsername());
			btnLogOut.setTextColor(context.getResources().getColor(R.color.white_text));
		}
	}
	
	private void setListener(){
		// 解绑
		btnLogOut.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				bindUser.setText("未绑定账号");
				btnLogOut.setTextColor(context.getResources().getColor(R.color.white_hint));
				BaseUtils.getInstance().setLibraryUser(null);
			}
		});
		// 用户名
		etUsername.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(etUsername.getText().toString()!=null && !etUsername.getText().toString().equals("")){
						etUsername.setError(null);
					}
				}
			}
		});
		// 密码
		etPassword.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(etPassword.getText().toString()!=null && !etPassword.getText().toString().equals("")){
						etPassword.setError(null);
					}
				}
			}
		});
		// 绑定
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!isLogining){
					boolean canLogin = true;
					if(etUsername.getText().toString().equals("")){
						canLogin = false;
						etUsername.setError("账号为空!");
					}
					else{
						etUsername.setError(null);
					}
					if(etPassword.getText().toString().equals("")){
						canLogin = false;
						etPassword.setError("密码为空!");
					}
					else{
						etPassword.setError(null);
					}
					if(canLogin){
						pb.setVisibility(View.VISIBLE);
						new Thread(loginRun).start();
					}
				}
			}
		});
		findViewById(R.id.av).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BindingActivity.this.finish();
			}
		});
		// 用于隐藏软键盘
		rlHide.setOnFocusChangeListener(new OnFocusChangeListener(){
			public void onFocusChange(View v, boolean hasFocus) {
				rlHide.performClick();
			}
		});
		rlHide.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				hideInputMethod();
			}
		});
	}
	
	private void change2Activity(Class mClass,boolean isFinish){
		Intent intent = new Intent(this,mClass);
		startActivity(intent);
		if(isFinish){
			this.finish();
		}
	}

	/*
	 * 登录线程
	 */
	Runnable loginRun = new Runnable(){
		@Override
		public void run() {
			isLogining = true;
			UserEntity user = new UserEntity();
			user.setPassword(etPassword.getText().toString());
			user.setUsername(etUsername.getText().toString());
			if(new LibraryService().Login(user)){
				sendMessage(LOGIN_SUCCEED,null,0);
			}
			else{
				sendMessage(LOGIN_FAIL,null,0);
			}
		}
	};

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==LOGIN_SUCCEED || msg.what==LOGIN_FAIL){
				isLogining=false;
				pb.setVisibility(View.GONE);
			}
			switch(msg.what){
			case LOGIN_SUCCEED:
				UserEntity user = BaseUtils.getInstance().getLibraryUser();
				bindUser.setText("已绑定:"+user.getUsername());
				btnLogOut.setTextColor(context.getResources().getColor(R.color.white_text));
				SnackbarManager.show(Snackbar.with(context).text("绑定成功"));
				break;
			case LOGIN_FAIL:
				SnackbarManager.show(Snackbar.with(context)
		                .text("遇到错误啦")
		                .actionLabel("重试")
		                .actionColorResource(R.color.yellow_500)
		                .actionListener(new ActionClickListener() {
		                    @Override
		                    public void onActionClicked(Snackbar snackbar) {
		                    	if(!isLogining){ 
		                    		new Thread(loginRun).start();
		                    	}
		                    }
		                }));

				break;
			}
		}
	};
	
	private void sendMessage(int message,Object obj,long delay){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if(obj != null) msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}
	
	private void hideInputMethod(){
		if(decorView != null){
			InputMethodManager mInputMethodManager = (InputMethodManager) 
					this.getSystemService(this.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
		}
	}
	
}
