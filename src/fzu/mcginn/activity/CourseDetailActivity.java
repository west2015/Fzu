package fzu.mcginn.activity;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import fzu.mcginn.R;
import fzu.mcginn.entity.CourseEntity;
import fzu.mcginn.utils.MetricsConverter;

public class CourseDetailActivity extends Activity{
	
	private final int SHOW_PLACE = 1000;
	private final int SHOW_TEACHER = 1001;
	private final int SHOW_LESSON = 1002;
	private final int SHOW_SCHEDULE = 1003;
	private final int SHOW_FIRST_WORD = 1004;
	private final int SHOW_SECOND_WORD = 1005;
	private final int SHOW_AUTHOR = 1006;

	private Context context;
	
	private int bgColor = -1;
	private CourseEntity ce = null;

	private RelativeLayout rlTitle;
	// Basic Infomation
	private TextView tvName;
	private TextView tvPlace,textPlace;
	private TextView tvTeacher,textTeacher;
	private TextView tvLesson,textLesson;
	private TextView tvSchedule,textSchedule;
	// Wisdom
	private TextView tvFirstWord;
	private TextView tvSecondWord;
	private TextView tvAuthor;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_course_detail);
		
		findView();
		initValue();
		setListener();
	}

	private void findView(){
		rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvPlace = (TextView) findViewById(R.id.tv_place);
		tvTeacher = (TextView) findViewById(R.id.tv_teacher);
		tvLesson = (TextView) findViewById(R.id.tv_lesson);
		tvSchedule = (TextView) findViewById(R.id.tv_schedule);
		textPlace = (TextView) findViewById(R.id.text_place);
		textTeacher = (TextView) findViewById(R.id.text_teacher);
		textLesson = (TextView) findViewById(R.id.text_lesson);
		textSchedule = (TextView) findViewById(R.id.text_schedule);
		tvFirstWord = (TextView) findViewById(R.id.tv_first_word);
		tvSecondWord = (TextView) findViewById(R.id.tv_second_word);
		tvAuthor = (TextView) findViewById(R.id.tv_author);
	}
	
	private void initValue(){
		context = this;
		if(getIntent().getExtras() != null){
			Bundle b = getIntent().getExtras();
			bgColor = b.getInt("color");
			ce = (CourseEntity) b.getSerializable("course");
		}
		// Basic Infomation
		if(bgColor != -1){
			rlTitle.setBackgroundResource(bgColor);
		}
		if(ce != null){
			tvName.setText(ce.getName() != null ? ce.getName() : "");
			tvPlace.setText(ce.getPlace() != null ? ce.getPlace().replace("[", "").replace("]", "") : "");
			tvTeacher.setText(ce.getTeacherName() != null ? ce.getTeacherName() : "");
			tvLesson.setText(ce.getLesson() + "-" + (ce.getLesson() + ce.getLength() -1));
			tvSchedule.setText(ce.getStartWeek() + " ~ " + ce.getEndWeek() + "ÖÜ");
			if(bgColor != -1){
				int color = getResources().getColor(bgColor);
				int a = 255;
				int r = (color >> 16) & 0xff;
				int g = (color >> 8 ) & 0xff;
				int b = (color >> 0 ) & 0xff;
				color = a << 24 | r << 16 | g <<8 | b;
				tvPlace.setTextColor(color);
				tvTeacher.setTextColor(color);
				tvLesson.setTextColor(color);
				tvSchedule.setTextColor(color);
			}
		}
		// Wisdom
		Typeface typeLight = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_light.ttf");
		Typeface typeRegular = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_regular.ttf");
		tvFirstWord.setTypeface(typeRegular);
		tvSecondWord.setTypeface(typeRegular);
		tvAuthor.setTypeface(typeLight);
		initView();
	}
	
	private void initView(){
		// Show Title
		showTitle();
		// Basic Infomation
		sendMessage(SHOW_PLACE,300);
		sendMessage(SHOW_TEACHER,300);
		sendMessage(SHOW_LESSON,300);
		sendMessage(SHOW_SCHEDULE,300);
		// Wisdom
		sendMessage(SHOW_FIRST_WORD,300);
		sendMessage(SHOW_SECOND_WORD,500);
		sendMessage(SHOW_AUTHOR,700);
		
	}
	
	private void setListener(){
		findViewById(R.id.av_close).setOnClickListener(new OnClickListener(){public void onClick(View v) {
			CourseDetailActivity.this.finish();
		}});
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_PLACE:
			case SHOW_TEACHER:
			case SHOW_LESSON:
			case SHOW_SCHEDULE:
				showInfo(msg.what);
				break;
			case SHOW_FIRST_WORD:
			case SHOW_SECOND_WORD:
			case SHOW_AUTHOR:
				showWisdom(msg.what);
				break;
			}
		}
	};
	
	private void showTitle(){
		rlTitle.setVisibility(View.VISIBLE);
		float height = MetricsConverter.dpToPx(context, 128);
		ValueAnimator anim = ValueAnimator.ofFloat(-height, 0);
		anim.setTarget(rlTitle);
		anim.setDuration(1000).start();
		anim.addUpdateListener(new AnimatorUpdateListener(){
			public void onAnimationUpdate(ValueAnimator animation) {
//				rlTitle.setTranslationY((float) animation.getAnimatedValue());
				LayoutParams params = (LayoutParams) rlTitle.getLayoutParams();
				int topMargin = (int) Math.round((float) animation.getAnimatedValue());
				params.setMargins(params.leftMargin, topMargin, params.rightMargin, params.bottomMargin);
				rlTitle.setLayoutParams(params);
			}
		});
//		TranslateAnimation animationY = new TranslateAnimation(
//				Animation.RELATIVE_TO_SELF, 0f,
//				Animation.RELATIVE_TO_SELF, 0f,
//				Animation.RELATIVE_TO_SELF, -1f,
//				Animation.RELATIVE_TO_SELF, 0f);
//		animationY.setDuration(1000);
//		animationY.setInterpolator(new DecelerateInterpolator());
//		rlTitle.startAnimation(animationY);
	}
	
	private void showInfo(int msg){
		AlphaAnimation animationA = new AlphaAnimation(0f, 1f);
		animationA.setDuration(1000);
		animationA.setInterpolator(new DecelerateInterpolator());
		switch(msg){
		case SHOW_PLACE:
			tvPlace.setVisibility(View.VISIBLE);
			textPlace.setVisibility(View.VISIBLE);
			tvPlace.startAnimation(animationA);
			textPlace.startAnimation(animationA);
			break;
		case SHOW_TEACHER:
			tvTeacher.setVisibility(View.VISIBLE);
			textTeacher.setVisibility(View.VISIBLE);
			tvTeacher.startAnimation(animationA);
			textTeacher.startAnimation(animationA);
			break;
		case SHOW_LESSON:
			tvLesson.setVisibility(View.VISIBLE);
			textLesson.setVisibility(View.VISIBLE);
			tvLesson.startAnimation(animationA);
			textLesson.startAnimation(animationA);
			break;
		case SHOW_SCHEDULE:
			tvSchedule.setVisibility(View.VISIBLE);
			textSchedule.setVisibility(View.VISIBLE);
			tvSchedule.startAnimation(animationA);
			textSchedule.startAnimation(animationA);
			break;
		}
	}
	
	private void showWisdom(int msg){
		ScaleAnimation animationS = new ScaleAnimation(
				0.2f, 1.0f, 0.2f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 1.0f);
		animationS.setDuration(500);
		animationS.setInterpolator(new DecelerateInterpolator());
		switch(msg){
		case SHOW_FIRST_WORD:
			tvFirstWord.setVisibility(View.VISIBLE);
			tvFirstWord.startAnimation(animationS);
			break;
		case SHOW_SECOND_WORD:
			tvSecondWord.setVisibility(View.VISIBLE);
			tvSecondWord.startAnimation(animationS);
			break;
		case SHOW_AUTHOR:
			tvAuthor.setVisibility(View.VISIBLE);
			tvAuthor.startAnimation(animationS);
			break;
		}
	}
	
	private void sendMessage(int message, long delayMillis){
		Message msg = mHandler.obtainMessage();
		msg.what = message;
		mHandler.sendMessageDelayed(msg, delayMillis);
	}
}
