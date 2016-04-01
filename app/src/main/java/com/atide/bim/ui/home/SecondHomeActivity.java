package com.atide.bim.ui.home;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.model.ProjectModel;
import com.atide.bim.model.User;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.request.PartWebServiceRequest;
import com.atide.bim.ui.picture.PictureListActivity_;
import com.atide.bim.ui.popup.MainActivityPopup;
import com.atide.bim.utils.WebServiceUtils;
import com.atide.ui.XListView;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atide on 2016/3/25.
 */
@EActivity(R.layout.main_home)
public class SecondHomeActivity extends MainActionBarActivity{

    private Context mContext;

    private ArrayList<ProjectModel> sectModels;
   @Extra
   ProjectModel projectModel;
    @Extra
    String paramKey;
    @Extra
    String methodName;
    @Bean
    MainHomeAdapter adapter;
    @ViewById(R.id.dataListView)
    XListView mXListView;
    @ViewById(R.id.title)
    TextView title;
    @ViewById
    ProgressBar loadingBar;




    @AfterViews
    void initUI(){

        title.setVisibility(View.GONE);
        mContext = this;

        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAdapter(adapter);
        adapter.setOnClickCallBack(new MainHomeAdapter.OnClickCallBack() {
            @Override
            public void callBack(ProjectModel model) {
                GlobalEntity.getInstance().setPartId(model.get_id());
                SecondHomeActivity_.intent(mContext).methodName("GetChildParts").paramKey("parentNo").projectModel(model).start();
            }

            @Override
            public void imageCallBack(ProjectModel model) {
                PictureListActivity_.intent(mContext).partNo(model.get_id()).partName(model.getTitle()).start();
            }
        });
        getUserSectsChild();
    }




    @UiThread
    void cleanUi(){

        if (sectModels != null)
            sectModels.clear();
        adapter.reload(sectModels);
    }




    /**
     * 加载对应顶部数据
     * @param
     */
    private void getUserSectsChild(){
        loadingBar.setVisibility(View.VISIBLE);
        new PartWebServiceRequest(new WsRequest(){
            @Override
            public void onResponse(WsResponseMessage msg) {
                loadingBar.setVisibility(View.GONE);
                if(WebServiceUtils.getResponseData(mContext,msg)){
                    initAdapter(msg.mData);
                }

            }
        })
                .setMethodName(methodName)
                .addParam(paramKey, projectModel.get_id())
                .request();


    }

    /**
     * 初始化listview适配器
     * @param data
     */
    private void initAdapter(String data){
        try {
            JSONArray array = new JSONObject(data).optJSONArray("data");
            if (array==null || array.length()<1){

                if (sectModels != null)
                    sectModels.clear();
                adapter.reload(sectModels);
                return;
            }
            if( sectModels == null){
                sectModels = new ArrayList<>();
            }
            sectModels.clear();
            for (int i=0;i<array.length();i++){
                ProjectModel model = new ProjectModel();
                model.set_id(array.optJSONObject(i).optString("partno"));
                model.setTitle(array.optJSONObject(i).optString("partname"));
                model.setImageNums(array.optJSONObject(i).optString("imagenums","0"));
                model.setDescript(array.optJSONObject(i).toString());
                sectModels.add(model);
            }
            adapter.reload(sectModels);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadResult(ProjectModel model){
        new DrawingMarkServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                Toast.makeText(mContext,msg.mData,Toast.LENGTH_LONG).show();
            }
        }).setMethodName("GetDrawingImageInfos ")
                .addParam("partNo",model.get_id())
                .request();
    }

    @Override
    public String fetchTitle() {
        return projectModel.getTitle();
    }

    @Override
    public Activity fetchClass() {
        return this;
    }
}

