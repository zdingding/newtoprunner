package com.toprunner.ubii.toprunner.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.Trace;
import com.toprunner.ubii.toprunner.utils.UIUtils;

import java.lang.ref.WeakReference;

/**
 * Created by ${赵鼎} on 2016/9/19 0019.
 */
public class ToprunnerApplication extends Application {
    private static TrackHandler handler;
    private static Context context;
    private static int mainThreadId;
    private static Thread mainThread;

    private Context mContext = null;
    /**
     * 轨迹服务
     */
    private Trace trace = null;
    /**
     * 轨迹服务客户端
     */
    private LBSTraceClient client =null;
    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    private int serviceId = 126470;
    /**
     * entity标识
     */
    private String entityName = "myTrace";
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SDKInitializer.initialize(mContext);
        // 初始化轨迹服务
        client = new LBSTraceClient(mContext);
        trace = new Trace(mContext, serviceId, entityName, traceType);
        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);
        //Handler对象
        handler = new TrackHandler(this);
        //Context
        context = getApplicationContext();
        //主线程id,获取当前方法运行线程id,此方法运行在主线程中,所以获取的是主线程id
        mainThreadId = android.os.Process.myTid();
        //主线程对象
        mainThread = Thread.currentThread();
    }
    static class TrackHandler extends Handler {
        WeakReference<ToprunnerApplication> trackApp;

        TrackHandler(ToprunnerApplication trackApplication) {
            trackApp = new WeakReference<ToprunnerApplication>(trackApplication);
        }

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(trackApp.get().mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    }
    public Trace getTrace() {
        return trace;
    }
    public LBSTraceClient getClient() {
        return client;
    }
    public int getServiceId() {
        return serviceId;
    }
    public String getEntityName() {
        return entityName;
    }
    public static Handler getHandler() {
        return handler;
    }
    public static Context getContext() {
        return context;
    }
    public static int getMainThreadId() {
        return mainThreadId;
    }
    public static Thread getMainThread() {
        return mainThread;
    }

}
