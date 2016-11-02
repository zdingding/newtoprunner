package com.toprunner.ubii.toprunner.running;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.toprunner.ubii.toprunner.utils.Log4j;
import com.toprunner.ubii.toprunner.R;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Rec on 2016/3/22.
 */
public class RunActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap = null;
    private TextView tvTitle;
    private float distanceComplete = 0f; // 跑过的里程
    private long startTime = 0; // 开跑时间 单位：ms
    private long timePast = 0; // 已经跑过的时间 单位：s
    private Log4j log4j; // 日志系统
    private Logger logger;
    LatLng[] notifyPoints = {
            new LatLng(40.042263, 116.316999),
            new LatLng(40.044362, 116.315984),
            new LatLng(40.043913, 116.314178),
            new LatLng(40.046274, 116.312984),
            new LatLng(40.047766, 116.317924),
            new LatLng(40.045536, 116.319757),
            new LatLng(40.044983, 116.317790),
            new LatLng(40.043119, 116.319434),
            new LatLng(40.093119, 116.319434)
    };
    String[] notifyTexts = {
            "开始跑步，加油！",
            "左转",
            "右转",
            "右转",
            "右转，就快到达终点了，加油！"
    };
    // 跑步计时器
    Runnable timer;
    Handler handler;
    TextView tvTimer;
    private void startTimer() {
        startTime = System.currentTimeMillis();
        handler = new Handler();
        timer= new Runnable() {
            @Override
            public void run() {
                timePast = (System.currentTimeMillis() - startTime) / 1000;

                String debugString = String.format("跑步用时：%d分%d秒 平均速度：%.2fkm/h", (timePast % 3600) / 60, timePast % 60,
                        distanceComplete / (((double) timePast) / 3600f));
                String tvTimerString = String.format("t：%dm%ds \ns：%.2fkm/h", (timePast % 3600) / 60, timePast % 60,
                        distanceComplete / (((double) timePast) / 3600f));
                tvTimer.setText(tvTimerString);
                logger.trace(debugString);
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(timer, 1000);
        logger.debug("Timer Started");
    }
    private void stopTimer() {
        if(timer != null) {
            handler.removeCallbacks(timer);
            timer = null;
        }
        logger.debug("Timer Stopped.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().hide();
        }
        catch (NullPointerException e) {
            System.err.println("There is no supported bar.");
        }

        /* 初始化百度地图SDK
         必须在setContentView() 之前调用*/
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_run);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_switch_my_position).setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.mv_baidu_map);
        mBaiduMap = mMapView.getMap();
        tvTitle = (TextView) findViewById(R.id.tv_run_title);
        tvTimer = (TextView) findViewById(R.id.tv_timer);


        initLog(); // 开启Log系统
        initSpeech(); // 开启TTS
        insertDummyContactWrapper();
     //   initMapView(); // 定位
        logger.debug("OnCreate");
    }

    private void initLog() {
        log4j = new Log4j();
        log4j.configLog();
        logger = log4j.logger;
        logger.debug("——————————————————————————————————————————————————————————————————");
        logger.debug("日志系统初始化完毕");
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

  /*  @TargetApi(Build.VERSION_CODES.M)
    private void  insertDummyContactWrapper()
    {
        int hasWriteContactPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(hasWriteContactPermission != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        initMapView();
    }*/
  private void insertDummyContactWrapper() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
      {
          int hasWriteContactPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
          if(hasWriteContactPermission != PackageManager.PERMISSION_GRANTED)
          {
              requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);
              return;
          }
          initMapView();
      }
      else
      {
          int hasWriteContactsPermission = ContextCompat.checkSelfPermission(RunActivity.this,
                  Manifest.permission.ACCESS_FINE_LOCATION);
          if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
              if (!ActivityCompat.shouldShowRequestPermissionRationale(RunActivity.this,
                      Manifest.permission.ACCESS_FINE_LOCATION)) {
                  new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          ActivityCompat.requestPermissions(RunActivity.this,
                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                  REQUEST_CODE_ASK_PERMISSIONS);
                      }
                  };
                  return;
              }
              ActivityCompat.requestPermissions(RunActivity.this,
                      new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                      REQUEST_CODE_ASK_PERMISSIONS);
              return;
          }
          initMapView();
      }

  }

    // 定位信息
    private void initMapView() {

        setNotifyPoints();
        drawNotifyPoints();
        setOnClickMap();

        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(17));
        mMapView.showZoomControls(false);
        logger.debug("地图View初始化完毕");
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

    // 设置通知点
    private void setNotifyPoints() {
        for(int i = 0; i < notifyPoints.length; i++) {
            final int finalI = i;
            LatLng notifyPoint = notifyPoints[i];

        }
        logger.debug("通知点设置完毕");
    }

    // 绘制通知点
    List<OverlayOptions> notifyPointsOverlayOptions = new ArrayList<>();
    private void drawNotifyPoints() {
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
        Bitmap bpNotifyPoint = BitmapFactory.decodeResource(getResources(), R.mipmap.location_go);
        Bitmap bpResizedNotifyPoint = Bitmap.createBitmap(bpNotifyPoint, 0, 0, bpNotifyPoint.getWidth(), bpNotifyPoint.getHeight(), matrix, true);
        BitmapDescriptor bluePoint = BitmapDescriptorFactory.fromBitmap(bpResizedNotifyPoint);
        notifyPointsOverlayOptions.clear();
        for(int i = 0; i < notifyPoints.length; i++) {
            LatLng notifyPoint = new LatLng(notifyPoints[i].latitude, notifyPoints[i].longitude);
            notifyPointsOverlayOptions.add(new MarkerOptions()
                    .position(notifyPoint)
                    .icon(bluePoint));
        }
        mBaiduMap.addOverlays(notifyPointsOverlayOptions);
        logger.debug("通知点OverlayOptions 初始化完毕");
    }

    // 更新Player图标
    private BitmapDescriptor playerAvatar; // 玩家位置标志物图标
    private void updatePlayerAvatar(BDLocation bdLocation) {
        // 构建图标资源
        if(playerAvatar == null) {
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

    // 添加Speech
    private TextToSpeech tts;
    private void initSpeech() {
        // 实例化TTS变量
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int supported = tts.setLanguage(Locale.CHINA);
                    if(supported != TextToSpeech.LANG_AVAILABLE && supported != TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        Toast.makeText(RunActivity.this, "Language is not supported.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        logger.debug("Speech 初始化完毕");
    }

    private void disposeSpeech() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        logger.debug("Speech Disposed.");
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
        if(isDrawingTrace) {
            currentPosition = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                drawPointRangeMeters = 20;
            }
            else {
                drawPointRangeMeters = 45;
            }

            // 初始化记录点
            if(prePosition == null) {
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
            if(preDis >= drawPointRangeMeters) {
                if(bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    currentDrawPosition = new LatLng(currentPosition.latitude, currentPosition.longitude);
                }
                else {
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
                if(traceOverlay != null) {
                    traceOverlayToRemove = traceOverlay;
                    traceOverlay = mBaiduMap.addOverlay(tracePolylineOptions);
                    traceOverlayToRemove.remove();

                    /* 这里应该添加这句，以保证GC时被准确回收，但还没经过测试，如果有中途崩溃的情况发生，就删掉这句。删掉后是不会产生崩溃的情况的。*/
                    traceOverlayToRemove = null;
                }
                else {
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
                tvTitle.setText(debugString);
                logger.trace(debugString);
            }
        }
    }

    @Override
    protected void onDestroy() {
        try{
            stopTimer();
        }
        catch (Exception e) {
            logger.error(e);
        }
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        disposeSpeech();
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

    private void startDrawTrace() {
        startTime = System.currentTimeMillis();
        distanceComplete = 0f;
        randomDistance = new Random(startTime);
        prePosition = null;
        isDrawingTrace = true;
        startTimer();
        logger.debug("开始定位，绘制Trace。");
    }

    private void stopDrawTrace() {
        isDrawingTrace = false;
        stopTimer();
        logger.debug("停止定位，绘制Trace。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if(!isDrawingTrace) {
                    startDrawTrace();
                    ((Button) findViewById(R.id.btn_start)).setText("Stop");
                }
                else {
                    stopDrawTrace();
                    ((Button) findViewById(R.id.btn_start)).setText("Start");
                }
                break;
            case R.id.btn_switch_my_position:
                boolean switchToEnabled = !mBaiduMap.isMyLocationEnabled();
                mBaiduMap.setMyLocationEnabled(switchToEnabled);
                if(switchToEnabled) {
                    ((Button) findViewById(R.id.btn_switch_my_position)).setText("setOff");
                }
                else {
                    ((Button) findViewById(R.id.btn_switch_my_position)).setText("setOn");
                }
            default:
                break;
        }
    }
}
