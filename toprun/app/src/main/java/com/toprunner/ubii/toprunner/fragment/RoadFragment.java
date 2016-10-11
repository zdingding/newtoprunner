package com.toprunner.ubii.toprunner.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.application.ToprunnerApplication;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.service.MonitorService;
import com.toprunner.ubii.toprunner.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class RoadFragment extends BaseFragment implements View.OnClickListener {
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
    private LBSTraceClient client;
    private Trace trace;
    private ToprunnerApplication trackApp;


    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        // 初始化
        initView();

        // 初始化监听器
           initListener();

        // 设置采集周期
        // setInterval();

        // 设置http请求协议类型
        //setRequestType();
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
    }
    private void initOnStopTraceListener() {
        stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(UIUtils.getContext(), "停止", Toast.LENGTH_SHORT).show();
                trackApp.getClient().onDestroy();
            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        };
    }

    private void initOnStartTraceListener() {
            startTraceListener = new OnStartTraceListener() {
                @Override
                public void onTraceCallback(int i, String s) {
                    Toast.makeText(UIUtils.getContext(), i+s, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onTracePushCallback(byte b, String s) {

                }
            };
        }


    //初始化
    private void initView() {
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

}
