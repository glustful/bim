package com.atide.bim.factory;

import android.database.Cursor;

import com.atide.bim.model.Arrow;
import com.atide.bim.model.Brush;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.Ellipse;
import com.atide.bim.model.Line;
import com.atide.bim.model.NoticeShape;
import com.atide.bim.model.Rectangle;
import com.atide.bim.model.Shape;

/**
 * Created by atide on 2016/3/29.
 */
public class ShapeFactory {

    public static Shape createShape(Cursor cursor){
        int markTypeId = cursor.getInt(cursor.getColumnIndex("markTypeId"));
        Shape shape = createShape(markTypeId);

        return shape;
    }

    private static Shape createShape(int markTypeId){
        Shape shape = null;
        switch (markTypeId){
            case 0:
                shape = new NoticeShape();
                break;
            case 1:
                shape = new CameraShape();
                break;
            case 2:
                shape = new Brush();
                break;
            case 3:
                shape = new Line();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                shape = new Arrow();
                break;
            case 7:
                shape = new Rectangle();
                break;
            case 8:
                shape = new Ellipse();
                break;
            case 9:
                shape = new Rectangle();
                break;
        }
        return shape;
    }
}
