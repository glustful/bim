package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.utils.PointUtils;

/**
 * Created by atide on 2016/3/16.
 */
public class Arrow extends Shape{
    private PointF startP,endP;

    public PointF getStartP() {
        return startP;
    }

    public void setStartP(PointF startP) {
        this.startP = startP;
    }

    public PointF getEndP() {
        return endP;
    }

    public void setEndP(PointF endP) {
        this.endP = endP;
    }

    @Override
    public void draw(Canvas myCanvas,Paint myPaint,RectF displayRect) {

        if (this.endP == null)
            return;
        drawAL(myCanvas,myPaint,displayRect);
    }

    @Override
    public void invalidate(PointF pointF) {
        if (startP == null){
            startP = pointF;
        }else {
            setEndP(pointF);
        }
    }



    /**
     * 画箭头
     *
     */
    public void drawAL(Canvas myCanvas,Paint myPaint,RectF displayRect)
    {
        PointF start = PointUtils.convertPF(displayRect, getStartP());
        PointF end = PointUtils.convertPF(displayRect, getEndP());
        float sx = start.x;
        float sy = start.y;
        float ex = end.x;
        float ey = end.y;
        double H = 36*myPaint.getStrokeWidth()/5; // 箭头高度
        double L = 16*myPaint.getStrokeWidth()/5; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线
        myPaint.setStyle(Paint.Style.FILL);
        myCanvas.drawLine(sx, sy, ex, ey,myPaint);
        Path triangle = new Path();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.close();
        myCanvas.drawPath(triangle, myPaint);
        myPaint.setStyle(Paint.Style.STROKE);

    }

    // 计算
    public double[] rotateVec(float px, float py, double ang, boolean isChLen, double newLen)
    {
        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

    @Override
    public boolean isChecked(PointF pointF) {
        float allow = 0.01f;


        double destins = Math.sqrt(Math.pow(endP.y - pointF.y, 2) + Math.pow(endP.x - pointF.x, 2)) + Math.sqrt(Math.pow(startP.y - pointF.y, 2) + Math.pow(startP.x -pointF.x, 2));

        double dis = Math.sqrt(Math.pow(endP.y - startP.y, 2) + Math.pow(startP.x - endP.x, 2));

        dis = Math.abs(dis - destins);

        if(dis <= allow){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getName() {
        return "Arrow";
    }
}
