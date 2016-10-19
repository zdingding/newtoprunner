package com.toprunner.ubii.toprunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.TrackReceiver;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.service.MonitorService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.toprunner.ubii.toprunner.fragment.SportstrackFragment.msUpdate;


/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class RoadFragment extends BaseFragment implements View.OnClickListener {
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap;
    private Button btnStartTrace = null;
    private Intent serviceIntent = null;
    private Button btnStopTrace = null;
    /**
     * 开启轨迹服务监听器
     */
    protected static OnStartTraceListener startTraceListener = null;
    /**
     * 停止轨迹服务监听器
     */
    protected static OnStopTraceListener stopTraceListener = null;
    /**
     * Entity监听器
     */
    private static OnEntityListener entityListener = null;
    //轨迹点的集合
    private static List<LatLng> pointList = new ArrayList<LatLng>();
    private LBSTraceClient client;
    private Trace trace;
    private ToprunnerApplication trackApp;
    // 覆盖物
    private static OverlayOptions overlayOptions;
    //是否开启轨迹
    private boolean isTraceStarted = false;
    /**
     * 刷新地图线程(获取实时点)
     */
    protected RefreshThread refreshThread = null;
    /**
     * 图标
     */
    private static Overlay overlay = null;
    private static BitmapDescriptor realtimeBitmap;

    // 路线覆盖物
    private static PolylineOptions polyline = null;
    /**
     * 打包周期（单位 : 秒）
     */
    private int packInterval = 15;
    /**
     * 采集周期（单位 : 秒）
     */
    private int gatherInterval = 5;
    @Override
    public void setListener() {

    }
    private static boolean isRegister = false;
    protected static PowerManager pm = null;

    public static PowerManager.WakeLock wakeLock = null;
    private TrackReceiver trackReceiver = new TrackReceiver();
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        // 初始化
        initView();

        // 初始化监听器
        initListener();


        client = ((ToprunnerApplication) getActivity().getApplication()).getClient();
        // 设置采集周期
        setInterval();
        // 设置http请求协议类型
        setRequestType();

    }

    private void setRequestType() {
        int type = 0;
        client.setProtocolType(type);
    }

    private void setInterval() {
        client.setInterval(gatherInterval, packInterval);
    }

    private void initListener() {
        // 初始化开启轨迹服务监听器
        if (null == startTraceListener) {
            initOnStartTraceListener();
        }

        // 初始化停止轨迹服务监听器
        if (null == stopTraceListener) {
            initOnStopTraceListener();
        }

        // 初始化entity监听器
        if (null == entityListener) {
            initOnEntityListener();
        }
    }

    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub

            }

            // 添加entity回调接口
            public void onAddEntityCallback(String arg0) {
                // TODO Auto-generated method stub

            }

            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String message) {
                // TODO Auto-generated method stub
                TraceLocation entityLocation = new TraceLocation();
                try {
                    JSONObject dataJson = new JSONObject(message);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0
                            && dataJson.has("size") && dataJson.getInt("size") > 0) {
                        JSONArray entities = dataJson.getJSONArray("entities");
                        JSONObject entity = entities.getJSONObject(0);
                        JSONObject point = entity.getJSONObject("realtime_point");
                        JSONArray location = point.getJSONArray("location");
                        entityLocation.setLongitude(location.getDouble(0));
                        entityLocation.setLatitude(location.getDouble(1));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block

                    return;
                }
                showRealtimeTrack(entityLocation);
            }


            @Override
            public void onReceiveLocation(TraceLocation location) {
                // TODO Auto-generated method stub
                showRealtimeTrack(location);
            }

        };
    }

    private void showRealtimeTrack(TraceLocation entityLocation) {
        if (null == refreshThread || !refreshThread.refresh) {
            return;
        }
        double latitude = entityLocation.getLatitude();
        double longitude = entityLocation.getLongitude();
        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
        } else {
            LatLng latLng = new LatLng(latitude, longitude);
            if (1 == entityLocation.getCoordType()) {
                LatLng sourceLatLng = latLng;
                CoordinateConverter converter = new
                        CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(sourceLatLng);
                latLng = converter.convert();
            }
            pointList.add(latLng);
                // 绘制实时点
                drawRealtimePoint(latLng);
        }


    }

    private void drawRealtimePoint(LatLng point) {
        if (null != overlay) {
            overlay.remove();
        }
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        if (null == realtimeBitmap) {
            realtimeBitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.navi_map_gps_locked);
        }
        overlayOptions = new MarkerOptions().position(point)
                .icon(realtimeBitmap).zIndex(9).draggable(true);
        if (pointList.size() >= 2 && pointList.size() <= 10000) {
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(pointList);
        }
        addMarker();
    }

    private void addMarker() {
        if (null != msUpdate) {
            mBaiduMap.setMapStatus(msUpdate);
        }
        // 路线覆盖物
        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

        // 实时点覆盖物
        if (null != overlayOptions) {
            mBaiduMap.addOverlay(overlayOptions);
        }
    }

    private void initOnStopTraceListener() {
        stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                // TODO Auto-generated method stub
                startRefreshThread(false);
                trackApp.getClient().onDestroy();
            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        };
    }

    private void startRefreshThread(boolean isStart) {
        if (null == refreshThread) {
            refreshThread = new RefreshThread();
        }
        refreshThread.refresh = isStart;
        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }
    }

    private void initOnStartTraceListener() {
        startTraceListener = new OnStartTraceListener() {
            @Override
            public void onTraceCallback(int i, String s) {

            }

            // 轨迹服务推送接口（用于接收服务端推送消息
            @Override
            public void onTracePushCallback(byte b, String s) {
                if (0x03 == b || 0x04 == b) {

                }
            }
        };
    }


    //初始化
    private void initView() {  
        mMapView = (MapView) findViewById(R.id.road_baidu_map);
         mBaiduMap = mMapView.getMap();
        btnStartTrace = (Button) findViewById(R.id.btn_startTrace);
        btnStopTrace = (Button) findViewById(R.id.btn_stopTrace);
    }

    @Override
    protected void initData() {
        btnStartTrace.setOnClickListener(this);
        btnStopTrace.setOnClickListener(this);
        trackApp = (ToprunnerApplication) getActivity().getApplication();
        client = trackApp.getClient();
        trace = trackApp.getTrace();
    }

    @Override
    public int getLayoutId() {
        return R.layout.road;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startTrace:
                startTrace();
                if (!isRegister) {
                    if (null == pm) {
                        pm = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);
                    }
                    if (null == wakeLock) {
                        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
                    }
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(Intent.ACTION_SCREEN_OFF);
                    filter.addAction(Intent.ACTION_SCREEN_ON);
                    filter.addAction("com.baidu.trace.action.GPS_STATUS");
                    trackApp.registerReceiver(trackReceiver, filter);
                    isRegister = true;
                }

                break;
            case R.id.btn_stopTrace:
                stopTrace();
                break;
        }

    }

    private void stopTrace() {
        // 停止监听service
        MonitorService.isCheck = false;
        MonitorService.isRunning = false;
        // 通过轨迹服务客户端client停止轨迹服务
        client.stopTrace(trackApp.getTrace(), stopTraceListener);
        if (null != serviceIntent) {
            trackApp.stopService(serviceIntent);
        }
    }

    private void startTrace() {
        client.startTrace(trace, startTraceListener);

        if (!MonitorService.isRunning) {
            // 开启监听service
            MonitorService.isCheck = true;
            MonitorService.isRunning = true;

        }
    }


    //开启分线程
    protected class RefreshThread extends Thread {

        protected boolean refresh = true;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();
            while (refresh) {
                // 轨迹服务开启成功后，调用queryEntityList()查询最新轨迹；
                // 未开启轨迹服务时，调用queryRealtimeLoc()进行实时定位。
                if (isTraceStarted) {
                    queryEntityList();
                } else {
                    queryRealtimeLoc();
                }

            }
            Looper.loop();
        }
    }

    private void queryRealtimeLoc() {
        trackApp.getClient().queryRealtimeLoc(trackApp.getServiceId(), entityListener);
    }

    /**
     * 查询entityList
     */
    private void queryEntityList() {
        // entity标识列表（多个entityName，以英文逗号"," 分割）
        String entityNames = trackApp.getEntityName();
        // 属性名称（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
        int activeTime = (int) (System.currentTimeMillis() / 1000 - packInterval);
        // 分页大小
        int pageSize = 10;
        // 分页索引
        int pageIndex = 1;
        trackApp.getClient().queryEntityList(trackApp.getServiceId(), entityNames, columnKey, returnType, activeTime,
                pageSize,
                pageIndex, entityListener);


    }
}
