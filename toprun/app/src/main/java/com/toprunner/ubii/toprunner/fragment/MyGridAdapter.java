package com.toprunner.ubii.toprunner.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseViewHolder;
import com.toprunner.ubii.toprunner.utils.CacheUtils;
import com.toprunner.ubii.toprunner.utils.UIUtils;

/**
 * Created by ${赵鼎} on 2016/10/7 0007.
 */
public class MyGridAdapter extends BaseAdapter {
    private Context mContext;

    public String[] img_text = {"全程距离：公里", "配速：分钟/公里", "全程耗时", "消耗大卡", "步频：分钟", "总步数"};
    public String[] img_text2 = {"5公里", "07:01", CacheUtils.getString(UIUtils.getContext(),"time"), "374", "172", "81"};
    public int[] imgs = {R.mipmap.ren, R.mipmap.peisu,
            R.mipmap.daohang, R.mipmap.reliang,
            R.mipmap.bushu, R.mipmap.bupin
    };

    public MyGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }
        TextView tv1 = BaseViewHolder.get(convertView, R.id.tv_item1);
        TextView tv2 = BaseViewHolder.get(convertView, R.id.tv_item2);
        ImageView iv3 = BaseViewHolder.get(convertView, R.id.iv_item3);
        tv1.setText(img_text[position]);
        tv2.setText(img_text2[position]);
        iv3.setBackgroundResource(imgs[position]);
        return convertView;
    }
}
