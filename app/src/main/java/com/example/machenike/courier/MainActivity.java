package com.example.machenike.courier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    private MapFragment mapFragment;
    private BusinessFragment businessFragment;
    private MyDataFragment myDataFragment;
    String courierid;

    private FragmentManager manager;


    private GridView gridView;
    private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    private SimpleAdapter adapter;
    private String[] item_name={"业务","地图","我的"};
    private int[] item_image={R.drawable.yewu1,R.drawable.ditu1,R.drawable.wode1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(MainActivity.this.getApplicationContext());
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        courierid=intent.getStringExtra("id");
        init();

        manager=this.getFragmentManager();
        setTabSelection(1);
    }


    private void init(){
        gridView=(GridView)findViewById(R.id.gridView);
        this.initAdapter();
    }

    private void initAdapter() {
        for(int index=0;index<item_name.length;index++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img",item_image[index]);
            map.put("name", item_name[index]);
            list.add(map);
        }
        String [] from ={"img","name"};
        int [] to = {R.id.gridView_image,R.id.gridView_text};
        adapter=new SimpleAdapter(this,list,R.layout.gridview,from,to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setTabSelection(position);
    }

    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (businessFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    businessFragment = new BusinessFragment();
                    Bundle data = new Bundle();
                    data.putString("courierid", courierid);
                    businessFragment.setArguments(data);//通过Bundle向Activity中传递值
                    transaction.add(R.id.frame, businessFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(businessFragment);
                }
                break;
            case 1:
                if (mapFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    mapFragment = new MapFragment();
                    Bundle data = new Bundle();
                    data.putString("courierid", courierid);
                    mapFragment.setArguments(data);//通过Bundle向Activity中传递值
                    transaction.add(R.id.frame, mapFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(mapFragment);
                }
                break;
            case 2:
                if (myDataFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    myDataFragment = new MyDataFragment();
                    Bundle data = new Bundle();
                    data.putString("courierid", courierid);
                    myDataFragment.setArguments(data);//通过Bundle向Activity中传递值
                    transaction.add(R.id.frame, myDataFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(myDataFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (businessFragment != null) {
            transaction.hide(businessFragment);
        }
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        if (myDataFragment != null) {
            transaction.hide(myDataFragment);
        }
    }

    


}
