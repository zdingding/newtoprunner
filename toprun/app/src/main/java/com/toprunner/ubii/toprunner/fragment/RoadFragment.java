package com.toprunner.ubii.toprunner.fragment;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.view.PercentageBar;

import java.util.ArrayList;


/**
 * Created by ${赵鼎} on 2016/9/27 0027.
 */

public class RoadFragment extends BaseFragment implements View.OnClickListener {

    private PercentageBar mBarGraph;
    private ArrayList<Float> respectTarget;
    private ArrayList<String> respName;

    @Override
    public void setListener() {

    }
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        respectTarget = new ArrayList<Float>();
        respName = new ArrayList<String>();
        respectTarget.add(35.0f);
        respectTarget.add(20.0f);
        respectTarget.add(18.0f);
        respectTarget.add(15.0f);
        respectTarget.add(10.0f);
        respectTarget.add(8.0f);
        respectTarget.add(5.0f);
        respName.add("1");
        respName.add("2");
        respName.add("3");
        respName.add("4");
        respName.add("5");
        respName.add("6");
        respName.add("7");
        mBarGraph = (PercentageBar) findViewById(R.id.bargraph);
        mBarGraph.setRespectTargetNum(respectTarget);
        mBarGraph.setRespectName(respName);
        mBarGraph.setTotalBarNum(7);
        mBarGraph.setMax(40);
        mBarGraph.setBarWidth(50);
        mBarGraph.setVerticalLineNum(4);
        mBarGraph.setUnit("");

    }

    @Override
    protected void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.road;
    }


    @Override
    public void onClick(View v) {

    }
}
