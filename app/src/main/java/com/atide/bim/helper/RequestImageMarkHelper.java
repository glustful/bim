package com.atide.bim.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.atide.bim.Constant;
import com.atide.bim.MyApplication;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.model.User;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.sqlite.DatabaseManager;
import com.atide.bim.sqlite.SqliteHelper;
import com.atide.bim.ui.picture.PictureSureActivity;
import com.atide.bim.utils.TimeUtils;
import com.atide.bim.utils.Utils;
import com.atide.bim.utils.WebServiceUtils;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ByteDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by atide on 2016/3/30.
 */
@EBean
public class RequestImageMarkHelper {

    private Context mContext;
    private ArrayList<HashMap<String,Object>> dataResult;
    public RequestImageMarkHelper(Context context){
        this.mContext = context;
        dataResult = new ArrayList<>();
    }

    public void getImageMarkInfoEntities(final RequestMarkFinish call){
        new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                if (msg.mCode != 200) {
                    call.finish();
                    return;
                }
                try{
                    JSONArray jsonArray = new JSONObject(msg.mData).optJSONArray("data");
                    if (jsonArray != null && jsonArray.length()>0){
                        preperInsertDatabase(jsonArray);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                call.finish();
            }
        }).setMethodName("GetImageMarkInfoEntities")
                .addParam("partKey", GlobalEntity.getInstance().getPartId())
                .addParam("imageKey",GlobalEntity.getInstance().getImageId())
                .request();
    }

    public static void getMarkNoteBinary(String markinfoid, final PictureSureActivity.LoadDataFinish finish){
        new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                JSONArray jsonArray = null;
                if (msg.mCode == 200) {

                    try {
                        jsonArray = new JSONObject(msg.mData).optJSONArray("data");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish.finish(jsonArray);
            }
        }).setMethodName("GetMarkNoteBinary")
                .addParam("markInfoId", markinfoid)

                .request();
    }

    public void getMarkNoteBinaryFile(String markNoteKey){
        HashMap<String,String> param = new HashMap<>();
        param.put("markNoteKey", markNoteKey);
        HashMap<String,String> header = new HashMap<>();
        header.put("InnerToken", User.getLoginUser().getToken());
        header.put("UserHost", WebServiceUtils.getLocalIpAddress());

        ByteDownloader downloader = new ByteDownloader(DrawingMarkServiceRequest.NAMESPACE, "GetMarkNoteBinaryFile", Constant.mHost + DrawingMarkServiceRequest.Url, "TokenHeader", param, header);
        byte[] data = downloader.download();
        if (data != null){

        }
    }

    /**
     * 准备插入数据库
     * @param jsonArray
     */
    private void preperInsertDatabase(JSONArray jsonArray){
        try {
            GlobalEntity entity = GlobalEntity.getInstance();
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            db.delete(SqliteHelper.TABLE_NAME,"partId=? and sectId=? and imageId=? and upload=?",new String[]{entity.getPartId(),entity.getSectId(),entity.getImageId(),"1"});
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                boolean isImage = false;
                if (object.optString("imagemarktypeid").equals("1")) {
                    isImage = true;
                }
                String markId = object.optString("markinfoid");
               Cursor c= db.rawQuery("select _id from notice where markId=?", new String[]{markId});
                if (c!=null && c.getCount()>0){
                    c.close();
                    continue;
                }
               db.insert(SqliteHelper.TABLE_NAME, null, preperShape(object, isImage));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private ContentValues preperShape(JSONObject object,boolean isImage){
        ContentValues contentValues = new ContentValues();
        contentValues.put("markId",object.optString("markinfoid"));
        contentValues.put("markTypeId", object.optString("imagemarktypeid"));
        contentValues.put("partId",object.optString("partno"));
        contentValues.put("sectId",object.optString("sectno"));
        contentValues.put("imageId",object.optString("drawingimageid"));
        contentValues.put("themeTypeId",object.optString("themetypeid"));
        if (isImage){
            contentValues.put("markText", "http//" + object.optString("markinfoid"));
        }else {
            contentValues.put("markText", object.optString("marktext"));
        }
        contentValues.put("addUser",object.optString("markuserid"));
        contentValues.put("boundingBox",object.optString("boundingbox"));
        contentValues.put("color", object.optString("markcolor"));
        contentValues.put("createDate", object.optString("marktime"));
        contentValues.put("rang",object.optString("markrectangle"));
        contentValues.put("upload","1");
        return contentValues;
    }



    public static interface RequestMarkFinish{
        void finish();
    }

}
