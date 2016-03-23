package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.utils.PointUtils;

/**
 * Created by atide on 2016/3/21.
 */
public class RectShape extends Shape {
    private PointF leftTop,rightBottom;
    protected PointF lt,rb;


    public RectShape(){
        lt = new PointF();
        rb = new PointF();
    }

    public PointF getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(PointF leftTop) {
        this.leftTop = leftTop;
    }

    public PointF getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(PointF rightBottom) {
        this.rightBottom = rightBottom;
    }

    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {

        if (leftTop==null || rightBottom==null)
            return;
        initPoint(displayRect);

      //  myCanvas.drawRect(new RectF(lt.x, lt.y, rb.x, rb.y), myPaint);

    }

    @Override
    public void invalidate(PointF pointF) {
        if (leftTop == null)
            leftTop = pointF;
        else {
                rightBottom = pointF;
        }
    }

    private void initPoint(RectF displayRect){
        PointF p1 = PointUtils.convertPF(displayRect, leftTop);
        PointF p2 = PointUtils.convertPF(displayRect,rightBottom);
        lt.x = Math.min(p1.x,p2.x);
        lt.y = Math.min(p1.y,p2.y);
        rb.x = Math.max(p1.x, p2.x);
        rb.y = Math.max(p1.y,p2.y);
    }

    @Override
    public boolean isChecked(PointF pointF) {
        //pointF = PointUtils.con
        if (pointF.x >= Math.min(leftTop.x,rightBottom.x) && pointF.x <= Math.max(leftTop.x,rightBottom.x) && pointF.y >= Math.min(leftTop.y,rightBottom.y) && pointF.y <= Math.max(leftTop.y,rightBottom.y))
            return true;
        return false;
    }


}
