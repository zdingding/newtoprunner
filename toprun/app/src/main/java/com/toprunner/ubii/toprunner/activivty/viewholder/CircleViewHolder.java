package com.toprunner.ubii.toprunner.activivty.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.mvp.VideoLoadMvpView;
import com.toprunner.ubii.toprunner.view.CircleImageView;


/**
 * Created by yiw on 2016/8/16.
 */
public  class CircleViewHolder extends RecyclerView.ViewHolder implements VideoLoadMvpView {
    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;
    public int viewType;
    public CircleImageView headimage;
    public TextView tv_name;
    public TextView tv_time;
    public TextView content;
    public ImageView snsBtn;


    public CircleViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public CircleViewHolder(View view) {
        super(view);
        headimage = (CircleImageView) itemView.findViewById(R.id.headimage);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);//名字
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);//时间
        content = (TextView) itemView.findViewById(R.id.content);//评论内容
        content = (TextView) itemView.findViewById(R.id.content);//评论内容
        snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);//评论列表
    }
}
