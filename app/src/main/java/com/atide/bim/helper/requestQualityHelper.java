package com.atide.bim.helper;

import android.content.Context;

import com.atide.bim.entity.User;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.request.QualityServiceRequest;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.bim.utils.TimeUtils;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by atide on 2016/4/12.
 */
@EBean
public class RequestQualityHelper {
    private Context mContext;
    public RequestQualityHelper(Context context){
        mContext = context;
    }

    /**
     * 审核
     * @param listener
     * @param content
     * @param sendsms
     */
    public void approvalDatums(SendMessageActivity.RequestOnFinishListener listener,String datumId,String content,boolean sendsms,boolean isReturn){
        HashMap<String,String> param = new HashMap<>();
        param.put("datumId",datumId);
        param.put("approvalUserId", User.getLoginUser().getUserId());
        param.put("approvalDate", TimeUtils.convertToString(new Date(),"yyyy/MM/dd HH:mm:ss"));
        param.put("approvalIdea",content);
        param.put("isReturn",isReturn?"true":"false");
        param.put("isSendSMS",sendsms?"true":"false");
        request(listener, QualityServiceRequest.QualityServiceMethod.ApprovalDatums.getMethodName(),param);
    }
    /**
     * 申报
     * @param listener
     * @param ids
     */
    public void declareDatas(SendMessageActivity.RequestOnFinishListener listener,String ids){
        HashMap<String,String> param = new HashMap<>();
        param.put("datumIdStrs",ids);
        param.put("declareUserId", User.getLoginUser().getUserId());
        param.put("declareUnitId",User.getLoginUser().getUnitid());
        request(listener, QualityServiceRequest.QualityServiceMethod.DeclareDatas.getMethodName(),param);
    }
    /**
     * 获取检测项目的检测个数与类型
     * @param listener
     * @param datumsId
     * @param itemId
     */
    public void getMeasuredItemInpectNum(SendMessageActivity.RequestOnFinishListener listener,String datumsId,String itemId){
        HashMap<String,String> param = new HashMap<>();
        param.put("datumId",datumsId);
        param.put("itemId",itemId);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetMeasuredItemValueInspectNums.getMethodName(),param);
    }

    /**
     * 获取检测项目的检测值
     * @param listener
     * @param datumsId
     * @param itemId
     */
    public void getMeasuredItemInpectValues(SendMessageActivity.RequestOnFinishListener listener,String datumsId,String itemId){
        HashMap<String,String> param = new HashMap<>();
        param.put("datumId",datumsId);
        param.put("itemId",itemId);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetMeasuredValues.getMethodName(),param);
    }
    /**
     * 获取检测部位的信息与时间
     * @param listener
     * @param datumId
     */
    public void getMeasuredPart(SendMessageActivity.RequestOnFinishListener listener,String datumId){
        HashMap<String,String> param = new HashMap<>();
        param.put("datumId",datumId);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetMeasuredPart.getMethodName(),param);
    }
    /**
     * 获取实测项目的范围
     * @param listener
     * @param itemId
     */
    public void getMeasuredItemRange(SendMessageActivity.RequestOnFinishListener listener,String itemId){
        HashMap<String,String> param = new HashMap<>();
        param.put("itemId",itemId);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetMeasuredItemRanges.getMethodName(),param);
    }
    /**
     * 获取标段部位的模板
     * @param listener
     * @param partNo
     */
    public void getPartTemplates(SendMessageActivity.RequestOnFinishListener listener,String partNo){
        HashMap<String,String> param = new HashMap<>();
        param.put("partNo",partNo);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetPartTemplates.getMethodName(),param);
    }

    /**
     * 获取左边树形菜单
     * @param listener
     * @param objectkey
     */
    public void getMeasuredItems(SendMessageActivity.RequestOnFinishListener listener,String objectkey){
        HashMap<String,String> param = new HashMap<>();
        param.put("collId",objectkey);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetMeasuredItems.getMethodName(),param);
    }

    /**
     * 获取设计值
     * @param listener
     * @param objectkey
     * @param partNo
     */
    public void getDataDisgnDatas(SendMessageActivity.RequestOnFinishListener listener,String objectkey,String partNo){
        HashMap<String,String> param = new HashMap<>();
        param.put("collId",objectkey);
        param.put("partNo",partNo);
        request(listener, QualityServiceRequest.QualityServiceMethod.GetDataDisgnDatas.getMethodName(), param);
    }


    public void request(final SendMessageActivity.RequestOnFinishListener listener,String methodName,HashMap<String,String> params){
        new QualityServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                if (listener == null)
                    return;
                if (msg.mCode == 200)
                    listener.finish(msg.mData);
                else{
                    listener.fail(msg.mMsg);
                }
            }
        }).setMethodName(methodName)
                .addParam(params)
                .request();
    }
}
