package com.toprunner.ubii.toprunner.activivty;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseFragment;
import com.toprunner.ubii.toprunner.factory.SporrtdetailsFragmentFactory;
import com.toprunner.ubii.toprunner.utils.ScreenUtils;
import com.toprunner.ubii.toprunner.utils.UIUtils;

public class SportdetailsActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private ImageView mTabline;//指示线
    private int mScreen1_4;//屏幕的1/4
    private int mCurrentPageIndex;
    private RadioGroup rg_content_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportdetails);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        rg_content_fragment = (RadioGroup) findViewById(R.id.rg_content_fragment);
        initTabLine();
        initView();
    }

    private void initView() {

        rg_content_fragment.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline.getLayoutParams();
                if (mCurrentPageIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * mScreen1_4 + mCurrentPageIndex * mScreen1_4);
                } else if (mCurrentPageIndex == 1 && position == 0)// 1->0
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1) * mScreen1_4);
                } else if (mCurrentPageIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + positionOffset * mScreen1_4);
                } else if (mCurrentPageIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1) * mScreen1_4);
                } else if (mCurrentPageIndex == 2 && position == 2) // 2->3
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset) * mScreen1_4);
                } else if (mCurrentPageIndex == 3 && position == 3) {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 - (positionOffset) * mScreen1_4);
                }
                mTabline.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.sportstrack:

                    viewpager.setCurrentItem(0);

                    break;
                case R.id.detailsfragment://
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.roadfragment://
                    viewpager.setCurrentItem(2);
                    break;
                case R.id.chartfragment://
                    viewpager.setCurrentItem(3);

                    break;

                default:
                    break;

            }
        }
    }

    class MyPagerAdapter
            extends FragmentPagerAdapter

    {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            return SporrtdetailsFragmentFactory.getDatas().get(position);
        }

        @Override
        public int getCount() {
            return SporrtdetailsFragmentFactory.getDatas().size();
        }
    }

    private void initTabLine() {
        /*
        设置宽度为1/4
         */
        mTabline = (ImageView) findViewById(R.id.id_iv_tabline);
        mScreen1_4 = ScreenUtils.getScreenWidth(UIUtils.getContext()) / 4;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScreen1_4;
        mTabline.setLayoutParams(lp);
    }
}
