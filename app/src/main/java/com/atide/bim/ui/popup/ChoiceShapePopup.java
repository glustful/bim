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
import com.atide.bim.model.Line;
import com.atide.bim.model.Shape;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/17.
 */
public class ChoiceShapePopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private OnItemClickListener listener;
    private ArrayList<Shape> shapes;
    public ChoiceShapePopup(Context context){
        this.mContext = context;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public ChoiceShapePopup setListener(OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public ChoiceShapePopup setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
        return this;
    }

    public void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.choice_shape_popup, null);
            LinearLayout parentView = (LinearLayout)view.findViewById(R.id.parentView);
            for (Shape shape : shapes){
                parentView.addView(initView(shape));
            }
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);

        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
        //popupWindow.showAsDropDown(parent, 0, 0);
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);

    }

    private View initView(final Shape shape){
        TextView tv = new TextView(mContext);
        tv.setBackgroundResource(R.drawable.tools_selector);
        tv.setText(shape.getName());
        tv.setTextColor(Color.WHITE);
        tv.setPadding(10, 10, 10, 10);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(15);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.itemClick(shape);
                }
                popupWindow.dismiss();
            }
        });
        return tv;
    }

    public static interface OnItemClickListener{
        void itemClick(Shape shape);
    }


}
