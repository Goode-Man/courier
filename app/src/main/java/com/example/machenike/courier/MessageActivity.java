package com.example.machenike.courier;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machenike.database.DBmanager;
import com.example.machenike.model.Express;
import com.example.machenike.model.Template;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private Button message_send,message_bt1,message_bt2,message_bt3;
    private TextView message_back,message_checkall,message_update;
    private EditText message_edit;
    private ListView message_list;
    SmsManager sManager;
    private DBmanager db;
    private List<Map<String,String>> datalist;
    private List<Map<String,String>> dataslists=new ArrayList<Map<String,String>>();

    int courierid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        db=new DBmanager(MessageActivity.this);
//        Intent intent=getIntent();
//        courierid=Integer.parseInt(intent.getStringExtra("courieridid"));
        sManager = SmsManager.getDefault();
        init();
        click();

    }

    private void init(){
        Intent intent=getIntent();
        String text=intent.getStringExtra("TemplateText");
        message_send= (Button) findViewById(R.id.message_send);
        message_bt1= (Button) findViewById(R.id.message_bt1);
        message_bt2= (Button) findViewById(R.id.message_bt2);
        message_bt3= (Button) findViewById(R.id.message_bt3);
        message_back= (TextView) findViewById(R.id.message_back);
        message_update= (TextView) findViewById(R.id.message_update);
        message_checkall=(TextView) findViewById(R.id.message_checkall);
        message_edit= (EditText) findViewById(R.id.message_edit);
        message_list= (ListView) findViewById(R.id.message_list);
        datalist=buildData();
        message_edit.setText(text);
        MyAdapter adapter=new MyAdapter(this);
        message_list.setAdapter(adapter);


    }

    private void click(){
        message_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents=new Intent();
                intents.setClass(MessageActivity.this,TemplateActivity.class);
                startActivity(intents);
            }
        });

        //发送短信
        message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=message_edit.getText().toString();
                SparseBooleanArray checkedArray = message_list.getCheckedItemPositions();
                //dataslists要发送的数据
                for (int i = 0; i < checkedArray.size(); i++) {
                    if (checkedArray.valueAt(i)){
                        dataslists.add(datalist.get(i));
                    }
                }
                String q=null,w=null,e=null;
                String z=null,x=null,c=null;
                String result=null;
                String name=null,tel=null,express=null;
//                if(text.indexOf("btn1")!=-1){
//                    q=text.substring(0,text.indexOf("btn1"));
//                    z=text.substring(text.indexOf("btn1")+4,text.length());
//                }else{
//                    z=text;
//                }
//                if(z.indexOf("btn2")!=-1){
//                    w=z.substring(0,z.indexOf("btn2"));
//                    x=z.substring(z.indexOf("btn2")+4,z.length());
//                }else{
//                    x=z;
//                }
//                if (x.indexOf("btn2")!=-1){
//                    e=x.substring(0,z.indexOf("btn3"));
//                    c=x.substring(z.indexOf("btn2")+4,z.length());
//                }else{
//                    c=x;
//                }

                int b1=text.indexOf("btn1");//姓名
                int b2=text.indexOf("btn2");//电话
                int b3=text.indexOf("btn3");//快递单号

                 for(int i=0;i<dataslists.size();i++){
                    //创建一个PendingIntent对象
                    PendingIntent pi = PendingIntent.getActivity(
                            MessageActivity.this, 0, new Intent(),0);
                     name=dataslists.get(i).get("name");
                     tel=dataslists.get(i).get("tel");
                     express=dataslists.get(i).get("express");

                     if(b1>=0 && b2>=0 && b3>=0){
                         if(b1>b2 &&b2>b3){
                             q=text.substring(0,text.indexOf("btn3"));
                             z=text.substring(text.indexOf("btn3")+4,text.length());
                             w=z.substring(0,z.indexOf("btn2"));
                             x=z.substring(z.indexOf("btn2")+4,z.length());
                             e=x.substring(0,x.indexOf("btn1"));
                             c=x.substring(x.indexOf("btn1")+4,x.length());
                             result=q+express+w+tel+e+name+c;
                         }else if(b1>b3 &&b3>b2){
                             q=text.substring(0,text.indexOf("btn2"));
                             z=text.substring(text.indexOf("btn2")+4,text.length());
                             w=z.substring(0,z.indexOf("btn3"));
                             x=z.substring(z.indexOf("btn3")+4,z.length());
                             e=x.substring(0,x.indexOf("btn1"));
                             c=x.substring(x.indexOf("btn1")+4,x.length());
                             result=q+tel+w+express+e+name+c;
                         }else if(b2>b1 &&b1>b3){
                             q=text.substring(0,text.indexOf("btn3"));
                             z=text.substring(text.indexOf("btn3")+4,text.length());
                             w=z.substring(0,z.indexOf("btn1"));
                             x=z.substring(z.indexOf("btn1")+4,z.length());
                             e=x.substring(0,x.indexOf("btn2"));
                             c=x.substring(x.indexOf("btn2")+4,x.length());
                             result=q+express+w+name+e+tel+c;
                         }else if(b2>b3 &&b3>b1){
                             q=text.substring(0,text.indexOf("btn1"));
                             z=text.substring(text.indexOf("btn1")+4,text.length());
                             w=z.substring(0,z.indexOf("btn3"));
                             x=z.substring(z.indexOf("btn3")+4,z.length());
                             e=x.substring(0,x.indexOf("btn2"));
                             c=x.substring(x.indexOf("btn2")+4,x.length());
                             result=q+name+w+express+e+tel+c;
                         }else if(b3>b1 &&b1>b2){
                             q=text.substring(0,text.indexOf("btn2"));
                             z=text.substring(text.indexOf("btn2")+4,text.length());
                             w=z.substring(0,z.indexOf("btn1"));
                             x=z.substring(z.indexOf("btn1")+4,z.length());
                             e=x.substring(0,x.indexOf("btn3"));
                             c=x.substring(x.indexOf("btn3")+4,x.length());
                             result=q+tel+w+name+e+express+c;
                         }else if(b3>b2 &&b2>b1){
                             q=text.substring(0,text.indexOf("btn1"));
                             z=text.substring(text.indexOf("btn1")+4,text.length());
                             w=z.substring(0,z.indexOf("btn2"));
                             x=z.substring(z.indexOf("btn2")+4,z.length());
                             e=x.substring(0,x.indexOf("btn3"));
                             c=x.substring(x.indexOf("btn3")+4,x.length());
                             result=q+name+w+tel+e+express+c;
                         }

                     }else if(b1>=0 && b2>=0 && b3<0){
                         if (b1>b2){
                             q=text.substring(0,text.indexOf("btn2"));
                             z=text.substring(text.indexOf("btn2")+4,text.length());
                             w=z.substring(0,z.indexOf("btn1"));
                             x=z.substring(z.indexOf("btn1")+4,z.length());
                             result=q+tel+w+name+x;
                         }else if(b2>b1){
                             q=text.substring(0,text.indexOf("btn1"));
                             z=text.substring(text.indexOf("btn1")+4,text.length());
                             w=z.substring(0,z.indexOf("btn2"));
                             x=z.substring(z.indexOf("btn2")+4,z.length());
                             result=q+name+w+tel+x;
                         }
                     }else if(b1>=0 && b2<0 && b3>=0){
                         if (b1>b3){
                             q=text.substring(0,text.indexOf("btn3"));
                             z=text.substring(text.indexOf("btn3")+4,text.length());
                             w=z.substring(0,z.indexOf("btn1"));
                             x=z.substring(z.indexOf("btn1")+4,z.length());
                             result=q+express+w+name+x;
                         }else if(b3>b1){
                             q=text.substring(0,text.indexOf("btn1"));
                             z=text.substring(text.indexOf("btn1")+4,text.length());
                             w=z.substring(0,z.indexOf("btn3"));
                             x=z.substring(z.indexOf("btn3")+4,z.length());
                             result=q+name+w+express+x;
                         }
                     }else if(b1<0 && b2>=0 && b3>=0){
                         if (b2>b3){
                             q=text.substring(0,text.indexOf("btn3"));
                             z=text.substring(text.indexOf("btn3")+4,text.length());
                             w=z.substring(0,z.indexOf("btn2"));
                             x=z.substring(z.indexOf("btn2")+4,z.length());
                             result=q+express+w+tel+x;
                         }else if(b3>b2){
                             q=text.substring(0,text.indexOf("btn2"));
                             z=text.substring(text.indexOf("btn2")+4,text.length());
                             w=z.substring(0,z.indexOf("btn3"));
                             x=z.substring(z.indexOf("btn3")+4,z.length());
                             result=q+tel+w+express+x;
                         }
                     }else if(b1>=0 && b2<0 && b3<0){
                         q=text.substring(0,text.indexOf("btn1"));
                         z=text.substring(text.indexOf("btn1")+4,text.length());
                         result=q+name+z;
                     }else if(b1<0 && b2>=0 && b3<0){
                         q=text.substring(0,text.indexOf("btn2"));
                         z=text.substring(text.indexOf("btn2")+4,text.length());
                         result=q+tel+z;
                     }else if(b1<0 && b2<0 && b3>=0){
                         q=text.substring(0,text.indexOf("btn3"));
                         z=text.substring(text.indexOf("btn3")+4,text.length());
                         result=q+express+z;
                     }else if(b1<0 && b2<0 && b3<0){
                         q=text;
                         result=q;
                     }


//                     System.out.println(name);
//                     System.out.println(tel);
//                     System.out.println(express);
//                     System.out.println(result);
                    //发送短信
                    sManager.sendTextMessage(tel, null,
                            result, pi, null);
                }
                //提示短信群发完成
                Toast.makeText(MessageActivity.this, "短信群发完成！", Toast.LENGTH_LONG).show();

            }
        });

        //添加图片
        message_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Field field = R.drawable.class.getDeclaredField("btn1");
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    ImageSpan imageSpan = new ImageSpan(MessageActivity.this, bitmap);
                    SpannableString spannableString = new SpannableString("btn1");
                    spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message_edit.append(spannableString);
                    //测试图片位置
//                    if (spannableString.toString()=="btn1"){
//                        System.out.println("aaaa");
//                    }
//                    String a=message_edit.getText().toString();
//                    System.out.println(a.indexOf("btn1"));
//                    if(a.indexOf("btn1")!=-1){
//                        System.out.println(a.substring(0,a.indexOf("btn1")));
//                        System.out.println(a.substring(a.indexOf("btn1")+4,a.length()));
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        message_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Field field = R.drawable.class.getDeclaredField("btn2");
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    ImageSpan imageSpan = new ImageSpan(MessageActivity.this, bitmap);
                    SpannableString spannableString = new SpannableString("btn2");
                    spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message_edit.append(spannableString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        message_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Field field = R.drawable.class.getDeclaredField("btn3");
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    ImageSpan imageSpan = new ImageSpan(MessageActivity.this, bitmap);
                    SpannableString spannableString = new SpannableString("btn3");
                    spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message_edit.append(spannableString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //全选
        message_checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<datalist.size();i++){
                    message_list.setItemChecked(i, true);
                }
            }
        });
        //返回
        message_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });
        //修改模版
        message_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent=new Intent(MessageActivity.this, TemplateActivity.class);
                startActivity(inent);
            }
        });
    }


    public class MyAdapter extends BaseAdapter{
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
            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView= inflater.inflate(R.layout.list_message,null,true);
                holder.message_list_name= (TextView) convertView.findViewById(R.id.message_list_name);
                holder.message_list_tel= (TextView) convertView.findViewById(R.id.message_list_tel);
                holder.message_list_express= (TextView) convertView.findViewById(R.id.message_list_express);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.message_list_name.setText(datalist.get(position).get("name"));
            holder.message_list_tel.setText(datalist.get(position).get("tel"));
            holder.message_list_express.setText(datalist.get(position).get("express"));
            return convertView;
        }
    }

    private final class ViewHolder{
        TextView message_list_name;
        TextView message_list_tel;
        TextView message_list_express;
    }

    private List<Map<String,String>> buildData(){
        List<Map<String,String>> datas=new ArrayList<>();
        List<Express> listExpress=new ArrayList<Express>();
        listExpress=db.selectExpressByState(1);
        for(int i=0;i<listExpress.size();i++){
            Express express=listExpress.get(i);
            Map<String, String> map=new HashMap<String, String>();
            map.put("name",express.getAddressee());
            map.put("tel",express.getAddresseetel());
            map.put("express",express.getExpressid());
            datas.add(map);
        }
        return datas;
    }
}
