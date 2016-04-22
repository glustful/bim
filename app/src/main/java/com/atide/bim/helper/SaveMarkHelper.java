package com.atide.bim.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.factory.ShapeFactory;
import com.atide.bim.model.Shape;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.request.PrimaryKeyServiceRequest;
import com.atide.bim.sqlite.DatabaseManager;
import com.atide.bim.sqlite.SqliteHelper;
import com.atide.bim.utils.ImageSizeHelper;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import uk.co.senab.photoview.LayerView;

/**
 * Created by atide on 2016/3/29.
 */
@EBean
public class SaveMarkHelper {


    /**
     * 保存标注信息，参数
     */
    @Background
    public void saveMarkInfos(){
        try {
            SQLiteDatabase  db = DatabaseManager.getInstance().openDatabase();
           Cursor cursor = db.query(SqliteHelper.TABLE_NAME, new String[]{}, "upload=? and markId=?", new String[]{"0", "-1"}, null, null, null);
            if (cursor==null || cursor.getCount()<1)
                return;
            ArrayList<String> soapObject = getMarkInfoKey1("GetMarkInfoKey", cursor.getCount());
            if (soapObject==null)
                return;
            int i=0;
            String delMarkId = "";
            Cursor c = db.query(SqliteHelper.TABLE_NAME, new String[]{"markId"}, "upload=? and markId<>?", new String[]{"2", "-1"}, null, null, null);
            if (c != null && c.getCount()>0){
                while (c.moveToNext()){
                    delMarkId += c.getString(0) +",";
                }
                delMarkId = delMarkId.substring(0,delMarkId.length()-1);
                c.close();
            }


            while (cursor.moveToNext()){

               String markId = soapObject.get(i);
                uploadMarkInfo(cursor, markId, delMarkId);
                delMarkId = "";
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            try {
                DatabaseManager.getInstance().closeDatabase();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 保存标注附件信息，参数
     */

    public void saveMarkNotes(Cursor cursor,String markId){
        try {
            int markType = cursor.getInt(cursor.getColumnIndex("markTypeId"));
            if (markType != 1)
                return;
            String uri = cursor.getString(cursor.getColumnIndex("markText"));
            if (uri == null || uri.equals("") || uri.startsWith("http//"))
                return;

            ArrayList<String> soapObject = getMarkInfoKey1("GetMarkNoteKey", 1);
            if (soapObject==null)
                return;

                String markNoteId = soapObject.get(0);
                uploadMarkNote(cursor, markId,markNoteId, "");


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Background
    public void save(ArrayList<Shape> shapes,ArrayList<Shape> deleteShapes){
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (deleteShapes != null && deleteShapes.size() > 0) {
                db.beginTransaction();
                for (Shape shape:deleteShapes) {
                    if (shape.get_id() != -1) {
                        if (shape.getMarkId().equals("-1")){
                            db.delete(SqliteHelper.TABLE_NAME, "_id=?", new String[]{String.valueOf(shape.get_id())});
                        }else {

                            db.execSQL("update notice set upload=? where _id=?", new Object[]{"2", shape.get_id()});
                        }
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            }

            if (shapes == null || shapes.size() < 1)
                return;
            db.beginTransaction();
            for (Shape shape:shapes) {
                if (shape.get_id() == -1){
                    //db.update(SqliteHelper.TABLE_NAME,shape.getContentValue(),"_id=?",new String[]{String.valueOf(shape.get_id())});

                    long result = db.insert(SqliteHelper.TABLE_NAME, null,shape.getContentValue());
                    System.out.println(result);
                }

            }

            db.setTransactionSuccessful();
            db.endTransaction();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                DatabaseManager.getInstance().closeDatabase();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Background
    public void read(LayerView view){
        try{

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String sql = "select *from notice where sectId=? and partId=? and imageId=? and upload<>?";

            GlobalEntity entity = GlobalEntity.getInstance();
            /*if (entity.getThemeId()!=null && !entity.getThemeId().equals("")){
                sql += " and themeTypeId=" + entity.getThemeId();
            }*/
            Cursor cursor = db.rawQuery(sql,new String[]{entity.getSectId(),entity.getPartId(),entity.getImageId(),"2"});

            if (cursor==null || cursor.getCount()<1)
                return;
            ArrayList<Shape> shapes = initShapes(cursor);
            view.setShapes(shapes);
            cursor.close();
        }catch (Exception e){

        }finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private ArrayList<Shape> initShapes(Cursor cursor){
        ArrayList<Shape> shapes = new ArrayList<>();
        while (cursor.moveToNext()){
            Shape shape = ShapeFactory.createShape(cursor);
            if (shape == null)
                continue;
            if(shape.initData(cursor)) {
                shapes.add(shape);
            }
        }

        return shapes;
    }

    /**
     * 获取主键
     * @param nums
     * @return
     */
    public ArrayList<String> getMarkInfoKey1(String methodName,int nums){
        SoapObject result = (SoapObject)new PrimaryKeyServiceRequest().setMethodName(methodName).request(nums);
        ArrayList<String> results = new ArrayList<>();
        for (int i=0;i<result.getPropertyCount();i++){
            SoapObject child = (SoapObject)result.getProperty(i);
            for (int j=0;j<child.getPropertyCount();j++) {
                results.add(child.getProperty(j).toString());
            }
        }
        return results;
    }

    /**
     * 上传标注信息，不含图片
     * @param cursor
     * @param markId
     * @param delMarkId
     */
   private void uploadMarkInfo(final Cursor cursor,final String markId,final String delMarkId){
       String json = initStringFromCursor(cursor, markId);

        WsResponseMessage responseMessage = new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {

            }
        }).addParam("json", json)
                .addParam("delMarkInfoIds",delMarkId)
                .setMethodName("SaveMarkInfos")
                .request2();

       if (responseMessage.mCode != 200)
           return;
       saveMarkNotes(cursor, markId);
       SQLiteDatabase
               db = DatabaseManager.getInstance().openDatabase();
       db.execSQL("update notice set upload=?,markId=? where _id=?",new Object[]{"1",markId,cursor.getInt(cursor.getColumnIndex("_id"))});
       if (delMarkId!=null && !delMarkId.equals("")){
           db.delete(SqliteHelper.TABLE_NAME,"upload=?",new String[]{"2"});
       }
       DatabaseManager.getInstance().closeDatabase();

   }

    /**
     * 上传附件信息
     * 目前不支持删除
     * @param cursor
     * @param markId
     * @param markNoteId
     * @param delMarkId
     */
    private void uploadMarkNote(final Cursor cursor,final String markId,String markNoteId,String delMarkId) throws Exception{
       String uri = cursor.getString(cursor.getColumnIndex("markText"));
        String json = initNoteStringFromCursor(cursor, markId, markNoteId, uri);
        String noteString = new String(Base64.encode(ImageSizeHelper.resetImageSize(uri),Base64.DEFAULT));
        WsResponseMessage msg = new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {


            }
        }).addParam("json", json)
                .addParam("delMarkNoteIds",delMarkId)
                .setMethodName("SaveMarkNotes")
                .addParam("noteString",noteString)
                .request2();
        System.out.println(cursor);
        if (msg.mCode != 200)
            return;

    }

    private String initStringFromCursor(Cursor cursor,String markId){
        int typeId = cursor.getInt(cursor.getColumnIndex("markTypeId"));

        String json = "{";
        json += "\"markinfoid\":\"" + markId + "\",";
        json += "\"imagemarktypeid\":\"" + typeId + "\",";
        json += "\"drawingimageid\":\"" + cursor.getInt(cursor.getColumnIndex("imageId")) + "\",";
        json += "\"partno\":\"" + cursor.getInt(cursor.getColumnIndex("partId")) + "\",";
        json += "\"sectno\":\"" + cursor.getInt(cursor.getColumnIndex("sectId")) + "\",";
        json += "\"markrectangle\":\"" + cursor.getString(cursor.getColumnIndex("rang")) + "\",";
        json += "\"markuserid\":\"" + cursor.getString(cursor.getColumnIndex("addUser")) + "\",";
        json += "\"marktime\":\"" + cursor.getString(cursor.getColumnIndex("createDate")) + "\",";
        json += "\"markcolor\":\"" + cursor.getString(cursor.getColumnIndex("color")) + "\",";
        json += "\"boundingbox\":\"" + cursor.getString(cursor.getColumnIndex("boundingBox")) + "\",";
        json += "\"marktext\":\"" + (typeId==1?"":cursor.getString(cursor.getColumnIndex("markText"))) + "\",";
        json += "\"themetypeid\":\"" + cursor.getInt(cursor.getColumnIndex("themeTypeId")) + "\"}";

        return json;
    }

    private String initNoteStringFromCursor(Cursor cursor,String markId,String markNoteId,String uri) throws Exception{

        File file = new File(uri);
        String json = "{";
        json += "\"marknoteid\":\"" + markNoteId + "\",";
        json += "\"markinfoid\":\"" + markId + "\",";
        json += "\"drawingimageid\":\"" + cursor.getInt(cursor.getColumnIndex("imageId")) + "\",";
        json += "\"partno\":\"" + cursor.getInt(cursor.getColumnIndex("partId")) + "\",";
        json += "\"marknote\":\"" + "" + "\",";
        json += "\"marknotethumbnail\":\"" + "" + "\",";
        json += "\"marknotesize\":\"" + file.length() + "\",";
        json += "\"thumbnailsize\":\"" + "" + "\",";
        json += "\"creationtime\":\"" + "" + "\",";
        json += "\"lastwritetime\":\"" + cursor.getString(cursor.getColumnIndex("createDate")) + "\",";
        json += "\"marknotetitle\":\"" + "现场照片.jpg" + "\",";
        json += "\"marknotefilename\":\"" + file.getName() + "\",";
        json += "\"marknoteext\":\"" + ".jpg" + "\",";
        json += "\"marknoteremarks\":\"" + "" + "\"}";
        return json;
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(File file){
        byte[] buffer = null;
        try {


            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
