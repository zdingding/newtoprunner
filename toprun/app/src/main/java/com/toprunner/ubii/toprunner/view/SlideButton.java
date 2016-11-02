package com.toprunner.ubii.toprunner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.toprunner.ubii.toprunner.R;


/**
 * 
 * @author
 * 
 */
public class SlideButton extends View implements OnClickListener {

	private String TAG = "SlideButton";
	private Bitmap backgroundBitmap;
	private Paint paint;
	private Bitmap slideButton;
	private float slideButton_left;
	private boolean slide_status = false;
	private float lastX;
	private float firstX;
	private boolean isDrag;
	private Bitmap slideButtonOn;
	private Bitmap slideButtonOff;
	private boolean isOpen = false;
	private int on_switch_bg = R.mipmap.lanse;
	private int off_switch_bg = R.mipmap.huise;
	private int offSet = 4;

	/**
	 * 两个参数的构造方法，在布局文件中使用时调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlideButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initEvent();

	}

	private void initView() {
		setFocusable(true);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});
		/**
		 * 初始化背景图片
		 */
		backgroundBitmap = BitmapFactory.decodeResource(getResources(),
				off_switch_bg);

		slideButton = BitmapFactory.decodeResource(getResources(),
				R.mipmap.yuanxing);

		// 设置背景图片
		setBackgroundResource(off_switch_bg);
		/**
		 * 初始化画笔
		 */
		paint = new Paint();
		paint.setAntiAlias(true); // 打开抗矩齿
	}

	private void changeBackground() {
		if (slide_status) {
			offSet=-4;
			setBackgroundResource(on_switch_bg);
		} else {
			offSet=4;
			setBackgroundResource(off_switch_bg);
		}
		slideButton_left=slideButton_left+offSet;
		if(onSlideButtonClickListener!=null){
			onSlideButtonClickListener.click(slide_status);
		}
	}

	private void initEvent() {
		/**
		 * 注册点击事件
		 */
		setOnClickListener(this);
	}

	/**
	 * 一个参数的构造函数，在用new关键字实例化得时候调用
	 * 
	 * @param context
	 */
	public SlideButton(Context context) {
		super(context);
	}

	/**
	 * 测量view的大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight());
	}

	/**
	 * 确定view的位置 ，view自身有一些建议权，决定权在 父view手中。 自定义view的时候调用，作用不大
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	/**
	 * 绘制 view 的内容
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		canvas.drawBitmap(slideButton, slideButton_left+offSet,(backgroundBitmap.getHeight()-slideButton.getHeight())/2, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// 手指按下
			firstX = lastX = event.getX();
			isDrag = false;
			break;
		case MotionEvent.ACTION_MOVE:// 手指移动
			lastX = event.getX();
			float dis = lastX - firstX;// 移动的距离
			if (Math.abs(dis) > 5) {
				isDrag = true;
			}
			slideButton_left = firstX + dis;// 重新计算button的左边距
			firstX = lastX;

			break;
		case MotionEvent.ACTION_UP:// 手指抬起
			// 在发生拖动的情况下，根据最后的位置，判断当前开关的状态
			if (isDrag) {

				int maxLeft = backgroundBitmap.getWidth()
						- slideButton.getWidth(); // slideBtn
													// 左边届最大值
				/*
				 * 根据 slideBtn_left 判断，当前应是什么状态
				 */
				if (slideButton_left > maxLeft / 2) { // 此时应为 打开的状态
					slideButton_left = maxLeft;

				} else {
					slideButton_left = 0;
				}
			}
			flushStatus();
			break;

		default:
			break;
		}
		// 刷新view的位置
		flushView();
		return super.onTouchEvent(event);
	}

	/**
	 * 刷新view的位置
	 */
	private void flushView() {

		/**
		 * 让控件在合理的位置移动
		 */
		float maxDis = backgroundBitmap.getWidth() - slideButton.getWidth()-8;// 最大的移动距离
		slideButton_left = slideButton_left >= 0 ? slideButton_left : 8;
		slideButton_left = slideButton_left >= maxDis ? maxDis
				: slideButton_left;
		invalidate();
	}

	@Override
	public void onClick(View v) {
		/**
		 * 如果开关是关的状态，点击后就要变成开的状态
		 */
		if (!isDrag) {
			if (!slide_status) {
				slideButton_left = backgroundBitmap.getWidth()
						- slideButton.getWidth();
				slide_status = true;
			} else {
				slideButton_left = 0;
				slide_status = false;
			}
			/**
			 * 刷新view的位置，重新调用onDraw()方法
			 */
			changeBackground();
			invalidate();
			
		}

	}

	/**
	 * 刷新view的状态
	 */
	private void flushStatus() {

		if (slideButton_left == 0) {
			slide_status = false;
		}
		if (slideButton_left == (backgroundBitmap.getWidth() - slideButton
				.getWidth())) {
			slide_status = true;
		}
		changeBackground();
		flushView();
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		if (isOpen) {
			slideButton_left = (backgroundBitmap.getWidth() - slideButton
					.getWidth());
			slide_status = true;
		} else {
			slideButton_left = 0;
			slide_status = false;
		}
		changeBackground();
		invalidate();
		this.isOpen = isOpen;
	}
	

	public interface OnSlideButtonClickListener{
		public void click(boolean slide_status);
	}
	private OnSlideButtonClickListener onSlideButtonClickListener;

	public OnSlideButtonClickListener getOnSlideButtonClickListener() {
		return onSlideButtonClickListener;
	}
	public void setOnSlideButtonClickListener(
			OnSlideButtonClickListener onSlideButtonClickListener) {
		this.onSlideButtonClickListener = onSlideButtonClickListener;
	}
	

}
