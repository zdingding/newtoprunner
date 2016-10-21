package com.toprunner.ubii.toprunner.activivty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;

public class MainActivity extends Activity  {

    /***
     * 数据库相关内容
     */
    SQLiteDatabase mysql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          requestWindowFeature(Window.FEATURE_NO_TITLE);//取消上方bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        Button denglu = (Button) findViewById(R.id.btn1);
        Button register = (Button) findViewById(R.id.btn2);

        mysql = this.openOrCreateDatabase("student.db", MODE_PRIVATE, null);
        try                                                                               //防止第一次注册时不可以注册
        {
            mysql.execSQL("create table login_t(_id INTEGER PRIMART KEY,username TEXT ,name1 TEXT ,password Text)");
        } catch (Exception e) {

        }
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DengluActivity.class);//起始进入user界面
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("用户注册");
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.register, null);
                final EditText username = (EditText) view.findViewById(R.id.passnametext);
                final EditText userpass = (EditText) view.findViewById(R.id.passwordtext);
                builder.setView(view);
                // Toast.makeText(TopRunner.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO Auto-generated method stub

                        if ((username.getText().toString().trim().length() == 0) || (userpass.getText().toString().trim().length() == 0)) {
                            Dialog dialog1 = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("信息框")
                                    .setMessage("用户名或密码不能为空")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    }).create();
                            dialog1.show();
                        } else {
                            try {

                                Cursor m_cus = mysql.rawQuery("select max(_id) from login_t", null);
                                m_cus.moveToFirst();

                                int temp_id = m_cus.getInt(0) + 1;

                                mysql.execSQL("INSERT INTO login_t(_id,username,password) values(" + temp_id + ",'" + username.getText().toString().trim() + "','" + userpass.getText().toString().trim() + "')");

                                mysql.close();
                                LayoutInflater fac2 = LayoutInflater.from(MainActivity.this);
                                View dialogview2 = fac2.inflate(R.layout.tishi, null);

                                TextView view_1 = (TextView) dialogview2.findViewById(R.id.textView1);
                                view_1.setText("注册成功！");
                                AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("提示框")
                                        .setView(dialogview2)
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        Intent intent = new Intent();
                                                        intent.setClass(MainActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        //     MainActivity.this.finish();
                                                    }
                                                }).create();
                                dialog2.show();
                            } catch (Exception e) {
                                Dialog dialog3 = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("信息框")
                                        .setMessage("注册失败")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }


                                        }).create();
                                dialog3.show();
                            }
                        }


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }


 }











