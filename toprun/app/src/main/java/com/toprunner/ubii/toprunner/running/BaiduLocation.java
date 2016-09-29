package com.toprunner.ubii.toprunner.running;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Rec on 2016/4/6.
 */
public class BaiduLocation{
    private final int LOCATE_PERMISSION_REQUEST_CODE = 1; // 定位权限请求码
    private final String LOCATION_COOR_TYPE = "bd09ll"; // 百度地图坐标编码

    private LocationClient mLocationClient = null;
    private LocationClientOption mLocationClientOption = null;
    private BDLocationListener mLocationListener = null;

    private Context context; // 调用该模块的模块的Context

    public BaiduLocation(Context context) {
        this.context = context;
    }

    enum WorkingState {
        DEBUG,
        RUN
    }
    private final WorkingState currentWorkingState = WorkingState.RUN;


    public void initLocate(final IOnBaiduReceivedLocationCallback onBaiduReceivedLocationCallback) {
        // 检查定位权限
        if (Build.VERSION.SDK_INT >= 23) {
            ((AppCompatActivity) context).requestPermissions(new String[]{Manifest.permission.INTERNET,
                            Manifest.permission_group.LOCATION},
                    LOCATE_PERMISSION_REQUEST_CODE);
        }

        // 实例化
        mLocationClient = new LocationClient(context);

        int updateSpan = 1000; // 数据更新间隔，单位：ms
        mLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if(bdLocation.getLocType() == BDLocation.TypeGpsLocation
                        || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                        || bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {

                    onBaiduReceivedLocationCallback.onReceivedLocation(bdLocation);
                    if(currentWorkingState == WorkingState.DEBUG) {
                        String debugString = String.format("BaiduLocation.java - Locate Success(%d):\n东经%f  北纬%f Direction%f",
                                bdLocation.getLocType(), bdLocation.getLongitude(), bdLocation.getLatitude(), bdLocation.getDirection());
                        System.out.println(debugString);
                    }
                }
                else {
                    if(currentWorkingState == WorkingState.DEBUG) {
                        String debugString = String.format("BaiduLocation.java - Locate Failed.\n" +
                                "BaiduSDK Error code is %d.", bdLocation.getLocType());
                        System.err.println(debugString);
                    }
                }
            }
        };

        // 配置LocationClient,用于获取定位信息
        mLocationClientOption = new LocationClientOption();
        mLocationClientOption.setOpenGps(true);
        mLocationClientOption.setLocationNotify(true); // 设置是否当gps有效时按照1次/s频率输出GPS结果
        mLocationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClientOption.setEnableSimulateGps(false);
        mLocationClientOption.disableCache(false);
        mLocationClientOption.setCoorType(LOCATION_COOR_TYPE);
        mLocationClientOption.setScanSpan(updateSpan);
        mLocationClientOption.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(mLocationClientOption);
        mLocationClient.registerLocationListener(mLocationListener);
        System.out.println("BDLocation init done.");
    }

    public void startLocate() {
        if(mLocationClient != null) {
            mLocationClient.start();
            System.out.println("开始定位.");
        }
    }

    public void stopLocate() {
        if(mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    // 配置提醒事件
    BDNotifyListener bdNotifyListener;
    public void setNotifyLocation(double latitude, double longitude, float range, final IOnBaiduNotifyLocationCallback callback) {
        bdNotifyListener = new BDNotifyListener() {
            @Override
            public void onNotify(BDLocation bdLocation, float v) {
                callback.onNotifyLocation(bdLocation);
            }
        };
        bdNotifyListener.SetNotifyLocation(latitude, longitude, range, LOCATION_COOR_TYPE);
        if(mLocationClient != null) {
            mLocationClient.registerNotify(bdNotifyListener);
        }
    }

    // 配置提醒事件 WithFlag
    public void setNotifyLocationWithFlag(double latitude, double longitude, float range, final IOnBaiduNotifyLocationCallback callback, final String flag) {
         bdNotifyListener = new BDNotifyListener() {
            @Override
            public void onNotify(BDLocation bdLocation, float v) {
                callback.onNotifyLocationWithFlag(bdLocation, flag);
            }
        };
        bdNotifyListener.SetNotifyLocation(latitude, longitude, range, LOCATION_COOR_TYPE);
        if(mLocationClient != null) {
            mLocationClient.registerNotify(bdNotifyListener);
        }
    }


}
