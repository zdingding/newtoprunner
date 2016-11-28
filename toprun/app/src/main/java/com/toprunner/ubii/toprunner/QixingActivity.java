package com.toprunner.ubii.toprunner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class QixingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qixing);
    }
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

    }
    public void onBackClick(View view) {
        this.finish();
    }
}
