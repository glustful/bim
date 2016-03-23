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

/**
 * Created by atide on 2016/3/17.
 */
public class ChoiceShapePopup {
    private Context mContext;
    private PopupWindow popupWindow;
    public ChoiceShapePopup(Context context){
        this.mContext = context;
    }

    public void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout view = (LinearLayout)layoutInflater.inflate(R.layout.choice_shape_popup, null);
            view.addView(initView("pen"));
            view.addView(initView("brush"));
            view.addView(initView("arrow"));
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
        //popupWindow.showAsDropDown(parent, 0, 0);
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);

    }

    private View initView(String text){
        TextView tv = new TextView(mContext);
        tv.setBackgroundResource(R.drawable.tools_selector);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(10, 10, 10, 10);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(15);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        return tv;
    }


}
