package com.toprunner.ubii.toprunner.activivty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.toprunner.ubii.toprunner.R;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.tv_running:
//                intent = new Intent(SettingActivity.this,RunningActivity.class);
//                startActivity(intent);
//            break;
//            case R.id.run_quan:
//                intent = new Intent(SettingActivity.this,QuanActivity.class);
//                startActivity(intent);
//            break;

        }

    }
}
