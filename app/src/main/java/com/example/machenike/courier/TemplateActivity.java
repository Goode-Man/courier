package com.example.machenike.courier;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class TemplateActivity extends AppCompatActivity {

    private DBmanager db;
    private Button template_add,template_bt1,template_bt2,template_bt3;
    private TextView template_back;
    private ListView template_list;
    private EditText template_edit;

    private List<Map<String,String>> datalist;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        db=new DBmanager(TemplateActivity.this);
        init();
        click();
    }

    private void init(){
        template_add= (Button) findViewById(R.id.template_add);
        template_bt1= (Button) findViewById(R.id.template_bt1);
        template_bt2= (Button) findViewById(R.id.template_bt2);
        template_bt3= (Button) findViewById(R.id.template_bt3);
        template_back= (TextView) findViewById(R.id.template_back);
        template_list= (ListView) findViewById(R.id.template_list);
        template_edit= (EditText) findViewById(R.id.template_edit);
        datalist=buildData();
        adapter=new TemplateActivity.MyAdapter(this);
        template_list.setAdapter(adapter);
    }

    private void click(){
        //保存模版
        template_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=template_edit.getText().toString();
                db.InsertTemplate(text);
                datalist=buildData();
                template_edit.setText("");
                Toast.makeText(TemplateActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                template_list.setAdapter(adapter);
            }
        });


        //添加图片
        template_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Field field = R.drawable.class.getDeclaredField("btn1");
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    ImageSpan imageSpan = new ImageSpan(TemplateActivity.this, bitmap);
                    SpannableString spannableString = new SpannableString("btn1");
                    spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    template_edit.append(spannableString);
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

        template_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Field field = R.drawable.class.getDeclaredField("btn2");
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    ImageSpan imageSpan = new ImageSpan(TemplateActivity.this, bitmap);
                    SpannableString spannableString = new SpannableString("btn2");
                    spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    template_edit.append(spannableString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        template_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Field field = R.drawable.class.getDeclaredField("btn3");
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    ImageSpan imageSpan = new ImageSpan(TemplateActivity.this, bitmap);
                    SpannableString spannableString = new SpannableString("btn3");
                    spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    template_edit.append(spannableString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        template_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateActivity.this.finish();
            }
        });

        template_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intents=new Intent();
                intents.setClass(TemplateActivity.this,MessageActivity.class);
                intents.putExtra("TemplateText",datalist.get(position).get("Text"));
                startActivity(intents);
                finish();
            }
        });

        template_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(TemplateActivity.this).setTitle("系统提示")//设置对话框标题

                        .setMessage("确定删除该条模版吗?")//设置显示的内容

                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮



                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                db.deleteTemplate(Integer.parseInt(datalist.get(position).get("id")));
                                datalist=buildData();
                                Toast.makeText(TemplateActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                template_list.setAdapter(adapter);
                            }

                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();//在按键响应事件中显示此对话框
                return false;
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
            TemplateActivity.ViewHolder holder;
            if (convertView==null){
                holder=new TemplateActivity.ViewHolder();
                convertView= inflater.inflate(R.layout.template_list,null,true);
                holder.template_list_text= (TextView) convertView.findViewById(R.id.template_list_text);
                convertView.setTag(holder);
            }else {
                holder= (TemplateActivity.ViewHolder) convertView.getTag();
            }
            holder.template_list_text.setText(datalist.get(position).get("Text"));
            return convertView;
        }
    }

    private final class ViewHolder{
        TextView template_list_text;
    }

    private List<Map<String,String>> buildData(){
        List<Map<String,String>> datas=new ArrayList<>();
        List<Template> listTemplate=new ArrayList<Template>();
        listTemplate=db.selectTemplate();
        for(int i=0;i<listTemplate.size();i++){
            Template template=listTemplate.get(i);
            Map<String, String> map=new HashMap<String, String>();
            map.put("id",Integer.toString(template.getId()));
            map.put("Text",template.getTemplateText());
            datas.add(map);
        }
        return datas;
    }


}
