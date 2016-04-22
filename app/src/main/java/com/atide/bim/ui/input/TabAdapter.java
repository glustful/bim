package com.atide.bim.ui.input;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cg on 2015/9/26.
 */
public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> list_fragment;                         //fragment列表




    public TabAdapter(FragmentManager fm) {
        super(fm);
        this.list_fragment = new ArrayList<>();

    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return list_fragment.get(position % list_fragment.size()).getArguments().getString("title");
    }

    public void reload(ArrayList<SuperFragment> fragments){
        list_fragment.clear();
        list_fragment.addAll(fragments);
        notifyDataSetChanged();
    }
}
