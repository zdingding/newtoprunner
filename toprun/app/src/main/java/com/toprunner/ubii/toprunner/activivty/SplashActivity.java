package com.toprunner.ubii.toprunner.activivty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.common.Constants;
import com.toprunner.ubii.toprunner.common.PreferencesManager;
import com.toprunner.ubii.toprunner.utils.UIUtils;

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
        boolean isFirstRun = PreferencesManager.getInstance(UIUtils.getContext()).get(Constants.IS_FIRST_RUN, true);
        if(isFirstRun){
            Intent intent = new Intent(UIUtils.getContext(), GuideActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}

