package com.toprunner.ubii.toprunner.fragment;

import android.os.Bundle;
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
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.listener.MyOrientationListener;
import com.toprunner.ubii.toprunner.utils.UIUtils;

/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class SportstrackFragment extends BaseFragment {


   private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap;
    private double mLatitude;//定位的做标
    private double mLongtitude;//定位的做标
    private boolean isFirstIn = true;

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

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.run_baidu_map);
        btn_mylocation = (Button)findViewById(R.id.btn_mylocation);

        mBaiduMap = mMapView.getMap();
        initLocation();
        initMarker();

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
       // myOrientationListener.start();
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
     //   myOrientationListener.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()//
                //    .direction(mCurrentX)//
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
