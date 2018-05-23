package com.example.machenike.courier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.machenike.database.DBmanager;
import com.example.machenike.http.HttpConfig;
import com.example.machenike.http.HttpUtils;
import com.example.machenike.model.Courier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button sign_in_button;
    private DBmanager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db=new DBmanager(LoginActivity.this);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        sign_in_button= (Button) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(r).start();
            }
        });
    }

    Runnable r = new Runnable() {

        @Override
        public void run() {
            String usernames = username.getText().toString().trim();
            String passwords = password.getText().toString().trim();

            Map<String, String> map = new HashMap<>();
            map.put("username", usernames);
            map.put("password", passwords);

            JSONObject jsonObj = new JSONObject(map);
            String s = HttpUtils.doPost(HttpConfig.login, jsonObj);


            try {
                JSONObject obj = new JSONObject(s);
                String r = obj.getString("Result");

                if(r.equals("Logined")){
                    Courier couriers=db.LoginSelectBycourierid(Integer.parseInt(obj.getString("courierid")));
                    if(couriers==null){
                        Courier courier=new Courier();
                        courier.setCourierid(Integer.parseInt(obj.getString("courierid")));
                        courier.setDepotid(Integer.parseInt(obj.getString("depotid")));
                        courier.setPhone(obj.getString("phone"));
                        courier.setCouriername(obj.getString("couriername"));
                        courier.setUsername(usernames);
                        courier.setPassword(passwords);
                        db.LoginInsert(courier);
                    }
                    Message msg=handler.obtainMessage();
                    msg.what=1;
                    handler.sendMessage(msg);
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("id",obj.getString("courierid"));
					startActivity(intent);

                }
                if(r.equals("Failed")){
                    Message msg=handler.obtainMessage();
                    msg.what=2;
                    handler.sendMessage(msg);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    };

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String s="";
            switch(msg.what){
                case 1:
                    finish();
                    s="登陆成功";
                    break;
                case 2:
                    s="登陆失败,帐号或密码错误";
                    break;
            }
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };
}
