package com.atide.bim.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.atide.bim.R;

/**
 * Created by atide on 2016/3/21.
 */
public class CircleView extends View {
    int mColor;
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Circle_View_style);
        mColor = ta.getColor(R.styleable.Circle_View_style_circle_color,Color.RED);
        ta.recycle();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint pen = new Paint();
        pen.setColor(Color.WHITE);
        pen.setStyle(Paint.Style.STROKE);
        pen.setAntiAlias(true);
        pen.setDither(true);
        pen.setStrokeWidth(10);
        int size = Math.min(getWidth()/2,getHeight()/2);
        canvas.drawCircle(size, size, size - 5, pen);
        pen.setColor(mColor);
        pen.setStyle(Paint.Style.FILL);
        canvas.drawCircle(size, size, size - 10, pen);
    }

    public void setAppColor(int color){
        mColor = color;
        invalidate();
    }
}
