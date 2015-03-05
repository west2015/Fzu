package fzu.mcginn.activity;

import com.material.widget.InputText;

import fzu.mcginn.R;
import fzu.mcginn.utils.InfoUtils;
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

public class LoginActivity extends Activity{
	
	private RelativeLayout rlHide;
	private InputText etUsername,etPassword;
	private ProgressBar pb;
	
	private View decorView;
	private boolean isLogining;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_login);
		
		findView();
		setListener();
	}

	private void findView(){
		rlHide = (RelativeLayout) findViewById(R.id.rl_hide);
		pb = (ProgressBar) findViewById(R.id.pb);
		etUsername = (InputText) findViewById(R.id.et_username);
		etPassword = (InputText) findViewById(R.id.et_password);

		decorView = getWindow().getDecorView();
		isLogining = false;
	}
	
	private void setListener(){
		// ÓÃ»§Ãû
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
		// ÃÜÂë
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
		// µÇÂ¼°´Å¥
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		// ÓÃÓÚÒþ²ØÈí¼üÅÌ
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
	
	private boolean isLegalInput(){
		boolean isOk = true;
		if(etUsername.getText().toString() == null || etUsername.getText().toString().equals("")){
			etUsername.setError("ÕËºÅÎª¿Õ");
			isOk = false;
		}
		else
			etUsername.setError(null);
		
		if(etPassword.getText().toString() == null || etPassword.getText().toString().equals("")){
			etPassword.setError("ÃÜÂëÎª¿Õ");
			isOk = false;
		}
		else
			etPassword.setError(null);
		
		return isOk;
	}
	
	/*
	 * µÇÂ¼Ïß³Ì
	 */
	Runnable loginRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			isLogining = true;
				
			isLogining = false;
		}
	};
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_SUCCEED)){
				change2Activity(MainActivity.class,true);
			}
			else
			if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NETERROR)){
				
			}
			else
			if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_WRONG)){
				
			}
			else
			if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NULL)){
				etUsername.setError("ÕËºÅÎª¿Õ!");
				etPassword.setError("ÃÜÂëÎª¿Õ!");
			}
			else
			if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NULL_USERNAME)){
				etUsername.setError("ÕËºÅÎª¿Õ!");
			}
			if(msg.obj.toString().equals(InfoUtils.SR_LOGIN_NULL_PASSWORD)){
				etPassword.setError("ÃÜÂëÎª¿Õ!");
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
}
