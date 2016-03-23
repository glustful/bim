package com.atide.bim.utils;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by atide on 2016/3/18.
 */
public class PointUtils {
    public static PointF convertPF(RectF displayRect, PointF pointF) {

        PointF p = new PointF();
        p.set(pointF.x * displayRect.width() + displayRect.left, pointF.y * displayRect.height() + displayRect.top);
        return p;
    }

    public static PointF convertPercentPF(RectF displayRect,PointF point){

        float x = point.x, y = point.y;
        if (null != displayRect) {


            // Check to see if the user tapped on the photo
            if (displayRect.contains(x, y)) {

                x = (x - displayRect.left)
                        / displayRect.width();
                y = (y - displayRect.top)
                        / displayRect.height();

            }
        }
        return  new PointF(x,y);
    }
}
