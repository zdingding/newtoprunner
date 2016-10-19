package com.toprunner.ubii.toprunner.activivty;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.fragment.DiscoverFragment;
import com.toprunner.ubii.toprunner.fragment.me_user;

/**
 * Created by ly on 2016/4/25.
 */
public class User extends Activity {

    private com.toprunner.ubii.toprunner.fragment.me_user me_user;
    private DiscoverFragment DiscoverFragment;
    private FragmentManager fragmentManager;
    private TextView me;
    private TextView start;
    private TextView dc;
    public static  Bitmap bmap ;                                                //模糊处理需要的背景图是在this类里面完成的截屏的
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        fragmentManager = getFragmentManager();
        me = (TextView) findViewById(R.id.ME);
        start = (TextView) findViewById(R.id.start);
        dc = (TextView) findViewById(R.id.discover);


        setTabSelection(0);

        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0);

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);

                Intent intent = new Intent();
                intent.setClass(User.this, choose_run.class);                   //起始进入user界面
                startActivity(intent);
            }
        });
        dc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(2);
                System.out.println("点击了discover");

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清除掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片
                me.setBackgroundResource(R.mipmap.wo02);
                if (me_user == null) {
                    // 如果me_user为空，则创建一个并添加到界面上
                    me_user = new me_user();
                    transaction.add(R.id.id_title, me_user);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(me_user);
                }
                break;
            case 1:
                start.setBackgroundResource(R.mipmap.start02);
                bmap =  takeScreenshot();                                           //在此处进行截屏操作，确保截取到的是user界面的
                if (me_user == null) {                                                                                    // 如果me_user为空，则创建一个并添加到界面上
                    me_user = new me_user();
                    transaction.add(R.id.id_title, me_user);

                } else {
                                                                                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(me_user);

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
        me.setBackgroundResource(R.mipmap.wo01);
        start.setBackgroundResource(R.mipmap.start01);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (me_user != null) {
            transaction.hide(me_user);
        }
        if (DiscoverFragment != null) {
            transaction.hide(DiscoverFragment);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "User Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.toprunner.ubii.toprunner/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "User Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.toprunner.ubii.toprunner/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }



    /**
     * 真~截屏函数
     * @return
     */
    public Bitmap takeScreenshot() {
        View rootView = this.findViewById(android.R.id.content).getRootView();    //当前界面的内容
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
}
