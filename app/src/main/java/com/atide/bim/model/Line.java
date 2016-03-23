package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.utils.PointUtils;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/18.
 */
public class Line extends Shape {
    private ArrayList<PointF> dots;
    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {
        drawLine(myCanvas,myPaint,displayRect);
    }

    @Override
    public void invalidate(PointF pointF) {
        if (dots == null)
            dots = new ArrayList<>();
        dots.add(pointF);
    }



    private void drawLine(Canvas canvas,Paint pen, RectF rectF) {

        if (dots == null || dots.size() < 2)
            return;
        Path path = new Path();
        PointF startP = PointUtils.convertPF(rectF, dots.get(0));
        path.moveTo(startP.x, startP.y);
        float preX = startP.x, preY = startP.y;
        for (PointF p : dots) {
            p = PointUtils.convertPF(rectF, p);
            path.quadTo(preX, preY, p.x, p.y);
            preX = p.x;
            preY = p.y;
        }
        canvas.drawPath(path, pen);
    }

    @Override
    public boolean isChecked(PointF pointF) {
        for (PointF p : dots){
            if  (Math.abs(pointF.x-p.x)<=0.01 && Math.abs(pointF.y-p.y)<=0.01)
                return true;
        }
        return false;
    }
}