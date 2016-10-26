package com.toprunner.ubii.toprunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.SettingActivity;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.view.ColorArcProgressBar;

import static com.toprunner.ubii.toprunner.R.layout.fragment_me_user;


public class Me_User extends BaseFragment implements View.OnClickListener {

    private ColorArcProgressBar sudu;
    private ColorArcProgressBar bushu;
    private ImageView setting;

    @Override
    protected void initData() {
        setting.setOnClickListener(this);
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        sudu =  findViewById(R.id.sudu);
        bushu =  findViewById(R.id.bushu);
        setting = findViewById(R.id.setting);

        sudu.setCurrentValues(77);//速度
        bushu.setCurrentValues(33);//步数
    }

    @Override
    public int getLayoutId() {
        return fragment_me_user;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            break;
        }
    }
}
