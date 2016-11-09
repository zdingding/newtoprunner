package com.toprunner.ubii.toprunner.activivty;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mapapi.map.MapView;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.fragment.TrackQueryFragment;
import com.toprunner.ubii.toprunner.fragment.TrackUploadFragment;

import butterknife.ButterKnife;

public class RunningActivity extends AppCompatActivity implements OnClickListener {

    private ToprunnerApplication trackApp = null;

    private ImageView btnTrackUpload;

    private ImageView btnTrackQuery;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    private TrackUploadFragment mTrackUploadFragment;

    private TrackQueryFragment mTrackQueryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_running);
        ButterKnife.bind(this);
        trackApp = (ToprunnerApplication) getApplicationContext();
        // 初始化组件
        initComponent();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // 设置默认的Fragment
        setDefaultFragment();
    }


    /**
     * 初始化组件
     */
    private void initComponent() {
        // 初始化控件
        btnTrackUpload = (ImageView) findViewById(R.id.btn_trackUpload);
        btnTrackQuery = (ImageView) findViewById(R.id.btn_trackQuery);

        btnTrackUpload.setOnClickListener(this);
        btnTrackQuery.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();

        trackApp.initBmap((MapView) findViewById(R.id.bmapView));

    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment() {
        handlerButtonClick(R.id.btn_trackUpload);
    }

    /**
     * 点击事件
     */
    public void onClick(View v) {
        // TODO Auto-generated method stub
        handlerButtonClick(v.getId());
    }

    /**
     * 处理tab点击事件
     *
     * @param id
     */
    private void handlerButtonClick(int id) {
        // 重置button状态
        onResetButton();
        // 开启Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏Fragment
        hideFragments(transaction);

        switch (id) {

            case R.id.btn_trackQuery:

                TrackUploadFragment.isInUploadFragment = false;

                if (mTrackQueryFragment == null) {
                    mTrackQueryFragment = TrackQueryFragment.newInstance(trackApp);
                    transaction.add(R.id.fragment_content, mTrackQueryFragment);
                } else {
                    transaction.show(mTrackQueryFragment);
                }
                if (null != mTrackUploadFragment) {
                    mTrackUploadFragment.startRefreshThread(false);
                }
                mTrackQueryFragment.addMarker();
                btnTrackQuery.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                trackApp.getmBaiduMap().setOnMapClickListener(null);
                break;

            case R.id.btn_trackUpload:

                TrackUploadFragment.isInUploadFragment = true;

                if (mTrackUploadFragment == null) {
                    mTrackUploadFragment = TrackUploadFragment.newInstance(trackApp);
                    transaction.add(R.id.fragment_content, mTrackUploadFragment);
                } else {
                    transaction.show(mTrackUploadFragment);
                }

                mTrackUploadFragment.startRefreshThread(true);
                mTrackUploadFragment.addMarker();
                btnTrackUpload.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                trackApp.getmBaiduMap().setOnMapClickListener(null);
                break;
        }
        // 事务提交
        transaction.commit();

    }

    /**
     * 重置button状态
     */
    private void onResetButton() {
        btnTrackQuery.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
        btnTrackUpload.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (mTrackQueryFragment != null) {
            transaction.hide(mTrackQueryFragment);
        }
        if (mTrackUploadFragment != null) {
            transaction.hide(mTrackUploadFragment);
        }
        // 清空地图覆盖物
        trackApp.getmBaiduMap().clear();
    }

    @Override
    protected void onResume() {
        trackApp.getBmapView().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        trackApp.getBmapView().onPause();
        TrackUploadFragment.isInUploadFragment = false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        trackApp.getClient().onDestroy();
        trackApp.getBmapView().onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
