package com.atide.bim.ui.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.model.CameraShape;
import com.atide.bim.model.Shape;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/17.
 */
public class ChoiceOperationPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private OnItemClickListener listener;
    private Shape shape;
    public ChoiceOperationPopup(Context context){
        this.mContext = context;
    }

    public Shape getShape() {
        return shape;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public ChoiceOperationPopup setListener(OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public ChoiceOperationPopup setShapes(Shape shape) {
        this.shape = shape;
        return this;
    }

    public void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.choice_shape_popup, null);
            LinearLayout parentView = (LinearLayout)view.findViewById(R.id.parentView);

                parentView.addView(initView("删除",Operation.REMOVE));

            if (shape.isEdit()){
                parentView.addView(initView("查看", Operation.EDIT));
            }

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);

        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
        //popupWindow.showAsDropDown(parent, 0, 0);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

    }

    private View initView(String title, final Operation operation){
        TextView tv = new TextView(mContext);
        tv.setBackgroundResource(R.drawable.tools_selector);
        tv.setText(title);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(10, 10, 10, 10);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(15);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.itemClick(operation);
                }
                popupWindow.dismiss();
            }
        });

        return tv;
    }



    public static interface OnItemClickListener{
        void itemClick(Operation operation);
    }

    public static enum Operation{
        REMOVE,
        EDIT
    }

}
