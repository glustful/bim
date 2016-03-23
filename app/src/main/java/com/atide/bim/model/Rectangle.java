package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.utils.PointUtils;

/**
 * Created by atide on 2016/3/21.
 */
public class Rectangle extends RectShape {



    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {
        super.draw(myCanvas,myPaint,displayRect);

        myCanvas.drawRect(new RectF(lt.x,lt.y,rb.x,rb.y),myPaint);


    }


}
