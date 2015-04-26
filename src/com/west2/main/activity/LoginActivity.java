package com.west2.main.activity;

import android.app.Activity;
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

import com.material.widget.InputText;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.ActionSwipeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.west2.main.R;
import com.west2.main.entity.UserEntity;
import com.west2.main.service.LoginService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

public class LoginActivity extends Activity{
	
	private RelativeLayout rlHide;
	private InputText etUsername,etPassword;
	private ProgressBar pb;
	
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
		setContentView(R.layout.activity_login);
		
		findView();
		setListener();
		//����ͳ��
		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
	}

	private void findView(){
		rlHide = (RelativeLayout) findViewById(R.id.rl_hide);
		pb = (ProgressBar) findViewById(R.id.pb);
		etUsername = (InputText) findViewById(R.id.et_username);
		etPassword = (InputText) findViewById(R.id.et_password);
		
		// ��ȡ��������
		UserEntity userEntity = BaseUtils.getInstance().getUserEntity();
		if(userEntity != null){
			change2Activity(MainActivity.class,true);
			etUsername.setText(userEntity.getUsername() != null ? userEntity.getUsername() : "");
			etPassword.setText(userEntity.getPassword() != null ? userEntity.getPassword() : "");
		}

		decorView = getWindow().getDecorView();
		isLogining = false;
	}
	
	private void setListener(){
		// �û���
		etUsername.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(etUsername.getText().toString()!=null && !etUsername.getText().toString().equals("")){
						etUsername.setError(null);
					}
				}
			}
		});
		// ����
		etPassword.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(etPassword.getText().toString()!=null && !etPassword.getText().toString().equals("")){
						etPassword.setError(null);
					}
				}
			}
		});
		// ��¼��ť
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isLogining){
					pb.setVisibility(View.VISIBLE);
					new Thread(loginRun).start();
				}
			}
		});
		// �������������
		rlHide.setOnFocusChangeListener(new OnFocusChangeListener(){
			public void onFocusChange(View v, boolean hasFocus) {
				rlHide.performClick();
			}
		});
		rlHide.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
	 * ��¼�߳�
	 */
	Runnable loginRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			isLogining = true;
			UserEntity user = new UserEntity();
			user.setPassword(etPassword.getText().toString());
			user.setUsername(etUsername.getText().toString());
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			msg.obj = new LoginService().login(user);
			mHandler.sendMessage(msg);
		}
	};
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 2){
				change2Activity((Class)msg.obj,true);
			}
			else
			if(msg.what == 1){
				// ��¼�ɹ� 
				if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_SUCCEED)){
					Message msg2 = new Message();
					msg2.what = 2;
					msg2.obj = MainActivity.class;
					mHandler.sendMessageDelayed(msg2, 1000);
					return ;
				}
				// ��¼ʧ��
				isLogining = false;
				pb.setVisibility(View.GONE);
				
				if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NETERROR)){
					SnackbarManager.show(
	                        Snackbar.with(LoginActivity.this)
	                                .text("����������")
	                                .actionLabel("����")
	                                .actionColorResource(R.color.yellow_500)
	                                .swipeListener(new ActionSwipeListener() {
	                                    @Override
	                                    public void onSwipeToDismiss() {
	                                    	
	                                    }
	                                })
	                                .actionListener(new ActionClickListener() {
	                                    @Override
	                                    public void onActionClicked(Snackbar snackbar) {
	                                    	if(!isLogining){
	                                    		new Thread(loginRun).start();
	                                    	}
	                                    }
	                                }));
				}
				else
				if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_WRONG)){
					SnackbarManager.show(
	                        Snackbar.with(LoginActivity.this)
	                                .text("�û������������")
	                                .swipeListener(new ActionSwipeListener() {
	                                    @Override
	                                    public void onSwipeToDismiss() {
	                                    	
	                                    }
	                                }));
				}
				else
				if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NULL)){
					etUsername.setError("�˺�Ϊ��!");
					etPassword.setError("����Ϊ��!");
				}
				else
				if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NULL_USERNAME)){
					etUsername.setError("�˺�Ϊ��!");
				}
				if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NULL_PASSWORD)){
					etPassword.setError("����Ϊ��!");
				}
			}
		}
	};
	
	private void hideInputMethod(){
		if(decorView != null){
			InputMethodManager mInputMethodManager = (InputMethodManager) 
					this.getSystemService(this.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
		}
	}
	
	
	//����ͳ��
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
