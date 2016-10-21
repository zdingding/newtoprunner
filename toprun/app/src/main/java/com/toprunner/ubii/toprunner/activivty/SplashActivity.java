package com.toprunner.ubii.toprunner.activivty;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.utils.CacheUtils;


public class SplashActivity extends Activity {

	private Context context;
	private boolean connected;

	private RelativeLayout rl_splash_root;

	public static final String GET_START_DATA = "get_start_data";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_splash);
		context = this;
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.9f, 1);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);

		rl_splash_root.startAnimation(alphaAnimation);

		alphaAnimation.setAnimationListener(new MyAnimationListener());
	}
	class MyAnimationListener implements AnimationListener {

		// 当动画播放完成的时候回调
		@Override
		public void onAnimationEnd(Animation animation) {
			connected = isConnected();
			if(!connected) {
				Toast.makeText(SplashActivity.this, "没有网络连接,请检查您的网络!", Toast.LENGTH_SHORT).show();
			}
			// 是否进入过主页面判断
			boolean result = CacheUtils.getBoolean(SplashActivity.this,
					GET_START_DATA);
			if (result) {
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
				
			} else {
				Intent intent = new Intent(SplashActivity.this,
						Guide1Activity.class);
				startActivity(intent);

			}
			// 关闭Splash页面
			finish();

		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
	/**
	 * 网络
	 * @return
	 */
	private boolean isConnected() {
		connected = false;
		
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if(networkInfo!=null) {
			connected = networkInfo.isConnected();
		}
		
		return connected;
	}
}
