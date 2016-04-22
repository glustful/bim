package com.atide.bim.ui.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.ui.message.SendMessageActivity;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atide on 2016/3/17.
 */
public class ListViewPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private ListView dataListView;
    private AdapterView.OnItemClickListener listener;

    public ListViewPopup(Context context){
        this.mContext = context;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.history_vistor_layout, null);
            dataListView = (ListView)view.findViewById(R.id.history);

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.toolsPopupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.white)));
    }

    public void showPopupWindow(View parent,int gravity,int offsetX,int offsetY) {

        if (gravity == -1) {
             popupWindow.showAsDropDown((View)parent.getParent(), offsetX, offsetY);
        }else {

                popupWindow.showAtLocation(parent, gravity, offsetX, offsetY);

        }

    }

    public ListViewPopup initAdapter(ArrayList<HashMap<String,Object>> datas,String name){

            dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onItemClick(parent, view, position, id);
                    }
                    popupWindow.dismiss();
                }
            });
            SimpleAdapter adapter = new SimpleAdapter(mContext, datas, android.R.layout.simple_list_item_1, new String[]{name}, new int[]{android.R.id.text1});
            dataListView.setAdapter(adapter);
        return this;

    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return listener;
    }

    public ListViewPopup setOnItemClickListener(AbsListView.OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }


}
