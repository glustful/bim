package com.atide.bim.ui.quality;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.entity.PartTempleteEntity;
import com.atide.bim.entity.QualityDatumEntity;
import com.atide.bim.entity.User;
import com.atide.bim.helper.RequestQualityHelper;
import com.atide.bim.request.QualityServiceRequest;
import com.atide.bim.ui.input.InputDataActivity_;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.bim.ui.popup.ListViewPopup;
import com.atide.bim.utils.Utils;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atide on 2016/4/12.
 */
@EActivity(R.layout.quality_main_layout)
public class QualityMainActivity extends MainActionBarActivity {


    private ArrayList<QualityDatumEntity> qualityDatumEntities;
    @ViewById
    ListView dataListView;
    @ViewById
    TextView noData;
    @ViewById
    ProgressBar loadingBar;
    @ViewById(R.id.management)
    RadioButton management;
    @ViewById(R.id.building)
    RadioButton building;
    @Bean
    RequestQualityHelper requestQualityHelper;
    @Bean
    QualityDatumAdapter adapter;
    @CheckedChange({R.id.management,R.id.building})
    void usedUnitChange(CompoundButton button,boolean ischecked){
        if (!ischecked)
            return;
        switch (button.getId()){
            case R.id.building:
                GlobalEntity.getInstance().setUsedUnit("2");
                break;
            case R.id.management:
                GlobalEntity.getInstance().setUsedUnit("1");
                break;
        }
        request();
    }
    @ItemClick(R.id.dataListView)
    void itemClick(QualityDatumEntity entity){
        PartTempleteEntity entity1 = new PartTempleteEntity();
        entity1.setTemplateid(entity.getTemplateID());
        entity1.setObjectkey(entity.getCollId());
        InputDataActivity_.intent(mContext).partNo(GlobalEntity.getInstance().getPartId()).partTempleteEntity(entity1).qualityDatumEntity(entity).start();

    }
    @ItemLongClick(R.id.dataListView)
    void itemLongClick(final QualityDatumEntity entity){
        int resaId = 0;
        if (DatumDataStatus.get(entity.getApprovalStatus()) == DatumDataStatus.DECLARESTATUS){
            resaId = R.array.quality_declare;
        }else if (DatumDataStatus.get(entity.getApprovalStatus())==DatumDataStatus.APPROVALSTATUS){
            resaId = R.array.quality_approval;
        }else {
            return;
        }
        final int resId = resaId;
        new AlertDialog.Builder(mContext).setItems(resId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] array = getResources().getStringArray(resId);
                if (array[which].equals("申报")) {
                    declareDatas(entity);
                } else if (array[which].equals("审核")) {
                    ApprovalActivity_.intent(mContext).qualityDatumEntity(entity).start();
                } else if (array[which].equals("删除")) {
                    deleteDatumData(entity);
                }

            }
        }).setTitle("选择操作")
                .show();
    }
    @AfterViews
    void initUI(){
        mContext = this;
        building.setChecked(true);
        dataListView.setEmptyView(noData);
        dataListView.setAdapter(adapter);
       // request();
    }

    @Override
    protected void onResume() {
        request();
        super.onResume();
    }

    private void request(){
        loadingBar.setVisibility(View.VISIBLE);

        new QualityServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                loadingBar.setVisibility(View.GONE);
                qualityDatumEntities = new ArrayList<QualityDatumEntity>();
                try{
                if (msg.mCode!= 200)
                    return;

                    JSONArray jsonArray = new JSONObject(msg.mData).optJSONArray("data");
                    Gson gson = new Gson();

                    for (int i=0;i<jsonArray.length();i++){
                        QualityDatumEntity entity = gson.fromJson(jsonArray.optJSONObject(i).toString(),QualityDatumEntity.class);
                        entity.setCollId(jsonArray.optJSONObject(i).optString("measuredcollid"));
                        qualityDatumEntities.add(entity);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    adapter.reload(qualityDatumEntities);
                }



            }
        }).setMethodName(QualityServiceRequest.QualityServiceMethod.GetInpectDatums.getMethodName())
                .addParam("partNo", GlobalEntity.getInstance().getPartId())
                .addParam("usedUnit", GlobalEntity.getInstance().getUsedUnit())
                .request();
    }
    @Override
    public String fetchTitle() {
        return "现场检验";
    }

    @Override
    public Activity fetchClass() {
        return this;
    }

    @Override
    public void setRightButtonOnClickListener() {
        super.setRightButtonOnClickListener();
        rightButton1.setVisibility(View.VISIBLE);
        rightButton1.setText("添加");
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                loadingBar.setVisibility(View.VISIBLE);
                requestQualityHelper.getPartTemplates(new SendMessageActivity.RequestOnFinishListener() {
                    @Override
                    public void finish(String msg) {
                        loadingBar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONObject(msg).optJSONArray("data");
                            Gson gson = new Gson();
                            ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, Object> item = new HashMap<String, Object>();
                                PartTempleteEntity entity = gson.fromJson(jsonArray.optJSONObject(i).toString(), PartTempleteEntity.class);
                                item.put("name", entity.getTemplatename());
                                item.put("obj", entity);
                                datas.add(item);
                            }
                            new ListViewPopup(mContext).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    PartTempleteEntity entity = (PartTempleteEntity)((HashMap<String,Object>)parent.getItemAtPosition(position)).get("obj");
                                    InputDataActivity_.intent(mContext).partTempleteEntity(entity).partNo(GlobalEntity.getInstance().getPartId()).start();
                                }
                            }).initAdapter(datas, "name")
                                    .showPopupWindow(v, -1, 0, 0);
                        } catch (Exception e) {
                            Utils.showMsg("返回数据为空");
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        loadingBar.setVisibility(View.GONE);
                        Utils.showMsg(msg);
                    }
                }, GlobalEntity.getInstance().getPartId());

            }
        });
    }

    /**
     * 删除主表数据
     * @param entity
     */
    private void deleteDatumData(QualityDatumEntity entity){
        loadingBar.setVisibility(View.VISIBLE);
        new QualityServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                loadingBar.setVisibility(View.GONE);
                if (msg.mCode==200) {
                    try{
                        Utils.showMsg(new JSONObject(msg.mData).optString("msg"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    request();
                }else{
                    Utils.showMsg(msg.mMsg);
                }
            }
        }).setMethodName(QualityServiceRequest.QualityServiceMethod.SaveInpectDatas.getMethodName())
                .addParam("datumDatas", "")
                .addParam("itemValueDatas","")
                .addParam("inpectPartDatas","")
                .addParam("inpectNumDatas","")
                .addParam("designDatas","")
                .addParam("delDatumIDStrs",entity.getDatumID())
                .addParam("delItemValueIdStrs","")
                .addParam("delDataValueIdStrs","")
                .request();
    }

    /**
     * 申报功能
     * @param entity
     */
    private void declareDatas(QualityDatumEntity entity){
        if (DatumDataStatus.get(entity.getApprovalStatus())==DatumDataStatus.DECLARESTATUS) {
            loadingBar.setVisibility(View.VISIBLE);
            requestQualityHelper.declareDatas(new SendMessageActivity.RequestOnFinishListener(){
                @Override
                public void finish(String msg) {
                    loadingBar.setVisibility(View.GONE);
                    try{
                        Utils.showMsg(new JSONObject(msg).optString("msg"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    request();
                }

                @Override
                public void fail(String msg) {
                    loadingBar.setVisibility(View.GONE);
                    Utils.showMsg(msg);
                }
            },entity.getDatumID());
        }else{
            Utils.showMsg("当前状态不在申报阶段");
        }
    }
}
