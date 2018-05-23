package com.example.machenike.courier;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.machenike.database.DBmanager;
import com.example.machenike.model.Staterecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateRecordActivity extends AppCompatActivity {

    TextView state_record_back,state_record_state1,state_record_state2;
    ListView state_record_list;
    private DBmanager db;

    int courierid;
    MyAdapter adapter;
    private List<Map<String,String>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_record);
        db=new DBmanager(StateRecordActivity.this);
        init();
        Intent intent=getIntent();
        courierid=Integer.parseInt(intent.getStringExtra("courieridid"));
        adapter=new StateRecordActivity.MyAdapter(this);
        datalist=buildData(courierid,2);
        state_record_list.setAdapter(adapter);
        onclick();
    }

    private void init(){
        state_record_back= (TextView) findViewById(R.id.state_record_back);
        state_record_state1= (TextView) findViewById(R.id.state_record_state1);
        state_record_state2= (TextView) findViewById(R.id.state_record_state2);
        state_record_list= (ListView) findViewById(R.id.state_record_list);
    }

    private void onclick(){
        state_record_state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state_record_state1.setTextColor(Color.parseColor("#00c8ff"));
                state_record_state2.setTextColor(Color.parseColor("#60000000"));
                datalist=buildData(courierid,2);
                state_record_list.setAdapter(adapter);
            }
        });
        state_record_state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state_record_state1.setTextColor(Color.parseColor("#60000000"));
                state_record_state2.setTextColor(Color.parseColor("#00c8ff"));
                datalist=buildData(courierid,4);
                state_record_list.setAdapter(adapter);
            }
        });
        state_record_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public MyAdapter(Context context) {
            this.inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StateRecordActivity.ViewHolder holder;
            if (convertView==null){
                holder=new StateRecordActivity.ViewHolder();
                convertView= inflater.inflate(R.layout.record_list,null,true);
                holder.record_list_expressid= (TextView) convertView.findViewById(R.id.record_list_expressid);
                holder.record_list_timename= (TextView) convertView.findViewById(R.id.record_list_timename);
                holder.record_list_time= (TextView) convertView.findViewById(R.id.record_list_time);
                convertView.setTag(holder);
            }else {
                holder= (StateRecordActivity.ViewHolder) convertView.getTag();
            }
            holder.record_list_expressid.setText(datalist.get(position).get("expressid"));
            holder.record_list_timename.setText(datalist.get(position).get("statename"));
            holder.record_list_time.setText(datalist.get(position).get("time"));
            return convertView;
        }
    }

    private final class ViewHolder{
        TextView record_list_expressid;
        TextView record_list_timename;
        TextView record_list_time;
    }

    private List<Map<String,String>> buildData(int id,int state){
        List<Map<String,String>> datas=new ArrayList<>();
        List<Staterecord> listExpress=new ArrayList<Staterecord>();
        listExpress=db.selectRecondByCourierid(id,state);
        for(int i=0;i<listExpress.size();i++){
            Staterecord staterecord=listExpress.get(i);
            Map<String, String> map=new HashMap<String, String>();
            String statename=null;
            if (state==2){
                statename="签收时间:";
            }else if(state==4){
                statename="揽件时间:";
            }
            map.put("statename",statename);
            map.put("expressid",staterecord.getExpressid());
            map.put("time",staterecord.getDatetimes());
            datas.add(map);
        }
        return datas;
    }
}
