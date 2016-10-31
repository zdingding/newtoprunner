package com.toprunner.ubii.toprunner.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.toprunner.ubii.toprunner.R;

/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    private ViewFlipper mContentView;
    protected RelativeLayout layout_head;
    protected Button btn_left;
    protected Button btn_right;
    protected TextView tv_title;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            exit = false;//使2s之前点击back失效, 需要再点击两次才退出
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.setContentView(R.layout.layout_base);
        mContext = this;
        //初始化公共头部
        mContentView = (ViewFlipper) super.findViewById(R.id.layout_container);
        layout_head = (RelativeLayout) super.findViewById(R.id.layout_head);
        btn_left = (Button) super.findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_right = (Button) super.findViewById(R.id.btn_right);
        tv_title = (TextView) super.findViewById(R.id.tv_title);


    }
    private boolean exit = false;//代表是否退出
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK) {//点击的是back
            if(!exit) {
                Toast.makeText(BaseActivity.this,"再按一次退出应用!",Toast.LENGTH_SHORT).show();
                exit = true;
                handler.sendEmptyMessageDelayed(1, 2000);
                return true;//不退出
            }
        }
        return super.onKeyUp(keyCode, event);
    }

}
