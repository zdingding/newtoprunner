package com.toprunner.ubii.toprunner.activivty;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.toprunner.ubii.toprunner.R;



public class GuideActivity2 extends Activity implements OnClickListener {
	/**
	 * 
	 */
	private ImageView ib_guide_worker;
	private ImageView ib_guide_else;
	private ImageView ib_guide_student;
	private ImageView ib_guide_older;
	private Button btn_guide2_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity2);
		ib_guide_worker = (ImageView) findViewById(R.id.ib_guide_worker);
		ib_guide_else = (ImageView) findViewById(R.id.ib_guide_else);
		ib_guide_student = (ImageView) findViewById(R.id.ib_guide_student);
		ib_guide_older = (ImageView) findViewById(R.id.ib_guide_older);
		btn_guide2_next = (Button) findViewById(R.id.btn_guide2_next);
		
		btn_guide2_next.setEnabled(false);
		
		ib_guide_worker.setOnClickListener(this);
		ib_guide_else.setOnClickListener(this);
		ib_guide_student.setOnClickListener(this);
		ib_guide_older.setOnClickListener(this);
		btn_guide2_next.setOnClickListener(this);
	}
	public void pre(View v) {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_guide_worker:
			btn_guide2_next.setEnabled(true);
			ib_guide_worker.setImageResource(R.mipmap.ug_people_worker_d);
			ib_guide_else.setImageResource(R.mipmap.ug_people_else);
			ib_guide_student.setImageResource(R.mipmap.ug_people_student);
			ib_guide_older.setImageResource(R.mipmap.ug_people_older);
			btn_guide2_next.setBackgroundResource(R.drawable.shape_search_style_bg);
			
			break;

		case R.id.ib_guide_else:
			btn_guide2_next.setEnabled(true);
			ib_guide_worker.setImageResource(R.mipmap.ug_people_worker);
			ib_guide_else.setImageResource(R.mipmap.ug_people_else_d);
			ib_guide_student.setImageResource(R.mipmap.ug_people_student);
			ib_guide_older.setImageResource(R.mipmap.ug_people_older);
			btn_guide2_next.setBackgroundResource(R.drawable.shape_search_style_bg);
			break;
		case R.id.ib_guide_student:
			btn_guide2_next.setEnabled(true);
			ib_guide_worker.setImageResource(R.mipmap.ug_people_worker);
			ib_guide_else.setImageResource(R.mipmap.ug_people_else);
			ib_guide_student.setImageResource(R.mipmap.ug_people_student_d);
			ib_guide_older.setImageResource(R.mipmap.ug_people_older);
			btn_guide2_next.setBackgroundResource(R.drawable.shape_search_style_bg);
			break;
		case R.id.ib_guide_older:
			btn_guide2_next.setEnabled(true);
			ib_guide_worker.setImageResource(R.mipmap.ug_people_worker);
			ib_guide_else.setImageResource(R.mipmap.ug_people_else);
			ib_guide_student.setImageResource(R.mipmap.ug_people_student);
			ib_guide_older.setImageResource(R.mipmap.ug_people_older_d);
			btn_guide2_next.setBackgroundResource(R.drawable.shape_search_style_bg);
			break;

		case R.id.btn_guide2_next:
			finish();
			Intent intent = new Intent(this, GuideActivity3.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			break;

		default:
			break;
		}
	}
}
