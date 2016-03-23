package com.atide.bim.ui.home;


import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.model.ProjectModel;
import com.atide.bim.sqlite.DatabaseManager;
import com.atide.bim.ui.popup.MainActivityPopup;
import com.atide.ui.XListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by atide on 2016/3/17.
 */
@EActivity(R.layout.main_home)
public class MainActivity extends Activity implements XListView.IXListViewListener{

    private PopupWindow popupWindow;
    @Bean
    MainHomeAdapter adapter;
    @ViewById(R.id.dataListView)
    XListView mXListView;
    @ViewById(R.id.title)
    TextView title;

    @Click(R.id.title)
    void changeProject(View view){
       // DatabaseManager.getInstance().openDatabase();
        new MainActivityPopup(this).setContentChangeListener(new ContentChangeListener(){
            @Override
            public void contentChange(String content) {
                title.setText(content);
                adapter.reload(initData());
            }
        }).showPopupWindow(view);
    }


    @AfterViews
    void initUI(){
        mXListView.setXListViewListener(this);

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.main_home_item,R.id.title,data);
        mXListView.setAdapter(adapter);
        adapter.reload(initData());
    }

    private ArrayList<ProjectModel> initData(){
        ArrayList<ProjectModel> data = new ArrayList<ProjectModel>();
        for (int i = 0;i<30;i++){
            ProjectModel model = new ProjectModel();
            model.setTitle(title.getText().toString()+"---"+i);
            model.setPhotoCount(i);
            data.add(model);
        }
        return data;
    }
    @Override
    public void onRefresh() {
       delay();
    }

    @Override
    public void onLoadMore() {
        delay();
    }

    @Background
    void delay(){
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){

        }
        completeLoad();
    }

    @UiThread
    void completeLoad(){
        mXListView.stopLoadMore();
        mXListView.stopRefresh();
        mXListView.setRefreshTime("刚刚");
    }

    public static interface ContentChangeListener{
        void contentChange(String content);
    }
}
