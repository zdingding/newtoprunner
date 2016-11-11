package com.toprunner.ubii.toprunner.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.toprunner.ubii.toprunner.R;

/**
 * 
* @ClassName: CommonUtils 
* @Description: 
* @author yiw
* @date 2015-12-28 下午4:16:01 
*
 */
public class CommonUtils {

	public static void showSoftInput(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		//imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	
	public static void hideSoftInput(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}
	
	public static boolean isShowSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		//获取状态信息
		return imm.isActive();//true 打开
	}
	public static void toastShow(Activity activity, String message) {
		StringBuilder strBuilder = new StringBuilder("<font face='" + activity.getString(R.string.font_type) + "'>");
		strBuilder.append(message).append("</font>");
		View toastRoot = activity.getLayoutInflater().inflate(R.layout.self_toast, null);
		Toast toast = new Toast(activity);
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.text_info);
		tv.setText(Html.fromHtml(strBuilder.toString()));
		toast.setGravity(Gravity.BOTTOM, 0, activity.getResources().getDisplayMetrics().heightPixels / 5);
		toast.show();

	}
}
