package com.toprunner.ubii.toprunner.activivty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.running.Run;
import com.toprunner.ubii.toprunner.running.RunActivity;
import com.toprunner.ubii.toprunner.utils.FastBlur;


public class choose_run extends Activity implements View.OnClickListener {
    private ImageView image,v1,v4,s1,s4;
    public Button quweipao,shiwaipao,backbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_choose_run);

        image = (ImageView) findViewById(R.id.image_bg);
        quweipao = (Button)findViewById(R.id.leftquweipao);
        shiwaipao = (Button)findViewById(R.id.rightshiwaipao);
        backbutton = (Button) findViewById(R.id.button_back);
        v1 = (ImageView)findViewById(R.id.quwei1);
        v4 = (ImageView)findViewById(R.id.quwei4);
        s1 = (ImageView)findViewById(R.id.shiwai1);
        s4 = (ImageView)findViewById(R.id.shiwai4);
        //0~360度旋转
        final Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        //360~0度旋转
        final Animation operatingAnim2 = AnimationUtils.loadAnimation(this, R.anim.tip2);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim2.setInterpolator(lin);
        applyBlur();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final Handler handler = new Handler();
        quweipao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quweipao.setEnabled(false);
                System.out.println("点击左侧");
                //左侧按钮有两个控件旋转
                v1.startAnimation(operatingAnim);
                v4.startAnimation(operatingAnim2);
                Runnable rTimer = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(choose_run.this, Run.class);//起始进入user界面
                        startActivity(intent);
                        //关闭两个动画
                        v1.clearAnimation();
                        v4.clearAnimation();
                        quweipao.setEnabled(true);
                    }
                };
                handler.postDelayed(rTimer,2500);
                //   handler.removeCallbacks(rTimer);
            }

        });
        shiwaipao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiwaipao.setEnabled(false);
                System.out.println("点击右侧");
                //右侧按钮有两个控件旋转
                s1.startAnimation(operatingAnim);
                s4.startAnimation(operatingAnim2);
                final Handler handler = new Handler();
                Runnable rTimer = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(choose_run.this, RunActivity.class);//起始进入user界面
                        startActivity(intent);
                        //关闭两个动画
                        s1.clearAnimation();
                        s4.clearAnimation();
                        shiwaipao.setEnabled(true);
                    }
                };
                handler.postDelayed(rTimer,2500);
             /*   Intent intent = new Intent();
                intent.setClass(choose_run.this, run.class);        //起始进入user界面
                startActivity(intent);*/
            }
        });
        //模糊方法调用
    }


    /**
     * 模糊函数 ，调用blur方法
     */
    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                blur(UserActivity.bmap, image);
                return true;
            }
        });
    }


    /**
     * 这里是模糊处理方法blur
     * @param bkg
     * @param view
     */
    @SuppressLint("NewApi")
    private void blur(Bitmap bkg,View view) {
        //long startMs = System.currentTimeMillis();
        float radius = 2;
        float scaleFactor = 80;                                         //模糊度数值 越大越模糊

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()/scaleFactor), (int)(view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        if(bkg==null)
        {
            System.out.println("bitmap为空了");
        }
        else {
            canvas.drawBitmap(bkg, 0, 0, paint);
            overlay = FastBlur.doBlur(overlay, (int)radius, true);
            view.setBackground(new BitmapDrawable(getResources(), overlay));
        }


        //  statusText.setText("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }

    /**
     * 获取系统状态栏和软件标题栏，部分软件没有标题栏，看自己软件的配置；
     * @return
     */
    private int getOtherHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;
        return statusBarHeight + titleBarHeight;
    }

    /**
     * 调用返回键的方法
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override

    public void onClick(View v) {
    }
}




