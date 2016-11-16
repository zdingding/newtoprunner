package com.toprunner.ubii.toprunner.activivty.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.CirclePresenter;
import com.toprunner.ubii.toprunner.activivty.viewholder.CircleViewHolder;
import com.toprunner.ubii.toprunner.activivty.viewholder.HeaderViewHolder;
import com.toprunner.ubii.toprunner.base.BaseRecycleViewAdapter;
import com.toprunner.ubii.toprunner.bean.CircleItem;
import com.toprunner.ubii.toprunner.bean.CommentConfig;
import com.toprunner.ubii.toprunner.utils.UIUtils;

/**
 * Created by ${赵鼎} on 2016/10/26 0026.
 */
public class CircleAdapter extends BaseRecycleViewAdapter  {
    private Context context;
    private CirclePresenter presenter;//更新数据
    public final static int TYPE_HEAD = 0;
    public static final int HEADVIEW_SIZE = 1;
    public CircleAdapter(Context context){
        this.context = context;
    }
    public void setCirclePresenter(CirclePresenter presenter){
        this.presenter = presenter;
    }
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEAD;
        }
        int itemType = 0;
        CircleItem item = (CircleItem) datas.get(position-1);
        if (CircleItem.TYPE_URL.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_URL;
        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if(CircleItem.TYPE_VIDEO.equals(item.getType())){
            itemType = CircleViewHolder.TYPE_VIDEO;
        }
        return itemType;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == TYPE_HEAD){
            View headView = UIUtils.inflate(R.layout.head_circle);
           viewHolder = new HeaderViewHolder(headView);
        }else{
            View view = UIUtils.inflate(R.layout.adapter_circle_item);
            viewHolder =new CircleViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(getItemViewType(position)==TYPE_HEAD){
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            holder.pathlenght.setText("0");
            Glide.with(context).load(R.mipmap.ren).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.headimage);
        }else {
            final int circlePosition = position - HEADVIEW_SIZE;
            CircleViewHolder holder = (CircleViewHolder) viewHolder;
            Glide.with(context).load(R.mipmap.jiantou).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.headimage);
            holder.tv_name.setText("很随意的名字");
            holder.tv_time.setText("很随意的时间");
            holder.content.setText("很随意的内容");
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentConfig config = new CommentConfig();
                    config.circlePosition = circlePosition;
                    config.commentType = CommentConfig.Type.PUBLIC;
                    presenter.showEditTextBody(config);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }


}
