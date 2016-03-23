package com.atide.bim.ui.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.atide.bim.R;
import com.atide.bim.helper.LayerViewClickHelper;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.NoticeShape;
import com.atide.bim.model.Shape;
import com.atide.bim.ui.CircleView;
import com.atide.bim.ui.dialog.NoticeEditDailog_;
import com.atide.bim.ui.popup.ChoiceShapePopup;
import com.atide.bim.ui.popup.ColorPickerPopup;
import com.atide.bim.ui.popup.ToolsPopup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.LayerView;
import uk.co.senab.photoview.PhotoViewAttacher;

@EActivity(R.layout.image_detail_pager)
public class PictureDetailActivity extends Activity {

	private PhotoViewAttacher mAttacher;
	private String capturePath;
	private Context mContext;
	private CompoundButton checkedBox;
	private LayerViewClickHelper layerViewClickHelper;
	@ViewById(R.id.image)
	LayerView mImageView;

	@Click(R.id.colorPicker)
	void colorPicker(final View view){
		new ColorPickerPopup(this).setColorPickerListener(new ColorPickerPopup.ColorPickerListener() {
			@Override
			public void colorPicker(int color) {
				((CircleView)view).setAppColor(color);
				mImageView.setPenColor(color);
			}
		}).showPopupWindow(view);
	}

	@CheckedChange({R.id.pencil,R.id.brush,R.id.ellipse,R.id.rectangle,R.id.jiantou,R.id.camera,R.id.notice})
	void checkedChangedOnSomeButtons(CompoundButton button, boolean isChicked) {

		if (!button.isChecked()){
			checkedBox = null;
			mAttacher.setAllowHandController(false);
			return;
			//rb.setChecked(false);
		}
		if (checkedBox != null)
			checkedBox.setChecked(false);
		checkedBox = button;
		mImageView.setTag(checkedBox);
		//changeBackground(button);
		mAttacher.setAllowHandController(true);
		switch (button.getId()){
			case R.id.ellipse:

				mAttacher.setType(Shape.ShapeType.ELLIPSE);
				break;
			case R.id.rectangle:
				mAttacher.setType(Shape.ShapeType.RECTANGLE);
				break;
			case R.id.pencil:
				mAttacher.setType(Shape.ShapeType.LINE);
				break;
			case R.id.jiantou:
				mAttacher.setType(Shape.ShapeType.ARROW);
				break;
			case R.id.camera:
				takePhoto();
				break;
			case R.id.brush:
				mAttacher.setType(Shape.ShapeType.BRUSH);
				break;
			case R.id.notice:
				mAttacher.setType(Shape.ShapeType.NOTICE);
				mAttacher.setNoticeCallBack(new NoticeCallBack() {
					@Override
					public void callBack(Shape shape) {
						NoticeEditDailog_.builder().build().setShape((NoticeShape) shape).show(getFragmentManager(), "noticeEdit");
					}
				});

				break;
			default:
				mAttacher.setAllowHandController(false);
				break;
		}

	}

	private void changeBackground(CompoundButton button){
		LinearLayout parent = (LinearLayout)button.getParent();
		for (int i=0;i<parent.getChildCount();i++){
			((CompoundButton)parent.getChildAt(i)).setChecked(false);
		}
		button.setChecked(true);
	}

	@AfterViews
	void initUI(){
		mContext = this;
		mAttacher = new PhotoViewAttacher(mImageView);
		layerViewClickHelper = new LayerViewClickHelper(mImageView);
		mImageView.setAttacher(mAttacher);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				layerViewClickHelper.clickEvent(x,y);
			}
		});

		mAttacher.setAllowHandController(false);
		//mImageView.setImageResource(R.drawable.project_pictrue);
		ImageLoader.getInstance().displayImage("http://pic41.nipic.com/20140521/2531170_143935854000_2.jpg", mImageView, new SimpleImageLoadingListener() {
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



	private void takePhoto(){

		mAttacher.setType(Shape.ShapeType.CAMERA);
		mAttacher.setImageCallBack(new ImageCallBack() {
			public void callBack() {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED))

				{
					Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
					String out_file_path = Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/";
					File dir = new File(out_file_path);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					capturePath = out_file_path + System.currentTimeMillis() + ".jpg";
					getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
					getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					startActivityForResult(getImageByCamera, 1);
				} else

				{
					Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK ) {
			Shape shape = mImageView.getShapes().get(mImageView.getShapes().size()-1);
			if (shape instanceof CameraShape){
				((CameraShape)shape).setPhotoUri(capturePath);
			}
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(new File(capturePath));
			intent.setData(uri);
			sendBroadcast(intent);
			PictureSureActivity_.intent(this).photoUri(capturePath).start();

		}
	}

	public static interface ImageCallBack{
		void callBack();
	}

	public static interface NoticeCallBack{
		void callBack(Shape shape);
	}
}