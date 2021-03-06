package com.toprunner.ubii.toprunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.TraceLocation;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.broadcastreceiver.TrackReceiver;
import com.toprunner.ubii.toprunner.service.MonitorService;
import com.toprunner.ubii.toprunner.utils.CommonUtils;
import com.toprunner.ubii.toprunner.utils.DateUtils;
import com.toprunner.ubii.toprunner.utils.UIUtils;
import com.toprunner.ubii.toprunner.view.MiSportButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹追踪
 */
public class TrackUploadFragment extends Fragment {
    private MiSportButton mBtn;
    boolean ischeck  =false;
    private ToprunnerApplication trackApp = null;
    private Button btnOperator = null;

    protected TextView tvEntityName = null;


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

    /**
     * 采集周期（单位 : 秒）
     */
    private int gatherInterval = 5;

    /**
     * 打包周期（单位 : 秒）
     */
    private int packInterval = 15;

    /**
     * 图标
     */
    private static BitmapDescriptor realtimeBitmap;

    private static Overlay overlay = null;

    // 覆盖物
    protected static OverlayOptions overlayOptions;

    // 路线覆盖物
    private static PolylineOptions polyline = null;

    private static List<LatLng> pointList = new ArrayList<LatLng>();

    private Intent serviceIntent = null;

    /**
     * 刷新地图线程(获取实时点)
     */
    protected RefreshThread refreshThread = null;

    protected static MapStatusUpdate msUpdate = null;

    private View view = null;

    private LayoutInflater mInflater = null;

    public static boolean isInUploadFragment = true;

    private static boolean isRegister = false;

    protected static PowerManager pm = null;

    public static WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = new TrackReceiver();

    private TrackUploadHandler mHandler = null;

    private boolean isTraceStarted = false;
    private TextView speed;
    private Chronometer c_time;
    private long rangeTime =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trackupload, container, false);

        mInflater = inflater;

        System.out.println("TrackUploadFragment onCreateView");

        // 初始化
        init();

        // 初始化监听器
        initListener();

        // 设置采集周期
        setInterval();

        // 设置http请求协议类型
        setRequestType();

        mHandler = new TrackUploadHandler(this);

        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        mBtn = (MiSportButton) view.findViewById(R.id.mi_btn);
        c_time = (Chronometer) view.findViewById(R.id.c_time);
        speed = (TextView) view.findViewById(R.id.speed);
        btnOperator = (Button) view.findViewById(R.id.btn_operator);
        if(!ischeck){
            startTrace();
            ischeck =true;
            c_time.setBase(SystemClock.elapsedRealtime());
            c_time.start();
        }
        mBtn.setMiSportBtnLongClickListener(new MiSportButton.miSportButtonLongClickListener() {



            @Override
            public void longPressClick() {

                    c_time.stop();
 rangeTime =    SystemClock.elapsedRealtime() - c_time.getBase();

            }
        });
        mBtn.setMiSportBtnClickListener(new MiSportButton.miSportButtonClickListener() {
            @Override
            public void finishClick() {
                if(ischeck){
                    Toast.makeText(getActivity(),"正在停止", Toast.LENGTH_SHORT).show();
                    stopTrace();
                    ischeck =false;
                    c_time.stop();
                    c_time.setBase(SystemClock.elapsedRealtime());
                }

            }

            @Override
            public void continueClick() {
                c_time.setBase(SystemClock.elapsedRealtime()-rangeTime);
                c_time.start();
            }


        });



    }

    public void startMonitorService() {
        serviceIntent = new Intent(trackApp,
                MonitorService.class);
        trackApp.startService(serviceIntent);
    }

    /**
     * 初始化监听器
     */
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

    /**
     * 开启轨迹服务
     */
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        CommonUtils.toastShow(getActivity(), getString(R.string.starting_travel));
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




        UIUtils.getClient().startTrace(UIUtils.getTrace(), startTraceListener);

        if (!MonitorService.isRunning) {
            // 开启监听service
            MonitorService.isCheck = true;
            MonitorService.isRunning = true;
            startMonitorService();
        }
    }

    /**
     * 停止轨迹服务
     */
    private void stopTrace() {

        if (isRegister) {
            try {
                trackApp.unregisterReceiver(trackReceiver);
                isRegister = false;
            } catch (Exception e) {
                // TODO: handle
            }
        }
        // 停止监听service
        MonitorService.isCheck = false;
        MonitorService.isRunning = false;

        // 通过轨迹服务客户端client停止轨迹服务
        UIUtils.getClient().stopTrace(UIUtils.getTrace(), stopTraceListener);

        if (null != serviceIntent) {
            trackApp.stopService(serviceIntent);
        }
    }

    /**
     * 设置采集周期和打包周期
     */
    private void setInterval() {
        UIUtils.getClient().setInterval(gatherInterval, packInterval);
    }

    /**
     * 设置请求协议
     */
    protected void setRequestType() {
        int type = 0;
        UIUtils.getClient().setProtocolType(type);
    }

    /**
     * 查询实时轨迹
     */
    private void queryRealtimeLoc() {
        UIUtils.getClient().queryRealtimeLoc(trackApp.getServiceId(), entityListener);
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

        UIUtils.getClient().queryEntityList(trackApp.getServiceId(), entityNames, columnKey, returnType, activeTime,
                pageSize,
                pageIndex, entityListener);
    }

    /**
     * 初始化OnStartTraceListener
     */
    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {

            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            public void onTraceCallback(int arg0, String arg1) {
                // TODO Auto-generated method stub
                 if(arg0==0){

                     CommonUtils.toastShow(getActivity(), getString(R.string.starting_chenggong));

                 }else{

                     CommonUtils.toastShow(getActivity(), getString(R.string.starting_shibai));

                 }
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            public void onTracePushCallback(byte arg0, String arg1) {
                // TODO Auto-generated method stub
                if (0x03 == arg0 || 0x04 == arg0) {
                    try {
                        JSONObject dataJson = new JSONObject(arg1);
                        if (null != dataJson) {
                            String mPerson = dataJson.getString("monitored_person");
                            String action = dataJson.getInt("action") == 1 ? "进入" : "离开";
                            String date = DateUtils.getDate(dataJson.getInt("time"));
                            long fenceId = dataJson.getLong("fence_id");
                            mHandler.obtainMessage(-1,
                                    "监控对象[" + mPerson + "]于" + date + " [" + action + "][" + fenceId + "号]围栏")
                                    .sendToTarget();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        mHandler.obtainMessage(-1, "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]")
                                .sendToTarget();
                    }
                } else {
                    mHandler.obtainMessage(-1, "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]").sendToTarget();
                }
            }

        };
    }

    /**
     * 初始化OnStopTraceListener
     */
    private void initOnStopTraceListener() {
        // 初始化stopTraceListener
        stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                // TODO Auto-generated method stub
                mHandler.obtainMessage(1, "停止轨迹服务成功").sendToTarget();
                startRefreshThread(false);
                UIUtils.getClient().onDestroy();
            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                // TODO Auto-generated method stub
                mHandler.obtainMessage(-1, "停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]").sendToTarget();
                startRefreshThread(false);
            }
        };
    }

    /**
     * 初始化OnEntityListener
     */
    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                trackApp.getmHandler().obtainMessage(0, "entity请求失败回调接口消息 : " + arg0).sendToTarget();
            }

            // 添加entity回调接口
            public void onAddEntityCallback(String arg0) {
                // TODO Auto-generated method stub
                trackApp.getmHandler().obtainMessage(0, "添加entity回调接口消息 : " + arg0).sendToTarget();
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
                    trackApp.getmHandler().obtainMessage(0, "解析entityList回调消息失败").sendToTarget();
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

                try {
                    Thread.sleep(gatherInterval * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println("线程休眠失败");
                }
            }
            Looper.loop();
        }
    }

    /**
     * 显示实时轨迹
     *
     * @param location
     */
    protected void showRealtimeTrack(TraceLocation location) {

        if (null == refreshThread || !refreshThread.refresh) {
            return;
        }
        int  mySpeed   = (int) location.getSpeed();//速度
       speed.setText(mySpeed+"");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
           mHandler.obtainMessage(-1, "当前查询无轨迹点").sendToTarget();
        } else {
            LatLng latLng = new LatLng(latitude, longitude);
            if (1 == location.getCoordType()) {
                LatLng sourceLatLng = latLng;
                CoordinateConverter converter = new
                        CoordinateConverter();
                converter.from(CoordType.GPS);
                converter.coord(sourceLatLng);
                latLng = converter.convert();
            }
            pointList.add(latLng);

            if (isInUploadFragment) {
                // 绘制实时点
                drawRealtimePoint(latLng);
            }
        }
    }

    /**
     * 绘制实时点
     *
     * @param point
     */
    private void drawRealtimePoint(LatLng point) {

        if (null != overlay) {
            overlay.remove();
        }

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19).build();

        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        if (null == realtimeBitmap) {
            realtimeBitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_geo);
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

    /**
     * 添加地图覆盖物
     */
    public void addMarker() {

        if (null != msUpdate) {
            trackApp.getmBaiduMap().setMapStatus(msUpdate);
        }

        // 路线覆盖物
        if (null != polyline) {
            trackApp.getmBaiduMap().addOverlay(polyline);
        }

        // 实时点覆盖物
        if (null != overlayOptions) {
            overlay = trackApp.getmBaiduMap().addOverlay(overlayOptions);
        }

    }

    public void startRefreshThread(boolean isStart) {
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

    public static final TrackUploadFragment newInstance(ToprunnerApplication trackApp) {
        TrackUploadFragment fragment = new TrackUploadFragment();
        fragment.trackApp = trackApp;
        return fragment;
    }


    static class TrackUploadHandler extends Handler {
        WeakReference<TrackUploadFragment> trackUpload;

        TrackUploadHandler(TrackUploadFragment trackUploadFragment) {
            trackUpload = new WeakReference<TrackUploadFragment>(trackUploadFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            TrackUploadFragment tu = trackUpload.get();
            Toast.makeText(tu.trackApp, (String) msg.obj, Toast.LENGTH_LONG).show();

            switch (msg.what) {
                case 0:
                case 10006:
                case 10008:
                case 10009:
                    tu.isTraceStarted = true;
                    break;

                case 1:
                case 10004:
                    tu.isTraceStarted = false;
                    break;

                default:
                    break;
            }
        }
    }
}
