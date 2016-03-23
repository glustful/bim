package com.atide.bim.ui.picture;

import android.app.Activity;

import com.atide.bim.R;
import com.atide.bim.model.ProjectModel;
import com.atide.ui.XListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/18.
 */
@EActivity(R.layout.picture_list_layout)
public class PictureListActivity extends Activity {

    @Bean
    PictureListAdapter adapter;
    @ViewById(R.id.dataListView)
    XListView mXListView;

    @AfterViews
    void initUI(){
        mXListView.setAdapter(adapter);
        loadData();
    }

    private void loadData(){
        ArrayList<ProjectModel> models = new ArrayList<>();
        for (int i=0;i<10;i++){
            ProjectModel model = new ProjectModel();
            model.setTitle("图纸"+i);
            model.setDescript("http://pic41.nipic.com/20140521/2531170_143935854000_2.jpg");
            models.add(model);
        }
        adapter.reload(models);
    }
}
