package com.atide.bim.ui.input;


import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.entity.PartTempleteEntity;
import com.atide.bim.entity.QualityHeaderEntity;
import com.atide.bim.utils.TimeUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 表头页面
 *
 */
@EFragment(R.layout.quality_measured_header)
public class HeaderFragment extends SuperFragment {

    QualityHeaderEntity qualityHeaderEntity;
    @ViewById(R.id.number)
    EditText number;
    @ViewById(R.id.partNO)
    EditText partName;
    @ViewById(R.id.lichengNO)
    EditText lichengNo;
    @ViewById(R.id.projectName)
    EditText projectName;
    @ViewById(R.id.childProjectName)
    EditText childProjectName;
    @ViewById(R.id.year)
    EditText year;
    @ViewById(R.id.month)
    EditText month;
    @ViewById(R.id.day)
    EditText day;
    @ViewById(R.id.building)
    RadioButton building;
    @ViewById(R.id.management)
    RadioButton management;
    @CheckedChange({R.id.building,R.id.management})
    void changeIdentity(CompoundButton button,boolean isChecked){
        if (isChecked){
            qualityHeaderEntity.setIdentity(button.getTag().toString());
        }
    }

    @AfterViews
    void initUi(){


        qualityHeaderEntity = (QualityHeaderEntity) getArguments().getSerializable("entity");

        if (qualityHeaderEntity.getMeasuredPartID()!=null && !qualityHeaderEntity.getMeasuredPartID().equals("")){
            initData();
        }else {
            building.setChecked(true);
            initDate();
        }
    }

    private void initData(){
        number.setText(qualityHeaderEntity.getTableNo());
        partName.setText(qualityHeaderEntity.getDivisionPartName());
        lichengNo.setText(qualityHeaderEntity.getInspectPickets());
        projectName.setText(qualityHeaderEntity.getPartName());
        childProjectName.setText(qualityHeaderEntity.getInspectPartName());
        if (qualityHeaderEntity.getIdentity().equals("1")){
            management.setChecked(true);
        }else{
            building.setChecked(true);
        }
        converDate();
    }

    private void initDate(){
        Calendar calendar = Calendar.getInstance();
        year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        month.setText(String.valueOf(calendar.get(Calendar.MONTH)+1));
        day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    public void clean() {

    }

    @Override
    void save() {
        qualityHeaderEntity.setIdentity(GlobalEntity.getInstance().getUsedUnit());
        qualityHeaderEntity.setTableNo(number.getText().toString());
        qualityHeaderEntity.setDivisionPartName(partName.getText().toString());
        qualityHeaderEntity.setInspectPickets(lichengNo.getText().toString());
        qualityHeaderEntity.setPartName(projectName.getText().toString());
        qualityHeaderEntity.setInspectPartName(childProjectName.getText().toString());
        qualityHeaderEntity.setInspectDate(year.getText()+"/"+month.getText()+"/"+day.getText()+" "+ TimeUtils.convertToString(new Date(),"HH:mm:ss"));
    }

    private void converDate(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = simpleDateFormat.parse(qualityHeaderEntity.getInspectDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            month.setText(String.valueOf(calendar.get(Calendar.MONTH)+1));
            day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }catch (Exception e){
            initDate();
        }
    }


}



