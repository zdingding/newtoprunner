package com.toprunner.ubii.toprunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.BluetoothActivity;
import com.toprunner.ubii.toprunner.activivty.JibuActivity;
import com.toprunner.ubii.toprunner.activivty.QuanActivity;
import com.toprunner.ubii.toprunner.activivty.SettingActivity;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.view.ColorArcProgressBar;

import static com.toprunner.ubii.toprunner.R.layout.fragment_me_user;


public class Me_User extends BaseFragment implements View.OnClickListener {

    private ColorArcProgressBar sudu;
    private ColorArcProgressBar bushu;
    private ImageView setting;
    private ImageView massage;
    private TextView tv_lanya;
    private Intent intent;

    @Override
    protected void initData() {

        setting.setOnClickListener(this);
        massage.setOnClickListener(this);
        tv_lanya.setOnClickListener(this);
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        sudu =  findViewById(R.id.sudu);
        bushu =  findViewById(R.id.bushu);
        setting = findViewById(R.id.setting);
        massage = findViewById(R.id.massage);
        tv_lanya = findViewById(R.id.tv_lanya);

        sudu.setCurrentValues(77);//速度
        bushu.setCurrentValues(0);//步数
        bushu.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return fragment_me_user;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_lanya:
                intent = new Intent(getActivity(),BluetoothActivity.class);
                startActivity(intent);
            break;
            case R.id.setting:
                intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            break;
            case R.id.massage:
                intent = new Intent(getActivity(),QuanActivity.class);
                startActivity(intent);
            break;
            case R.id.bushu:
                intent = new Intent(getActivity(),JibuActivity.class);
                startActivity(intent);
            break;
        }
    }
}
