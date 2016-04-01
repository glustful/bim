package com.atide.bim.ui.picture;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.atide.bim.Constant;
import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.helper.RequestImageMarkHelper;
import com.atide.bim.model.User;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.utils.WebServiceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by atide on 2016/3/21.
 */
@EActivity(R.layout.picture_sure_layout)
public class PictureSureActivity extends Activity {

    @Extra
    String photoUri;

    @ViewById(R.id.viewPager)
    ViewPager mViewPager;
    @ViewById
    ProgressBar loadingBar;
    List<ImageView> imageViews;
    ImageAdapter adapter;
    boolean isLocal = false;

    @AfterViews
    void initUI() {
        adapter = new ImageAdapter(this);
        mViewPager.setAdapter(adapter);
        if (photoUri == null)
            return;
        if (photoUri.startsWith("http//")){
            isLocal = false;
            loadingBar.setVisibility(View.VISIBLE);
            RequestImageMarkHelper.getMarkNoteBinary(photoUri.replace("http//",""),new LoadDataFinish(){
                @Override
                public void finish(JSONArray jsonArray) {
                    loadingBar.setVisibility(View.GONE);
                    if (jsonArray==null || jsonArray.length()<1)
                        return;
                    adapter.reload(jsonArray);
                }
            });
        }else{
            isLocal = true;
            adapter.reload();
        }


    }


    class ImageAdapter extends PagerAdapter {
        Context context;




        public void reload(JSONArray jsonArray){
            for (int i = 0; i < jsonArray.length(); i++) {
                ImageView image = new ImageView(context);
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
               image.setTag(jsonArray.optJSONObject(i).optString("marknoteid"));

                imageViews.add(image);

            }
            notifyDataSetChanged();
        }

        public void reload(){
            ImageView image = new ImageView(context);



            imageViews.add(image);
            notifyDataSetChanged();
        }

        public ImageAdapter(Context context) {
            this.context = context;
            imageViews = new ArrayList<ImageView>();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            ImageView imageView = imageViews.get(position);
            if (isLocal){
                //使图片实现可以放大缩小的功能
                final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
                ImageLoader.getInstance().displayImage("file://"+photoUri, imageView, MyApplication.getOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        loadingBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        loadingBar.setVisibility(View.GONE);
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
                        loadingBar.setVisibility(View.GONE);
                        mAttacher.update();
                    }
                });
            }else {
                HashMap<String, String> param = new HashMap<>();
                param.put("markNoteKey", imageView.getTag().toString());
                HashMap<String, String> header = new HashMap<>();
                header.put("InnerToken", User.getLoginUser().getToken());
                header.put("UserHost", WebServiceUtils.getLocalIpAddress());
                //使图片实现可以放大缩小的功能
                final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
                ImageLoader.getInstance().displayImage(DrawingMarkServiceRequest.NAMESPACE, "GetMarkNoteBinaryFile", Constant.mHost + DrawingMarkServiceRequest.Url, "TokenHeader", param, header, imageView, MyApplication.getOptions(), null, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mAttacher.update();
                    }
                }, null);
            }
            return imageViews.get(position);
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    public static interface LoadDataFinish{
        void finish(JSONArray jsonArray);
    }
}
