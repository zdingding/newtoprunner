package com.toprunner.ubii.toprunner.activivty.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.toprunner.ubii.toprunner.mvp.VideoLoadMvpView;


/**
 * Created by yiw on 2016/8/16.
 */
public abstract class CircleViewHolder extends RecyclerView.ViewHolder implements VideoLoadMvpView {
    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;
    public int viewType;


    public CircleViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

}
