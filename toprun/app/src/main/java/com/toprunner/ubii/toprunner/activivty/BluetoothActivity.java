package com.toprunner.ubii.toprunner.activivty;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.base.BaseActivity;
import com.toprunner.ubii.toprunner.fragment.BluetoothChatFragment;




public class BluetoothActivity extends BaseActivity{


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
    }
}
