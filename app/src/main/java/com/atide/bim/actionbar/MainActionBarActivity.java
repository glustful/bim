package com.atide.bim.actionbar;

import com.atide.bim.R;
import com.atide.bim.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActionBarActivity extends AppCompatActivity {
	private LinearLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
	private Toolbar mToolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContentView(R.layout.layout_baseactivity);
		mToolbar = (Toolbar)findViewById(R.id.toolbar);
		mToolbar.setTitle("项目主页");
		setSupportActionBar(mToolbar);

		mToolbar.setLogo(android.R.drawable.ic_menu_add);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 初始化contentview
	 */
	private void initContentView(int layoutResID) {
		ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
		viewGroup.removeAllViews();
		parentLinearLayout = new LinearLayout(this);
		parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
		viewGroup.addView(parentLinearLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);


	}

	@Override
	public void setContentView(int layoutResID) {

		View view = LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);


	}

	@Override
	public void setContentView(View view) {

		parentLinearLayout.addView(view);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {

		parentLinearLayout.addView(view, params);

	}
	/*String showBackStr = null;
	Boolean isShowBackButton = false;
	ActionBar actionBar;

	protected View headView;

	protected Button rightButton;

	protected Button titleButton;

	protected Button backButton;


	LayoutInflater inflater;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.main_action_bar, null);
		backShow();
	}

	private void backShow() {
		if (showHeadView() == null || !showHeadView()) {
			return;
		}
		headView.setVisibility(View.VISIBLE);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		backButton = (Button) headView.findViewById(R.id.leftBtn);
		rightButton = (Button) headView.findViewById(R.id.rightBtn);
		titleButton = (Button) headView.findViewById(R.id.titleBtn);
		backButton.setTextColor(Color.WHITE);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backButtonClick(v);
				//overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});

		titleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				titleButtonClick(v);
			}
		});
		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rightButtonClick(v);
			}
		});
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.LEFT;
		actionBar.setCustomView(headView, lp);
	}

	public void setRightButtonClickable(Boolean clickable) {
		if (rightButton != null) {
			rightButton.setClickable(clickable);
		}
	}

	public Button getCenterButton() {
		return titleButton;
	}

	public Button getRightButton() {
		return rightButton;
	}

	public Button getLeftButton() {
		return backButton;
	}

	public ActionBar getMainActionBar() {
		return actionBar;
	}

	int touchY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {

			activityYMove();

		}

		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	protected void activityYMove() {
		Utils.hiddenSoftBorad(this);
	}

	public abstract void backButtonClick(View v);

	public abstract void titleButtonClick(View v);

	public abstract void rightButtonClick(View v);

	public abstract Boolean showHeadView();*/

}