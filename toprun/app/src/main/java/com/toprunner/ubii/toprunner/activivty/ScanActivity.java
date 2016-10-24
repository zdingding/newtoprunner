package com.toprunner.ubii.toprunner.activivty;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.toprunner.ubii.toprunner.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate, View.OnClickListener {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;
    private Button open_light;
    private Button close_light;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        open_light = (Button) findViewById(R.id.open_light);
        close_light = (Button) findViewById(R.id.close_light);
       mQRCodeView.setDelegate(this);
        open_light.setOnClickListener(this);
        close_light.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
    }
    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot();
    }


    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = "data/";

            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(ScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_light:
                mQRCodeView.openFlashlight();
            break;
            case R.id.close_light:
                mQRCodeView.closeFlashlight();
            break;
        }
    }
}
