package com.west2.main.activity;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.umeng.analytics.MobclickAgent;
import com.west2.main.R;
import com.west2.main.entity.JWCNoticeEntity;
import com.west2.main.service.JWCNoticeService;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;
import com.west2.main.utils.MetricsConverter;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class JwcNoticeDetatilActivity extends Activity {

	
	private Context context;
	private RelativeLayout rlBar;
	private RelativeLayout rlTitle;
	private TextView tvTitle;
	private WebView mWebView;
	
	private JWCNoticeEntity mEntity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(BaseUtils.getInstance().getCustomTheme().equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			this.setTheme(R.style.DarkTheme);
		}
		else{
			this.setTheme(R.style.LightTheme);
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwc_notice_detatil);
		context= this;
		findView();
		init();
		setListener();
	}

	
	private void findView(){
		rlBar = (RelativeLayout) findViewById(R.id.rl_bar);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		mWebView = (WebView) findViewById(R.id.webview);
	}
	
	private void init(){
		
		WebSettings settings= mWebView.getSettings(); 
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		
		
		mEntity = (JWCNoticeEntity) this.getIntent().getExtras().get("entity");
		if(mEntity.getTitle()!=null)
		tvTitle.setText(mEntity.getTitle());
		show();
		new GetHtmlTask().execute();
		
	}
	
	private void setListener() {
		findViewById(R.id.av_back).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				JwcNoticeDetatilActivity.this.finish();
			}
		});
		
		findViewById(R.id.btn_openinexplore).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEntity.getUrl()));
	             startActivity(intent);
			}
		});
	}
	
	private void show(){
		
		rlBar.setVisibility(View.VISIBLE);
		float height = MetricsConverter.dpToPx(context, 128);
		ValueAnimator anim = ValueAnimator.ofFloat(-height, 0);
		anim.setTarget(rlBar);
		anim.setDuration(1000).start();
		anim.addUpdateListener(new AnimatorUpdateListener(){
			public void onAnimationUpdate(ValueAnimator animation) {
				LayoutParams params = (LayoutParams) rlBar.getLayoutParams();
				int topMargin = (int) Math.round((float) animation.getAnimatedValue());
				params.setMargins(params.leftMargin, topMargin, params.rightMargin, params.bottomMargin);
				rlBar.setLayoutParams(params);
			}
		});
		findViewById(R.id.scrollview).setVisibility(View.VISIBLE);
		AlphaAnimation animation = new AlphaAnimation(0f, 1f);
		animation.setStartOffset(1000);
		animation.setDuration(1000);
		animation.setStartTime(1000);
		animation.setInterpolator(new DecelerateInterpolator());
		findViewById(R.id.scrollview).startAnimation(animation);

	}
	
	
	
	class GetHtmlTask extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result!=null && result.length()>10){
				mWebView.loadDataWithBaseURL("", result, "text/html", "utf-8", "");
			}
			else{
				SnackbarManager.show(Snackbar.with(context)
                      	.text("”ˆµΩ¥ÌŒÛ¿≤")
                        .actionLabel("÷ÿ ‘")
                        .actionColorResource(R.color.yellow_500)
                        .actionListener(new ActionClickListener() {
                            public void onActionClicked(Snackbar snackbar) {
                            	
                            	new GetHtmlTask().execute();
                            }
                        }));
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(mEntity!=null&&mEntity.getUrl()!=null){
				String detailHtml = JWCNoticeService.getDetatilHtml(mEntity.getUrl());
				mEntity.setDetailHtml(detailHtml);
				return detailHtml;
			}
			return null;
		}
		
	
	}
	
	//”—√ÀÕ≥º∆
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
