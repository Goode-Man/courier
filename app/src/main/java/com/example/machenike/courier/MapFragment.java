package com.example.machenike.courier;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.machenike.database.DBmanager;
import com.example.machenike.http.HttpConfig;
import com.example.machenike.http.HttpUtils;
import com.example.machenike.model.Courier;
import com.example.machenike.model.Express;
import com.example.machenike.model.Staterecord;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment{
    private View view;
    //百度地图控件
    private MapView mMapView = null;

    //百度地图对象
    private BaiduMap mBaiduMap;
    //定位图层显示模式 (普通-跟随-罗盘)
    private LocationMode mCurrentMode;
    //当前位置经纬度
    private double latitude;
    private double longitude;
    //是否首次定位
    private boolean isFirstLoc = true;
    //定位图标描述
    private BitmapDescriptor mCurrentMarker = null;
    //定位SDK的核心类
    private LocationClient mLocClient;
    //定位的覆盖物
    private Overlay myOverlay1,myOverlay2;

    private DBmanager db;

    private InfoWindow mInfoWindow;

    Bundle courierid;

    RoutePlanSearch mSearch = null;    // 搜索模块


    int nowSearchType = -1 ; // 当前进行的检索，供判断浏览节点时结果使用。

    //按钮 定位按钮
    private Button locationbtn;

    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.location22);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.location11);
    BitmapDescriptor bdM = BitmapDescriptorFactory
            .fromResource(R.drawable.location3);

    //定位SDK监听函数
    public MyLocationListenner locListener = new MyLocationListenner();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        SDKInitializer.initialize(getActivity().getApplicationContext());

        view=inflater.inflate(R.layout.fragment_map,container, false);
        courierid=getArguments();
        db=new DBmanager(getActivity());
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
        init();
        map();
        location();
        addmark1();
        addmark2();
        handler.postDelayed(runnable, 3000000);//每300秒执行一次runnable.
        return view;
    }

    public void init(){
        mMapView= (MapView) view.findViewById(R.id.mMapView);
        locationbtn= (Button) view.findViewById(R.id.location_button);
        locationbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isFirstLoc=true;
                location();
            }
        });
    }

    public void map(){
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (marker.getZIndex()==1) {
                    final Bundle bundles1 = marker.getExtraInfo();
                    final String bundlesresult = bundles1.getString("id");
                    LatLng weizhi = new LatLng(bundles1.getDouble("lat"), bundles1.getDouble("lon"));
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(weizhi));
                    LayoutInflater inflaters = LayoutInflater.from(getActivity().getApplicationContext());
                    View views = inflaters.inflate(R.layout.infowindows, null);
                    TextView popup_left = (TextView) views.findViewById(R.id.popup_left);
                    TextView popup_middle = (TextView) views.findViewById(R.id.popup_middle);
                    TextView popup_right = (TextView) views.findViewById(R.id.popup_right);

                    popup_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng st = new LatLng(latitude, longitude);
                            LatLng en = new LatLng(bundles1.getDouble("lat"), bundles1.getDouble("lon"));
                            PlanNode stNode = PlanNode.withLocation(st);
                            PlanNode enNode = PlanNode.withLocation(en);
                            mSearch.walkingSearch((new WalkingRoutePlanOption())
                                    .from(stNode)
                                    .to(enNode));
                        }
                    });

                    popup_middle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                            Staterecord staterecord = new Staterecord(Integer.parseInt(courierid.getString("courierid")), time, 2, bundlesresult);
                            db.InsertStateRecord(staterecord);
                            db.UpdateExpressState(bundlesresult, 2);
                            marker.remove();
                        }
                    });

                    popup_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBaiduMap.hideInfoWindow();
                        }
                    });


                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(views, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }


                if (marker.getZIndex()==2) {
                    final Bundle bundles1 = marker.getExtraInfo();
                    final int sendertel = bundles1.getInt("tel");
                    LatLng weizhi = new LatLng(bundles1.getDouble("lat"), bundles1.getDouble("lon"));
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(weizhi));
                    LayoutInflater inflaters = LayoutInflater.from(getActivity().getApplicationContext());
                    View views = inflaters.inflate(R.layout.infowindowss, null);
                    final EditText popup_edit= (EditText) views.findViewById(R.id.popup_edit);
                    TextView popup_left1 = (TextView) views.findViewById(R.id.popup_left1);
                    TextView popup_middle1 = (TextView) views.findViewById(R.id.popup_middle1);
                    TextView popup_right1 = (TextView) views.findViewById(R.id.popup_right1);

                    popup_left1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng st = new LatLng(latitude, longitude);
                            LatLng en = new LatLng(bundles1.getDouble("lat"), bundles1.getDouble("lon"));
                            PlanNode stNode = PlanNode.withLocation(st);
                            PlanNode enNode = PlanNode.withLocation(en);
                            mSearch.walkingSearch((new WalkingRoutePlanOption())
                                    .from(stNode)
                                    .to(enNode));
                        }
                    });

                    popup_middle1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newid=popup_edit.getText().toString().trim();
                            if (newid==""||newid==null){
                                Toast.makeText(getActivity(),"请输入寄件人订单号",Toast.LENGTH_SHORT).show();
                            }else {
                                db.UpdateExpressId(newid,sendertel);
                                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                Staterecord staterecord = new Staterecord(Integer.parseInt(courierid.getString("courierid")), time, 4, newid);
                                db.InsertStateRecord(staterecord);
                                db.UpdateExpressState(newid, 4);
                                marker.remove();
                            }

                        }
                    });

                    popup_right1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBaiduMap.hideInfoWindow();
                        }
                    });


                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(views, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return false;
            }
        });
    }

    /**
     * 路线规划结果监听
     */
    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }

        /**
         * 步行
         */

        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // 获取步行线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(getActivity(), "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
                        mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            // 获取公交换乘路径规划结果
        }

        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            // 获取驾车线路规划结果
        }
    };

    /**
     * 继承步行规划的子类,通过覆盖相应方法实现功能
     *
     * BitmapDescriptor getStartMarker() 覆写此方法以改变默认起点图标 BitmapDescriptor
     * getTerminalMarker() 覆写此方法以改变默认终点图标
     *
     * @author liling
     *
     */
    class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
        }
    }




    public void addmark1(){
        List<Express> express1=db.selectExpressByState(1);
        for(int i=0;i<express1.size();i++){
            LatLng pt1 = new LatLng(express1.get(i).getLatitude(), express1.get(i).getLongitude());
            MarkerOptions ooA = new MarkerOptions().position(pt1).icon(bdA)
                    .zIndex(1);
            Bundle bundles=new Bundle();
            bundles.putString("id", express1.get(i).getExpressid());
            bundles.putDouble("lat",express1.get(i).getLatitude());
            bundles.putDouble("lon",express1.get(i).getLongitude());
            ooA.extraInfo(bundles);
            Marker Marker = (Marker)mBaiduMap.addOverlay(ooA);
        }

    }
    public void addmark2(){
        List<Express> express1=db.selectExpressByState(3);
        for(int i=0;i<express1.size();i++){
            LatLng pt1 = new LatLng(express1.get(i).getLatitude(), express1.get(i).getLongitude());
            MarkerOptions ooB = new MarkerOptions().position(pt1).icon(bdB)
                    .zIndex(2);
            Bundle bundles=new Bundle();
            bundles.putString("tel", express1.get(i).getSendertel());
            bundles.putDouble("lat",express1.get(i).getLatitude());
            bundles.putDouble("lon",express1.get(i).getLongitude());
            ooB.extraInfo(bundles);
            Marker Marker = (Marker)mBaiduMap.addOverlay(ooB);
        }

    }
    public void clearmark(){
        mBaiduMap.clear();
    }

    public void location(){
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(locListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);//设置发起定位请求的间隔时间为3000ms
        option.disableCache(false);//禁止启用缓存定位
        mLocClient.setLocOption(option);//设置定位参数
        mLocClient.start();//调用此方法开始定位
    }

    public class MyLocationListenner implements BDLocationListener {
            @Override
            public void onReceiveLocation(BDLocation location) {
                //mapview 销毁后不在处理新接收的位置
                if (location == null || mBaiduMap == null) {
                    Toast.makeText(getActivity(),"请检查您的网络",Toast.LENGTH_SHORT).show();
                }
                if (location.getLatitude() == 4.9e-324 && location.getLongitude() == 4.9e-324) {
                    Toast.makeText(getActivity(),"请检查定位权限是否已打开",Toast.LENGTH_SHORT).show();
                }
                //MyLocationData.Builder定位数据建造器
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(100)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                //设置定位数据
                mBaiduMap.setMyLocationData(locData);
                mCurrentMode = LocationMode.FOLLOWING;
                //获取经纬度
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //Toast.makeText(getApplicationContext(), String.valueOf(latitude), Toast.LENGTH_SHORT).show();
                //第一次定位的时候，那地图中心点显示为定位到的位置
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //地理坐标基本数据结构
                    LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
                    //MapStatusUpdate描述地图将要发生的变化
                    //MapStatusUpdateFactory生成地图将要反生的变化
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
                    mBaiduMap.animateMapStatus(msu);
                    clearMyOverlay();
                    addMyLocation();
                    addCircleOverlay();
                }else{//第x次定位（x!=1）
                    clearMyOverlay();
//                    LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
//                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
//                    mBaiduMap.animateMapStatus(msu);
                    addMyLocation();
                    addCircleOverlay();
                }
            }
        }




        /**
         * 定位并添加标注
         */
    private void addMyLocation() {
        //更新
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location3);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bdM);
//        mBaiduMap.addOverlay(option);
//        DotOptions dotOptions = new DotOptions();
//        dotOptions.center(new LatLng(latitude, longitude));//设置圆心坐标
//        dotOptions.color(0XFFfaa755);//颜色
//        dotOptions.radius(25);//设置半径
        myOverlay1 =mBaiduMap.addOverlay(option);
    }

    /**
     * 添加覆盖物
     */
    private void addCircleOverlay() {
            //DotOptions 圆点覆盖物
            LatLng pt = new LatLng(latitude, longitude);
            CircleOptions circleOptions = new CircleOptions();
            //circleOptions.center(new LatLng(latitude, longitude));
            circleOptions.center(pt);                          //设置圆心坐标
            circleOptions.fillColor(0x666FF00);               //圆填充颜色
            circleOptions.radius(15);                         //设置半径
            circleOptions.stroke(new Stroke(2, 0xAA00FF00));   // 设置边框
        myOverlay2 =mBaiduMap.addOverlay(circleOptions);
        }

    private void clearMyOverlay(){
        if (myOverlay2!=null){
            myOverlay2.remove();
        }
        if (myOverlay1!=null){
            myOverlay1.remove();
        }
    }


    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            clearmark();
            addmark1();
            addmark2();
            handler.postDelayed(this, 3000000);
        }
    };


    public void onDestroy() {
            // 退出时销毁定位
            mLocClient.stop();
        mSearch.destroy();
        // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mMapView.onDestroy();
            mMapView = null;
            super.onDestroy();
        }

        public void onResume() {
            super.onResume();
            mMapView.onResume();
        }

        public void onPause() {
            super.onPause();
            mMapView.onPause();
        }
}

