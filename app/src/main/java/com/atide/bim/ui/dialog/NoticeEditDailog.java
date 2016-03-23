package com.atide.bim.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atide.bim.MyApplication_;
import com.atide.bim.R;
import com.atide.bim.model.NoticeShape;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by atide on 2016/3/21.
 */
@EFragment(R.layout.notice_dialog_layout)
public class NoticeEditDailog extends DialogFragment {

    private NoticeShape shape;

    @ViewById(R.id.id_txt_content)
    EditText content;
    @ViewById
    CheckBox border;
    @ViewById
    RadioGroup group;
    @CheckedChange(R.id.border)
    void border(boolean isChecked){

        shape.setIsBorder(isChecked);

    }
    @Click(R.id.id_sure_save)
    void svae(){
        shape.setContent(content.getText().toString());
        this.dismiss();
    }
    @CheckedChange({R.id.small,R.id.middle,R.id.large,R.id.bigLarge})
    void checkedChange(CompoundButton button,boolean isChecked){
        if (!isChecked)
            return;
       shape.setFont(Integer.parseInt(button.getTag().toString().trim()));
    }

    @AfterViews
    void initUI(){
        content.setText(shape.getContent());
        border.setChecked(shape.isBorder());
        int index = 0;
        switch (shape.getFont()){
            case 10:
                index = 0;
                break;
            case 12:
                index = 1;
                break;
            case 14:
                index = 2;
                break;
            case 16:
                index = 3;
                break;
            default:
                index = 0;
                break;
        }
        ((RadioButton)group.getChildAt(index)).setChecked(true);
    }
    @Override
    public void onResume() {

        super.onResume();
        getDialog().getWindow().setLayout(MyApplication_.getInstance().getDeviceInfo(getActivity()).widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().setTitle("Edit text");
    }

    public NoticeEditDailog setShape(NoticeShape shape){
        this.shape = shape;
        return this;
    }
}
