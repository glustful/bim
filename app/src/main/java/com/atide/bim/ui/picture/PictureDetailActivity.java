package com.atide.bim.ui.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.helper.LayerViewClickHelper;
import com.atide.bim.helper.RequestImageMarkHelper;
import com.atide.bim.helper.SaveMarkHelper;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.NoticeShape;
import com.atide.bim.model.Shape;
import com.atide.bim.entity.User;
import com.atide.bim.ui.CircleView;
import com.atide.bim.ui.dialog.NoticeEditDailog_;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.bim.ui.message.SendMessageActivity_;
import com.atide.bim.ui.popup.ColorPickerPopup;
import com.atide.bim.ui.popup.HistoryVistorPopup;
import com.atide.bim.utils.WebServiceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;

import uk.co.senab.photoview.LayerView;
import uk.co.senab.photoview.PhotoViewAttacher;

@EActivity(R.layout.image_detail_pager)
public class PictureDetailActivity extends MainActionBarActivity {

	private PhotoViewAttacher mAttacher;
	private String capturePath;
	private Context mContext;
	private CompoundButton checkedBox;
	private static PictureDetailActivity instance;
	private HashMap<String,String> currentTheme;
	private LayerViewClickHelper layerViewClickHelper;
	@Bean
	RequestImageMarkHelper requestImageMarkHelper;
	@Extra
	String partNo;
	@Extra
	String imageKey;
	@Extra
	String imageName;
	@ViewById(R.id.image)
	LayerView mImageView;
	@ViewById(R.id.tools)
	ScrollView tools;
	@ViewById
	ProgressBar loadingBar;

	@Click(R.id.messageSend)
	void messageSend(){
		SendMessageActivity_.intent(mContext).imageKey(imageKey).start();
	}

	@Click(R.id.colorPicker)
	void colorPicker(final View view){
		new ColorPickerPopup(this).setColorPickerListener(new ColorPickerPopup.ColorPickerListener() {
			@Override
			public void colorPicker(int color) {
				((CircleView) view).setAppColor(color);
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
		//new HistoryVistorPopup(mContext).setIsThemeData(true).showPopupWindow(button);

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
		instance = this;

		GlobalEntity.getInstance().setImageId(imageKey);
		mAttacher = new PhotoViewAttacher(mImageView);
		layerViewClickHelper = new LayerViewClickHelper(mImageView);
		mImageView.setAttacher(mAttacher);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				layerViewClickHelper.clickEvent(x, y);
			}
		});

		mAttacher.setAllowHandController(false);
		loadingBar.setVisibility(View.VISIBLE);
		requestImageMarkHelper.getImageMarkInfoEntities(new RequestImageMarkHelper.RequestMarkFinish() {
			@Override
			public void finish() {
				loadImage();
			}
		});


	}

	private void loadImage(){
		HashMap<String,String> param = new HashMap<>();
		param.put("partKey", partNo);
		param.put("imageKey", imageKey);
		HashMap<String,String> header = new HashMap<>();
		header.put("InnerToken", User.getLoginUser().getToken());
		header.put("UserHost", WebServiceUtils.getLocalIpAddress());
		ImageLoader.getInstance().displayImage("http://www.atidesoft.com/DrawingMarkService/", "GetDrawingImageNote", "http://220.164.192.83:9300/Services/DrawingMarkService.asmx", "TokenHeader", param, header, mImageView, MyApplication.getOptions(), null, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {

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
				Object tag = view.getTag(R.id.image_loader_photo_size);
				if (tag instanceof ImageSize) {
					ImageSize size = (ImageSize) tag;
					GlobalEntity.getInstance().setWidth(size.getWidth());
					GlobalEntity.getInstance().setHeight(size.getHeight());
					if (currentTheme != null){
						GlobalEntity.getInstance().setThemeId(currentTheme.get("id"));
					}
					new SaveMarkHelper().read(mImageView);
				}


			}
		}, null);
	}



	private void takePhoto(){
		mAttacher.setType(Shape.ShapeType.CAMERA);
		mAttacher.setImageCallBack(new ImageCallBack() {
			public void callBack() {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED))

				{
					Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
					String out_file_path = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/";
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

	public static PictureDetailActivity getInstance(){
		return instance;
	}

	@Override
	public void setRightButtonOnClickListener() {
		super.setRightButtonOnClickListener();
		rightButton1.setVisibility(View.VISIBLE);
		rightButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tools.getVisibility()==View.VISIBLE){
					Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.back_right_out);
					tools.startAnimation(animation);
					tools.setVisibility(View.GONE);
				}else{
					Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.push_right_in);
					tools.startAnimation(animation);
					tools.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void setTitleButtonOnClickListener() {

		titleTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new HistoryVistorPopup(mContext).setListener(new SendMessageActivity.ThemeChangeListener() {
					@Override
					public void themeChange(HashMap<String, String> theme) {
						if (theme == currentTheme) {
							return;
						}
						currentTheme = theme;
						GlobalEntity.getInstance().setThemeId(theme.get("id"));
						titleTextView.setText(currentTheme.get("name"));
						mImageView.invalidate();
					}
				}).setIsThemeData(true).showPopupWindow(v);
			}
		});

	}

	@Override
	public String fetchTitle() {
		if (MyApplication.getInstance().getThemeDatas()==null || MyApplication.getInstance().getThemeDatas().size()<1){

			return imageName;
		}
		currentTheme = MyApplication.getInstance().getThemeDatas().get(0);

		return currentTheme.get("name");
	}

	@Override
	public Activity fetchClass() {
		return this;
	}

	public static interface ImageCallBack{
		void callBack();
	}

	public static interface NoticeCallBack{
		void callBack(Shape shape);
	}
}