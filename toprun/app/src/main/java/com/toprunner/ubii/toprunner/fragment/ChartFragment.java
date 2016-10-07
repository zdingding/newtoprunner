package com.toprunner.ubii.toprunner.fragment;

import android.os.Bundle;
import android.view.View;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.utils.UIUtils;
import com.toprunner.ubii.toprunner.view.MyGridView;

/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class ChartFragment extends BaseFragment {
    private MyGridView gridview;

    @Override
    public void setListener() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        gridview = (MyGridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MyGridAdapter(UIUtils.getContext()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.chart;
    }
}
