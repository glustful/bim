package com.atide.bim.helper;

import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.atide.bim.MyApplication;
import com.atide.bim.ui.input.SimpleTreeAdapter;
import com.atide.treeview.bean.Bean;
import com.atide.treeview.bean.FileBean;
import com.atide.treeview.tree.bean.Node;
import com.atide.treeview.tree.bean.TreeListViewAdapter;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atide on 2016/4/7.
 */
@EBean
public class TreeViewHelper {
    private Context mContext;
    private List<Bean> mDatas = new ArrayList<Bean>();

    private TreeListViewAdapter mAdapter;
    public TreeViewHelper(Context context){
        mContext = context;
    }

    public void show(ListView mTree){
        initDatas();

        try
        {
            mAdapter = new SimpleTreeAdapter<Bean>(mTree, mContext, mDatas, 0);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        Toast.makeText(MyApplication.getInstance(), node.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        mTree.setAdapter(mAdapter);

    }

    private void initDatas()
    {
        mDatas.add(new Bean(1, 0, "根目录1"));
        mDatas.add(new Bean(2, 0, "根目录2"));
        mDatas.add(new Bean(3, 0, "根目录3"));
        mDatas.add(new Bean(4, 0, "根目录4"));
        mDatas.add(new Bean(5, 1, "子目录1-1"));
        mDatas.add(new Bean(6, 1, "子目录1-2"));

        mDatas.add(new Bean(7, 5, "子目录1-1-1"));
        mDatas.add(new Bean(8, 2, "子目录2-1"));

        mDatas.add(new Bean(9, 4, "子目录4-1"));
        mDatas.add(new Bean(10, 4, "子目录4-2"));

        mDatas.add(new Bean(11, 10, "子目录4-2-1"));
        mDatas.add(new Bean(12, 10, "子目录4-2-3"));
        mDatas.add(new Bean(13, 10, "子目录4-2-2"));
        mDatas.add(new Bean(14, 9, "子目录4-1-1"));
        mDatas.add(new Bean(15, 9, "子目录4-1-2"));
        mDatas.add(new Bean(16, 9, "子目录4-1-3"));


    }
}
