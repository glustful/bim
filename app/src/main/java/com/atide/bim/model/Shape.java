package com.atide.bim.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.entity.User;
import com.atide.bim.utils.TimeUtils;
import com.atide.bim.utils.Utils;

import java.util.Date;

/**
 * Created by atide on 2016/3/18.
 */
public abstract  class Shape {
    protected int mColor = Color.RED;
    protected String themeTypeId;
    protected String markId = "-1";
    protected ContentValues contentValues;
    private int _id = -1;
    public abstract void draw(Canvas myCanvas,Paint myPaint,RectF displayRect);

    public abstract void invalidate(PointF pointF);
    public abstract boolean isChecked(PointF pointF);
    public abstract String getName();
    public boolean initData(Cursor cursor){
        try {
            set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            setMarkId(cursor.getString(cursor.getColumnIndex("markId")));
            setThemeTypeId(cursor.getInt(cursor.getColumnIndex("themeTypeId"))+"");
            String color = cursor.getString(cursor.getColumnIndex("color"));
            String[] rgb = color.split(";");
            if (rgb.length == 3) {
                mColor = Color.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public ContentValues getContentValue(){
        if (contentValues == null)
            contentValues = new ContentValues();

        GlobalEntity entity = GlobalEntity.getInstance();
        contentValues.put("markId",markId);
        contentValues.put("markTypeId", Utils.getMarkTypeId(this));
        contentValues.put("partId",entity.getPartId());
        contentValues.put("sectId",entity.getSectId());
        contentValues.put("imageId",entity.getImageId());
        contentValues.put("themeTypeId",themeTypeId);
        contentValues.put("markText","");
        contentValues.put("addUser", User.getLoginUser().getUserId());
        contentValues.put("boundingBox","");
        contentValues.put("color",Color.red(mColor)+";" + Color.green(mColor) + ";" + Color.blue(mColor));
        contentValues.put("createDate", TimeUtils.convertToString(new Date(),"yyyy/MM/dd HH:mm:ss"));
        contentValues.put("upload","0");
        return contentValues;
    }
    public void setContentValues(ContentValues values){
        contentValues = values;
    }
    public void save(){

    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void onDraw(Canvas myCanvas,Paint myPaint,RectF displayRect){
        int tmpColor = myPaint.getColor();
        myPaint.setColor(mColor);
        draw(myCanvas,myPaint,displayRect);
        myPaint.setColor(tmpColor);
    }

    public boolean isEdit(){
        return false;
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public String getThemeTypeId() {
        return themeTypeId;
    }

    public void setThemeTypeId(String themeTypeId) {
        this.themeTypeId = themeTypeId;
    }

    public static enum ShapeType{
        LINE,
        BRUSH,
        ELLIPSE,
        RECTANGLE,
        ARROW,
        NOTICE,
        CAMERA
    }
}
