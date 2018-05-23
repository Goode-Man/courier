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
import com.example.machenike.model.Express;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressListActivity extends AppCompatActivity {

    private TextView express_list_back,express_list_state1,express_list_state2,express_list_state3,express_list_state4;
    private ListView express_list_list;
    MyAdapter adapter;
    private List<Map<String,String>> datalist;
    private DBmanager db;
    int courierid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_list);
        db=new DBmanager(ExpressListActivity.this);
        Intent intent=getIntent();
        courierid=Integer.parseInt(intent.getStringExtra("courieridid"));
        adapter=new MyAdapter(this);
        init();
        onclick();
    }


    private void init(){
        express_list_back= (TextView) findViewById(R.id.express_list_back);
        express_list_state1= (TextView) findViewById(R.id.express_list_state1);
        express_list_state2= (TextView) findViewById(R.id.express_list_state2);
        express_list_state3= (TextView) findViewById(R.id.express_list_state3);
        express_list_state4= (TextView) findViewById(R.id.express_list_state4);
        express_list_list= (ListView) findViewById(R.id.express_list_list);
        datalist=buildData(1);
        express_list_list.setAdapter(adapter);

    }

    private void onclick(){
        express_list_state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                express_list_state1.setTextColor(Color.parseColor("#00c8ff"));
                express_list_state2.setTextColor(Color.parseColor("#60000000"));
                express_list_state3.setTextColor(Color.parseColor("#60000000"));
                express_list_state4.setTextColor(Color.parseColor("#60000000"));
                datalist=buildData(1);
                express_list_list.setAdapter(adapter);
            }
        });
        express_list_state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                express_list_state1.setTextColor(Color.parseColor("#60000000"));
                express_list_state2.setTextColor(Color.parseColor("#00c8ff"));
                express_list_state3.setTextColor(Color.parseColor("#60000000"));
                express_list_state4.setTextColor(Color.parseColor("#60000000"));
                datalist=buildData(2);
                express_list_list.setAdapter(adapter);
            }
        });
        express_list_state3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                express_list_state1.setTextColor(Color.parseColor("#60000000"));
                express_list_state2.setTextColor(Color.parseColor("#60000000"));
                express_list_state3.setTextColor(Color.parseColor("#00c8ff"));
                express_list_state4.setTextColor(Color.parseColor("#60000000"));
                datalist=buildDatas(3);
                express_list_list.setAdapter(adapter);
            }
        });
        express_list_state4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                express_list_state1.setTextColor(Color.parseColor("#60000000"));
                express_list_state2.setTextColor(Color.parseColor("#60000000"));
                express_list_state3.setTextColor(Color.parseColor("#60000000"));
                express_list_state4.setTextColor(Color.parseColor("#00c8ff"));
                datalist=buildDatas(4);
                express_list_list.setAdapter(adapter);
            }
        });

        express_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressListActivity.this.finish();
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
            ExpressListActivity.ViewHolder holder;
            if (convertView==null){
                holder=new ExpressListActivity.ViewHolder();
                convertView= inflater.inflate(R.layout.express_list,null,true);
                holder.express_list_name= (TextView) convertView.findViewById(R.id.express_list_name);
                holder.express_list_tel= (TextView) convertView.findViewById(R.id.express_list_tel);
                holder.express_list_expressid= (TextView) convertView.findViewById(R.id.express_list_expressid);
                holder.express_list_adress= (TextView) convertView.findViewById(R.id.express_list_adress);
                convertView.setTag(holder);
            }else {
                holder= (ExpressListActivity.ViewHolder) convertView.getTag();
            }
            holder.express_list_name.setText(datalist.get(position).get("name"));
            holder.express_list_tel.setText(datalist.get(position).get("tel"));
            holder.express_list_expressid.setText(datalist.get(position).get("express"));
            holder.express_list_adress.setText(datalist.get(position).get("adress"));
            return convertView;
        }
    }





    private final class ViewHolder{
        TextView express_list_expressid;
        TextView express_list_name;
        TextView express_list_tel;
        TextView express_list_adress;
    }

    private List<Map<String,String>> buildData(int state){
        List<Map<String,String>> datas=new ArrayList<>();
        List<Express> listExpress=new ArrayList<Express>();
        listExpress=db.selectExpressByState(state);
        for(int i=0;i<listExpress.size();i++){
            Express express=listExpress.get(i);
            Map<String, String> map=new HashMap<String, String>();
            String expressid=express.getExpressid();
            if (expressid==null){
                expressid="待添加订单号";
            }
            map.put("name",express.getAddressee());
            map.put("tel",express.getAddresseetel());
            map.put("express",expressid);
            map.put("adress",express.getAddresseecity()+express.getAddresseeadress());
            datas.add(map);
        }
        return datas;
    }

    private List<Map<String,String>> buildDatas(int state){
        List<Map<String,String>> datas=new ArrayList<>();
        List<Express> listExpress=new ArrayList<Express>();
        listExpress=db.selectExpressByState(state);
        for(int i=0;i<listExpress.size();i++){
            Express express=listExpress.get(i);
            Map<String, String> map=new HashMap<String, String>();
            String expressid=express.getExpressid();
            if (expressid==null){
                expressid="待添加订单号";
            }
            map.put("name",express.getSender());
            map.put("tel",express.getSendertel());
            map.put("express",expressid);
            map.put("adress",express.getSendercity()+express.getSenderadress());
            datas.add(map);
        }
        return datas;
    }
}
