package com.atide.bim.ui.popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.ui.message.SendMessageActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by atide on 2016/3/17.
 */
public class HistoryVistorPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private ListView historyView;
    private SendMessageActivity.ThemeChangeListener listener;
    private boolean isThemeData = false;
    public HistoryVistorPopup(Context context){
        this.mContext = context;
    }

    public void showPopupWindow(View parent) {
        if (isThemeData){
            if (MyApplication.getInstance().getThemeDatas()==null || MyApplication.getInstance().getThemeDatas().size()<1)
                return;
        }
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.history_vistor_layout, null);
            historyView = (ListView)view.findViewById(R.id.history);
            initAdapter();
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.white)));
        if (!isThemeData) {
             popupWindow.showAsDropDown((View)parent.getParent(), 0, 0);
        }else {
            if (listener != null){
                popupWindow.showAsDropDown((View)parent.getParent(), 0, 0);
            }else {
                popupWindow.showAtLocation(parent, Gravity.LEFT, 0, 0);
            }
        }

    }

    private void initAdapter(){
        if (!isThemeData) {
            historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, Object> item = (HashMap) parent.getItemAtPosition(position);
                    MyApplication.getInstance().backStack(item);
                    popupWindow.dismiss();
                }
            });
            SimpleAdapter adapter = new SimpleAdapter(mContext, MyApplication.getInstance().getHistory(), android.R.layout.simple_list_item_1, new String[]{"title"}, new int[]{android.R.id.text1});
            historyView.setAdapter(adapter);
        }else{
            historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, String> item = (HashMap) parent.getItemAtPosition(position);
                    if (listener == null) {
                        GlobalEntity.getInstance().setThemeId(item.get("id").toString());
                    }else{
                        listener.themeChange(item);
                    }
                    popupWindow.dismiss();
                }
            });
            SimpleAdapter adapter = new SimpleAdapter(mContext, MyApplication.getInstance().getThemeDatas(), android.R.layout.simple_list_item_1, new String[]{"name"}, new int[]{android.R.id.text1});
            historyView.setAdapter(adapter);
        }

    }

    public boolean isThemeData() {
        return isThemeData;
    }

    public HistoryVistorPopup setIsThemeData(boolean isThemeData) {
        this.isThemeData = isThemeData;
        return this;
    }

    public SendMessageActivity.ThemeChangeListener getListener() {
        return listener;
    }

    public HistoryVistorPopup setListener(SendMessageActivity.ThemeChangeListener listener) {
        this.listener = listener;
        return this;
    }
}
