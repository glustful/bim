package com.atide.bim.request;

import android.util.Base64;
import android.util.Log;

import com.atide.bim.Constant;
import com.atide.bim.model.User;
import com.atide.bim.utils.WebServiceUtils;
import com.atide.utils.net.webservice.WsHttpTransportSE;
import com.atide.utils.net.webservice.WsRequest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by atide on 2016/3/25.
 */
public class PrimaryKeyServiceRequest{
    private String NAMESPACE = "http://www.atidesoft.com/PrimaryKeyService/";
    private String Url = "/PrimaryKeyService.asmx";
    private String methodName = "";


    public String getUrl() {
        return Url;
    }


    public String getMethodName() {
        return methodName;
    }

    public PrimaryKeyServiceRequest setMethodName(String name){
        this.methodName = name;
        return this;
    }


    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public PrimaryKeyServiceRequest() {
       // super(request);
    }

    public Object request(int nums){
        SoapObject rpc = new SoapObject(NAMESPACE, methodName);

            rpc.addProperty("keyNums", nums);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);



            Element[] soapHeaders = new Element[1];
            soapHeaders[0] = new Element().createElement(NAMESPACE, "TokenHeader");
            try {
                    Element element = new Element().createElement(NAMESPACE, "InnerToken");
                    element.addChild(Node.TEXT, User.getLoginUser().getToken());
                    soapHeaders[0].addChild(Node.ELEMENT, element);
                Element element1 = new Element().createElement(NAMESPACE, "UserHost");
                element1.addChild(Node.TEXT, WebServiceUtils.getLocalIpAddress());
                soapHeaders[0].addChild(Node.ELEMENT, element1);

            } catch (Exception e) {
e.printStackTrace();
            }
            envelope.headerOut = soapHeaders;

        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        int timeout = 15000;
        WsHttpTransportSE ht = new WsHttpTransportSE(Constant.mHost + Url, timeout);

        ht.debug = true;
        try {
            ht.call(NAMESPACE + methodName, envelope);

            Object kk = envelope.bodyIn;


            return kk;
            // sendMessage(200, "网络请求成功", responseData);
        } catch (IOException e) {
            e.printStackTrace();
           // Log.e(TAG, "网络请求错误io-" + e.getMessage());
            // sendMessage(201, "网络请求失败", null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
           // Log.e(TAG, "网络请求错误xml-" + e.getMessage());
            // sendMessage(201, "网络请求失败", null);
        } catch (NullPointerException e) {
            e.printStackTrace();
           // Log.e(TAG, "网络请求错误null-" + e.getMessage());
            //sendMessage(200, "返回数据为空", null);
        }

        return null;
    }



}
