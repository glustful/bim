package com.atide.bim.ui.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.atide.bim.R;
import com.atide.ui.XListView;

/**
 * Created by atide on 2016/3/17.
 */
public class ToolsPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    public ToolsPopup(Context context){
        this.mContext = context;
    }

    public void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.picture_tools_popup, null);

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.white)));
        //popupWindow.showAsDropDown(parent, 0, 0);
        popupWindow.showAtLocation(parent, Gravity.LEFT,0,0);

    }


}
