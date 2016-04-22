package com.atide.bim.helper;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.atide.bim.MyApplication;
import com.atide.bim.entity.MeasuredItemEntity;
import com.atide.bim.entity.MessageEntity;
import com.atide.bim.ui.input.InputDataActivity;
import com.atide.bim.ui.input.SimpleTreeAdapter;
import com.atide.treeview.bean.Bean;
import com.atide.treeview.bean.FileBean;
import com.atide.treeview.tree.bean.Node;
import com.atide.treeview.tree.bean.TreeListViewAdapter;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atide on 2016/4/7.
 */
@EBean
public class TreeViewHelper {
    private Context mContext;
    private List<Bean> mDatas = new ArrayList<Bean>();
    private InputDataActivity.TreeItemSelectListener listener;

    private TreeListViewAdapter mAdapter;
    public TreeViewHelper(Context context){
        mContext = context;
    }


    public void show(final DrawerLayout drawerLayout,ListView mTree){


        try
        {
            mAdapter = new SimpleTreeAdapter<Bean>(mTree, mContext, mDatas, 0);
            if (listener != null) {
                listener.selected((MeasuredItemEntity)mAdapter.getSelectedNode().getTag());
            }
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    if (node.isLeaf()) {
                        drawerLayout.closeDrawers();
                        if (listener != null) {
                            listener.selected((MeasuredItemEntity)node.getTag());
                        }
                    }
                }

            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        mTree.setAdapter(mAdapter);


    }

    public TreeViewHelper initDatas(JSONArray jsonArray)
    {
        if (jsonArray == null)
            return this;
        Gson gson = new Gson();
        for (int i =0 ;i<jsonArray.length();i++){
            JSONObject object = jsonArray.optJSONObject(i);
            MeasuredItemEntity entity = gson.fromJson(object.toString(),MeasuredItemEntity.class);
            mDatas.add(new Bean(object.optString("measureditemid"),object.optString("parentno"),object.optString("measureditemname"),entity));
        }

        return this;

    }

    public void setSelectedItemListener(InputDataActivity.TreeItemSelectListener listener){
        this.listener = listener;
    }
}
