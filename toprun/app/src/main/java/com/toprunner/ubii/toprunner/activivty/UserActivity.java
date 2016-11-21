package com.toprunner.ubii.toprunner.activivty;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseActivity;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.fragment.DiscoverFragment;
import com.toprunner.ubii.toprunner.fragment.Me_User;


/**
 * Created by ly on 2016/4/25.
 */
public class UserActivity extends BaseActivity {
    private static final int SPORTDETAIL = 1;
    private BaseFragment Me_User;
    private BaseFragment DiscoverFragment;
    private TextView me;
    private TextView start;
    private TextView dc;
    public static Bitmap bmap;//模糊处理需要的背景图是在this类里面完成的截屏
    private FrameLayout id_bottom;
    private ImageView jiantou;
    private RadioButton zhinanzhen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        me = (TextView) findViewById(R.id.wode);
        start = (TextView) findViewById(R.id.start);
        zhinanzhen = (RadioButton) findViewById(R.id.zhinanzhen);
        dc = (TextView) findViewById(R.id.discover);
        id_bottom = (FrameLayout) findViewById(R.id.id_bottom);
        jiantou = (ImageView) findViewById(R.id.jiantou);
        setTabSelection(0);
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0);

            }
        });
        zhinanzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserActivity.this,SportdetailsActivity.class);
                    startActivity(intent);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
                Intent intent = new Intent();
                intent.setClass(UserActivity.this, choose_run.class);                   //起始进入user界面
                startActivityForResult(intent,SPORTDETAIL);

            }
        });
        jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
                id_bottom.setVisibility(View.VISIBLE);
            }
        });
        dc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(2);
                id_bottom.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清除掉上次的选中状态
        clearSelection();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片
                me.setBackgroundResource(R.mipmap.mine);
                if (Me_User == null) {
                    // 如果me_user为空，则创建一个并添加到界面上
                    Me_User = new Me_User();
                    transaction.add(R.id.id_title, Me_User);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(Me_User);
                }
                break;
            case 1:
                start.setBackgroundResource(R.mipmap.start02);
                bmap = takeScreenshot();                                           //在此处进行截屏操作，确保截取到的是user界面的
                if (Me_User == null) {                                                                                    // 如果me_user为空，则创建一个并添加到界面上
                    Me_User = new Me_User();
                    transaction.add(R.id.id_title, Me_User);

                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(Me_User);

                }
                break;
            case 2:
                if (DiscoverFragment == null) {
                    // 如果more_fragment为空，则创建一个并添加到界面上
                    DiscoverFragment = new DiscoverFragment();
                    transaction.add(R.id.id_title, DiscoverFragment);
                } else {                                                                                     // 如果more_fragment不为空，则直接将它显示出来
                    transaction.show(DiscoverFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        me.setBackgroundResource(R.mipmap.mine);
        start.setBackgroundResource(R.mipmap.start01);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (Me_User != null) {
            transaction.hide(Me_User);
        }
        if (DiscoverFragment != null) {
            transaction.hide(DiscoverFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  SPORTDETAIL:
             int i=0  ;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * 真~截屏函数
     *
     * @return
     */
    public Bitmap takeScreenshot() {
        View rootView = this.findViewById(android.R.id.content).getRootView();    //当前界面的内容
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

}
