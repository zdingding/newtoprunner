package com.toprunner.ubii.toprunner.activivty;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseActivity;


public class choose_run extends BaseActivity  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.fragment_choose_run);

    }

/**
 * 调用返回键的方法
 */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}




