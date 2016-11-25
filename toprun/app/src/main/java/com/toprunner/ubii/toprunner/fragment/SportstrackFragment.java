package com.toprunner.ubii.toprunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.MultiMapAcitivity;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.listener.MyOrientationListener;
import com.toprunner.ubii.toprunner.service.MonitorService;
import com.toprunner.ubii.toprunner.utils.CacheUtils;
import com.toprunner.ubii.toprunner.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class SportstrackFragment extends BaseFragment implements View.OnClickListener {
    private static final int REQUESTLOCATING = 1;
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00002;
    private static final int SAVEDATA = 2;
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap;
    private Marker mMoveMarker;//移动的坐标
    private double mLatitude;//定位的做标
    private double mLongtitude;//定位的做标
    private boolean isFirstIn = true;
    List< LatLng > pointstwo = new ArrayList< LatLng >();
    List<LatLng> points = new ArrayList<>();
    LatLng p1 = new LatLng(40.055428,116.307654);
    LatLng p2 = new LatLng(40.058369,116.306486);
    LatLng p3 = new LatLng(40.058987,116.309567);
    LatLng p4 = new LatLng(40.056174,116.310906);
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;
   private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private RelativeLayout mMarkerLy;
    // 定位相关
    private static List<LatLng> pointList = new ArrayList<LatLng>();
    private LocationClient mlocationClient;
    private MyLocationListener mLocationListener;
    private Button btn_mylocation;
    // 自定义定位图标
    private static Overlay overlay = null;
    /**
     * 刷新地图线程(获取实时点)
     */
    protected static MapStatusUpdate msUpdate = null;
    // 路线覆盖物
    private static PolylineOptions polyline = null;
    //卫星
    private RadioButton statellite = null;
    /**
     * 开启轨迹服务监听器
     */
    private  OnStartTraceListener startTraceListener = null;
    /**
     * Entity监听器
     */
    private static OnEntityListener entityListener = null;
    /**
     * 刷新地图线程(获取实时点)
     */
    private boolean isComplete =false;
    private Intent serviceIntent = null;
    private  long startTimer = 0;// 开始时间
//运动状态
private boolean isRunning =false;
    //运动的时间
    private long timeCount;//
    private Chronometer tv_run_time;
    /**
     * 采集周期（单位 : 秒）
     */
    private int gatherInterval = 5;

    /**
     * 打包周期（单位 : 秒）
     */
    private int packInterval = 15;
    private Handler mHandler;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REQUESTLOCATING: // 处理重复定位 定时定位 一秒定位一次
                    if(isRunning){
                        //秒表跑的实现方法
                        //运动距离
                        //平均速度
                        //消耗卡路里
                        handler.sendEmptyMessage(REQUESTLOCATING);
                    }
                    break;
                case SAVEDATA://保存运动数据
                    Toast.makeText(getActivity(), "正在结束轨迹服务，请稍候", Toast.LENGTH_LONG).show();
                    //结束轨迹
                    stopTrace();
                   tv_run_start.setText("开始运动");
                    break;
            }
        }
    };



    private LBSTraceClient client;
    private Trace trace;
    private Button btn_stopTrace;

    private TextView tv_run_start;
    private LinearLayout ll_bottom;
    private TextView tv_run_countinue;
    private TextView tv_run_stop;
    // 普通折线，点击时改变宽度
    Polyline mPolyline;
    private Marker mMarker;
    private BitmapDescriptor bdA;

    @Override
    public void setListener() {
        mLocationListener = new MyLocationListener();//定位成功以后的回调
        mlocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);//位置
        option.setScanSpan(1000);
        mlocationClient.setLocOption(option);
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cebterToMyLocation();
            }
        });

    }

    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {
            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int i, String s) {
                if(i == 0 || i == 10006){

                }
                Toast.makeText(UIUtils.getContext(),i+""+s,Toast.LENGTH_SHORT).show();
            }
            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte b, String s) {
                Toast.makeText(UIUtils.getContext(),"用于接收服务端推送消息",Toast.LENGTH_SHORT).show();
            }
        };
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_run_start://点击了開始跑步
                //开启轨迹
               initfolling();
                tv_run_time.setBase(SystemClock.elapsedRealtime());
                tv_run_time.start();
                startTrace();
                //正在跑
                isRunning = true;
                sendRequestLocation();
                tv_run_start.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);
                tv_run_countinue.setText(R.string.run_pause);
                break;
            case R.id.tv_run_countinue://点击暂停/继续
                String text = tv_run_countinue.getText().toString();
                if ("继续".equals(text)) {
                    isRunning = true;
                    sendRequestLocation();
                    tv_run_time.start();
                    tv_run_countinue.setText(R.string.run_pause);
                } else {
                    tv_run_countinue.setText(R.string.run_countinue);
                    tv_run_time.stop();
                    isRunning = false;
                }
                break;
            case R.id.tv_run_stop:
                isRunning = false;
                isComplete = true;
                String format = tv_run_time.getFormat();
                CacheUtils.putString(getActivity(),"time",format);
                tv_run_time.stop();
                tv_run_time.setBase(SystemClock.elapsedRealtime());
                ll_bottom.setVisibility(View.GONE);
                tv_run_start.setText("保存数据");
                tv_run_start.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(SAVEDATA);
                break;
            case R.id.statellite:
              //  if (checked) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
              //  }else{
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
              //  }
                break;
            case R.id.btn_stopTrace:
                Intent intent =new Intent(getActivity(),MultiMapAcitivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initfolling() {
        mBaiduMap.setMyLocationConfigeration( new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, mIconLocation));
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mMapView = findViewById(R.id.run_baidu_map);
        btn_mylocation = findViewById(R.id.btn_mylocation);
        statellite = (RadioButton) view.findViewById(R.id.statellite);//卫星地图
        btn_stopTrace = (Button) view.findViewById(R.id.btn_stopTrace);
        tv_run_start = (TextView) view.findViewById(R.id.tv_run_start);
        ll_bottom = findViewById(R.id.ll_bottom);
        tv_run_countinue = findViewById(R.id.tv_run_countinue);
        tv_run_stop = findViewById(R.id.tv_run_stop);
        tv_run_time = findViewById(R.id.tv_run_time);
        mMapView.showZoomControls(false);//不缩放
        mBaiduMap = mMapView.getMap();
        drawline();
        // 设置采集周期
        // 设置http请求协议类型
        setRequestType();
        //使用消息处理机制处理消息
        initLocation();
        initMarker();
    }

    private void drawline() {
        LatLng llA = new LatLng(40.033579,116.311027);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);
        bdA = BitmapDescriptorFactory
                .fromResource(R.mipmap.d);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
    }

    protected void initData() {
        mBaiduMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
            @Override
            public boolean onPolylineClick(Polyline polyline) {
                if(polyline == mPolyline){
                    Toast.makeText(getActivity(),"点击了线",Toast.LENGTH_SHORT).show();
                    mHandler = new Handler(Looper.getMainLooper());
                    moveLooper();
                }
                return false;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        getActivity(),
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });
        //开始运动
        tv_run_start.setOnClickListener(this);
        tv_run_countinue.setOnClickListener(this);
        tv_run_stop.setOnClickListener(this);
        statellite.setOnClickListener(this);
        btn_stopTrace.setOnClickListener(this);
        client = ((ToprunnerApplication) getActivity().getApplication()).getClient();
        setInterval();
        trace = ((ToprunnerApplication) getActivity().getApplication()).getTrace();
    }
//开始运动
    private void moveLooper() {
        new Thread() {

            public void run() {

                while (true) {

                    for (int i = 0; i < points.size() - 1; i++) {


                        final LatLng startPoint = points.get(i);
                        final LatLng endPoint = points.get(i+1);
                        mMoveMarker
                                .setPosition(startPoint);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mMapView == null) {
                                    return;
                                }
                                mMoveMarker.setRotate((float) getAngle(startPoint,
                                        endPoint));
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);

                        double intercept = getInterception(slope, startPoint);

                        double xMoveDistance = isReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);


                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse);
                             j = j - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }

                            final LatLng finalLatLng = latLng;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMapView == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);
                                }
                            });
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

        }.start();

    }
    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }
    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }
    //发送消息
    private void sendRequestLocation() {
        handler.sendEmptyMessage(REQUESTLOCATING);
    }

    private void setRequestType() {
    }

    private void setInterval() {
        client.setInterval(gatherInterval, packInterval);
    }


    private void stopTrace() {
    }
    private void startTrace() {
        if (null == startTraceListener) {
            initOnStartTraceListener();
        }
        client.startTrace(trace, startTraceListener);
        if (!MonitorService.isRunning) {
            // 开启监听service
            MonitorService.isCheck = true;
            MonitorService.isRunning = true;
            startMonitorService();
        }
 // 初始化entity监听器

        if (null == entityListener) {
            initOnEntityListener();
        }
        /**
         * 查询实时轨迹
         */
        client.queryRealtimeLoc(126470,entityListener);
    }

    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {
                //失败地1回调接口
            @Override
            public void onRequestFailedCallback(String s) {
                Toast.makeText(UIUtils.getContext(),"初始化entity失败",Toast.LENGTH_SHORT).show();
            }
            // 添加entity回调接口
            @Override
            public void onAddEntityCallback(String s) {
                super.onAddEntityCallback(s);
                Toast.makeText(UIUtils.getContext(),"初始化entity回调"+s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQueryEntityListCallback(String s) {
                super.onQueryEntityListCallback(s);
                Toast.makeText(UIUtils.getContext(),"成功entity"+s,Toast.LENGTH_SHORT).show();
                TraceLocation entityLocation = new TraceLocation();
                try {
                    JSONObject dataJson =new JSONObject(s);
                    if(null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0
                            && dataJson.has("size") && dataJson.getInt("size") > 0){
                        JSONArray entities = dataJson.getJSONArray("entities");
                        JSONObject entity = entities.getJSONObject(0);
                        JSONObject point = entity.getJSONObject("realtime_point");
                        JSONArray location = point.getJSONArray("location");
                        entityLocation.setLongitude(location.getDouble(0));
                        entityLocation.setLatitude(location.getDouble(1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UIUtils.getContext(),"解析失败",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onReceiveLocation(TraceLocation traceLocation) {
                super.onReceiveLocation(traceLocation);
            }
        };
    }



    private void startMonitorService() {
        serviceIntent = new Intent(getActivity().getApplication(),
                MonitorService.class);
        getActivity().getApplication().startService(serviceIntent);
    }

    private void initMarker() {
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p1);
         int[] color = { 0xFFFBE01C, 0xFFE1E618, 0xFF7DFF00, 0xffDE2C00 };
        List<Integer> colorList = new ArrayList<>();
        for(int i=0;i<points.size();i++){
            colorList.add(color[i%4]);
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .colorsValues(colorList).points(points);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        mPolyline.setDottedLine(true);//虚线

        OverlayOptions   markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked)).position(points.get(0))
                .rotate((float) getAngle(0));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
        // 添加多边形
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts = new ArrayList<>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        OverlayOptions ooPolygon = new PolygonOptions().points(pts)
                .stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
        mBaiduMap.addOverlay(ooPolygon);
        // 添加文字 公司名字
        LatLng ubii = new LatLng(40.0430120000,116.3177550000);
        OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
                .fontSize(24).fontColor(0xFFFF00FF).text("优比交互")
                .position(ubii);
        mBaiduMap.addOverlay(ooText);
    }

    private void initLocation() {
        // 初始化图标
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.mipmap.navi_map_gps_locked);
        mlocationClient = new LocationClient(UIUtils.getContext());
        mLocationListener = new MyLocationListener();
        mlocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mlocationClient.setLocOption(option);
        //
        myOrientationListener = new MyOrientationListener(UIUtils.getContext());
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.sportstrack;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mlocationClient.isStarted())
            mlocationClient.start();
        // 开启方向传感器
        myOrientationListener.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
        // 停止方向传感器
       myOrientationListener.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        bdA.recycle();
        mMapView.onDestroy();
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }
    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }
    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)//
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();
           mBaiduMap.setMyLocationData(data);
            // 更新经纬度
            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();
                initlocationMode();
            // 设置自定义图标

            if (isFirstIn)
            {
                isFirstIn = false;
                cebterToMyLocation();
                Toast.makeText(UIUtils.getContext(), bdLocation.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initlocationMode() {
        mBaiduMap.setMyLocationConfigeration( new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation));
    }

    private void cebterToMyLocation() {
        LatLng latLng = new LatLng(mLatitude,
                mLongtitude);
        pointList.add(latLng);
        // 绘制实时点

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }


}
