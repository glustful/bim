package com.atide.bim.ui.input;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.atide.android_horizontal_listview.HorizontalListView;
import com.atide.bim.R;
import com.atide.bim.entity.DesignEntity;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atide on 2016/4/13.
 */
@EFragment(R.layout.quality_measured_disgn)
public class DesignFragment extends SuperFragment {

    private ArrayList<DesignEntity> designEntities;
    @ViewById(R.id.horizontalListView)
    ListView horizontalListView;
    @Bean
    DesignAdapter designAdapter;
    @AfterViews
    void initUI(){
        designEntities = (ArrayList<DesignEntity>) getArguments().getSerializable("entities");
        horizontalListView.setAdapter(designAdapter);

        designAdapter.reload(designEntities);

    }



    @Override
    public void clean() {
        System.out.println();
    }

    @Override
    void save() {

    }
}
