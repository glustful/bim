package com.atide.bim.request;

import com.atide.bim.Constant;
import com.atide.utils.net.webservice.WsHttpTransportSE;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by atide on 2016/3/25.
 */
public class QualityServiceRequest extends Request{
    public static String NAMESPACE = "http://www.atidesoft.com/QualityService/";
    public static String Url = "/QualityService.asmx";
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

    public QualityServiceRequest(WsRequest request) {
        super(request);
    }


    @Override
    public WsResponseMessage request2() {
        init();
        SoapObject rpc = new SoapObject(wsRequest.getNameSpace(), wsRequest.getMethodName());

        LinkedHashMap<String, Object> map = wsRequest.getParams();
        if (map == null) {
            return sendMessage(201, "参数不能为空", null);
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            rpc.addProperty(key, entry.getValue());
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        LinkedHashMap<String, Object> head = wsRequest.getHeads();
        if (head.size() > 0) {
            Element[] header = new Element[1];
            header[0] = new Element().createElement(wsRequest.getNameSpace(), wsRequest.getTokenHeader());
            try {
                for (Map.Entry<String, Object> entry : head.entrySet()) {
                    Element element = new Element().createElement(wsRequest.getNameSpace(), entry.getKey());
                    element.addChild(Node.TEXT, entry.getValue());
                    header[0].addChild(Node.ELEMENT, element);
                }
            } catch (Exception e) {

            }
            envelope.headerOut = header;
        }
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);
        String url = Constant.mHost + getUrl();
        int timeout = 15000;
        WsHttpTransportSE ht = new WsHttpTransportSE(url, timeout);

        ht.debug = true;
        try {
            ht.call(wsRequest.getNameSpace() + wsRequest.getMethodName(), envelope);
            Object kk = envelope.getResponse();
            String responseData = kk.toString();//(String) envelope.getResponse();
           return sendMessage(200, "网络请求成功", responseData);
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
           // sendMessage(200, "返回数据为空", null);
        }
        return sendMessage(201, "网络请求失败", null);
    }

    private WsResponseMessage sendMessage(int code, String successMsg, String data) {
        WsResponseMessage wsMsg = new WsResponseMessage();
        wsMsg.mCode = code;
        wsMsg.mMsg = successMsg;
        wsMsg.mData = data;

       return wsMsg;

    }

    public static enum QualityServiceMethod {
        GetInpectDatums("GetInpectDatums"),//获取部位对应的主表信息
        GetMeasuredPart("GetMeasuredPart"),//获取表头,获取现场检验部位及检验时间
        ApprovalDatums("ApprovalDatums"),//质检资料审核
        DeclareDatas("DeclareDatas"),//质检资料申报
        GetDataDisgnDatas("GetDataDisgnDatas"),//获取实测项目设计值
        GetInspectDefaultFlowId("GetInspectDefaultFlowId"),//获取检查表使用的流程信息主键ID
        GetMeasuredItemRanges("GetMeasuredItemRanges"),//获取实测项目检测范围
        GetMeasuredItemValueInspectNums("GetMeasuredItemValueInspectNums"),//获取实测项的个数及类型
        GetMeasuredItems("GetMeasuredItems"),//获取实测项目
        GetMeasuredResultValue("GetMeasuredResultValue"),//获取实测项的实测结果
        GetMeasuredValues("GetMeasuredValues"),//获取实测项的值
        GetPartTemplates("GetPartTemplates"),//获取部位使用的检查表模板
        SaveInpectDatas("SaveInpectDatas");//保存现场检验数据;datumDatas:主表Json;itemValueDatas:检测值json;inpectPartDatas:监测部位json;inpectNumDatas:检测个数json；designDatas:设计值json
        private String methodName;
        private QualityServiceMethod(String methodName){
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }
    }
}
