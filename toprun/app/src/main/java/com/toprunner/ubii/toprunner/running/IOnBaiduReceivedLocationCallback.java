package com.toprunner.ubii.toprunner.running;

import com.baidu.location.BDLocation;

/**
 * Created by Rec on 2016/4/7.
 */
public interface IOnBaiduReceivedLocationCallback {
    void onReceivedLocation(BDLocation bdLocation);
}
