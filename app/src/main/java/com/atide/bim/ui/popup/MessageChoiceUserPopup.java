package com.atide.bim.ui.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.entity.ProjectModel;
import com.atide.bim.entity.UserEntity;
import com.atide.bim.ui.home.MainActivity;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.ui.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by atide on 2016/3/17.
 */
public class MessageChoiceUserPopup {
    private Context mContext;
    private PopupWindow popupWindow;
    private SendMessageActivity.UsersCheckedChangeListener listener;
    private ArrayList<UserEntity> projectModels;
    private ArrayList<UserEntity> checkedModels;
    public static HashMap<Integer, Boolean> isSelected;
    public MessageChoiceUserPopup(Context context){
        this.mContext = context;
    }

    public void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.message_choice_user_popup, null);
            final ListView listView = (ListView)view.findViewById(R.id.dataListView);
            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
            view.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.checked(isSelected);
                    }
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DataAdapter.ViewHolder holder = (DataAdapter.ViewHolder) view.getTag();
                    holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
                    isSelected.put(position, holder.cb.isChecked()); // 同时修改map的值保存状态

                }
            });
            constructAdapter(listView);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.topBottomAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.black)));

        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        Rect outRect = new Rect();
        ((Activity)mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        popupWindow.setHeight(location[1]-outRect.top);

        popupWindow.showAtLocation(parent, Gravity.TOP,0,0);

    }

    private void constructAdapter(ListView listView){


        listView.setAdapter(new DataAdapter());
    }

    public MessageChoiceUserPopup setContentChangeListener(SendMessageActivity.UsersCheckedChangeListener listener){
        this.listener = listener;
        return this;
    }

    public MessageChoiceUserPopup setContent(ArrayList<UserEntity> models,ArrayList<UserEntity> checked){
        this.projectModels = models;
        this.checkedModels = checked;
        return this;
    }

    class DataAdapter extends BaseAdapter{


        private LayoutInflater inflater = null;

        public DataAdapter() {

            inflater = LayoutInflater.from(mContext);
            init();
        }

        // 初始化 设置所有checkbox都为未选择
        public void init() {
            isSelected = new HashMap<Integer, Boolean>();
            for (int i = 0; i < projectModels.size(); i++) {
                if (checkedModels != null){
                    if(checkedModels.contains(projectModels.get(i))){
                        isSelected.put(i, true);
                        continue;
                    }
                }
                isSelected.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return projectModels.size();
        }

        @Override
        public Object getItem(int arg0) {
            return projectModels.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup arg2) {
            ViewHolder holder = null;
            if (holder == null) {
                holder = new ViewHolder();
                if (view == null) {
                    view = inflater.inflate(R.layout.message_choice_user_item, null);
                }
                holder.tv = (TextView) view.findViewById(R.id.item_tv);
                holder.cb = (CheckBox) view.findViewById(R.id.item_cb);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

                holder.tv.setText(projectModels.get(position).getUsername() + "(" + projectModels.get(position).getUnitname() + ")");

            holder.cb.setChecked(isSelected.get(position));
            return view;
        }

        public class ViewHolder {
            public TextView tv = null;
            public CheckBox cb = null;
        }
    }
}
