package com.toprunner.ubii.toprunner.activivty;

import com.toprunner.ubii.toprunner.mvp.contract.CircleContract;

/**
 * Created by ${赵鼎} on 2016/10/27 0027.
 * 请求服务器和通知view更新的数据接口
 */

public class CirclePresenter implements CircleContract.Presenter{
    private CircleContract.View view;
    public CirclePresenter(CircleContract.View view) {
        this.view = view;
    }

    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle(){
        this.view = null;
    }

    @Override
    public void loadData(int loadType) {

    }
}
