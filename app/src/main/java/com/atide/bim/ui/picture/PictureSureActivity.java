package com.atide.bim.ui.picture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by atide on 2016/3/21.
 */
@EActivity(R.layout.picture_sure_layout)
public class PictureSureActivity extends Activity {

    private PhotoViewAttacher mAttacher;
    @Extra
    String photoUri;

    @ViewById(R.id.image)
    ImageView imageView;

    @AfterViews
    void initUI(){

        mAttacher = new PhotoViewAttacher(imageView);

        ImageLoader.getInstance().displayImage("file://"+photoUri, imageView, MyApplication.getOptions(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });
    }
}
