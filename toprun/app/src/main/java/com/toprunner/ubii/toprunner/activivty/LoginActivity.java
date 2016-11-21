package com.toprunner.ubii.toprunner.activivty;


import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseActivity;
import com.toprunner.ubii.toprunner.view.LoginView;

public class LoginActivity extends BaseActivity  {

    private ImageView btn_login;

    private   View view_mask;

    private   LoginView mLoginView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            initView();
        }

    private void initView() {
        mLoginView = (LoginView)findViewById(R.id.mLoginView);
        view_mask = (View)findViewById(R.id.view_mask);
        btn_login = (ImageView)findViewById(R.id.btn_login);
        mLoginView.setEnabled(true);
        //设置遮罩阴影层点击消失该界面
        view_mask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mLoginView.isShow()){
                    mLoginView.dismiss();
                }
            }
        });
        //设置登录界面状态监听
        mLoginView.setOnStatusListener(new LoginView.onStatusListener() {

            @Override
            public void onShow() {
                //显示
                view_mask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDismiss() {
                //隐藏
                view_mask.setVisibility(View.GONE);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mLoginView.isShow()){
                    mLoginView.dismiss();
                }else{
                    mLoginView.show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==  KeyEvent.KEYCODE_BACK){
            if(mLoginView.isShow()){
                mLoginView.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
