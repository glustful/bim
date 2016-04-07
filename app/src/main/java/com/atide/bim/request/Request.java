package com.atide.bim.request;

import com.atide.bim.Constant;
import com.atide.bim.entity.User;
import com.atide.bim.utils.WebServiceUtils;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atide on 2016/3/25.
 */
public abstract class Request implements Serializable {
    WsRequest wsRequest;

    public Request(WsRequest request){
        wsRequest = request;
    }

    public Request addParam(String key,String value){
        wsRequest.addParam(key,value);
        return this;
    }

    public Request addParam(HashMap<String,String> param){
        if (param != null){
            for (Map.Entry<String,String> entry : param.entrySet()){
                wsRequest.addParam(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public abstract String getUrl();
    public abstract String getMethodName();
    public abstract String getNAMESPACE();
    public abstract Request setMethodName(String name);

    public void init(){
        wsRequest.setHost(Constant.mHost)
                .setUrl(getUrl())
                .setFlag(true)
                .setTokenHeader("TokenHeader")
                .addHead("InnerToken", User.getLoginUser().getToken())
                .addHead("UserHost", WebServiceUtils.getLocalIpAddress())
                .setNameSpace(getNAMESPACE())
                .setMethodName(getMethodName());
    }

    public WsResponseMessage request2(){
        return null;
    }
    public void  request(){
        wsRequest.setHost(Constant.mHost)
                .setUrl(getUrl())
                .setFlag(true)
                .setTokenHeader("TokenHeader")
                .addHead("InnerToken", User.getLoginUser().getToken())
                .addHead("UserHost", WebServiceUtils.getLocalIpAddress())
                .setNameSpace(getNAMESPACE())
                .setMethodName(getMethodName())

        .notifyRequest();
    }
}
