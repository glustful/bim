package com.atide.bim.helper;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atide.bim.MyApplication;
import com.atide.bim.entity.UserEntity;
import com.atide.bim.view.LabelGroup;

/**
 * Created by atide on 2016/4/1.
 */
public class UserLabelViewHelper {
    private LabelGroup labelGroup;
    public UserLabelViewHelper(LabelGroup labelGroup){
        this.labelGroup = labelGroup;
    }
    public void addUser(UserEntity entity){
        TextView textView = new TextView(MyApplication.getInstance());
        textView.setText(entity.getUsername());
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.BLACK);
        textView.setTag(entity);
        textView.setBackgroundResource(android.R.drawable.editbox_background);
        labelGroup.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public boolean deleteUser(UserEntity entity){
        for (int i=0;i<labelGroup.getChildCount();i++){
            View view = labelGroup.getChildAt(i);
            if (view.getTag().equals(entity)){
                labelGroup.removeViewAt(i);
                return true;
            }
        }
        return false;
    }
}
