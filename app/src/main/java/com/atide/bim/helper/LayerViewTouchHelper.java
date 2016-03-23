package com.atide.bim.helper;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import com.atide.bim.model.Arrow;
import com.atide.bim.model.Brush;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.Ellipse;
import com.atide.bim.model.Line;
import com.atide.bim.model.NoticeShape;
import com.atide.bim.model.Rectangle;
import com.atide.bim.model.Shape;
import com.atide.bim.utils.PointUtils;

import uk.co.senab.photoview.LayerView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by atide on 2016/3/18.
 * 作标注时控件LayerView的触摸事件辅助类
 * 处理相关事件操作
 */
public class LayerViewTouchHelper {


    private Shape.ShapeType shapeType;
    private RectF dirtyRect = new RectF();
    private PhotoViewAttacher attacher;
    public LayerViewTouchHelper(PhotoViewAttacher mAttacher){
        this.attacher = mAttacher;
    }

    public Shape.ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(Shape.ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void onTouch(MotionEvent ev){
        switch (shapeType) {
            case CAMERA:
                cameraEvent(ev);

                break;
            case LINE:
                lineEvent(new Line(),ev);
                break;
            case ARROW:
                rectEvent(new Arrow(), ev);
                break;
            case ELLIPSE:
                rectEvent(new Ellipse(),ev);
                break;
            case RECTANGLE:
                rectEvent(new Rectangle(), ev);
                break;
            case NOTICE:
                noticeEvent(ev);

                break;
            case BRUSH:
                lineEvent(new Brush(),ev);
                break;
            default:
                break;
        }


    }

    private void rectEvent(Shape shape,MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                dealWithUpEvent(ev);
                break;
            case MotionEvent.ACTION_DOWN:
                dealWithDownEvent(shape,ev);
                break;
            case MotionEvent.ACTION_MOVE:
                dealWithMoveRectEvent(ev);
                break;
        }
    }

       private void cameraEvent(MotionEvent ev){
        if (ev.getAction() == MotionEvent.ACTION_UP){
            dealWithPointEvent(new CameraShape(),ev);
            if(attacher.getImageCallBack()!=null){
                attacher.getImageCallBack().callBack();
            }
        }
    }

    private void noticeEvent(MotionEvent ev){
        if (ev.getAction() == MotionEvent.ACTION_UP){
            Shape shape = new NoticeShape();
            dealWithPointEvent(shape,ev);
            if(attacher.getNoticeCallBack()!=null){
                attacher.getNoticeCallBack().callBack(shape);
            }
        }
    }

    private void dealWithPointEvent(Shape shape,MotionEvent ev){

        shape.invalidate(PointUtils.convertPercentPF(attacher.getDisplayRect(), new PointF(ev.getX(), ev.getY())));
        ((LayerView)attacher.getImageView()).addShape(shape);
        attacher.getImageView().invalidate();
       // attacher.setmAllowHandController(false);
    }

    private float preX,preY;
    private void lineEvent(Shape shape,MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                dealWithUpEvent(ev);
                break;
            case MotionEvent.ACTION_DOWN:
               dealWithDownEvent(shape,ev);
                break;
            case MotionEvent.ACTION_MOVE:
                dealWithMoveEvent(ev);
                break;
        }
    }

    private void dealWithUpEvent(MotionEvent ev){
        ((LayerView) attacher.getImageView()).addShape(((LayerView) attacher.getImageView()).getCurrentShape());
       // attacher.setmAllowHandController(false);
    }

    private void dealWithDownEvent(Shape shape,MotionEvent ev){
        preX = ev.getX();
        preY = ev.getY();
        shape.invalidate(PointUtils.convertPercentPF(attacher.getDisplayRect(), new PointF(ev.getX(), ev.getY())));
        ((LayerView) attacher.getImageView()).setCurrentShape(shape);
    }

    private  void dealWithMoveEvent(MotionEvent ev){
        resetDirtyRect(preX,preY,ev.getX(), ev.getY());

        int historySize = ev.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            float historicalX = ev.getHistoricalX(i);
            float historicalY = ev.getHistoricalY(i);
            expandDirtyRect(historicalX, historicalY);
            PointF p1 = PointUtils.convertPercentPF(attacher.getDisplayRect(), new PointF(historicalX, historicalY));
            ((LayerView)attacher.getImageView()).getCurrentShape().invalidate(p1);

        }
        PointF p1 = PointUtils.convertPercentPF(attacher.getDisplayRect(), new PointF(ev.getX(), ev.getY()));

        ((LayerView)attacher.getImageView()).getCurrentShape().invalidate(p1);
        attacher.getImageView().invalidate(
                (int) (dirtyRect.left - 30),
                (int) (dirtyRect.top - 30),
                (int) (dirtyRect.right + 30),
                (int) (dirtyRect.bottom + 30));
        preX = ev.getX();
        preY = ev.getY();
    }

    private void dealWithMoveRectEvent(MotionEvent ev){
        PointF p1 = PointUtils.convertPercentPF(attacher.getDisplayRect(), new PointF(ev.getX(), ev.getY()));
        ((LayerView)attacher.getImageView()).getCurrentShape().invalidate(p1);
        attacher.getImageView().invalidate((int)Math.min(preX,ev.getX())-15,(int)Math.min(preY,ev.getY())-15,(int)Math.max(preX, ev.getX())+15,(int)Math.max(preY, ev.getY())+15);
       // attacher.getImageView().invalidate();
    }


    /**
     * Called when replaying history to ensure the dirty region includes all
     * points.
     */
    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float lastTouchX,float lastTouchY,float eventX, float eventY) {

        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }
}
