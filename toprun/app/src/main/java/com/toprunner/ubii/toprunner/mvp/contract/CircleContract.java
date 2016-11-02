package com.toprunner.ubii.toprunner.mvp.contract;

import com.toprunner.ubii.toprunner.bean.CommentConfig;

/**
 * Created by ${赵鼎} on 2016/10/27 0027.
 */

public interface CircleContract {
    interface View{
            //刷新数据
        //
        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);//键盘的隐藏显示
    }
    interface Presenter{
        void loadData(int loadType);//加载数据


        //删除
        //评论
        //点赞
    }
}
