package com.atide.bim.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by atide on 2016/4/13.
 */
public class NoScrollerViewPage extends ViewPager {
    public NoScrollerViewPage(Context context) {
        super(context);
    }

    public NoScrollerViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {

            return false;


    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {

            return false;


    }
}
