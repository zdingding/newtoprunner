package com.toprunner.ubii.toprunner.fragment;

import android.os.Bundle;
import android.view.View;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.view.ColorArcProgressBar;

import static com.toprunner.ubii.toprunner.R.layout.fragment_me_user;


public class Me_User extends BaseFragment {

    private ColorArcProgressBar sudu;
    private ColorArcProgressBar bushu;
    @Override
    protected void initData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        sudu =  findViewById(R.id.sudu);
        bushu =  findViewById(R.id.bushu);
        sudu.setCurrentValues(77);
        bushu.setCurrentValues(33);
    }

    @Override
    public int getLayoutId() {
        return fragment_me_user;
    }


}
