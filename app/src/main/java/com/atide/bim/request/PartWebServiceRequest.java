package com.atide.bim.request;

import com.atide.utils.net.webservice.WsRequest;

import java.io.Serializable;

/**
 * Created by atide on 2016/3/25.
 */
public class PartWebServiceRequest extends Request{
    private String NAMESPACE = "http://www.atidesoft.com/PartWebService/";
    private String Url = "/PartWebService.asmx";
    private String methodName = "";

    @Override
    public String getUrl() {
        return Url;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public Request setMethodName(String name){
        this.methodName = name;
        return this;
    }

    @Override
    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public PartWebServiceRequest(WsRequest request) {
        super(request);
    }



    public static enum RequestMethod{
        GetChildParts,
        GetSectChildParts,
        GetUserProjects,
        GetUserSects
    }

}
