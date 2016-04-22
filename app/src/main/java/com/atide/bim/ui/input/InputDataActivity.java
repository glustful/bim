package com.atide.bim.ui.input;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.DesignEntity;
import com.atide.bim.entity.MeasuredItemEntity;
import com.atide.bim.entity.MeasuredItemInspectNumEntity;
import com.atide.bim.entity.MeasuredItemRangeEntity;
import com.atide.bim.entity.PartTempleteEntity;
import com.atide.bim.entity.QualityDatumEntity;
import com.atide.bim.entity.QualityHeaderEntity;
import com.atide.bim.helper.QualitySaveDataHelper;
import com.atide.bim.helper.RequestQualityHelper;
import com.atide.bim.helper.TreeViewHelper;
import com.atide.bim.request.QualityServiceRequest;
import com.atide.bim.ui.message.SendMessageActivity;
import com.atide.bim.ui.quality.DatumDataStatus;
import com.atide.bim.utils.Utils;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by atide on 2016/4/5.
 */
@EActivity(R.layout.input_data_main_layout)
public class InputDataActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private ArrayAdapter arrayAdapter;
    private AnimationDrawable mAnimationDrawable;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private MeasuredItemEntity measuredItemEntity;
    private ArrayList<DesignEntity> designEntities;
    private HashMap<String,HashMap<String,Object>> measuredValuew = new HashMap<>();
    private HashMap<String,String> ids = new HashMap<>();
    private HashMap<String,MeasuredItemRangeEntity> measuredItemRange = new HashMap<>();
    private QualityHeaderEntity qualityHeaderEntity;
    @Extra
    PartTempleteEntity partTempleteEntity;
    @Extra
    String partNo;
    @Extra
    QualityDatumEntity qualityDatumEntity;
    @ViewById(R.id.tablayout)
    TabLayout tabLayout;
    @ViewById(R.id.viewPager)
    ViewPager viewPager;
    @ViewById(R.id.loadingBar)
    ProgressBar loadingBar;
    @ViewById(R.id.value)
    TextView textViewValue;//规定值或误差
    @ViewById(R.id.method)
    TextView textViewMethod;//检查方法和频率
    @Bean
    TreeViewHelper treeViewHelper;
    @Bean
    RequestQualityHelper requestQualityHelper;
    @Bean
    QualitySaveDataHelper qualitySaveDataHelper;
    TabAdapter tabAdapter;
    private ArrayList<SuperFragment> fragments;
    private FindFragment findFragment;
    @AfterViews
    void initUI(){

        initDrawerlayout();
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        findFragment = FindFragment_.builder().arg("title", "实测值").build();
        treeViewHelper.setSelectedItemListener(new TreeItemSelectListener() {

            @Override
            public void selected(MeasuredItemEntity entity) {
                toolbar.setTitle(entity.getMeasureditemname());
                getItemRange(entity.getMeasureditemid());
                if (measuredItemEntity == null) {
                    measuredItemEntity = entity;

                }
                if (qualityDatumEntity != null) {
                    getMeasuredItemValues(entity);
                    return;
                }

                if (measuredItemEntity != entity) {

                    initFindFragment(entity);

                }



            }
        });
        loadingBar.setVisibility(View.VISIBLE);
        if (qualityDatumEntity != null){
            atomicInteger.addAndGet(3);
            initMeasuredPart();
        }else{
            atomicInteger.addAndGet(2);
            qualityHeaderEntity = new QualityHeaderEntity();
            qualitySaveDataHelper.setQualityHeaderEntity(qualityHeaderEntity);
        }
        initTreeViewData();
        requestDesignData();

    }

    private void initMeasuredPart(){
        requestQualityHelper.getMeasuredPart(new SendMessageActivity.RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                try {
                    JSONArray jsonArray = new JSONObject(msg).optJSONArray("data");
                    Gson gson = new Gson();
                    qualityHeaderEntity = gson.fromJson(jsonArray.optJSONObject(0).toString(), QualityHeaderEntity.class);
                    qualityHeaderEntity.setIdentity(qualityDatumEntity.getUsedUnit());
                    qualitySaveDataHelper.setQualityHeaderEntity(qualityHeaderEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (atomicInteger.decrementAndGet() == 0) {
                    initFragment();
                }
            }

            @Override
            public void fail(String msg) {

                if (atomicInteger.decrementAndGet() == 0) {
                    loadingBar.setVisibility(View.GONE);
                }
                Utils.showMsg(msg);
            }
        }, qualityDatumEntity.getDatumID());
    }

    private void initFragment(){
        loadingBar.setVisibility(View.GONE);
        fragments = new ArrayList<>();

        if (qualityHeaderEntity == null){
            qualityHeaderEntity = new QualityHeaderEntity();
            qualitySaveDataHelper.setQualityHeaderEntity(qualityHeaderEntity);
        }
        if (qualityDatumEntity != null){
            qualityHeaderEntity.setIdentity(qualityDatumEntity.getUsedUnit());
        }
        fragments.add(HeaderFragment_.builder().arg("entity", qualityHeaderEntity).arg("title", "部位信息").build());
        fragments.add(findFragment);
        fragments.add(DesignFragment_.builder().arg("title", "设计规范").arg("entities", designEntities).build());

        tabAdapter.reload(fragments);
        treeViewHelper.show(mDrawerLayout, lvLeftMenu);
    }




    private void initDrawerlayout() {
        findViews(); //获取控件
        toolbar.setTitle("数据录入");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }
    private void findViews() {

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

    private void initTreeViewData(){

        requestQualityHelper.getMeasuredItems(new SendMessageActivity.RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                loadingBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONObject(msg).optJSONArray("data");
                    if (jsonArray == null || jsonArray.length() < 1) {
                        Utils.showMsg("数据为空");
                        return;
                    }
                    treeViewHelper.initDatas(jsonArray);
                    //设置菜单列表

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showMsg("数据为空");
                }
                if (atomicInteger.decrementAndGet() == 0) {
                    initFragment();
                }

            }

            @Override
            public void fail(String msg) {
                if (atomicInteger.decrementAndGet() == 0) {
                    loadingBar.setVisibility(View.GONE);
                }
                Utils.showMsg(msg);
            }
        }, partTempleteEntity.getObjectkey());
    }

    private void initFindFragment(MeasuredItemEntity entity){
        HashMap result = findFragment.getValues();
        if (result != null) {
            measuredValuew.put(measuredItemEntity.getMeasureditemid(), result);
        }
        findFragment.initValues(measuredValuew.get(entity.getMeasureditemid()));
        measuredItemEntity = entity;
    }

    private void requestDesignData(){
        requestQualityHelper.getDataDisgnDatas(new SendMessageActivity.RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                initDesignData(msg);

                if (atomicInteger.decrementAndGet() == 0) {
                    initFragment();
                }
            }

            @Override
            public void fail(String msg) {
                if (atomicInteger.decrementAndGet() == 0) {
                    loadingBar.setVisibility(View.GONE);
                }
                Utils.showMsg(msg);
            }
        }, partTempleteEntity.getObjectkey(), partNo);
    }

    private void initDesignData(String json){
        try{
            JSONArray jsonArray = new JSONObject(json).optJSONArray("data");
            designEntities = new ArrayList<>();
            Gson gson = new Gson();
            for(int i=0;i<jsonArray.length();i++){
                DesignEntity entity = gson.fromJson(jsonArray.optString(i),DesignEntity.class);
                designEntities.add(entity);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getItemRange(final String itemId){
        MeasuredItemRangeEntity obj = measuredItemRange.get(itemId);
        if (obj!=null){
            textViewValue.setText(obj.getMeasureddetailname());
            textViewMethod.setText(obj.getAssessmentmethod());
            return;
        }

            requestQualityHelper.getMeasuredItemRange(new SendMessageActivity.RequestOnFinishListener() {
                @Override
                public void finish(String msg) {
                    try{
                        Gson gson = new Gson();
                        MeasuredItemRangeEntity obj = gson.fromJson(new JSONObject(msg).optJSONArray("data").optJSONObject(0).toString(),MeasuredItemRangeEntity.class);
                        textViewValue.setText(obj.getMeasureddetailname());
                        textViewMethod.setText(obj.getAssessmentmethod());
                        measuredItemRange.put(itemId,obj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String msg) {

                }
            },itemId);

    }

    private void getMeasuredItemValues(final MeasuredItemEntity entity){
        if (measuredValuew.get(entity.getMeasureditemid())!=null){
            initFindFragment(entity);

            return;
        }

        loadingBar.setVisibility(View.VISIBLE);
        final HashMap<String,Object> map = new HashMap<>();

        requestQualityHelper.getMeasuredItemInpectNum(new SendMessageActivity.RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                try {
                    JSONArray jsonArray = new JSONObject(msg).optJSONArray("data");
                    final String type = jsonArray.optJSONObject(0).optString("inputtype");
                    map.put("type", type.equals("0") ? "number" : "symbol");
                    ids.put(entity.getMeasureditemid(),jsonArray.optJSONObject(0).optString("inspectid"));
                    requestQualityHelper.getMeasuredItemInpectValues(new SendMessageActivity.RequestOnFinishListener() {
                        @Override
                        public void finish(String msg) {
                            try {
                                JSONArray jsonArray = new JSONObject(msg).optJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject item = jsonArray.optJSONObject(i);
                                    String value = type.equals("0")?item.optString("itemvalue"):item.optString("itemcharvalue");
                                    String sn = item.optString("itemvaluesn");
                                    map.put(sn, value);
                                    ids.put(type+entity.getMeasureditemid()+sn,item.optString("itemvalueid"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                                loadingBar.setVisibility(View.GONE);
                                measuredValuew.put(entity.getMeasureditemid(), map);
                                initFindFragment(entity);

                        }

                        @Override
                        public void fail(String msg) {

                                loadingBar.setVisibility(View.GONE);
                            initFindFragment(entity);

                        }
                    }, qualityDatumEntity.getDatumID(), entity.getMeasureditemid());
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("type", "number");
                    loadingBar.setVisibility(View.GONE);
                    measuredValuew.put(entity.getMeasureditemid(), map);
                    initFindFragment(entity);
                }



            }

            @Override
            public void fail(String msg) {

                    loadingBar.setVisibility(View.GONE);
                    initFindFragment(entity);

            }
        }, qualityDatumEntity.getDatumID(), entity.getMeasureditemid());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (qualityDatumEntity == null || DatumDataStatus.get(qualityDatumEntity.getApprovalStatus())==DatumDataStatus.DECLARESTATUS) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadingBar.setVisibility(View.VISIBLE);

       save();
        return true;
    }

    @Background
    void save(){
        for (SuperFragment fragment : fragments){
            fragment.save();
        }
        HashMap values = findFragment.getValues();
        if (values != null) {
            measuredValuew.put(measuredItemEntity.getMeasureditemid(),values );
        }
        qualitySaveDataHelper.setQualityDatumEntity(qualityDatumEntity);
        qualitySaveDataHelper.setPartTempleteEntity(partTempleteEntity);
        qualitySaveDataHelper.saveQualityDatum();
        qualitySaveDataHelper.saveMeasuredItemValue(measuredValuew,ids);
        qualitySaveDataHelper.setDesignEntitys(designEntities);
        qualitySaveDataHelper.saveDesignValue();
        qualitySaveDataHelper.saveHeaderPart();
        HashMap<String,String> result = qualitySaveDataHelper.save();
        saveToServer(result);
    }

    @UiThread
    void saveToServer(HashMap<String,String> params){

        new QualityServiceRequest(new WsRequest() {
            @Override
            public void onResponse(WsResponseMessage msg) {
                loadingBar.setVisibility(View.GONE);
                if (msg.mCode==200) {
                    finish();
                }else{
                    Utils.showMsg(msg.mMsg);
                }
            }
        }).setMethodName(QualityServiceRequest.QualityServiceMethod.SaveInpectDatas.getMethodName())
                .addParam(params)
                .request();
    }

    public static interface TreeItemSelectListener{
        void selected(MeasuredItemEntity entity);
    }
}
