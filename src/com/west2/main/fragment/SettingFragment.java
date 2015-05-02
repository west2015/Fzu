package com.west2.main.fragment;

import java.io.Serializable;

import com.material.widget.CheckBox;
import com.material.widget.DampScrollView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.west2.main.R;
import com.west2.main.activity.AboutActivity;
import com.west2.main.activity.BindingActivity;
import com.west2.main.interfaces.MessageInterface;
import com.west2.main.utils.BaseUtils;
import com.west2.main.utils.InfoUtils;

public class SettingFragment extends Fragment {

	private Context context;
	private Activity activity;
	private MessageInterface mListener;

	private DampScrollView dsv;
	private RelativeLayout toolbarAll;

	private String curStyle;
	private boolean isReload;
	private CheckBox cbWhite,cbBlack;
	
	// �������
	final UMSocialService mController = 
			UMServiceFactory.getUMSocialService("com.umeng.share");

	private String shareUrl;
	private String shareContent;
	private UMImage shareImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		activity = this.getActivity();
		mListener = (MessageInterface) context;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		curStyle = BaseUtils.getInstance().getCustomTheme();
		if(curStyle.equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			context.setTheme(R.style.DarkTheme);
		}
		else{
			context.setTheme(R.style.LightTheme);
		}
		
		View view = inflater.inflate(R.layout.fragment_setting, null);
		findView(view);
		setListener(view);
		initUm();
		return view;
	}

	private void findView(View view) {
		dsv = (DampScrollView) view.findViewById(R.id.dsv);
		toolbarAll = (RelativeLayout) view.findViewById(R.id.toolbar_all);
		cbWhite = (CheckBox) view.findViewById(R.id.cb_white);
		cbBlack = (CheckBox) view.findViewById(R.id.cb_black);
		
		setCheckBox(false);
	}

	private void setListener(View view) {
		cbWhite.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			isReload=false;
			if(curStyle.equals(InfoUtils.SR_SETTING_THEME_BLACK)) isReload=true;
			curStyle=InfoUtils.SR_SETTING_THEME_WHITE;
			setCheckBox(isReload);
		}});
		cbBlack.setOnClickListener(new OnClickListener(){public void onClick(View v) {
			isReload=false;
			if(curStyle.equals(InfoUtils.SR_SETTING_THEME_WHITE)) isReload=true;
			curStyle=InfoUtils.SR_SETTING_THEME_BLACK;
			setCheckBox(isReload);
		}});
		view.findViewById(R.id.btn_binding).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(activity,BindingActivity.class);
				activity.startActivity(intent);
			}
		});
		view.findViewById(R.id.btn_feedback).setOnClickListener(
				new OnClickListener() {
					public void onClick(View arg0) {
						FeedbackAgent agent = new FeedbackAgent(context);
						agent.startFeedbackActivity();
					}
				});
		view.findViewById(R.id.av).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mListener.Message(InfoUtils.OPEN_DRAWER);
			}
		});
		
		view.findViewById(R.id.btn_update).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(activity, "��������", Toast.LENGTH_SHORT).show();
						UmengUpdateAgent.setUpdateAutoPopup(false);
						UmengUpdateAgent.forceUpdate(activity);
						UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
						    @Override
						    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
						        switch (updateStatus) {
						        case UpdateStatus.Yes:
						            UmengUpdateAgent.showUpdateDialog(activity, updateInfo);
						            break;
						        case UpdateStatus.No:
						            Toast.makeText(activity, "�������°汾", Toast.LENGTH_SHORT).show();
						            break;
						        case UpdateStatus.Timeout:
						            Toast.makeText(activity, "��ʱ", Toast.LENGTH_SHORT).show();
						            break;
						        }
						    }
						});
					}
				});
		
		view.findViewById(R.id.btn_share).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mController.openShare(activity, false);
						// ���˻ص� 200�ɹ�
						mController.getConfig().registerListener(new SnsPostListener() {
							@Override
							public void onStart() {
								// TODO Auto-generated method stub
							}
							@Override
							public void onComplete(SHARE_MEDIA arg0, int arg1,SocializeEntity arg2) {
								// TODO Auto-generated method stub
								if (arg1 == 200) {
									Toast.makeText(activity, "����ɹ�", Toast.LENGTH_SHORT).show();
								//	new ShareTask().execute();
//									mHandler.post(shareThread);
								}
							}
						});
					}
				});
		view.findViewById(R.id.btn_about).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(activity,AboutActivity.class);
				activity.startActivity(intent);
			}
		});
	}

	private void setCheckBox(boolean isReload){
		if(curStyle.equals(InfoUtils.SR_SETTING_THEME_BLACK)){
			cbWhite.setChecked(false);
			cbBlack.setChecked(true);
		}
		else{
			cbWhite.setChecked(true);
			cbBlack.setChecked(false);
		}
		BaseUtils.getInstance().setCustomTheme(curStyle);
		if(isReload)
			mListener.Message(InfoUtils.RELOAD);
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	private void showPushDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setView(LayoutInflater.from(context).inflate(
				R.layout.dialog_push, null));
		dialog.show();
		Window w = dialog.getWindow();
		w.setContentView(R.layout.dialog_push);
		// findView
		// initValue
		// setListener
		w.findViewById(R.id.btn_save).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		w.findViewById(R.id.btn_cancel).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	}

	private void sendMessage(int message, Object obj, long delay) {
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		if (obj != null)
			msg.obj = obj;
		mHandler.sendMessageDelayed(msg, delay);
	}

	private void initUm() {

		// mController.getConfig().getSocializeConfig().closeToast();
		// ��������
		shareUrl = this.getString(R.string.share_url);
		shareContent = this.getString(R.string.share_content);
		shareImage = new UMImage(activity, R.drawable.erweima);
		
		mController.getConfig().removePlatform(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.TENCENT);
		// ����Ϊ��ӵ���ữ���
		mController.setShareContent("��ӭ���ظ�������    " + shareUrl);
		// ���÷���ͼƬ������2Ϊ����ͼƬ����Դ����
		mController.setShareMedia(shareImage);
		mController.setAppWebSite(shareUrl);
		mController.setShareContent(shareContent);

//		// ΢������Ȧ
	
		String appID = "wx3094ebc70768f741";
		String appSecret = "f5787e981d93de08b1d3479a3708db99";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID,appSecret);
		wxHandler.addToSocialSDK();
		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID,appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContent + shareUrl);
		circleMedia.setTitle("��������");
		circleMedia.setShareImage(shareImage);
		circleMedia.setTargetUrl(shareUrl);
		mController.setShareMedia(circleMedia);

		// QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqMedia = new QQShareContent();
		qqMedia.setShareContent(shareContent + shareUrl);
		qqMedia.setTargetUrl(shareUrl);
		qqMedia.setShareImage(shareImage);
		qqMedia.setTitle("��������");
		mController.setShareMedia(qqMedia);
		// Qzone
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		QZoneShareContent qZoneShareContent = new QZoneShareContent();
		qZoneShareContent.setShareContent(shareContent + shareUrl);
		qZoneShareContent.setTargetUrl(shareUrl);
		qZoneShareContent.setTitle("��������");
		qZoneShareContent.setShareImage(shareImage);
		mController.setShareMedia(qZoneShareContent);

	//	mController.getConfig().setSsoHandler(new SinaSsoHandler());
		SinaShareContent sinaShareContent = new SinaShareContent();
		sinaShareContent.setShareContent(shareContent + shareUrl);
		sinaShareContent.setTargetUrl(shareUrl);
		sinaShareContent.setTitle("��������");
		sinaShareContent.setShareImage(shareImage);
		mController.setShareMedia(sinaShareContent);

	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Toast.makeText(getActivity(), resultCode, Toast.LENGTH_LONG).show();
	    /**ʹ��SSO��Ȩ����������´��� */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("SettingFragment"); //ͳ��ҳ��(����Activity��Ӧ����SDK�Զ����ã�����Ҫ����д)
	    MobclickAgent.onResume(getActivity());;          //ͳ��ʱ��
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("SettingFragment"); // ������Activity��Ӧ����SDK�Զ����ã�����Ҫ����д����֤ onPageEnd ��onPause ֮ǰ����,��Ϊ onPause �лᱣ����Ϣ 
	    MobclickAgent.onPause(getActivity());
	}
}
