package com.atide.bim.ui.picture;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.model.PartImageModel;
import com.atide.bim.model.ProjectModel;

import com.atide.bim.model.User;
import com.atide.bim.utils.WebServiceUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atide on 2016/3/18.
 */
@EBean
public class PictureListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PartImageModel> data;
    private String partNo;
    public PictureListAdapter(Context context){
        this.mContext = context;
    }
    public void setPartNo(String partNo){
        this.partNo = partNo;
    }
    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.picture_list_item_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.initData(data.get(position));
        return convertView;
    }

    public void reload(ArrayList<PartImageModel> models){
        this.data = models;
        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView title;
        ImageView pictrue;


        public ViewHolder(View view){

            title = (TextView)view.findViewById(R.id.title);
            pictrue = (ImageView)view.findViewById(R.id.picture);

            pictrue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureDetailActivity_.intent(mContext).partNo(partNo).imageKey(((PartImageModel)v.getTag()).getId()).start();
                }
            });
        }

        public void initData(PartImageModel model){

            title.setText(model.getName());
            pictrue.setTag(model);
            HashMap<String,String> param = new HashMap<>();
            param.put("partKey", partNo);
            param.put("imageKey", model.getId());
            HashMap<String,String> header = new HashMap<>();
            header.put("InnerToken", User.getLoginUser().getToken());
            header.put("UserHost", WebServiceUtils.getLocalIpAddress());
            ImageLoader.getInstance().displayImage("http://www.atidesoft.com/DrawingMarkService/", "GetDrawingImageNote", "http://220.164.192.83:9300/Services/DrawingMarkService.asmx", "TokenHeader", param, header, pictrue, MyApplication.getOptions());

        }

    }
}
