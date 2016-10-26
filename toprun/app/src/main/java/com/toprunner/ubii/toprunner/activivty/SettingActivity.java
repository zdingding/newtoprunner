package com.toprunner.ubii.toprunner.activivty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.toprunner.ubii.toprunner.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button tv_running;
    private Intent intent;
    private Button run_quan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_running = (Button) findViewById(R.id.tv_running);
        run_quan = (Button) findViewById(R.id.run_quan);
        tv_running.setOnClickListener(this);
        run_quan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_running:
                intent = new Intent(SettingActivity.this,RunningActivity.class);
                startActivity(intent);
            break;
            case R.id.run_quan:
                intent = new Intent(SettingActivity.this,QuanActivity.class);
                startActivity(intent);
            break;

        }

    }
}
