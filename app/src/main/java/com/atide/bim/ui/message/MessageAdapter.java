package com.atide.bim.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atide.bim.MyApplication;
import com.atide.bim.entity.MessageEntity;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

/**
 * Created by atide on 2016/4/1.
 */
@EBean
public class MessageAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<MessageEntity> messageEntities;
    public MessageAdapter(Context context){
        mContext = context;
        messageEntities = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return messageEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return messageEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2,null);
        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
        TextView textView1 = (TextView)convertView.findViewById(android.R.id.text2);
        MessageEntity entity = messageEntities.get(position);
        textView.setText(entity.getSenduserid() + "  " + entity.getSenddate());
        textView1.setText(entity.getMessage());
        return convertView;
    }

    public void reload(ArrayList<MessageEntity> messageEntities){
        this.messageEntities.clear();
        if (messageEntities != null)
        this.messageEntities.addAll(messageEntities);
        notifyDataSetChanged();
    }
}
