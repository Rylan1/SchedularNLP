package com.example.nlp_sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nlp_sch.model.Sch_DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class arrayAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<Sch_DB> data;
    public arrayAdapter(Context context, ArrayList<Sch_DB> data){
        mcontext=context;
        this.data=data;

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.rowdata,parent,false);
        }
        Sch_DB tempData=(Sch_DB) getItem(position);
        TextView topic=(TextView)convertView.findViewById(R.id.topicTxt);
        TextView sTime=(TextView)convertView.findViewById(R.id.satrtTxt);
        TextView eTime=(TextView)convertView.findViewById(R.id.endTxt);
        TextView title=(TextView)convertView.findViewById(R.id.title2);
        topic.setText(tempData.getTopic());
        SimpleDateFormat f=new SimpleDateFormat("hh:mm aa");
        sTime.setText(f.format(tempData.getStart()));
        eTime.setText(f.format(tempData.getEnd()));
        title.setText(tempData.getTitle());
        return convertView;
    }
}
