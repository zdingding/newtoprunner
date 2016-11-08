package com.toprunner.ubii.toprunner.activivty;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.TraceLocation;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.utils.UIUtils;
import com.toprunner.ubii.toprunner.view.MiSportButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RunningActivity extends AppCompatActivity {
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaiduMap = null;
    private MiSportButton	mBtn;
    /**
     * 开启轨迹服务监听器
     */
    private static OnStartTraceListener startTraceListener = null;
    /**
     * 停止轨迹服务监听器
     */
    private static OnStopTraceListener stopTraceListener = null;
    /**
     * 图标
     */
    private static BitmapDescriptor realtimeBitmap;
    /**
     * Entity监听器
     */
    private static OnEntityListener entityListener = null;
    protected  MapStatusUpdate msUpdate = null;
    // 覆盖物
    protected static OverlayOptions overlayOptions;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.fragment_run);
        mMapView = (MapView) findViewById(R.id.baidu_map);
        mBaiduMap = mMapView.getMap();
        mBtn = (MiSportButton) findViewById(R.id.mi_btn);
        // 初始化监听器
        initListener();
        mBtn.setMiSportBtnClickListener(new MiSportButton.miSportButtonClickListener() {
            @Override
            public void finishClick() {
                //点击了关闭

            }

            @Override
            public void continueClick() {
                //点击了继续

            }

            @Override
            public void startClick() {
                //点击开始
                startTrace();
            }
        });
        mBtn.setMiSportBtnLongClickListener(new MiSportButton.miSportButtonLongClickListener() {
            @Override
            public void longPressClick() {
                        //长时间点击

            }
        });

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

            @Override
            public void onRequestFailedCallback(String s) {
                Toast.makeText(RunningActivity.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQueryEntityListCallback(String message) {
                super.onQueryEntityListCallback(message);
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
                    e.printStackTrace();
                    return;
                }
                showRealtimeTrack(entityLocation);
            }

            @Override
            public void onReceiveLocation(TraceLocation traceLocation) {
                super.onReceiveLocation(traceLocation);
                showRealtimeTrack(traceLocation);
            }
        };
    }

    private void showRealtimeTrack(TraceLocation location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
                        return;
        } else {
            LatLng latLng = new LatLng(latitude, longitude);
            drawRealtimePoint(latLng);
        }


    }

    private void drawRealtimePoint(LatLng latLng) {

        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(19).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        if (null == realtimeBitmap) {
            realtimeBitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.d);
        }
        overlayOptions = new MarkerOptions().position(latLng)
                .icon(realtimeBitmap).zIndex(9).draggable(true);
        // 实时点覆盖物
        if (null != overlayOptions) {
            mBaiduMap.addOverlay(overlayOptions);

        }
    }

    private void initOnStopTraceListener() {
        stopTraceListener = new OnStopTraceListener() {

            @Override
            public void onStopTraceSuccess() {
                UIUtils.getClient().onDestroy();
            }

            @Override
            public void onStopTraceFailed(int i, String s) {

            }
        };
    }

    private void initOnStartTraceListener() {
        startTraceListener = new OnStartTraceListener() {

            @Override
            public void onTraceCallback(int i, String s) {
                Toast.makeText(RunningActivity.this,s,Toast.LENGTH_SHORT).show();
            }
            // 轨迹服务推送接口
            @Override
            public void onTracePushCallback(byte b, String s) {
            if(0x03 == b || 0x04 == b){
                try {
                    JSONObject dataJson = new JSONObject(s);
                    if (null != dataJson) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }
        };
    }
    //开始记录轨迹
    private void startTrace() {
        Toast.makeText(RunningActivity.this, "正在开启轨迹", Toast.LENGTH_SHORT).show();
// 通过轨迹服务客户端client开启轨迹服务
        UIUtils.getClient().startTrace(UIUtils.getTrace(), startTraceListener);
    }
}