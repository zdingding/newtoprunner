package com.toprunner.ubii.toprunner.activivty;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.adpter.ViewPagerAdapter;
import com.toprunner.ubii.toprunner.common.Constants;
import com.toprunner.ubii.toprunner.common.PreferencesManager;
import com.toprunner.ubii.toprunner.utils.UIUtils;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager guidePages;
    private ArrayList<View> pageViews;
    private ImageView imageView;
    private ImageView[] imageViews;
    private ViewGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        PreferencesManager.getInstance(UIUtils.getContext()).put(Constants.IS_FIRST_RUN, false);
//欢迎页面

        pageViews = new ArrayList<View>();
        pageViews.add(UIUtils.inflate(R.layout.layout_guide_item1));
        pageViews.add(UIUtils.inflate(R.layout.layout_guide_item1));
        pageViews.add(UIUtils.inflate(R.layout.layout_guide_item1));
        pageViews.add(UIUtils.inflate(R.layout.layout_guide_item1));
        imageViews = new ImageView[pageViews.size()];
        guidePages = (ViewPager) findViewById(R.id.guidePages);
        group = (ViewGroup) findViewById(R.id.viewGroup);
        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new ImageView(UIUtils.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;

            // 默认选中第一张图片
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.mipmap.ic_launcher);
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.denglu);
            }
            group.addView(imageViews[i]);
        }

        guidePages.setAdapter(new ViewPagerAdapter(pageViews));
        guidePages.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[position].setBackgroundResource(R.mipmap.page_indicator_focused);
            if (position != i) {
                imageViews[i].setBackgroundResource(R.mipmap.page_indicator);
            }
        }

        //点击最后一页，跳转到主页
        if(position == 3){
            View itemView = pageViews.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
