package com.toprunner.ubii.toprunner.factory;

import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.fragment.ChartFragment;
import com.toprunner.ubii.toprunner.fragment.DetailsFragment;
import com.toprunner.ubii.toprunner.fragment.RoadFragment;
import com.toprunner.ubii.toprunner.fragment.SportstrackFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式工厂类管理
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class SporrtdetailsFragmentFactory {
    private SporrtdetailsFragmentFactory() {
    }

    ;
    private static List<BaseFragment> mDatas = null;

    public static synchronized List<BaseFragment> getDatas() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
            mDatas.add(new SportstrackFragment());
            mDatas.add(new DetailsFragment());
            mDatas.add(new RoadFragment());
            mDatas.add(new ChartFragment());
        }
        return mDatas;
    }
}
