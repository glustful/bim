package com.atide.bim.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.MyApplication;
import com.atide.bim.MyApplication_;
import com.atide.bim.R;
import com.atide.bim.utils.PointUtils;

/**
 * Created by atide on 2016/3/18.
 */
public class CameraShape extends PointShape {
    private String photoUri;
    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }



    @Override
    public void draw(Canvas myCanvas, Paint myPaint, RectF displayRect) {
        super.draw(myCanvas,myPaint,displayRect);
       // PointF p = PointUtils.convertPF(displayRect, centerPoint);

       // myCanvas.drawBitmap(BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.drawable.camera),p.x,p.y,myPaint);
    }

    @Override
    public int getIcon() {
        return R.drawable.camera;
    }


}
