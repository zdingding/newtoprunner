package com.toprunner.ubii.toprunner.activivty;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.toprunner.ubii.toprunner.R;
import com.toprunner.ubii.toprunner.activivty.adpter.CircleAdapter;
import com.toprunner.ubii.toprunner.bean.CommentConfig;
import com.toprunner.ubii.toprunner.mvp.contract.CircleContract;
import com.toprunner.ubii.toprunner.utils.CommonUtils;
import com.toprunner.ubii.toprunner.widgets.DivItemDecoration;

public class QuanActivity extends AppCompatActivity implements CircleContract.View {
    protected static final String TAG = MainActivity.class.getSimpleName();
    private SuperRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private LinearLayout edittextbody;//评论框
    private CommentConfig commentConfig;//评论配置
    private EditText editText;
    private ImageView sendIv;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private CircleAdapter circleAdapter;
    private CirclePresenter presenter;//数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_quan);
        presenter = new CirclePresenter(this);//设置数据
        initView();
        //实现自动下拉刷新功能
        recyclerView.getSwipeToRefresh().post(new Runnable(){
            @Override
            public void run() {
                recyclerView.setRefreshing(true);//执行下拉刷新的动画
                refreshListener.onRefresh();//执行数据加载操作
            }
        });
    }

    private void initView() {
        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        editText = (EditText) findViewById(R.id.circleEt);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       //更新数据
                    }
                }, 2000);
            }
        };

        recyclerView.setRefreshListener(refreshListener);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(QuanActivity.this).resumeRequests();
                }else{
                    Glide.with(QuanActivity.this).pauseRequests();
                }

            }
        });
        circleAdapter = new CircleAdapter(this);
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);
    }
//设置评论框的显示
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);
        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if(View.VISIBLE==visibility){
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput( editText.getContext(),  editText);

        }else if(View.GONE==visibility){
            //隐藏键盘
            CommonUtils.hideSoftInput( editText.getContext(),  editText);
        }
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if(commentConfig == null)
            return;
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
//只能返回当前可见区域（列表可滚动）的子项.

    }
}
