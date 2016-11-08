package com.toprunner.ubii.toprunner.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.Trace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by ${赵鼎} on 2016/9/19 0019.
 */
public class ToprunnerApplication extends Application {
    private static TrackHandler handler;
    private static int mainThreadId;
    private static Thread mainThread;
    private static  Context mContext = null;
    /**
     * 轨迹服务
     */
    private static Trace trace = null;
    /**
     * 轨迹服务客户端
     */
    private static LBSTraceClient client = null;
    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    private int serviceId =126470;
    /**
     * entity标识
     */
    private String entityName = "myTrace";//使用getImei代替
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        ShareSDK.initSDK(mContext,"18880eaf53a64");
        SDKInitializer.initialize(mContext);
        MapView.setMapCustomEnable(true);
        setMapCustomFile(mContext);//设置地图颜色 在文件中修改
        // 初始化轨迹服务
        client = new LBSTraceClient(mContext);
        if(null!=getImei(mContext)){
            entityName=getImei(mContext);
        }else{
            Toast.makeText(mContext,"无法获取Imei码",Toast.LENGTH_SHORT).show();
        }
        // 初始化轨迹服务
        trace = new Trace(mContext, serviceId, entityName, traceType);
        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);
        //Handler对象
        handler = new TrackHandler(this);
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
    public  static Trace getTrace() {
        return trace;
    }
    public static  LBSTraceClient getClient() {
        return client;
    }
    public static Handler getHandler() {
        return handler;
    }
    public static Context getContext() {
        return mContext;
    }
    public static int getMainThreadId() {
        return mainThreadId;
    }
    public static Thread getMainThread() {
        return mainThread;
    }
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





    //设置个性化地图
    private void setMapCustomFile(Context context) {
        // TODO Auto-generated method stub
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/custom_config.txt");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + "custom_config.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/custom_config.txt");
    }
}
