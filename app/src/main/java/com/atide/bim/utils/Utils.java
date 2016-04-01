package com.atide.bim.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.atide.bim.model.Arrow;
import com.atide.bim.model.Brush;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.Ellipse;
import com.atide.bim.model.Line;
import com.atide.bim.model.NoticeShape;
import com.atide.bim.model.Rectangle;
import com.atide.bim.model.Shape;

public class Utils {
	public static final String TAG = "Utils";

	public static void hiddenSoftBorad(Context context) {
		try {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {

		}
	}

	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static String getMarkTypeId(Shape shape){
		if (shape instanceof Arrow){
			return "6";
		}
		if (shape instanceof Brush){
			return "2";
		}
		if(shape instanceof CameraShape){
			return "1";
		}
		if(shape instanceof Ellipse){
			return "8";
		}
		if (shape instanceof NoticeShape){
			return "0";
		}
		if (shape instanceof Rectangle){
			return "7";
		}
		if (shape instanceof Line){
			return "3";
		}
		return "";
	}

}