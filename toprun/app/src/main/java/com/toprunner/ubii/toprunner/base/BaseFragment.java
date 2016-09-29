package com.toprunner.ubii.toprunner.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 赵鼎
 * @version 创建时间：2016年9月27日 下午2:13:16 类说明
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    private View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    final public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 强制竖屏显示
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 此方法一定在此处要去调用，返回界面成功的展示效果
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initData(getArguments());
        initView(mRootView, savedInstanceState);
        setListener();
        return mRootView;

    }


    final protected <T extends View> T findViewById(int id) {
        if (mRootView == null) {
            return null;
        }

        return (T) mRootView.findViewById(id);
    }

    // 初始化数据
    private void initData(Bundle arguments) {
        // TODO Auto-generated method stub

    }

    // 监听
    public abstract void setListener();

    protected abstract void initView(View view, Bundle savedInstanceState);

    public abstract int getLayoutId();

}
