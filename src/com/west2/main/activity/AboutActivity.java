package com.west2.main.activity;

import com.west2.main.R;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			this.setTheme(R.style.DarkTheme);
		}
		else{
			this.setTheme(R.style.LightTheme);
		}
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_about);
		findView();
		setListener();
	}
	
	private void findView(){
		
	}
	
	private void setListener(){
		findViewById(R.id.av).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			AboutActivity.this.finish();
		}});
	}
	
}
