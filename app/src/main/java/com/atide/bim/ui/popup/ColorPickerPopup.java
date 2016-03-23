package com.atide.bim.ui.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.atide.bim.R;

/**
 * Created by atide on 2016/3/17.
 */
public class ColorPickerPopup implements View.OnClickListener{
    private Context mContext;
    private PopupWindow popupWindow;
    private ColorPickerListener mColorPickerListener;
    public ColorPickerPopup(Context context){
        this.mContext = context;
    }

    public ColorPickerPopup setColorPickerListener(ColorPickerListener l){
        this.mColorPickerListener = l;
        return this;
    }

    public void showPopupWindow(View parent) {
        int height = 0;
        int width = 0;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.color_picker_popup, null);
            initClickEvent(view);
            view.measure(0,0);
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
        popupWindow.showAsDropDown(parent, -width, -height);
     //   popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);

    }

    private void initClickEvent(View view){
        view.findViewById(R.id.c1).setOnClickListener(this);
        view.findViewById(R.id.c2).setOnClickListener(this);
        view.findViewById(R.id.c3).setOnClickListener(this);
        view.findViewById(R.id.c4).setOnClickListener(this);
        view.findViewById(R.id.c5).setOnClickListener(this);
        view.findViewById(R.id.c6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mColorPickerListener != null){
            mColorPickerListener.colorPicker(Color.parseColor(v.getTag().toString()));

        }
        popupWindow.dismiss();
    }

    public interface ColorPickerListener{
        void colorPicker(int color);
    }


}
