package com.atide.bim.helper;

import android.content.Context;

import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.entity.User;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import org.androidannotations.annotations.EBean;

import java.util.HashMap;

/**
 * Created by atide on 2016/4/1.
 */
@EBean
public class RequestMessageHelper {
    private Context mContext;
    private String imageKey;
    private String themeId;
    public RequestMessageHelper(Context context){
        mContext = context;
    }

    /**
     * 请求消息体
     */
    public void requestMessage(SendMessageActivity.RequestOnFinishListener listener){
        request(listener, "GetMessage", null);

    }

    /**
     * 请求成员
     */
    public void requestGetUsers(SendMessageActivity.RequestOnFinishListener listener){
        request(listener, "GetMessageLeaguer", null);
    }

    /**
     * 添加成员
     */
    public void requestAddUsers(SendMessageActivity.RequestOnFinishListener listener,String userIds){
        HashMap<String,String> map = new HashMap<>();

        map.put("userIDs", userIds);
        request(listener, "AddMessageLeaguer", map);

    }

    /**
     * 删除成员
     */
    public void requestDeleteUsers(SendMessageActivity.RequestOnFinishListener listener,String userIds){
        HashMap<String,String> map = new HashMap<>();

        map.put("userIDs", userIds);
        request(listener, "DeleteMessageLeaguer", map);

    }

    /**
     * 发送消息
     */
    public void sendMessage(SendMessageActivity.RequestOnFinishListener listener,String message){
       HashMap<String,String> map = new HashMap<>();
        map.put("message",message);
        map.put("userID", User.getLoginUser().getUserId());
        request(listener, "SendMessage", map);

    }

    public void getSectRelevantUnitUser(final SendMessageActivity.RequestOnFinishListener listener){
        new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                if (msg.mCode == 200)
                    listener.finish(msg.mData);
                else{
                    listener.fail(msg.mMsg);
                }
            }
        }).setMethodName("GetSectRelevantUnitUser")
                .addParam("sectNo", GlobalEntity.getInstance().getSectId())

                .request();
    }

    /**
     * 删除消息体
     * @param messageId
     */
    public void deleteMessage(String messageId){
       new DrawingMarkServiceRequest(new WsRequest() {
           @Override
           public void onResponse(WsResponseMessage msg) {

           }
       }).setMethodName("DeleteMessage")
               .addParam("messageID",messageId)
               .request();
    }

    public void request(final SendMessageActivity.RequestOnFinishListener listener,String methodName,HashMap<String,String> params){
        new DrawingMarkServiceRequest(new WsRequest() {
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
                .addParam("drawingImageID", imageKey)
                .addParam("themeID",themeId)
                .addParam(params)
                .request();
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }
}
