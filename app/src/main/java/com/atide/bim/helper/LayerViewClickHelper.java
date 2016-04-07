package com.atide.bim.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;

import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.NoticeShape;
import com.atide.bim.model.Shape;
import com.atide.bim.ui.dialog.NoticeEditDailog_;
import com.atide.bim.ui.picture.PictureSureActivity_;
import com.atide.bim.ui.popup.ChoiceOperationPopup;
import com.atide.bim.ui.popup.ChoiceShapePopup;

import java.util.ArrayList;

import uk.co.senab.photoview.LayerView;

/**
 * Created by atide on 2016/3/23.
 * 辅助类
 * 处理点击选中事件
 */
public class LayerViewClickHelper {
    private LayerView mLayerView;
    private Context mContext;
    public LayerViewClickHelper(LayerView layerView){
        this.mLayerView = layerView;
        mContext = this.mLayerView.getContext();
    }

    public void clickEvent(float x,float y){
        final ArrayList<Shape> shapes = mLayerView.getShapes();
        if (shapes == null || shapes.size() < 1)
            return;

        final ArrayList<Shape> checkedShaps = new ArrayList<>();
        for (Shape shape : shapes) {
            if (shape.getThemeTypeId().equals(GlobalEntity.getInstance().getThemeId())) {
                if (shape.isChecked(new PointF(x, y))) {

                    checkedShaps.add(shape);

                }
            }
        }
        if (checkedShaps.size()==0)
            return;

        if (checkedShaps.size()==1){
            new ChoiceOperationPopup(mContext).setShapes(checkedShaps.get(0)).setListener(new ChoiceOperationPopup.OnItemClickListener() {
                @Override
                public void itemClick(ChoiceOperationPopup.Operation operation) {
                    if (operation == ChoiceOperationPopup.Operation.REMOVE) {
                        if (shapes.remove(checkedShaps.get(0))){
                            mLayerView.addDeleteShape(checkedShaps.get(0));
                        }
                        mLayerView.invalidate();
                    }
                    else{
                        openEdit(checkedShaps.get(0));
                    }
                }
            }).showPopupWindow(mLayerView);
        } else {
            new ChoiceShapePopup(mContext).setShapes(checkedShaps).setListener(new ChoiceShapePopup.OnItemClickListener() {
                @Override
                public void itemClick(final Shape shape) {
                    new ChoiceOperationPopup(mContext).setShapes(shape).setListener(new ChoiceOperationPopup.OnItemClickListener() {
                        @Override
                        public void itemClick(ChoiceOperationPopup.Operation operation) {
                            if (operation == ChoiceOperationPopup.Operation.REMOVE) {
                                mLayerView.addDeleteShape(shape);
                                shapes.remove(shape);
                                mLayerView.invalidate();
                            } else {
                                openEdit(shape);
                            }
                        }
                    }).showPopupWindow(mLayerView);
                }
            }).showPopupWindow(mLayerView);
        }
    }

    private void openEdit(Shape shape){
        if (shape instanceof CameraShape){
            PictureSureActivity_.intent(mContext).photoUri(((CameraShape)shape).getPhotoUri()).start();
        }else{
            NoticeEditDailog_.builder().build().setShape((NoticeShape)shape).show(((Activity)mContext).getFragmentManager(),"Edit text");
        }
    }
}
