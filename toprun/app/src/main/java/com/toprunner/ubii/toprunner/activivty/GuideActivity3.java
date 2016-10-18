package com.toprunner.ubii.toprunner.activivty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.utils.CacheUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GuideActivity3 extends Activity implements OnClickListener {
	private Button btn_guide3_next;
	private GridView gv_guide;
	private List<Map<String, Object>> items;

	private Context context;

	private Button btn_guide;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity3);
		
		btn_guide3_next = (Button) findViewById(R.id.btn_guide3_next);
		// btn_guide3_next.setEnabled(false);
		btn_guide3_next.setOnClickListener(this);

		gv_guide = (GridView) findViewById(R.id.gv_guide);
		// 名称
				String[] tabs = { "跑步", "羽毛球", "骑行", "滑冰", "新闻", "户外跑步", "室内跑步", "走路",
						"健康", "运动圈", "赛事", "运动技巧",  "马拉松",  "教育","减肥燃脂"};

				// 准备要添加的数据条目
				items = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < tabs.length; i++) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("btnItem", tabs[i]);
					items.add(item);
				}

				// 实例化一个适配器
				SimpleAdapter adapter = new SimpleAdapter(this, items,
						R.layout.guide_btn, new String[] { "btnItem" },
						new int[] { R.id.btn_guide });
				gv_guide.setAdapter(adapter);
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
}
