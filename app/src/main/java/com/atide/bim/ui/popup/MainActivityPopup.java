package com.atide.bim.ui.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.ui.home.MainActivity;
import com.atide.ui.XListView;

/**
 * Created by atide on 2016/3/17.
 */
public class MainActivityPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private MainActivity.ContentChangeListener listener;
    public MainActivityPopup(Context context){
        this.mContext = context;
    }

    public void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.main_home_popup, null);
            final XListView listView = (XListView)view.findViewById(R.id.dataListView);
            listView.setPullLoadEnable(false);
            listView.setPullRefreshEnable(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    popupWindow = null;
                    if (listener != null) {
                        listener.contentChange(parent.getItemAtPosition(position).toString());
                    }
                }
            });
            constructAdapter(listView);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.black)));
        popupWindow.showAsDropDown(parent, 0, 0);


    }

    private void constructAdapter(XListView listView){
        String[] data = new String[30];
        for (int i = 0;i<30;i++){
            data[i] = "xxxx项目"+i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,android.R.id.text1,data);
        listView.setAdapter(adapter);
    }

    public MainActivityPopup setContentChangeListener(MainActivity.ContentChangeListener listener){
        this.listener = listener;
        return this;
    }
}
