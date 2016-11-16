package com.toprunner.ubii.toprunner.activivty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.toprunner.ubii.toprunner.R;



public class KaishiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaishi);
        RotateAnimation rotateAnimation = new RotateAnimation(720,0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2500);
        //startAnimation(rotateAnimation);
    }
}
