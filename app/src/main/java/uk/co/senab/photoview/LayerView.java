package uk.co.senab.photoview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.atide.bim.model.Shape;
import java.util.ArrayList;

/**
 * Created by atide on 2016/3/15.
 */
public class LayerView extends ImageView {

    private PhotoViewAttacher mAttacher;
    private Paint pen;

    private ArrayList<Shape> mShapes;
    private Shape mCurrentShape;

    public Shape getCurrentShape() {
        return mCurrentShape;
    }

    public void setCurrentShape(Shape mCurrentShape) {
        this.mCurrentShape = mCurrentShape;
        this.mCurrentShape.setmColor(getPen().getColor());
    }

    public void addShape(Shape shape){
        if (mShapes == null)
            mShapes = new ArrayList<>();
        //shape.setmColor(pen.getColor());
        mShapes.add(shape);
        mCurrentShape = null;
    }

    public ArrayList<Shape> getShapes(){
        return mShapes;
    }


    public void setAttacher(PhotoViewAttacher attacher) {
        this.mAttacher = attacher;
    }

    public LayerView(Context context) {
        super(context);
    }

    public LayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF displayRect = null;
        if (mCurrentShape != null ){
           displayRect = initParam(displayRect);

            mCurrentShape.onDraw(canvas,pen,displayRect);

        }
        if (mShapes!=null && mShapes.size()>0){
            displayRect = initParam(displayRect);
            for (Shape shape : mShapes){
                shape.onDraw(canvas,pen,displayRect);
            }
        }
    }

    private RectF initParam(RectF displayRect){
        if (displayRect == null)
            displayRect = mAttacher.getDisplayRect();

        if (pen == null)
            setPen();
        pen.setStrokeWidth(5*mAttacher.getScale());
        return displayRect;
    }


    private void setPen() {
        pen = new Paint();
        pen.setStyle(Paint.Style.STROKE);
        pen.setColor(Color.RED);
        pen.setStrokeWidth(5);
        pen.setAntiAlias(true);
        pen.setDither(true);
    }

    public void setPenColor(int color){
        if (pen==null)
            setPen();
        pen.setColor(color);
    }

    public Paint getPen(){
        if (pen==null)
            setPen();
        return pen;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
