package com.toprunner.ubii.toprunner.activivty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseActivity;

public class SrartrunActivity extends BaseActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srartrun);
      Button go  = (Button) findViewById(R.id.go);
        go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(SrartrunActivity.this,RunningActivity.class);
//        startActivity(intent);
//        finish();
    }
}
