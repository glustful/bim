package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by atide on 2016/3/18.
 */
public abstract  class Shape {
    protected int mColor = Color.RED;
    public abstract void draw(Canvas myCanvas,Paint myPaint,RectF displayRect);

    public abstract void invalidate(PointF pointF);
    public abstract boolean isChecked(PointF pointF);
    public abstract String getName();
    public void save(){

    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public void onDraw(Canvas myCanvas,Paint myPaint,RectF displayRect){
        int tmpColor = myPaint.getColor();
        myPaint.setColor(mColor);
        draw(myCanvas,myPaint,displayRect);
        myPaint.setColor(tmpColor);
    }

    public boolean isEdit(){
        return false;
    }

    public static enum ShapeType{
        LINE,
        BRUSH,
        ELLIPSE,
        RECTANGLE,
        ARROW,
        NOTICE,
        CAMERA
    }
}
