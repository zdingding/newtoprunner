package com.toprunner.ubii.toprunner.activivty.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.view.CircleImageView;

/**
 * Created by ${赵鼎} on 2016/10/27 0027.
 * 头部
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder{
    public final TextView pathlenght;//路长
    public final CircleImageView headimage;//头像
    public final RelativeLayout rl_bg;//背景

    public HeaderViewHolder(View itemView) {
        super(itemView);
        pathlenght = (TextView) itemView.findViewById(R.id.pathlenght);
        headimage = (CircleImageView) itemView.findViewById(R.id.headimage);
        rl_bg = (RelativeLayout) itemView.findViewById(R.id.rl_bg);
    }
}
