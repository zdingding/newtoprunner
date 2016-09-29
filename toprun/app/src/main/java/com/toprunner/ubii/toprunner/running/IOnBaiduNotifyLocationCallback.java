package com.toprunner.ubii.toprunner.running;

import com.baidu.location.BDLocation;

/**
 * Created by Rec on 2016/4/7.
 */
public abstract class IOnBaiduNotifyLocationCallback {
    public void onNotifyLocationWithFlag(BDLocation bdLocation, String locationFlag){}
    public void onNotifyLocation(BDLocation bdLocation){}
}
