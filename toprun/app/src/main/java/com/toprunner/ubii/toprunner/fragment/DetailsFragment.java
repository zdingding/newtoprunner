package com.toprunner.ubii.toprunner.fragment;

import android.os.Bundle;
import android.view.View;

import com.test.sign_calender.DatePicker2;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseFragment;

import java.util.Calendar;

/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class DetailsFragment extends BaseFragment {

    private DatePicker2 picker;

    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        picker = (DatePicker2) findViewById(R.id.main_dp);
        picker.setFestivalDisplay(true); //是否显示节日
        picker.setHolidayDisplay(true); //是否显示假期
        picker.setDeferredDisplay(true); //是否显示补休
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        final int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
    }

    @Override
    public int getLayoutId() {
        return R.layout.details;
    }
}
