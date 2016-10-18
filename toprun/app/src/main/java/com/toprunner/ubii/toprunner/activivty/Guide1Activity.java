package com.toprunner.ubii.toprunner.activivty;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;




public class Guide1Activity extends Activity implements OnClickListener{
	/**
	 * 
	 */
	private ImageView ib_guide_girl;
	private ImageView ib_guide_boy;
	private Button btn_guide1_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide1);
		ib_guide_girl = (ImageView) findViewById(R.id.ib_guide_girl);
		ib_guide_boy = (ImageView) findViewById(R.id.ib_guide_boy);
		btn_guide1_next = (Button) findViewById(R.id.btn_guide1_next);

		btn_guide1_next.setEnabled(false);
		
		ib_guide_girl.setOnClickListener(this);
		ib_guide_boy.setOnClickListener(this);
		btn_guide1_next.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ib_guide_girl:
			btn_guide1_next.setEnabled(true);
			ib_guide_girl.setImageResource(R.mipmap.ug_gender_girl_d);
			ib_guide_boy.setImageResource(R.mipmap.ug_gender_boy);
			btn_guide1_next.setBackgroundResource(R.drawable.shape_search_style_bg);
			
			break;

		case R.id.ib_guide_boy:
			btn_guide1_next.setEnabled(true);
			ib_guide_boy.setImageResource(R.mipmap.ug_gender_boy_d);
			ib_guide_girl.setImageResource(R.mipmap.ug_gender_girl);
			btn_guide1_next.setBackgroundResource(R.drawable.shape_search_style_bg);		
			break;

		case R.id.btn_guide1_next:
			finish();
			Intent intent = new Intent(Guide1Activity.this, GuideActivity2.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			
			break;

		default:
			break;
		}
	}
}
