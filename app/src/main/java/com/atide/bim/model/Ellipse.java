package com.atide.bim.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


/**
 * Created by atide on 2016/3/18.
 */
public class Ellipse extends RectShape {

    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {
       super.draw(myCanvas,myPaint,displayRect);

        myCanvas.drawOval(new RectF(lt.x,lt.y,rb.x,rb.y),myPaint);
    }

    @Override
    public String getName() {
        return "Ellipse";
    }
}
