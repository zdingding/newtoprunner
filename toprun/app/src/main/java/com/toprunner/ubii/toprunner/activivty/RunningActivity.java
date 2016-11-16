package com.toprunner.ubii.toprunner.activivty;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.favorite.FavoriteManager;
import com.baidu.mapapi.favorite.FavoritePoiInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.fragment.TrackQueryFragment;
import com.toprunner.ubii.toprunner.fragment.TrackUploadFragment;

import butterknife.ButterKnife;

public class RunningActivity extends AppCompatActivity implements OnClickListener, BaiduMap.OnMapLongClickListener{
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.d);
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
        // 初始化收藏夹
        FavoriteManager.getInstance().init();//本地收藏夹
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

        trackApp.initBmap((TextureMapView) findViewById(R.id.bmapView));

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
                trackApp.getmBaiduMap().setOnMapLongClickListener(this);
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
                trackApp.getmBaiduMap().setOnMapLongClickListener(this);
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
        // 释放收藏夹功能资源
        FavoriteManager.getInstance().destroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions option = new MarkerOptions().icon(bdA).position(latLng);
        trackApp.getmBaiduMap().addOverlay(option);
        final FavoritePoiInfo info = new FavoritePoiInfo();//保存收藏的坐标点
        final EditText editText = new EditText(this);
        editText.setHint("名称不能重复");
        info.pt(latLng);
        new AlertDialog.Builder(this)
                .setTitle("输入收藏位置的名称")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = editText.getText().toString();
                        info.poiName(newName);
                        if (FavoriteManager.getInstance().add(info) == 1) {
                            Toast.makeText(RunningActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RunningActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();


    }


}
