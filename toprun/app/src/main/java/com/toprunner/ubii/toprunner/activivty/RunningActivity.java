package com.toprunner.ubii.toprunner.activivty;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.fragment.Share;
import com.toprunner.ubii.toprunner.fragment.twobutton;
import com.toprunner.ubii.toprunner.utils.Log4j;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunningActivity extends AppCompatActivity implements twobutton.onTestListener {
    public int dy = 0;
    public int jaintou_long = 0;
    Button greendrag, share, myback;
    boolean isvisible = false;

    private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap = null;

    private TextView tvTitle;
    private float distanceComplete = 0f; // 跑过的里程
    private long startTime = 0; // 开跑时间 单位：ms
    private long timePast = 0; // 已经跑过的时间 单位：s
    private String tvTimerString = null;
    private long susTime = 0;//暂停是的时间 单位： ms
    private Log4j log4j; // 日志系统
    private Logger logger;
    boolean suspended = false; //判断暂停
    // 跑步计时器
    Runnable timer;
    Handler handler;
    TextView tvTimer, tvspeed;
    public String run_time, run_speed, _run_distance;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private void startTimer() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        } else {
            startTime = startTime + (System.currentTimeMillis() - susTime) + 1;
        }
        handler = new Handler();
        timer = new Runnable() {
            @Override
            public void run() {
                timePast = (System.currentTimeMillis() - startTime) / 1000;
                System.out.println("当前starttime为" + startTime);
                String debugString = String.format("跑步用时：%d分%d秒 平均速度：%.2fkm/h", (timePast % 3600) / 60, timePast % 60,
                        distanceComplete / (((double) timePast) / 3600f));
                //  String tvTimerString = String.format("t：%dm%ds \ns：%.2fkm/h", (timePast % 3600) / 60, timePast % 60,
                //  distanceComplete / (((double) timePast) / 3600f));
                tvTimerString = String.format("%dh:%dm:%ds", (timePast / 3600), (timePast % 3600) / 60, timePast % 60);
                run_time = String.format("%d:%d:%d", (timePast / 3600), (timePast % 3600) / 60, timePast % 60);
                tvTimer.setText(tvTimerString);
                tvspeed.setText(String.format("%.2fkm/h", distanceComplete / (((double) timePast) / 3600f)));
                run_speed = String.format("%.2fkm/h", distanceComplete / (((double) timePast) / 3600f));
                logger.trace(debugString);
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(timer, 1000);
        logger.debug("Timer Started");
    }
    private void stopTimer() {
        if (timer != null) {
            handler.removeCallbacks(timer);
            timer = null;
        }
        susTime = System.currentTimeMillis();
        logger.debug("Timer Stopped.");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        SDKInitializer.initialize(getApplicationContext());
        /* 初始化百度地图SDK
         必须在setContentView() 之前调用*/
        setContentView(R.layout.fragment_run);

        Message msg = new Message();
        msg.what = 2;
        Bundle bundle = new Bundle();
        bundle.putString("juli",String.format("%.3f KM", distanceComplete));
       // bundle.putString("nowspeed",);
        bundle.putString("tvspeed",String.format("%.2fkm/h", distanceComplete / (((double) timePast) / 3600f)));
        bundle.putString("nowtimer",tvTimerString);
        //bundle.putString("alltimer",);
        msg.setData(bundle);

        mMapView = (MapView) findViewById(R.id.run_baidu_map);
        mBaiduMap = mMapView.getMap();
        tvTitle = (TextView) findViewById(R.id.textView19);
        tvTimer = (TextView) findViewById(R.id.textView16);
        tvspeed = (TextView) findViewById(R.id.textView18);

        insertDummyContactWrapper();
        final ImageView l = (ImageView) findViewById(R.id.jiantou_long);
        final RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) l.getLayoutParams(); // 取控件mGrid当前的布局参数
        myback = (Button) findViewById(R.id.myback);
        myback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        greendrag = (Button) findViewById(R.id.greendrag);
        greendrag.setOnTouchListener(new View.OnTouchListener() {
            //private float startX = 0;
            private float startY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        // 计算y偏移量
                        dy = (int) (event.getRawY() - startY);
                        // 计算控件的区域
                        int top = v.getTop() + dy;
                        int bottom = v.getBottom() + dy;
                        jaintou_long = l.getHeight() - dy;
                        // 超出范围检测

                        if (top < 0) {
                            top = 0;
                            bottom = 0 + v.getHeight();
                        }
                        if (bottom > 450) {
                            bottom = 450;
                            top = 450 - v.getHeight();
                        }

                        v.layout(v.getLeft(), top, v.getRight(), bottom);
                        linearParams.height = jaintou_long;
                        if (linearParams.height > 0) {
                            l.setLayoutParams(linearParams);
                        }

                        //  startX = event.getRawX();
                        startY = event.getRawY();
                        break;

                    }
                    case MotionEvent.ACTION_UP: {
                        int dy = (int) (event.getRawY() - startY);
                        int top = v.getTop() + dy;
                        int bottom = v.getBottom() + dy;
                        if (bottom > 350) {
                            twobutton twobutton = new twobutton();
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.buttonlayout, twobutton)
                                    .addToBackStack(null)
                                    .commit();
                            linearParams.height = -10;
                            l.setLayoutParams(linearParams);
                            stopDrawTrace();
                        } else {
                            linearParams.height = -10;
                            l.setLayoutParams(linearParams);

                        }
                    }
                    break;
                }
                return false;
            }
        });

        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entershare();
                Bundle bundle = new Bundle();
                bundle.putString("run_time", run_time);
                Share sharefragment = new Share();
                sharefragment.setArguments(bundle);
                share.setEnabled(false);
            }
        });
        startDrawTrace();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    /*   @TargetApi(Build.VERSION_CODES.M)
       private void insertDummyContactWrapper() {
           int hasWriteContactPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
           if (hasWriteContactPermission != PackageManager.PERMISSION_GRANTED) {
               requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
               return;
           }
           initMapView();
       }
   */
    private void insertDummyContactWrapper() {

            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(RunningActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(RunningActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RunningActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    };
                    return;
                }
                ActivityCompat.requestPermissions(RunningActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }



    // 单击事件
    private void setOnClickMap() {
        BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String debugString = String.format("OnClicked: Lat:%f Lng:%f", latLng.latitude, latLng.longitude);
                logger.trace(debugString);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        };
        mBaiduMap.setOnMapClickListener(onMapClickListener);
        logger.debug("地图单击事件注册完毕");
    }

    // 更新Player图标
    private BitmapDescriptor playerAvatar; // 玩家位置标志物图标

    private void updatePlayerAvatar(BDLocation bdLocation) {
        // 构建图标资源
        if (playerAvatar == null) {
            // 缩小图标
            Matrix matrix = new Matrix();
            matrix.postScale(0.3f, 0.3f);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.player);
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // 设置Player图标
            playerAvatar = BitmapDescriptorFactory.fromBitmap(resizeBitmap);
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, playerAvatar)); // 跟随模式，地图视野锁定在Me身上，没有罗盘
            logger.debug("MyLocation 初始化完毕");
        }

        MyLocationData myLocationData = new MyLocationData.Builder()
                .direction(bdLocation.getDirection())
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .build();
        mBaiduMap.setMyLocationData(myLocationData);
    }

    // 绘制运动痕迹
    LatLng currentDrawPosition;
    LatLng prePosition;
    LatLng currentPosition;
    List<LatLng> positionsList; // 保存绘制折线各点
    List<BitmapDescriptor> lineTextureList; // 保存各折线纹理 纹理为纵向图
    List<Integer> lineTextureIndexList; // 保存各折线纹理的索引
    boolean isDrawingTrace = false;
    Random randomDistance; // 增加里程的随机变量
    double drawPointRangeMeters; // 每隔多少米进行一次画线
    int currentLineTextureIndex = 0; // 当前纹理索引
    PolylineOptions tracePolylineOptions;
    Overlay traceOverlayToRemove;
    Overlay traceOverlay;

    private void drawTrace(BDLocation bdLocation) {
        if (isDrawingTrace) {
            currentPosition = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                drawPointRangeMeters = 20;
            } else {
                drawPointRangeMeters = 45;
            }

            // 初始化记录点
            if (prePosition == null) {
                positionsList = new ArrayList<>();
                lineTextureList = new ArrayList<>();
                lineTextureIndexList = new ArrayList<>();
                prePosition = new LatLng(currentPosition.latitude, currentPosition.longitude);
                currentDrawPosition = new LatLng(currentPosition.latitude, currentPosition.longitude);
                positionsList.add(currentDrawPosition);
                lineTextureList.add(BitmapDescriptorFactory.fromResource(R.mipmap.line_texture_1));
                lineTextureList.add(BitmapDescriptorFactory.fromResource(R.mipmap.line_texture_2));
                logger.debug("痕迹记录点初始化完毕");
            }

            // 计算距离上一个记录点（非Draw）的距离，单位：米
            double preDis = DistanceUtil.getDistance(prePosition, currentPosition);

            // 画线和更新里程
            if (preDis >= drawPointRangeMeters) {
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    currentDrawPosition = new LatLng(currentPosition.latitude, currentPosition.longitude);
                } else {
                    currentDrawPosition = new LatLng((prePosition.latitude + currentPosition.latitude) / 2f,
                            (prePosition.longitude + currentPosition.longitude) / 2f);
                }
                positionsList.add(currentDrawPosition); // 添加新的痕迹点

                // 更新材质索引
                currentLineTextureIndex++;
                currentLineTextureIndex %= lineTextureList.size();
                lineTextureIndexList.add(currentLineTextureIndex);

                // 生成新的痕迹的配置
                tracePolylineOptions = new PolylineOptions()
                        .points(positionsList)
                        .customTextureList(lineTextureList)
                        .textureIndex(lineTextureIndexList)
                        .width(10); // 默认 5
                prePosition = new LatLng(currentPosition.latitude, currentPosition.longitude);

                /* 目前这里还不清楚如何更新绘制的痕迹，没有找到任何可以更新原有素材的方法，所以目前的做法是将原有Overlay删除，然后添加新的Overlay。
                * 不能直接将traceOverlay remove()然后赋值新的Overlay，因为无法保证这个操作的原子性，会产生某一时刻刚刚remove()掉缓存中的Overlay，尚未生成新的Overlay时就接受了Draw的指令，就会产生崩溃。
                * 所以此处引入新的引用，保证该Overlay实例在新的Overlay产生前不会被GC掉。*/
                if (traceOverlay != null) {
                    traceOverlayToRemove = traceOverlay;
                    traceOverlay = mBaiduMap.addOverlay(tracePolylineOptions);
                    traceOverlayToRemove.remove();

                    /* 这里应该添加这句，以保证GC时被准确回收，但还没经过测试，如果有中途崩溃的情况发生，就删掉这句。删掉后是不会产生崩溃的情况的。*/
                    traceOverlayToRemove = null;
                } else {
                    traceOverlay = mBaiduMap.addOverlay(tracePolylineOptions);
                }


                // 更新已经跑过的距离
                distanceComplete += ((float) preDis) / 1000f;

                LatLng lastDrawedLatLng = new LatLng(positionsList.get(positionsList.size() - 2).latitude,
                        positionsList.get(positionsList.size() - 2).longitude);
                LatLng curDrawLatLng = new LatLng(positionsList.get(positionsList.size() - 1).latitude,
                        positionsList.get(positionsList.size() - 1).longitude);
                String debugString = String.format("Draw Trace. 绘制距离：%f\n From:(%f, %f) To:(%f, %f) TextureIndex:%d",
                        preDis, lastDrawedLatLng.latitude, lastDrawedLatLng.longitude, curDrawLatLng.latitude, curDrawLatLng.longitude,
                        currentLineTextureIndex);
                logger.trace(debugString);
            }
            DEBUG:
            {
                //
                String debugString = String.format("prePosition:(%f, %f)\npreDis:%.2fm\nDistance:%.3f km",
                        prePosition.latitude, prePosition.longitude, preDis, distanceComplete);
                // tvTitle.setText(debugString);

                tvTitle.setText(String.format("%.3f KM", distanceComplete));
                _run_distance = String.format("%.3f KM", distanceComplete);
                logger.trace(debugString);
            }
        }
    }


    private void startDrawTrace() {
        //startTime = System.currentTimeMillis();
        distanceComplete = 0f;
        // randomDistance = new Random(startTime);
        prePosition = null;
        isDrawingTrace = true;
        startTimer();

    }

    private void stopDrawTrace() {
        isDrawingTrace = false;
        stopTimer();
        // locationClient.stopLocate();
        logger.debug("停止定位，绘制Trace。");
    }

 /*   @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
*/

    private void entershare()                                           //进入分享界面
    {
        RelativeLayout baidumap = (RelativeLayout) findViewById(R.id.share_layout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) baidumap.getLayoutParams();
        layoutParams.bottomMargin = 0;
        baidumap.setLayoutParams(layoutParams);
        System.out.println("params=0+++++++++++++++++++++++++++++++++++++++");
        Share myshare = new Share();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.share_layout, myshare)
                .addToBackStack(null)
                .commit();
        RelativeLayout xiangxishuju;
        xiangxishuju = (RelativeLayout) findViewById(R.id.xiangxishuju);
        xiangxishuju.setVisibility(View.INVISIBLE);
        isvisible = true;

    }

    private void exitshare()                                           //退出分享界面
    {
        RelativeLayout baidumap = (RelativeLayout) findViewById(R.id.share_layout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) baidumap.getLayoutParams();
        layoutParams.bottomMargin = 1000;
        baidumap.setLayoutParams(layoutParams);
        System.out.println("params=1000+++++++++++++++++++++++++++++++++++++++");
        RelativeLayout xiangxishuju;
        xiangxishuju = (RelativeLayout) findViewById(R.id.xiangxishuju);
        xiangxishuju.setVisibility(View.VISIBLE);
        isvisible = false;
        share.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (isvisible) {
            exitshare();
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onTest() {
        startDrawTrace();
        suspended = true;
        System.out.println("调用了接口方法!");
    }

    @Override
    protected void onDestroy() {
        try {
            stopTimer();
        } catch (Exception e) {
            logger.error(e);
        }
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        logger.debug("OnDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        logger.debug("OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        logger.debug("OnPause");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RunningActivity Page", // TODO: Define a title for the content shown.
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
                "RunningActivity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.toprunner.ubii.toprunner/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect(
    );
    }
}
