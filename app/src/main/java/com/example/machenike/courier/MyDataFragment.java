package com.example.machenike.courier;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machenike.database.DBmanager;
import com.example.machenike.http.HttpConfig;
import com.example.machenike.http.HttpUtils;
import com.example.machenike.model.Courier;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MyDataFragment extends Fragment {
    private View view;
    private DBmanager db;
    Bundle data;
    Courier courier=null;

    int depotid;
    String depotname;
    String depottel;
    int companyid;
    String companyname;
    String companytel;

    String passwords;
    String phones;

    TextView fragment_mydata_name,fragment_mydata_tel,fragment_mydata_company,fragment_mydata_depot;
    Button fragment_mydata_updatetel,fragment_mydata_updatepwd,fragment_mydata_exit;
    LinearLayout fragment_mydata_layout_company,fragment_mydata_layout_depot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data=getArguments();
        view=inflater.inflate(R.layout.fragment_my_data, container, false);
        db=new DBmanager(getActivity());
        courier=db.LoginSelectBycourierid(Integer.parseInt(data.getString("courierid")));
        init();
        onclick();
        return view;
    }

    private void init(){
        fragment_mydata_name= (TextView) view.findViewById(R.id.fragment_mydata_name);
        fragment_mydata_tel= (TextView) view.findViewById(R.id.fragment_mydata_tel);
        fragment_mydata_company= (TextView) view.findViewById(R.id.fragment_mydata_company);
        fragment_mydata_depot= (TextView) view.findViewById(R.id.fragment_mydata_depot);
        fragment_mydata_updatetel= (Button) view.findViewById(R.id.fragment_mydata_updatetel);
        fragment_mydata_updatepwd= (Button) view.findViewById(R.id.fragment_mydata_updatepwd);
        fragment_mydata_exit= (Button) view.findViewById(R.id.fragment_mydata_exit);
        fragment_mydata_layout_company= (LinearLayout) view.findViewById(R.id.fragment_mydata_layout_company);
        fragment_mydata_layout_depot= (LinearLayout) view.findViewById(R.id.fragment_mydata_layout_depot);
        fragment_mydata_name.setText(courier.getCouriername());
        fragment_mydata_tel.setText(courier.getPhone());
        depotid=courier.getDepotid();
        new Thread(r).start();
    }

    private void onclick(){
        fragment_mydata_layout_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +companytel));
                startActivity(intent);
            }
        });
        fragment_mydata_layout_depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +depottel));
                startActivity(intent);
            }
        });
        fragment_mydata_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        fragment_mydata_updatepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog1();
            }
        });
        fragment_mydata_updatetel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog2();
            }
        });
    }

    public void Dialog1(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.update_dialog,
                (ViewGroup) view.findViewById(R.id.update_dialog));

        final EditText update_pwd1= (EditText) layout.findViewById(R.id.update_pwd1);
        final EditText update_pwd2=(EditText) layout.findViewById(R.id.update_pwd2);
        final EditText update_pwd3=(EditText) layout.findViewById(R.id.update_pwd3);



        new AlertDialog.Builder(getActivity()).setTitle("修改密码").setView(layout)
                .setPositiveButton("确定修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!update_pwd1.getText().toString().equals(courier.getPassword())){
                            Toast.makeText(getActivity(), "原密码错误", Toast.LENGTH_SHORT).show();
                        }else{
                            if(update_pwd2.getText().toString()==""||update_pwd2.getText().toString()==null){
                                Toast.makeText(getActivity(), "新密码不能为空", Toast.LENGTH_SHORT).show();
                            }else {
                                if(update_pwd1.getText().toString().equals(update_pwd2.getText().toString())){
                                    Toast.makeText(getActivity(), "新密码与旧密码一致", Toast.LENGTH_SHORT).show();
                                }else {
                                    if(update_pwd2.getText().toString().length()<5||update_pwd2.getText().toString().length()>16){
                                        Toast.makeText(getActivity(), "新密码必须大于5位小于16位", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if(!update_pwd2.getText().toString().equals(update_pwd3.getText().toString())){
                                            Toast.makeText(getActivity(), "新密码不一致", Toast.LENGTH_SHORT).show();
                                        }else {
                                            passwords=update_pwd2.getText().toString();
                                            new Thread(t).start();
                                            db.UpdatePassword(Integer.parseInt(data.getString("courierid")),update_pwd2.getText().toString());
                                            Intent intents=new Intent();
                                            intents.setClass(getActivity(),LoginActivity.class);
                                            startActivity(intents);
                                            Toast.makeText(getActivity(), "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                    }

                                }
                            }

                        }
                    }
                })
                .setNeutralButton("取消",null).show();
    }

    public void Dialog2(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.updatephone_dialog,
                (ViewGroup) view.findViewById(R.id.updatephone_dialog));

        final TextView update_dialog_phone= (TextView) layout.findViewById(R.id.update_dialog_phone);
        final EditText update_dialog_newphone=(EditText) layout.findViewById(R.id.update_dialog_newphone);
        update_dialog_phone.setText("原手机号:"+courier.getPhone());


        new AlertDialog.Builder(getActivity()).setTitle("修改联系方式").setView(layout)
                .setPositiveButton("确定修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!isMobileNO(update_dialog_newphone.getText().toString())){
                            Toast.makeText(getActivity(), "手机号不正确或为空", Toast.LENGTH_SHORT).show();
                        }else{
                            if(update_dialog_newphone.getText().toString().equals(courier.getPhone())){
                                Toast.makeText(getActivity(), "与旧手机号一致", Toast.LENGTH_SHORT).show();
                            }else {
                                            phones=update_dialog_newphone.getText().toString();
                                            new Thread(y).start();
                                            db.UpdatePhone(Integer.parseInt(data.getString("courierid")),phones);
                                            fragment_mydata_tel.setText(phones);
                                            Toast.makeText(getActivity(), "联系方式修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNeutralButton("取消",null).show();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {

        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }



    Runnable r = new Runnable() {

        @Override
        public void run() {
            Map<String, Integer> map = new HashMap<>();
            map.put("depotid", depotid);

            JSONObject jsonObj = new JSONObject(map);
            String s = HttpUtils.doPost(HttpConfig.selectDepot, jsonObj);

            try {
                JSONObject obj = new JSONObject(s);
                depotname = obj.getString("depotname");
                depottel= obj.getString("depottel");
                companyid=Integer.parseInt(obj.getString("companyid"));

                    Message msg=handler.obtainMessage();
                    msg.what=1;
                    handler.sendMessage(msg);
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
                companytel=obj.getString("companytel");

                Message msg=handler.obtainMessage();
                msg.what=2;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


    Runnable t = new Runnable() {

        @Override
        public void run() {
            Map<String, String> map = new HashMap<>();
            map.put("passwords", passwords);
            map.put("courierid", data.getString("courierid"));
            JSONObject jsonObj = new JSONObject(map);
            String s = HttpUtils.doPost(HttpConfig.updatepwd, jsonObj);

            try {
                JSONObject obj = new JSONObject(s);
                Message msg=handler.obtainMessage();
                handler.sendMessage(msg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    Runnable y = new Runnable() {

        @Override
        public void run() {
            Map<String, String> map = new HashMap<>();
            map.put("phone", phones);
            map.put("courierid", data.getString("courierid"));
            JSONObject jsonObj = new JSONObject(map);
            String s = HttpUtils.doPost(HttpConfig.updatephone, jsonObj);

            try {
                JSONObject obj = new JSONObject(s);
                Message msg=handler.obtainMessage();
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
                    fragment_mydata_depot.setText(depotname);
                    break;
                case 2:
                    fragment_mydata_company.setText(companyname);
                    break;
            }
        }
    };
}
