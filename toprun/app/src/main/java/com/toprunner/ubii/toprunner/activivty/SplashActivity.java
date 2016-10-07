package com.toprunner.ubii.toprunner.activivty;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.utils.SpUtils;
import com.toprunner.ubii.toprunner.utils.UIUtils;

public class SplashActivity extends AppCompatActivity {
    private long startTime;//开始的时间
    private String currentVersion;
    private TextView tv_welcome_version;
    private static final int WHAT_START_MAIN = 1;//进入主界面
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_START_MAIN:

                    // boolean isFirstRun = PreferencesManager.getInstance(UIUtils.getContext()).get(Constants.IS_FIRST_RUN, true);
                    boolean isFirstRun = false;
                    if (isFirstRun) {
                        Intent intent = new Intent(UIUtils.getContext(), GuideActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_welcome_version = (TextView) findViewById(R.id.tv_welcome_version);
        //1得到当前时间
        startTime = System.currentTimeMillis();
        //2. 显示当前版本号
        try {
            currentVersion = getVersion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_welcome_version.setText("版本号: " + currentVersion);
        //3. 版本更新检查
        checkVersion();

        intoMainPage();
        //4. 生成应用桌面快捷方式
        makeShortcut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void makeShortcut() {
        boolean shortcut = SpUtils.getInstance(this).getBoolean(SpUtils.SHORT_CUT, false);
        if (!shortcut) {//说明没有生成过
            Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            //图标
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.logox));
            //名称
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "领跑者");
            //点击启动Activity的intent
            Intent clickIntent = new Intent("com.toprunner.ubii.toprunner.MainAction");
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, clickIntent);
            //发广播
            sendBroadcast(intent);
            //保存
            SpUtils.getInstance(this).save(SpUtils.SHORT_CUT, true);
        }

    }

    private void checkVersion() {
        boolean connected = isConnected();
        if (!connected) {
            Toast.makeText(this, "没有联网...", Toast.LENGTH_SHORT).show();
            //进入主界面
            intoMainPage();
        } else {//联上了
            new Thread(new Runnable() {

                @Override
                public void run() {


                }
            }).start();


        }
    }

    private void intoMainPage() {
        long time = System.currentTimeMillis();
        //得到需要延迟的时间
        int delayTime = (int) (2000 - (time - startTime));

        if (delayTime < 0) {
            delayTime = 0;
        }
        handler.sendEmptyMessageDelayed(WHAT_START_MAIN, delayTime);

    }

    public String getVersion() throws PackageManager.NameNotFoundException {
        String version = "未知版本";
        PackageManager manager = getPackageManager();
        PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
        version = packageInfo.versionName;
        return version;
    }

    public boolean isConnected() {
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }
        return connected;
    }
}

