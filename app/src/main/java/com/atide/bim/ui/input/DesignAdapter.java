package com.atide.bim.ui.input;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.atide.bim.R;
import com.atide.bim.entity.DesignEntity;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
@EBean
public class DesignAdapter extends BaseAdapter {
      private Context mContext;
        private ArrayList<DesignEntity> designEntities;
        public DesignAdapter(Context context) {
            mContext = context;
      
        }  
      
        @Override  
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

                convertView = LayoutInflater.from(mContext).inflate(R.layout.design_list_item,null);
               holder = new ViewHolder(convertView);
               convertView.setTag(holder);

            holder.init(designEntities.get(position),position);
      
            int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色
      
            convertView.setBackgroundColor(colors[position%2]);// 每隔item之间颜色不同
      
            return convertView;
        }

    @Override
    public int getCount() {
        return designEntities==null?0:designEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return designEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void reload(ArrayList<DesignEntity> entities){
        designEntities = entities;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView number,name,unit,symbol;
        EditText minvalue,maxvalue;
        public ViewHolder(View view){
            number = (TextView) view.findViewById(R.id.number);
            name = (TextView) view.findViewById(R.id.name);
            unit = (TextView) view.findViewById(R.id.unit);
            symbol = (TextView) view.findViewById(R.id.symbol);
            minvalue = (EditText) view.findViewById(R.id.minvalue);
            maxvalue = (EditText) view.findViewById(R.id.maxvalue);
        }

        public void init(final DesignEntity entity,int position){
            number.setText(String.valueOf(position + 1));
            name.setText(entity.getDatacollname());
            minvalue.setText(entity.getLowerlimitvalue());
            maxvalue.setText(entity.getUpperlimitvalue());
            symbol.setText(entity.getDatasysbom());
            minvalue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!entity.getDatatype().equals("0")) {
                        if (s.length() > 0) {
                            symbol.setText(">=" + s);
                            entity.setLowerlimitvalue(s.toString());
                            entity.setDatasysbom(">=" + s);
                        } else {
                            symbol.setText("");
                            entity.setLowerlimitvalue("");
                        }
                    }
                }
            });
            maxvalue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (entity.getDatatype().equals("0")) {
                        if (s.length() > 0) {
                            symbol.setText("<=" + s);
                            entity.setUpperlimitvalue(s.toString());
                            entity.setDatasysbom("<=" + s);
                        } else {
                            symbol.setText("");
                            entity.setUpperlimitvalue("");
                        }
                    }
                }
            });
        }
    }
}