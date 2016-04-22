package com.atide.bim.helper;

import com.atide.bim.entity.DesignEntity;
import com.atide.bim.entity.DesignValueEntity;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.entity.MeasuredItemEntity;
import com.atide.bim.entity.MeasuredItemInspectNumEntity;
import com.atide.bim.entity.MeasuredItemValueEntity;
import com.atide.bim.entity.PartTempleteEntity;
import com.atide.bim.entity.QualityDatumEntity;
import com.atide.bim.entity.QualityHeaderEntity;
import com.atide.bim.entity.User;
import com.atide.bim.request.PrimaryKeyServiceRequest;
import com.atide.bim.request.QualityServiceRequest;
import com.atide.bim.utils.TimeUtils;
import com.atide.bim.utils.Utils;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atide on 2016/4/14.
 */
@EBean
public class QualitySaveDataHelper {
    private PartTempleteEntity partTempleteEntity;
    private ArrayList<DesignEntity> designEntitys;
    private QualityHeaderEntity qualityHeaderEntity;
    //private MeasuredItemEntity measuredItemEntity;
    private QualityDatumEntity qualityDatumEntity;
    private String datumId;
    private HashMap<String,HashMap<String,Object>> measuredItemDatas;
    private String measuredItemValuesDelId = "";

    ArrayList<DesignValueEntity> designValueEntities = new ArrayList<>();
    ArrayList<MeasuredItemValueEntity> measuredItemValueEntities = new ArrayList<>();
    ArrayList<MeasuredItemInspectNumEntity> measuredItemInspectNumEntities = new ArrayList<>();

    public QualitySaveDataHelper(){

    }

    public PartTempleteEntity getPartTempleteEntity() {
        return partTempleteEntity;
    }

    public void setPartTempleteEntity(PartTempleteEntity partTempleteEntity) {
        this.partTempleteEntity = partTempleteEntity;
    }

    public ArrayList<DesignEntity> getDesignEntitys() {
        return designEntitys;
    }

    public void setDesignEntitys(ArrayList<DesignEntity> designEntity) {
        this.designEntitys = designEntity;
    }

    public QualityHeaderEntity getQualityHeaderEntity() {
        return qualityHeaderEntity;
    }

    public void setQualityHeaderEntity(QualityHeaderEntity qualityHeaderEntity) {
        this.qualityHeaderEntity = qualityHeaderEntity;
    }

    public QualityDatumEntity getQualityDatumEntity() {
        return qualityDatumEntity;
    }

    public void setQualityDatumEntity(QualityDatumEntity qualityDatumEntity) {
        this.qualityDatumEntity = qualityDatumEntity;
    }

    public void saveQualityDatum(){
        if (qualityDatumEntity!=null)
        {
            datumId = qualityDatumEntity.getDatumID();
            qualityDatumEntity.setUsedUnit(qualityHeaderEntity.getIdentity());
            qualityDatumEntity.setSectNo(GlobalEntity.getInstance().getSectId());
            return;
        }
        qualityDatumEntity = new QualityDatumEntity();
        qualityDatumEntity.setTemplateID(partTempleteEntity.getTemplateid());
        qualityDatumEntity.setUsedUnit(qualityHeaderEntity.getIdentity());
        qualityDatumEntity.setPartNo(GlobalEntity.getInstance().getPartId());
        qualityDatumEntity.setSectNo(GlobalEntity.getInstance().getSectId());
        qualityDatumEntity.setInputUserID(User.getLoginUser().getUserId());
        qualityDatumEntity.setApprovalStatus("0");
        qualityDatumEntity.setInputDate(TimeUtils.convertToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        qualityDatumEntity.setDatumName(partTempleteEntity.getTemplatename()+"_"+qualityHeaderEntity.getPartName());
        qualityDatumEntity.setOrderSN("1");
        WsResponseMessage msg = new QualityServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {

            }
        }).setMethodName(QualityServiceRequest.QualityServiceMethod.GetInspectDefaultFlowId.getMethodName())
                .addParam("usedUnit",qualityHeaderEntity.getIdentity())
                .request2();
        if (msg.mCode==200) {
            qualityDatumEntity.setFlowID(msg.mData);
        }
        datumId = getPrimaryKey("GeQulityDatumKey",1).get(0);
        qualityDatumEntity.setDatumID(datumId);

    }

    public void saveMeasuredItemValue(HashMap<String,HashMap<String,Object>> measuredValuew,HashMap<String,String> ids){
        ArrayList<MeasuredItemInspectNumEntity> inspectNumEntities = new ArrayList<>();
        ArrayList<MeasuredItemValueEntity> valueEntities = new ArrayList<>();
        for (Map.Entry<String,HashMap<String,Object>> entry : measuredValuew.entrySet()){
            String type = entry.getValue().get("type").toString();
            MeasuredItemInspectNumEntity measuredItemInspectNumEntity = new MeasuredItemInspectNumEntity();
            measuredItemInspectNumEntity.setInputType(type.equals("number")?"0":"1");
            measuredItemInspectNumEntity.setMeasuredItemID(entry.getKey());
            measuredItemInspectNumEntity.setDatumID(datumId);
            String inspectId = ids.get(entry.getKey());
            String preStr = measuredItemInspectNumEntity.getInputType().equals("0")?"1":"0";
            for (Map.Entry<String,String> id : ids.entrySet()){
                if (id.getKey().startsWith(preStr+entry.getKey())){
                    measuredItemValuesDelId += id.getValue() + ",";
                }
            }
            measuredItemInspectNumEntity.setInspectID(inspectId);
            measuredItemInspectNumEntity.setInspectNum(String.valueOf(entry.getValue().size() - 1));
            measuredItemInspectNumEntity.setIsAttachRep("false");
            measuredItemInspectNumEntity.setIsDeviation("false");
            measuredItemInspectNumEntities.add(measuredItemInspectNumEntity);
            if (inspectId==null || inspectId.equals("")){
                inspectNumEntities.add(measuredItemInspectNumEntity);
            }
            for(Map.Entry<String,Object> entry1 : entry.getValue().entrySet()) {
                if (entry1.getKey().equals("type"))
                    continue;
                MeasuredItemValueEntity entity = new MeasuredItemValueEntity();
                entity.setDatumID(datumId);
                String valueId = ids.get(measuredItemInspectNumEntity.getInputType()+entry.getKey() + entry1.getKey());
                entity.setItemValueID(valueId);
                entity.setPartNo(GlobalEntity.getInstance().getPartId());
                entity.setSectNo(GlobalEntity.getInstance().getSectId());
                entity.setItemValueSN(entry1.getKey());
                entity.setMeasuredItemID(entry.getKey());

                if (type.equals("number")) {
                    entity.setItemValue(entry1.getValue().toString());
                } else {
                    entity.setItemCharValue(entry1.getValue().toString());
                }
                measuredItemValueEntities.add(entity);
                if (valueId==null || valueId.equals("")){
                    valueEntities.add(entity);
                }

            }
        }
        ArrayList<String> inpectNumKey = getPrimaryKey("GeMeasuredNumKey", inspectNumEntities.size());
        int i=0;
        for (MeasuredItemInspectNumEntity  entity :inspectNumEntities){
            entity.setInspectID(inpectNumKey.get(i));
            i++;
        }
        i = 0;
        ArrayList<String> itemValueKey = getPrimaryKey("GeMeasuredItemValueKey",valueEntities.size());
        for (MeasuredItemValueEntity entity : valueEntities){
            entity.setItemValueID(itemValueKey.get(i));
            i++;
        }
    }

    public void saveDesignValue(){
        if (designEntitys == null || designEntitys.size()<1)
            return;
        boolean isKey = false;
        ArrayList<String> keys = null;
        if (designEntitys.get(0).getDatacollid()==null || designEntitys.get(0).getDatacollid().equals("")) {
            keys = getPrimaryKey("GetQualityDataValueKey", designEntitys.size());
            isKey = true;
        }
        int i=0;
        for (DesignEntity entity : designEntitys){
            DesignValueEntity designValueEntity = new DesignValueEntity();
            designValueEntity.setValueCollID(isKey?keys.get(i):entity.getDatacollid());
            designValueEntity.setDataCollID(entity.getDatacollid());
            designValueEntity.setPartNo(GlobalEntity.getInstance().getPartId());
            designValueEntity.setSectNo(GlobalEntity.getInstance().getSectId());
            designValueEntity.setLowerLimitValue(entity.getLowerlimitvalue());
            designValueEntity.setUpperLimitValue(entity.getUpperlimitvalue());
            designValueEntity.setFormatText(entity.getDatasysbom());
            designValueEntities.add(designValueEntity);
            i++;
        }
    }

    public void saveHeaderPart(){
        if (qualityHeaderEntity.getMeasuredPartID()!=null && !qualityHeaderEntity.getMeasuredPartID().equals(""))
            return;
        qualityHeaderEntity.setPartNo(GlobalEntity.getInstance().getPartId());
        qualityHeaderEntity.setDatumID(datumId);
        qualityHeaderEntity.setMeasuredPartID(getPrimaryKey("GeMeasuredPartKey",1).get(0));
    }

    public HashMap<String,String> save(){
        HashMap<String,String> params = new HashMap<>();
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        Gson gson = gsonBuilder.create();
        String qualityDatum = gson.toJson(qualityDatumEntity);
        String itemValues = gson.toJson(measuredItemValueEntities);

        String header = gson.toJson(qualityHeaderEntity);
        String itemNums = gson.toJson(measuredItemInspectNumEntities);
        String designValues = gson.toJson(designValueEntities);
       params.put("datumDatas",qualityDatum);
        params.put("itemValueDatas",itemValues);
        params.put("inpectPartDatas",header);
        params.put("inpectNumDatas",itemNums);
        params.put("designDatas",designValues);
        params.put("delDatumIDStrs","");
        String ids = measuredItemValuesDelId;
        if (ids.length()>0){
            ids = ids.substring(0,ids.length()-1);
        }
        params.put("delItemValueIdStrs",ids);
        params.put("delDataValueIdStrs","");
        return params;
    }


    public ArrayList<String> getPrimaryKey(String methodName,int keys){
        SoapObject result = (SoapObject)new PrimaryKeyServiceRequest().setMethodName(methodName).request(keys);
        ArrayList<String> results = new ArrayList<>();
        for (int i=0;i<result.getPropertyCount();i++){
            SoapObject child = (SoapObject)result.getProperty(i);
            for (int j=0;j<child.getPropertyCount();j++) {
                results.add(child.getProperty(j).toString());
            }
        }
        return results;
    }

    public String getMeasuredItemValuesDelId() {
        return measuredItemValuesDelId;
    }

    public void setMeasuredItemValuesDelId(String measuredItemValuesDelId) {
        this.measuredItemValuesDelId = measuredItemValuesDelId;
    }
}
