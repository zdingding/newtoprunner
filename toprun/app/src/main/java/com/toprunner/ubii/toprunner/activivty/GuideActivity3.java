package com.toprunner.ubii.toprunner.activivty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.utils.CacheUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class GuideActivity3 extends Activity implements OnClickListener {
	private Button btn_guide3_next;
	private List<Map<String, Object>> items;
	private Context context;
	private Button btn_guide;
	private ViewPager viewpager;
	int ids[] = { R.mipmap.diagram_one, R.mipmap.diagram_two, R.mipmap.diagram_three};
	private ArrayList<ImageView> imageViews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity3);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		btn_guide3_next = (Button) findViewById(R.id.btn_guide3_next);
		btn_guide3_next.setVisibility(View.GONE);
		imageViews = new ArrayList<>();
		for (int i = 0; i < ids.length; i++){
			ImageView imageView = new ImageView(GuideActivity3.this);
			imageView.setBackgroundResource(ids[i]);
			imageViews.add(imageView);
		}
		viewpager.setAdapter(new MyPagerAdapter());
		btn_guide3_next.setOnClickListener(this);
		viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if(position==imageViews.size()-1){
					btn_guide3_next.setVisibility(View.VISIBLE);

				}else{
					btn_guide3_next.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
	public void pre(View v) {
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
		finish();
	}
	@Override
	public void onClick(View v) {
		CacheUtils.putBoolean(GuideActivity3.this,
				SplashActivity.GET_START_DATA, true);
		startActivity(new Intent(GuideActivity3.this, MainActivity.class));
		btn_guide3_next.setBackgroundResource(R.drawable.shape_search_style_bg);
		finish();
	}
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = imageViews.get(position);
			container.addView(imageView);
			return imageView;
		}
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}
	}
}
