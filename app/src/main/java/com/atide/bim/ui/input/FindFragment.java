package com.atide.bim.ui.input;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.entity.DesignEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发现页面
 */
@EFragment(R.layout.input_data_detail)
public class FindFragment extends SuperFragment {
    private ArrayList<EditText> editTextArrayList;
    private HashMap<String,Object> values;
    private boolean isInit = false;
    private boolean numberButtonChecked = true;
    @ViewById(R.id.parentView)
    LinearLayout parentView;
    @ViewById(R.id.numberType)
    RadioButton numberType;
    @ViewById(R.id.symbolType)
    RadioButton symbolType;


    @CheckedChange({R.id.numberType,R.id.symbolType})
    void checkedType(CompoundButton button,boolean isChecked){
        if (!isChecked)
            return;
        if (isInit)
            return;
        parentView.removeAllViews();
        editTextArrayList.clear();
        values.clear();
        switch (button.getId()){
            case R.id.numberType:
                values.put("type","number");
                for (int i=0;i<10;i++){
                    initItem(true,0,null);
                }
                initBottom();
                break;
            case R.id.symbolType:
                values.put("type", "symbol");
                initItem(false,0,null);
                break;
        }
    }
    @AfterViews
    void initUi(){
        values = new HashMap<>();
        values.put("type", "number");

        editTextArrayList = new ArrayList<>();
        if (numberButtonChecked)
        numberType.setChecked(true);else {
            symbolType.setChecked(true);
        }

    }

    private void initItem(final boolean isNumber,int key,String value){
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(getContext());
        final String index = value==null?String.valueOf(parentView.getChildCount() + 1):String.valueOf(key);
        textView.setText(String.valueOf(index)+"、");
        EditText editText = new EditText(getContext());
        editText.setTag(""+index);
        if (value!=null){
            editText.setText(value);
        }else{
            values.put(String.valueOf(index), "");
        }
        editText.setBackgroundResource(android.R.drawable.editbox_background);
        if (isNumber){
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }else{
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    values.put(String.valueOf(index),s.toString());
                }
            }
        });
     //
        editTextArrayList.add(editText);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(editText,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parentView.addView(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initBottom(){
        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setImageResource(android.R.drawable.ic_input_add);
        imageButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentView.removeViewAt(parentView.getChildCount() - 1);

                initItem(true,0,null);

                initBottom();

            }
        });
        parentView.addView(imageButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void clean() {
        if (editTextArrayList != null)
            editTextArrayList.clear();
        if (parentView!=null) {
            parentView.removeAllViews();
        }

    }

    @Override
    void save() {

    }

    public void initValues(HashMap<String,Object> tvalues){
        clean();
        isInit = true;
        if (tvalues == null){
            values = new HashMap<>();
            values.put("type","number");
            numberType.setChecked(true);
            for (int i=0;i<10;i++){
                initItem(true,0,null);
            }
            initBottom();
            isInit = false;
            return;
        }
        this.values = (HashMap)tvalues.clone();
        String type = values.get("type").toString();
        if (type.equals("number")){
            numberButtonChecked = true;
            if (numberType != null)
            numberType.setChecked(true);
            if (values.size()==1){
                for (int i = 0; i < 10; i++) {

                    initItem(true, 0, null);
                }
            }else {
                for (int i = 1; i < values.size(); i++) {

                    initItem(true, i, values.get("" + i).toString());
                }
            }
           /* for (Map.Entry<String,Object> entry: values.entrySet()){
                if (entry.getKey().equals("type"))
                    continue;
                initItem(true,entry);
            }*/
            initBottom();
        }else{
            numberButtonChecked = false;
            if (symbolType != null)
            symbolType.setChecked(true);
            if (values.size()==1) {
                values.put("1", "");
            }
            initItem(false,1,values.get("1").toString());
        }
        isInit = false;
    }

    public HashMap<String,Object> getValues(){
        if(values==null)
            return null;
        for (Map.Entry<String,Object> entry : values.entrySet()){
            if (entry.getKey().equals("type"))
                continue;
            if (!entry.getValue().equals("")){
                return (HashMap)values.clone();
            }
        }
        return null;
    }
}



