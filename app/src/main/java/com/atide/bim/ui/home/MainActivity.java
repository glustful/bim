package com.atide.bim.ui.home;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.entity.GlobalEntity;
import com.atide.bim.helper.SaveMarkHelper;
import com.atide.bim.entity.ProjectModel;
import com.atide.bim.entity.User;
import com.atide.bim.request.PartWebServiceRequest;
import com.atide.bim.ui.input.InputDataActivity;
import com.atide.bim.ui.input.InputDataActivity_;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atide on 2016/3/17.
 */
@EActivity(R.layout.main_home)
public class MainActivity extends Activity implements XListView.IXListViewListener{

    private PopupWindow popupWindow;
    private Context mContext;
    private ArrayList<ProjectModel> projectModels;
    private ArrayList<ProjectModel> sectModels;
    private ProjectModel currentProjectModel;
    @Bean
    MainHomeAdapter adapter;
    @Bean
    SaveMarkHelper saveMarkHelper;
    @ViewById(R.id.dataListView)
    XListView mXListView;
    @ViewById(R.id.title)
    TextView title;
    @ViewById
    ProgressBar loadingBar;

    @Click(R.id.title)
    void changeProject(View view){

        new MainActivityPopup(this).setContent(projectModels).setContentChangeListener(new ContentChangeListener() {
            @Override
            public void contentChange(ProjectModel content) {
                if (content == currentProjectModel){
                    return;
                }
                title.setText(content.getTitle());
                loadingBar.setVisibility(View.VISIBLE);
               getUserSects(content.get_id());

            }
        }).showPopupWindow(view);
    }


    @AfterViews
    void initUI(){
        mContext = this;
        mXListView.setXListViewListener(this);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAdapter(adapter);
        adapter.setOnClickCallBack(new MainHomeAdapter.OnClickCallBack() {
            @Override
            public void callBack(ProjectModel model) {
                GlobalEntity.getInstance().setSectId(model.get_id());
                SecondHomeActivity_.intent(mContext).methodName("GetSectChildParts").paramKey("sectNo").projectModel(model).start();
            }

            @Override
            public void imageCallBack(ProjectModel model) {
                InputDataActivity_.intent(mContext).start();
            }
        });
        getUserProjects();
        saveMarkHelper.saveMarkInfos();
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 初始化顶部菜单项
     */
    @Background
    void delay(JSONArray array){
        try {

           if( projectModels == null){
                projectModels = new ArrayList<>();
            }
            projectModels.clear();
            for(int i=0;i<array.length();i++){
                ProjectModel model = new ProjectModel();
                model.set_id(array.optJSONObject(i).optString("prjid"));
                model.setTitle(array.optJSONObject(i).optString("prjname"));
                model.setDescript(array.optJSONObject(i).toString());
                if (i==0){
                    currentProjectModel = model;
                }
                projectModels.add(model);
            }
        }catch (Exception e){

        }

    }

    @UiThread
    void cleanUi(){
        title.setText("");

        if (projectModels!=null)
        projectModels.clear();
        if (sectModels != null)
        sectModels.clear();
        adapter.reload(sectModels);
    }

    @UiThread
    void completeLoad(){
        mXListView.stopLoadMore();
        mXListView.stopRefresh();
        mXListView.setRefreshTime("刚刚");
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().getHistory().clear();
        super.onDestroy();
    }

    /**
     * 加载顶部数据
     */
    private void getUserProjects(){
        loadingBar.setVisibility(View.VISIBLE);
        new PartWebServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                if (WebServiceUtils.getResponseData(mContext,msg)){
                    try {
                        JSONArray array = new JSONObject(msg.mData).optJSONArray("data");
                        if (array == null || array.length()<1){
                            cleanUi();
                            return;
                        }
                        delay(array);
                        JSONObject obj = array.optJSONObject(0);
                        title.setText(obj.optString("prjname"));


                        getUserSects(obj.optString("prjid"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).addParam("userId", User.getLoginUser().getUserId())
                .setMethodName("GetUserProjects")
                .request();
    }

    /**
     * 加载对应顶部数据
     * @param parentId
     */
    private void getUserSects(String parentId){
        MyApplication.getInstance().addHistoryVistor(initHistory(),0);
        new PartWebServiceRequest(new WsRequest(){
            @Override
            public void onResponse(WsResponseMessage msg) {
                loadingBar.setVisibility(View.GONE);
                if(WebServiceUtils.getResponseData(mContext,msg)){
                    initAdapter(msg.mData);
                }

            }
        })
                .setMethodName("GetUserSects")
                .addParam("userId", User.getLoginUser().getUserId())
                .addParam("projectId", parentId).request();


    }

    private HashMap<String,Object> initHistory(){
        HashMap<String,Object> item = new HashMap<>();
        item.put("activity",MainActivity_.class);
        item.put("title",title.getText().toString());
        return item;
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
                model.set_id(array.optJSONObject(i).optString("sectid"));
                model.setTitle(array.optJSONObject(i).optString("sectname"));
                model.setDescript(array.optJSONObject(i).toString());
                sectModels.add(model);
            }
            adapter.reload(sectModels);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static interface ContentChangeListener{
        void contentChange(ProjectModel content);
    }

}
