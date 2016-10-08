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
    private static Handler handler;
    private static Context context;
    private static int mainThreadId;
    private static Thread mainThread;
    private LBSTraceClient client;
    private Context mContext = null;
    /**
     * 轨迹服务
     */
    private Trace trace = null;
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
        SDKInitializer.initialize(getApplicationContext());
        client = new LBSTraceClient(UIUtils.getContext());
        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);
        mContext = getApplicationContext();
        // 初始化轨迹服务
        entityName = getImei(mContext);
        trace = new Trace(mContext, serviceId, entityName, traceType);
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
        WeakReference<ToprunnerApplication> toprunnerApp;

        TrackHandler(ToprunnerApplication trackApplication) {
            toprunnerApp = new WeakReference<ToprunnerApplication>(trackApplication);
        }

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(toprunnerApp.get().mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    }
    public Trace getTrace() {
        return trace;
    }
    public LBSTraceClient getClient() {
        return client;
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
    /**
     * 获取设备IMEI码
     *
     * @param context
     * @return
     */
    protected static String getImei(Context context) {
        String mImei = "NULL";
        try {
            mImei = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            System.out.println("获取IMEI码失败");
            mImei = "NULL";
        }
        return mImei;
    }
}
