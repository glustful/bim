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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.helper.TreeViewHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by atide on 2016/4/5.
 */
@EActivity(R.layout.input_data_main_layout)
public class InputDataActivity extends MainActionBarActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;
    private AnimationDrawable mAnimationDrawable;
    @ViewById(R.id.tablayout)
    TabLayout tabLayout;
    @ViewById(R.id.viewPager)
    ViewPager viewPager;
    @Bean
    TreeViewHelper treeViewHelper;
    TabAdapter tabAdapter;
    private ArrayList<Fragment> fragments;
    @AfterViews
    void initUI(){
        initDrawerlayout();
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout.setupWithViewPager(viewPager);
        initFragment();

    }

    private void initFragment(){
        fragments = new ArrayList<>();

        fragments.add(FindFragment_.builder().arg("title", "数据录入").build());
        fragments.add(FindFragment_.builder().arg("title", "规范标准").build());
        fragments.add(FindFragment_.builder().arg("title", "属性录入").build());
        fragments.add(FindFragment_.builder().arg("title", "附加数据").build());

        tabAdapter.reload(fragments);
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
        //设置菜单列表
       treeViewHelper.show(lvLeftMenu);
    }
    private void findViews() {

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

    @Override
    public String fetchTitle() {
        return "数据录入";
    }

    @Override
    public Activity fetchClass() {
        return this;
    }
}
