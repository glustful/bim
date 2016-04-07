package com.atide.bim.ui.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.entity.ProjectModel;
import com.atide.bim.ui.home.MainActivity;
import com.atide.ui.XListView;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/17.
 */
public class MainActivityPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private MainActivity.ContentChangeListener listener;
    private ArrayList<ProjectModel> projectModels;
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
                        listener.contentChange(projectModels.get(position-1));
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


        listView.setAdapter(new DataAdapter());
    }

    public MainActivityPopup setContentChangeListener(MainActivity.ContentChangeListener listener){
        this.listener = listener;
        return this;
    }

    public MainActivityPopup setContent(ArrayList<ProjectModel> models){
        this.projectModels = models;
        return this;
    }

    class DataAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return projectModels==null?0:projectModels.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,null);
            }
            TextView title = (TextView)convertView.findViewById(android.R.id.text1);
            title.setText(projectModels.get(position).getTitle());
            return convertView;
        }
    }
}
