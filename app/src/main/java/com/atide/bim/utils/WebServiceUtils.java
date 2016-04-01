package com.atide.bim.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.atide.bim.MyApplication;
import com.atide.bim.entity.ShapeEntity;
import com.atide.bim.model.User;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.utils.net.httpservice.HtResponseMessage;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by yuan on 15-3-3.
 */
public class WebServiceUtils {
    private static final String TAG = "NetUtils";

//    /**
//     * 登录系统的web服务，将需要的参数以JSONObject的方式传入即可
//     *
//     * @param request
//     * @param object
//     * @return
//     */
//    public static boolean loginService(Context context, WsRequest request, JSONObject object) {
//        request = getBasicRequest(context, request, 0);
//        request.setNameSpace("http://www.atidesoft.com/AuthenticateService/")
//                .setMethodName("Login")
//                .setUrl("AuthenticateService.asmx");
//        request = setParam(request, object);
//        request.notifyRequest();
//        return true;
//    }

   /* private static WsRequest setParam(WsRequest request, JSONObject object) {
        try {
            //遍历json对象
            Iterator<String> objkey = object.keys();
            while (objkey.hasNext()) {
                String key = objkey.next().toString();
                String value = object.getString(key);
                request.addParam(key, value);
                Log.e(TAG, "key is " + key + ", " + "value is " + value);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json format error");
        }
        return request;
    }

    private static WsRequest getBasicRequest(Context context, WsRequest request, int mode) {
        request.setHost(Config.HOST_APPROVAL)
                .setFlag(true)
                .setTokenHeader("TokenHeader")
                .addHead("UserHost", OAUtils.getLocalIpAddress(context));
        if (mode == 1) {
            request.addHead("InnerToken", UserAccount.getInstance().getmId());
        }
        return request;
    }

    */

   public static WsRequest setToken(WsRequest request){
        request.setTokenHeader("TokenHeader")
                .addHead("InnerToken", User.getLoginUser().getToken())
                .addHead("UserHost", getLocalIpAddress());
        return request;
    }
    /**
     * 获取当前ip地址
     *
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) MyApplication.getInstance()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错了!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
/*
    public static void getSendApproval(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QueryWaitApprovalSendDocDatas");
    }

    private static boolean oaService(Context context, WsRequest request, JSONObject object, String method) {
        request = getBasicRequest(context, request, 1);
        request.setNameSpace("http://www.atidesoft.com/OAWebService/")
                .setMethodName(method)
                .setUrl(Config.URL_APPROVAL_OA);
        request = setParam(request, object);
        request.notifyRequest();
        return true;
    }

    public static void getSendApprovaled(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QueryApprovaledSendDocDatas");
    }

    public static void getSendApprovalIdea(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QuerySendApprovalIdea");
    }

    public static void getSendFlow(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QuerySendFlowDatas");
    }

    public static void getSendAffixesDatas(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "GetSendDocAffixesDatas");
    }

    public static void getSendDocInfos(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "GetSendDocInfos");
    }

    public static void getWaitingSendReturn(Context context, WsRequest request, JSONObject object){
        OAUtils.oaService(context, request, object, "ReturnSendDocument");
    }

    public static void approvaledSendWaiting(Context context, WsRequest request, JSONObject object){
        OAUtils.oaService(context, request, object, "ApprovalSendDocument");
    }




    public static void getReceiveApproval(Context context, WsRequest request, JSONObject object) {
        Log.e(TAG, "传入的参数--"+object.toString());
        OAUtils.oaService(context, request, object, "QueryWaitApprovalReceivedDocDatas");
    }

    public static void getReceiveApprovalIdea(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QueryReceivedApprovalIdea");
    }

    public static void isMobileCanApproval(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "CanMobileApproval");
    }

    public static void getReceiveAffixesDatas(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "GetReceivedDocAffixesDatas");
    }

    public static void getReceiveDocInfos(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "GetReceivedDocInfos");
    }

    public static void getReceiveApprovaled(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QueryApprovaledReceivedDocDatas");
    }

    public static void getReceiveFlow(Context context, WsRequest request, JSONObject object) {
        OAUtils.oaService(context, request, object, "QueryReceivedFlowDatas");
    }

    public static void getWaitingReceiveReturn(Context context, WsRequest request, JSONObject object){
        OAUtils.oaService(context, request, object, "ReturnReceivedDocument");
    }


    public static void approvaledReceiveWaiting(Context context, WsRequest request, JSONObject object){
        OAUtils.oaService(context, request, object, "ApprovalReceivedDocument");
    }
*/
    public static boolean getResponseData(Context context, WsResponseMessage msg) {

        if (msg.mCode != 200) {
            Toast.makeText(context, "网络请求异常，请检查网络", Toast.LENGTH_SHORT).show();
            return false;
        }else if(msg.mData == null || msg.mData.length() < 1){
            Toast.makeText(context, "返回的信息为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            JSONObject jResponse = new JSONObject(msg.mData);
            if (null == jResponse || jResponse.length() < 1) {
                Toast.makeText(context, "获取数据时发生错误，请联系管理员.", Toast.LENGTH_SHORT).show();
                return false;
            }

            Boolean success = jResponse.optBoolean("succeed");
            if (!success) {
                String dataMsg = jResponse.optString("msg");
                Toast.makeText(context, dataMsg, Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (JSONException e) {
            Toast.makeText(context, "获取数据时发生错误，请联系管理员.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getResponseData(Context context, HtResponseMessage msg) {

        if (msg.mCode != 200) {
            Toast.makeText(context, "网络请求异常，请检查网络", Toast.LENGTH_SHORT).show();
            return false;
        }else if(msg.mData == null || msg.mData.length() < 1){
            return false;
        }

        try {
            JSONObject jResponse = new JSONObject(msg.mData);
            if (null == jResponse || jResponse.length() < 1) {
                Toast.makeText(context, "获取数据时发生错误，请联系管理员.", Toast.LENGTH_SHORT).show();
                return false;
            }

            Boolean success = jResponse.optBoolean("succeed");
            if (!success) {
                String dataMsg = jResponse.optString("msg");
                Toast.makeText(context, dataMsg, Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (JSONException e) {
            Toast.makeText(context, "获取数据时发生错误，请联系管理员.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

    public static void downloadShapeInfo(){
        new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {

                    try {
                        JSONArray array = new JSONObject(msg.mData).optJSONArray("data");
                        if (array==null || array.length()<1)
                            return;
                        Gson gson = new Gson();

                        for (int i=0;i<array.length();i++){
                            ShapeEntity entity = gson.fromJson(array.optString(i),ShapeEntity.class);
                            MyApplication.getInstance().setShapeEntitie(entity);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            }
        }).setMethodName("GetImageMarkTypeEntity")
                .request();
    }
}
