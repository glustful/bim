package com.atide.bim.actionbar;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.ui.home.SecondHomeActivity_;
import com.atide.bim.ui.popup.HistoryVistorPopup;
import com.atide.bim.ui.popup.ToolsPopup;
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
import android.widget.TextView;

import java.util.HashMap;

public abstract class MainActionBarActivity extends AppCompatActivity {
	private LinearLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
	private Toolbar mToolbar;
	private HashMap<String,Object> item;
	protected TextView rightButton,rightButton1;
	protected TextView titleTextView;

	public abstract String fetchTitle();
	public abstract Activity fetchClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContentView(R.layout.main_action_bar);
		findViewById(R.id.leftBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		rightButton = (TextView)findViewById(R.id.rightBtn);
		rightButton1 = (TextView)findViewById(R.id.rightBtn1);
		titleTextView = ((TextView)findViewById(R.id.titleBtn));
		titleTextView.setText(fetchTitle());
		setRightButtonOnClickListener();
		setTitleButtonOnClickListener();
		MyApplication.getInstance().addHistoryVistor(initHistory(),-1);
		/*mToolbar = (Toolbar)findViewById(R.id.toolbar);
		mToolbar.setTitle("项目主页");
		setSupportActionBar(mToolbar);

		mToolbar.setLogo(android.R.drawable.ic_menu_add);*/

	}

	@Override
	protected void onDestroy() {
		MyApplication.getInstance().getHistory().remove(item);
		super.onDestroy();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}*/

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

	public void setTitleButtonOnClickListener(){

	}

	public void setRightButtonOnClickListener(){
		rightButton.setOnClickListener(historyVistor);
	}
	private HashMap<String,Object> initHistory(){
		item = new HashMap<>();
		item.put("activity", fetchClass());
		item.put("title", fetchTitle());
		return item;
	}

	private OnClickListener historyVistor = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new HistoryVistorPopup(MainActionBarActivity.this).showPopupWindow(v);
		}
	};

}