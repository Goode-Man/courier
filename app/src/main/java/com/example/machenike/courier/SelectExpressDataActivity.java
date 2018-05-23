package com.example.machenike.courier;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.machenike.database.DBmanager;
import com.example.machenike.http.HttpConfig;
import com.example.machenike.http.HttpUtils;
import com.example.machenike.model.Courier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectExpressDataActivity extends AppCompatActivity {

    private DBmanager db;
    TextView select_express_data_back;
    EditText select_express_data_company,select_express_data_number;
    Button select_express_data_button;

    int courierid;
    Courier courier=null;
    int companyid;
    String companyname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_express_data);
        db=new DBmanager(SelectExpressDataActivity.this);
        Intent intent=getIntent();
        courierid=Integer.parseInt(intent.getStringExtra("courieridid"));
        courier=db.LoginSelectBycourierid(courierid);
        init();
        onclick();
    }

    private void init(){
        select_express_data_back= (TextView) findViewById(R.id.select_express_data_back);
        select_express_data_company= (EditText) findViewById(R.id.select_express_data_company);
        select_express_data_number= (EditText) findViewById(R.id.select_express_data_number);
        select_express_data_button= (Button) findViewById(R.id.select_express_data_button);
        new Thread(r).start();
    }

    private void onclick(){
        select_express_data_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        select_express_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=select_express_data_company.getText().toString().trim();
                String b=select_express_data_number.getText().toString().trim();
                String result=HttpConfig.kuaidi100_1+a+HttpConfig.kuaidi100_2+b;
                Intent intents=new Intent();
                intents.setClass(SelectExpressDataActivity.this,SelectExpressActivity.class);
                intents.putExtra("http",result);
                startActivity(intents);
            }
        });
    }


    Runnable r = new Runnable() {

        @Override
        public void run() {
            Map<String, Integer> map = new HashMap<>();
            map.put("depotid", courier.getDepotid());

            JSONObject jsonObj = new JSONObject(map);
            String s = HttpUtils.doPost(HttpConfig.selectDepot, jsonObj);

            try {
                JSONObject obj = new JSONObject(s);
                companyid=Integer.parseInt(obj.getString("companyid"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Map<String, Integer> maps = new HashMap<>();
            maps.put("companyid", companyid);

            JSONObject jsonObjs = new JSONObject(maps);
            String ss = HttpUtils.doPost(HttpConfig.selectCompany, jsonObjs);

            try {
                JSONObject obj = new JSONObject(ss);
                companyname = obj.getString("companyname");

                Message msg=handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    };

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    select_express_data_company.setText(companyname);
                    break;
            }
        }
    };
}
