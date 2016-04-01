package uk.co.senab.photoview;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * Created by atide on 2016/3/30.
 */
public class BitmapResult {
    private Bitmap bitmap;
    private ImageSize imageSize;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }
}
