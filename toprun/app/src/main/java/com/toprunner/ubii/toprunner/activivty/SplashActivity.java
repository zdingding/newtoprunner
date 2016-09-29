package com.toprunner.ubii.toprunner.activivty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.toprunner.ubii.toprunner.R;

public class SplashActivity extends AppCompatActivity {
    private final String tag = SplashActivity.class.getSimpleName();
    private final int TEST_CODE_1 = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) {
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        intoMainPage();
    }

    private void intoMainPage() {



    }
}
