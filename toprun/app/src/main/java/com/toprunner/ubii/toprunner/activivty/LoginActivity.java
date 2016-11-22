package com.toprunner.ubii.toprunner.activivty;


import android.os.Bundle;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseActivity;
import com.toprunner.ubii.toprunner.view.LoginView;

import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingNotificationJumpHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingNotificationRotateHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchScaleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingRelativeLayout;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_login;

    private   View view_mask;

    private   LoginView mLoginView;
    private SpringingLinearLayout sll_mainContainer = null;
    private SpringingEditText sedt_account = null;
    private SpringingEditText sedt_password = null;
    private SpringingTextView stv_regist = null;
    private SpringingTextView stv_login = null;
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
        sll_mainContainer = (SpringingLinearLayout) findViewById(R.id.sll_mainContainer);
        sedt_account = (SpringingEditText) findViewById(R.id.sedt_account);
        sedt_password = (SpringingEditText) findViewById(R.id.sedt_password);
        stv_regist = (SpringingTextView) findViewById(R.id.stv_regist);
        stv_login = (SpringingTextView) findViewById(R.id.stv_login);
        initSpringLayout();
        initEvent();
        showViews();
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

    private void showViews() {
        new SpringingAlphaShowHandler(this, sll_mainContainer).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(this, sll_mainContainer).showChildrenSequence(500, 100);
    }

    private void initEvent() {

        stv_regist.setOnClickListener(this);
        stv_login.setOnClickListener(this);

    }

    private void initSpringLayout() {
        sll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, sll_mainContainer).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        sll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, sll_mainContainer).setOnlyOnChildren(true, sedt_account, sedt_password));
        stv_regist.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_regist));
        stv_login.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_login));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stv_regist:
                if (sedt_account.getText().toString().equals("")) {
                    new SpringingNotificationJumpHandler(this, sedt_account).start(1);
                    return;
                }
                if (sedt_password.getText().toString().equals("")) {
                    new SpringingNotificationJumpHandler(this, sedt_password).start(1);
                    return;
                }
            break;
            case R.id.stv_login:
                Toast.makeText(LoginActivity.this,"登录",Toast.LENGTH_SHORT).show();
            break;

        }
    }
}
