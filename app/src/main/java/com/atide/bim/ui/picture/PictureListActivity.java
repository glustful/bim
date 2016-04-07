package com.atide.bim.ui.picture;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.PartImageModel;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.utils.WebServiceUtils;
import com.atide.ui.XListView;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atide on 2016/3/18.
 */
@EActivity(R.layout.picture_list_layout)
public class PictureListActivity extends MainActionBarActivity {
    private Context mContext;
    private ArrayList<PartImageModel> partImageModels;
    @Extra
    String partNo;
    @Extra
    String partName;
    @Bean
    PictureListAdapter adapter;
    @ViewById(R.id.dataListView)
    XListView mXListView;
    @ViewById
    ProgressBar loadingBar;

    @AfterViews
    void initUI(){
        mContext = this;
        adapter.setPartNo(partNo);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAdapter(adapter);
        loadData();
        getThemeDatas();
    }

    private void loadData(){
        loadingBar.setVisibility(View.VISIBLE);
       new DrawingMarkServiceRequest(new WsRequest() {
           @Override
           public void onResponse(WsResponseMessage msg) {
               loadingBar.setVisibility(View.GONE);
                if (WebServiceUtils.getResponseData(mContext,msg)){
                    initAdapter(msg.mData);
                }
           }
       }).setMethodName("GetDrawingImageInfos")
               .addParam("partNo",partNo)
               .request();

    }

    private void initAdapter(String data){
        try {
            JSONArray array = new JSONObject(data).optJSONArray("data");
            if (array==null || array.length()<1){
                adapter.reload(null);
                return;
            }
            if (partImageModels == null)
                partImageModels = new ArrayList<>();
            partImageModels.clear();
            for (int i=0;i<array.length();i++){
                PartImageModel model = new PartImageModel();
                JSONObject obj = array.optJSONObject(i);
                if (obj == null)
                    continue;
                model.setId(obj.optString("drawingimageid"));
                model.setFileId(obj.optString("drawingfileid"));
                model.setSectNo(obj.optString("sectno"));
                model.setName(obj.optString("drawingimagename"));
                model.setDescript(obj.toString());
                partImageModels.add(model);
            }
            adapter.reload(partImageModels);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取主题数据
     */
    private void getThemeDatas(){
         ArrayList jsonArray = MyApplication.getInstance().getThemeDatas();
        if (jsonArray == null || jsonArray.size()<1){
            new DrawingMarkServiceRequest(new WsRequest() {
                @Override
                public void onResponse(WsResponseMessage msg) {
                    if (msg.mCode==200){
                        try{
                            JSONArray jsonArray = new JSONObject(msg.mData).optJSONArray("data");
                            if (jsonArray!=null && jsonArray.length()>0){
                                ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();
                                for (int i=0;i<jsonArray.length();i++){
                                    HashMap<String,String> map = new HashMap<String, String>();
                                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                                    map.put("id",jsonObject.optString("themetypeid"));
                                    map.put("name",jsonObject.optString("themename"));
                                    map.put("code",jsonObject.optString("themecode"));
                                    map.put("order",jsonObject.optString("ordersn"));
                                    result.add(map);
                                }
                                MyApplication.getInstance().setThemeDatas(result);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).setMethodName("GetThemeDatas")
                    .request();
        }
    }

    @Override
    public String fetchTitle() {
        return partName;
    }

    @Override
    public Activity fetchClass() {
        return this;
    }
}
