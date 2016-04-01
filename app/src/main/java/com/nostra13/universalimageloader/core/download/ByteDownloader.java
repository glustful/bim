package com.nostra13.universalimageloader.core.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.atide.utils.net.webservice.WsHttpTransportSE;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by atide on 2016/3/25.
 * WebService 下载图片
 * 图片以二进制数据返回给客户端
 * bye[]
 */
public class ByteDownloader implements ImageDownloader {
    String TAG = "BYTEDOWNLOADER";
    private String nameSpace;
    private String methodName;
    private String url;
    private String soapHeader;
    private HashMap<String,String> params;
    private HashMap<String,String> header;
    @Override
    public InputStream getStream(String imageUri, Object extra) throws IOException {
        return null;
    }

    public ByteDownloader(String nameSpace,String methodName,String url,String soapHeader,HashMap<String,String> params,HashMap<String,String> header){
        this.nameSpace = nameSpace;
        this.methodName = methodName;
        this.url = url;
        this.soapHeader = soapHeader;
        this.params = params;
        this.header = header;
    }

    public byte[] download(){
        SoapObject rpc = new SoapObject(nameSpace, methodName);


        if (params == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            rpc.addProperty(key, entry.getValue());
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);


        if (header.size() > 0) {
            Element[] soapHeaders = new Element[1];
            soapHeaders[0] = new Element().createElement(nameSpace, soapHeader);
            try {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    Element element = new Element().createElement(nameSpace, entry.getKey());
                    element.addChild(Node.TEXT, entry.getValue());
                    soapHeaders[0].addChild(Node.ELEMENT, element);
                }
            } catch (Exception e) {
                Log.e(TAG, "Parse element occur error.");
            }
            envelope.headerOut = soapHeaders;
        }
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        int timeout = 15000;
        WsHttpTransportSE ht = new WsHttpTransportSE(url, timeout);

        ht.debug = true;
        try {
            ht.call(nameSpace + methodName, envelope);

            Object kk = envelope.getResponse();
            String responseData = kk.toString();
            byte[] data = Base64.decode(responseData,Base64.DEFAULT);

            return data;
           // sendMessage(200, "网络请求成功", responseData);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "网络请求错误io-" + e.getMessage());
           // sendMessage(201, "网络请求失败", null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e(TAG, "网络请求错误xml-" + e.getMessage());
           // sendMessage(201, "网络请求失败", null);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "网络请求错误null-" + e.getMessage());
            //sendMessage(200, "返回数据为空", null);
        }

        return null;
    }
}
