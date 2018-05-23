package com.example.machenike.courier;

import android.app.Dialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machenike.database.DBmanager;
import com.example.machenike.http.HttpConfig;
import com.example.machenike.http.HttpUtils;
import com.example.machenike.model.Express;
import com.example.machenike.model.Staterecord;
import com.zbar.lib.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusinessFragment extends Fragment {
    private View view;
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private DBmanager db;
    Express express;
    String result;
    ImageView saoma,duanxin;
    Bundle datas;
    EditText edit_searchs;



    private GridView BusinessgridView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    private String[] item_name={"快件列表","查快递","操作记录","短信模版","同步数据"};
    private int[] item_image={R.drawable.kuaijian,R.drawable.chakuaidi,R.drawable.jilu,R.drawable.duanxinmoban,R.drawable.tongbu};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_business, container, false);
        db=new DBmanager(getActivity());
        datas=getArguments();
        saoma = (ImageView) view.findViewById(R.id.btn_saoma);
        duanxin= (ImageView) view.findViewById(R.id.btn_duanxin);
        BusinessgridView= (GridView) view.findViewById(R.id.Business_gridView);
        edit_searchs=(EditText)view.findViewById(R.id.edit_searchs);
        setListener();
        initAdapter();
        return view;
    }


    private void setListener() {
        saoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CaptureActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        duanxin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == 101) {
                    result = data.getStringExtra("QR_CODE");
                    // TODO 获取结果，做逻辑操作
                    exitDialog1();
                } else {
                    Toast.makeText(getActivity(), "无法获取扫码结果", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void exitDialog1(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog,
                (ViewGroup) view.findViewById(R.id.dialog));

        TextView dialagnumber= (TextView) layout.findViewById(R.id.dialagnumber);
        TextView dialagname=(TextView) layout.findViewById(R.id.dialagname);
        final TextView dialagtel=(TextView) layout.findViewById(R.id.dialagtel);
        TextView dialagadress=(TextView) layout.findViewById(R.id.dialagadress);
        dialagnumber.setText(result);
        express=db.selectByExpressId(result);
        if(express==null){
            Toast.makeText(getActivity(), "此单不归你派送或请更新数据", Toast.LENGTH_SHORT).show();
        }else {
            dialagname.setText(express.getAddressee());
            dialagtel.setText(express.getAddresseetel());
            dialagadress.setText(express.getAddresseecity()+express.getAddresseeadress());
        }
        new AlertDialog.Builder(getActivity()).setTitle("订单信息").setView(layout)
                .setPositiveButton("复制订单号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) getActivity().getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(result);
                        Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("拨打电话", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" +express.getAddresseetel()));
                        startActivity(intent);
                    }
                })
                .setNeutralButton("取消",null).show();
    }

    private void initAdapter() {
        for(int index=0;index<item_name.length;index++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img",item_image[index]);
            map.put("name", item_name[index]);
            list.add(map);
        }
        String [] from ={"img","name"};
        int [] to = {R.id.business_image,R.id.business_text};
        adapter=new SimpleAdapter(getActivity(),list,R.layout.business_gridview,from,to);
        BusinessgridView.setAdapter(adapter);
        BusinessgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                switch (position){
                    case 0:
                        intent.setClass(getActivity(),ExpressListActivity.class);
                        intent.putExtra("courieridid",datas.getString("courierid"));
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(),SelectExpressActivitys.class);
                        intent.putExtra("courieridid",datas.getString("courierid"));
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getActivity(),StateRecordActivity.class);
                        intent.putExtra("courieridid",datas.getString("courierid"));
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(getActivity(),TemplateActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        new Thread(r).start();
                        new Thread(t).start();
                        break;
                }
            }
        });
    }


    Runnable r = new Runnable() {

        @Override
        public void run() {
            Map<String, Integer> map = new HashMap<>();
            map.put("courierid", Integer.parseInt(datas.getString("courierid")));
            List<Express> express_list=new ArrayList<Express>();
            JSONObject jsonObj = new JSONObject(map);
            String s = HttpUtils.doPost(HttpConfig.sendData, jsonObj);
            try {
                JSONArray array = new JSONArray(s);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject e = array.getJSONObject(i);
                    String expressid=e.getString("expressid");
                    String sender=e.getString("sender");
                    String sendertel=e.getString("sendertel");
                    String sendercity=e.getString("sendercity");
                    String senderadress=e.getString("senderadress");
                    String Addressee=e.getString("addressee");
                    String addresseetel=e.getString("addresseetel");
                    String addresseecity=e.getString("addresseecity");
                    String addresseeadress=e.getString("addresseeadress");
                    int companyid=Integer.parseInt(e.getString("companyid"));
                    double longitude=Double.parseDouble(e.getString("longitude"));
                    double latitude=Double.parseDouble(e.getString("latitude"));
                    int state=Integer.parseInt(e.getString("state"));
                    Express ExpressData=new Express(expressid,sender,sendertel,sendercity,senderadress,Addressee,addresseetel,
                            addresseecity,addresseeadress,companyid,longitude,latitude,state);
                    Boolean result=db.judgeExpress(expressid);
                    if(result==true){
                        express_list.add(ExpressData);
                    }

                }
                if(express_list!=null) {
                    db.ExpressInsert(express_list);
                }
                Message msg=handler.obtainMessage();
                msg.what=2;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            List<Staterecord> staterecordList=new ArrayList<Staterecord>();
//            staterecordList=db.selectRecondByCourierid(Integer.parseInt(datas.getString("courierid")));
//            for (int i=0;i<staterecordList.size();i++){
//                Map<String, String> map = new HashMap<>();
//                map.put("courierid", datas.getString("courierid"));
//                map.put("datetimes",staterecordList.get(i).getDatetimes());
//                map.put("state",Integer.toString(staterecordList.get(i).getState()));
//                map.put("expressid",staterecordList.get(i).getExpressid());
//            }


            JSONObject jsonObjs = new JSONObject(map);
            String ss = HttpUtils.doPost(HttpConfig.sendreceivedata, jsonObjs);
            try {
                JSONArray array = new JSONArray(ss);
                List<Express> express_list1=new ArrayList<Express>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject e = array.getJSONObject(i);
                    String expressid=e.getString("expressid");
                    String sender=e.getString("sender");
                    String sendertel=e.getString("sendertel");
                    String sendercity=e.getString("sendercity");
                    String senderadress=e.getString("senderadress");
                    String Addressee=e.getString("addressee");
                    String addresseetel=("addresseetel");
                    String addresseecity=e.getString("addresseecity");
                    String addresseeadress=e.getString("addresseeadress");
                    int companyid=Integer.parseInt(e.getString("companyid"));
                    double longitude=Double.parseDouble(e.getString("longitude"));
                    double latitude=Double.parseDouble(e.getString("latitude"));
                    int state=Integer.parseInt(e.getString("state"));
                    Express ExpressData=new Express(expressid,sender,sendertel,sendercity,senderadress,Addressee,addresseetel,
                            addresseecity,addresseeadress,companyid,longitude,latitude,state);
                    Boolean result=db.judgeNullExpress(sendertel);
                    if(result==true){
                        express_list1.add(ExpressData);
                    }

                }
                if(express_list1!=null) {
                    db.ExpressInsert(express_list1);
                }
                Message msg=handler.obtainMessage();
                msg.what=1;
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
            List<Staterecord> staterecordList=db.selectRecondByCourierid(Integer.parseInt(datas.getString("courierid")));
            for (int i=0;i<staterecordList.size();i++){
                Map<String, String> map = new HashMap<>();
                map.put("courierid", datas.getString("courierid"));
                map.put("datetimes", staterecordList.get(i).getDatetimes());
                map.put("state", Integer.toString(staterecordList.get(i).getState()));
                map.put("expressid", staterecordList.get(i).getExpressid());
                JSONObject jsonObj = new JSONObject(map);
                String s=HttpUtils.doPost(HttpConfig.sendRecord, jsonObj);
                System.out.print(s);
            }

        }};

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getActivity(),"数据同步完成",Toast.LENGTH_SHORT).show();
            }
        }
    };



}

