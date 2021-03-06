package com.atide.bim;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.atide.bim.entity.ShapeEntity;
import com.atide.bim.exception.CrashHandler;
import com.atide.bim.sqlite.DatabaseManager;
import com.atide.bim.sqlite.SqliteHelper;
import com.atide.bim.utils.WebServiceUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.androidannotations.annotations.EApplication;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@EApplication
public class MyApplication extends Application {
	protected static final String TAG = "MyApplication";
	private static MyApplication mInstance;
	private static DisplayImageOptions mOptions;
	private static ImageLoadingListener mLoadingListener;
	private ArrayList<HashMap<String,Object>> historyVistor;
	private ArrayList<HashMap<String,String>> themeDatas;

	private ArrayList<ShapeEntity> shapeEntities;

	/**
	 * 获取全局Application
	 * @return
	 */
	public static MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		mInstance = this;
		DatabaseManager.initializeInstance(SqliteHelper.getInstance(this));

		initImageLoader();
		WebServiceUtils.downloadShapeInfo();

		super.onCreate();
	}

	private void initImageLoader() {

		File meCacheDir = StorageUtils.getOwnCacheDirectory(this, getPackageName() + "/cache/imageloaderCache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(100 * 1024 * 1024)

				// 100 Mb
				.diskCacheFileCount(200).diskCache(new UnlimitedDiskCache(meCacheDir))// 自定义缓存路径
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove for
																					// release app
				.build();
		ImageLoader.getInstance().init(config);

	}


	/*private List<Activity> mActivityList = new ArrayList<Activity>();

	public void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}

	public Boolean activityTaskStackContains(Activity activity) {
		return mActivityList.contains(activity);
	}*/

	/*public Boolean isActivityStackTop() {
		return mActivityList.size() <= 1 ? true : false;
	}*/

	public String getVersionCode() throws PackageManager.NameNotFoundException {
		PackageManager manager = getPackageManager();
		PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
		String versionName = info.versionName;
		return versionName;
	}

	public static DisplayImageOptions getOptions() {
		if (mOptions == null) {
			mOptions = new DisplayImageOptions.Builder().showImageOnLoading(android.R.drawable.stat_sys_download)
					.showImageForEmptyUri(android.R.drawable.stat_sys_download_done).showImageOnFail(android.R.drawable.stat_sys_download)
					.cacheInMemory(true).cacheOnDisk(true).build();
		}
		return mOptions;
	}

	public static ImageLoadingListener getLoadingListener() {
		if (mLoadingListener == null) {
			mLoadingListener = new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
				}

				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (imageUri.equals(view.getTag().toString())) {
						
						((ImageView) view).setImageBitmap(loadedImage);
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
			};
		}
		return mLoadingListener;
	}

	public DisplayMetrics getDeviceInfo(Activity activity) {
		// 获取屏幕像素相关信息
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public ArrayList<HashMap<String ,Object>> getHistory(){
		return historyVistor;
	}

	public void addHistoryVistor(HashMap<String,Object> item,int index){
		if (historyVistor == null){
			historyVistor = new ArrayList<>();
		}
		if (index == -1){
			historyVistor.add(item);
			return;
		}
		if (historyVistor.size()>index){
			historyVistor.remove(index);
		}

		historyVistor.add(index, item);
	}

	public void backStack(HashMap<String,Object> item){
		if(historyVistor.contains(item)){
			while (true) {
				if (historyVistor.get(historyVistor.size() - 1) != item) {
					((Activity) historyVistor.remove(historyVistor.size() - 1).get("activity")).finish();
				}else {
					return;
				}
			}
		}


	}

	public ArrayList<ShapeEntity> getShapeEntities() {
		return shapeEntities;
	}

	public void setShapeEntitie(ShapeEntity shapeEntity) {
		if (shapeEntities == null){
			shapeEntities = new ArrayList<ShapeEntity>();
		}
		shapeEntities.add(shapeEntity);
	}

	public ArrayList<HashMap<String,String>> getThemeDatas() {
		return themeDatas;
	}

	public void setThemeDatas(ArrayList themeDatas) {
		this.themeDatas = themeDatas;
	}
}