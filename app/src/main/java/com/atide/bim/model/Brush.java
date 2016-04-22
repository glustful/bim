package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by atide on 2016/3/21.
 */
public class Brush extends Line {
    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {

        float width = myPaint.getStrokeWidth();
        myPaint.setStrokeWidth(5*width);
        super.draw(myCanvas, myPaint, displayRect);
        myPaint.setStrokeWidth(width);
    }

    @Override
    public String getName() {
        return "笔刷";
    }
}
