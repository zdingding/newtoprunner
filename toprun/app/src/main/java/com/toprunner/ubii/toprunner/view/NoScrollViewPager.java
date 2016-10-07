package com.toprunner.ubii.toprunner.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yys on 2016/9/30.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    /**
     * 屏幕了系统自带带特有的左右滑动事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }


}
