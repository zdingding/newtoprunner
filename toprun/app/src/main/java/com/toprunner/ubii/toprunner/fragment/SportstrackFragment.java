package com.toprunner.ubii.toprunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.Trace;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.GuideActivity;
import com.toprunner.ubii.toprunner.activivty.MainActivity;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.listener.MyOrientationListener;
import com.toprunner.ubii.toprunner.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class SportstrackFragment extends BaseFragment {
   private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap;
    private double mLatitude;//定位的做标
    private double mLongtitude;//定位的做标
    private boolean isFirstIn = true;
    List< LatLng > pointstwo = new ArrayList< LatLng >();
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;
   private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private MyLocationConfiguration.LocationMode mlocationMode;

    private BitmapDescriptor mMarker;
    private RelativeLayout mMarkerLy;
    // 定位相关

    private LocationClient mlocationClient;
    private MyLocationListener mLocationListener;
    private Button btn_mylocation;
    // 自定义定位图标


    //轨迹相关
    private Button btnStartTrace = null;
    /**
     * 开启轨迹服务监听器
     */
    private  OnStartTraceListener startTraceListener = null;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

            }
        }
    };
    private LBSTraceClient client;
    private Trace trace;
    private Button btn_stopTrace;

    @Override
    public void setListener() {
        mLocationListener = new MyLocationListener();//定位成功以后的回调
        mlocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);//位置
        option.setScanSpan(1000);
        mlocationClient.setLocOption(option);
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cebterToMyLocation();
            }
        });

    }

    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {
            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int i, String s) {

    Toast.makeText(UIUtils.getContext(),i+""+s,Toast.LENGTH_SHORT).show();
            }
            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte b, String s) {
                Toast.makeText(UIUtils.getContext(),"用于接收服务端推送消息",Toast.LENGTH_SHORT).show();
            }
        };
    }
    class TrackUploadHandler extends Handler {

        public void handleMessage(Message msg) {


        }

    }
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.run_baidu_map);
        btn_mylocation = (Button)findViewById(R.id.btn_mylocation);
        btnStartTrace = (Button) view.findViewById(R.id.btn_startTrace);
        btn_stopTrace = (Button) view.findViewById(R.id.btn_stopTrace);
        mBaiduMap = mMapView.getMap();
        initTrack();
        // 设置采集周期
        setInterval();
        // 设置http请求协议类型
        setRequestType();
        //使用消息处理机制处理消息
        initLocation();
        initMarker();

    }

    private void setRequestType() {
    }

    private void setInterval() {
    }
//开启关闭轨迹
    private void initTrack() {
        btnStartTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "正在开启轨迹服务，请稍候", Toast.LENGTH_LONG).show();

                //开启轨迹
                startTrace();
            }


        });
        btn_stopTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "正在结束轨迹服务，请稍候", Toast.LENGTH_LONG).show();
                //结束轨迹
                stopTrace();
            }


        });
    }

    private void stopTrace() {
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        client = ((ToprunnerApplication) getActivity().getApplication()).getClient();
        trace = ((ToprunnerApplication) getActivity().getApplication()).getTrace();
    }

    private void startTrace() {
        if (null == startTraceListener) {
            initOnStartTraceListener();
        }
        client.startTrace(trace, startTraceListener);

    }
    private void initMarker() {
    }

    private void initLocation() {
        // 初始化图标
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.mipmap.navi_map_gps_locked);
        mlocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mlocationClient = new LocationClient(UIUtils.getContext());
        mLocationListener = new MyLocationListener();
        mlocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mlocationClient.setLocOption(option);
        //
        myOrientationListener = new MyOrientationListener(UIUtils.getContext());
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.sportstrack;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mlocationClient.isStarted())
            mlocationClient.start();
        // 开启方向传感器
        myOrientationListener.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
        // 停止方向传感器
       myOrientationListener.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)//
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();


           mBaiduMap.setMyLocationData(data);

            // 更新经纬度
            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();

            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mlocationMode, true, mIconLocation);
           mBaiduMap.setMyLocationConfigeration(config);
            if (isFirstIn)
            {
                isFirstIn = false;
                cebterToMyLocation();
              ;
                Toast.makeText(UIUtils.getContext(), bdLocation.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cebterToMyLocation() {
        LatLng latLng = new LatLng(mLatitude,
                mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }
}
