package com.atide.bim.ui.quality;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.QualityDatumEntity;
import com.atide.bim.helper.RequestQualityHelper;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.bim.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * Created by atide on 2016/4/19.
 */
@EActivity(R.layout.approval_datum_layout)
public class ApprovalActivity extends MainActionBarActivity {
    private boolean sendsms = false;
    @Extra
    QualityDatumEntity qualityDatumEntity;
    @ViewById(R.id.datumName)
    TextView datumName;
    @ViewById(R.id.content)
    EditText content;
    @ViewById
    ProgressBar loadingBar;
    @Bean
    RequestQualityHelper requestQualityHelper;
    @CheckedChange(R.id.sendsms)
    void sendSMS(boolean isChecked){
        sendsms = isChecked;
    }
    @Click({R.id.confir,R.id.isReturn})
    void confir(View view){
        String conStr = content.getText().toString();
        if (TextUtils.isEmpty(conStr)){
            Utils.showMsg("说点什么吧.....");
            return;
        }
        loadingBar.setVisibility(View.VISIBLE);
        boolean isReturn = false;
        if (view.getId()==R.id.isReturn){
            isReturn = true;
        }
        requestQualityHelper.approvalDatums(new SendMessageActivity.RequestOnFinishListener(){
            @Override
            public void finish(String msg) {
                loadingBar.setVisibility(View.GONE);
                try{
                    Utils.showMsg(new JSONObject(msg).optString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                ApprovalActivity.this.finish();
            }

            @Override
            public void fail(String msg) {
                loadingBar.setVisibility(View.GONE);
                Utils.showMsg(msg);
            }
        },qualityDatumEntity.getDatumID(),conStr,sendsms,isReturn);
    }

    @AfterViews
    void initUI(){
        datumName.setText(qualityDatumEntity.getDatumName());
    }
    @Override
    public String fetchTitle() {
        return "审核资料";
    }

    @Override
    public Activity fetchClass() {
        return this;
    }
}
