package com.atide.bim.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.utils.PointUtils;

/**
 * Created by atide on 2016/3/18.
 */
public abstract class PointShape extends Shape {
    protected PointF centerPoint;

    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {

        PointF p = PointUtils.convertPF(displayRect, centerPoint);
        int size = (int)(15*myPaint.getStrokeWidth());
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), getIcon()), size, size, true);
        myCanvas.drawBitmap(bitmap,p.x-size/2,p.y-size/2,myPaint);
    }

    @Override
    public void invalidate(PointF pointF) {
        centerPoint = pointF;
    }

    @Override
    public boolean isChecked(PointF pointF) {
        if (Math.abs(pointF.x-centerPoint.x)<=0.01 || Math.abs(pointF.y-centerPoint.y)<=0.01)
            return true;
        return false;
    }

    public abstract int getIcon();
}