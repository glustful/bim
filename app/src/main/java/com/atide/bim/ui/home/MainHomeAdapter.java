package com.atide.bim.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.model.ProjectModel;
import com.atide.bim.ui.picture.PictureListActivity_;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

/**
 * Created by atide on 2016/3/18.
 */
@EBean
public class MainHomeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProjectModel> data;
    public MainHomeAdapter(Context context){
        this.mContext = context;
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
        this.data = models;
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
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity_.intent(mContext).start();
                }
            });
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureListActivity_.intent(mContext).start();
                }
            });
        }

        public void initData(ProjectModel model){
            photoCount.setText(String.valueOf(model.getPhotoCount()));
            title.setText(model.getTitle());
            photo.setTag(model);
        }
    }
}
