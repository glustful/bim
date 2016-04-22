package com.atide.bim.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.entity.ProjectModel;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/18.
 */
@EBean
public class MainHomeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProjectModel> data;
    private OnClickCallBack onClickCallBack;

    public OnClickCallBack getOnClickCallBack() {
        return onClickCallBack;
    }

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }


    public MainHomeAdapter(Context context){
        this.mContext = context;
        this.data = new ArrayList<>();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_home_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.initData(data.get(position));
        return convertView;
    }

    public void reload(ArrayList<ProjectModel> models){
        this.data.clear();
        this.data.addAll(models);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView photoCount;
        TextView title;
        ImageButton photo;

        public ViewHolder(View view){
            photoCount = (TextView)view.findViewById(R.id.count);
            title = (TextView)view.findViewById(R.id.title);
            photo = (ImageButton)view.findViewById(R.id.bphoto);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickCallBack != null){
                        onClickCallBack.callBack((ProjectModel) ((ViewHolder) v.getTag()).photo.getTag());
                    }
                }
            });
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickCallBack != null){
                        onClickCallBack.imageCallBack((ProjectModel)v.getTag());
                    }
                }
            });
        }

        public void initData(ProjectModel model){
            if (model.getImageNums()==null || model.getImageNums().equals("")){
                photoCount.setVisibility(View.GONE);
                //photo.setVisibility(View.GONE);
            }else{
                photoCount.setVisibility(View.VISIBLE);
            }
            photoCount.setText(model.getImageNums());
            title.setText(model.getTitle());
            photo.setTag(model);
        }
    }

    public static interface OnClickCallBack{
        void callBack(ProjectModel model);
        void imageCallBack(ProjectModel model);
    }
}
