package com.atide.bim.ui.quality;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.QualityDatumEntity;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

/**
 * Created by atide on 2016/4/18.
 */
@EBean
public class QualityDatumAdapter extends BaseAdapter{
    private ArrayList<QualityDatumEntity> qualityDatumEntities;
    private Context mContext;
    public QualityDatumAdapter(Context context){
        this.mContext = context;
        qualityDatumEntities = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return qualityDatumEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return qualityDatumEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2,null);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(qualityDatumEntities.get(position).getDatumName());
        textView.setTextColor(Color.BLACK);
        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text2);
        textView1.setText("当前状态："+DatumDataStatus.get(qualityDatumEntities.get(position).getApprovalStatus()).getStatusName());
        return convertView;
    }

    public void reload(ArrayList<QualityDatumEntity> entities){
        this.qualityDatumEntities.clear();
        this.qualityDatumEntities.addAll(entities);
        notifyDataSetChanged();
    }
}
