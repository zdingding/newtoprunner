package com.toprunner.ubii.toprunner.activivty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.toprunner.ubii.toprunner.R;

/**
 * Created by ly on 2016/4/27.
 */
public class DengluActivity extends Activity {
    ProgressDialog m_dlog;
    SQLiteDatabase mysql;
    Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denglu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        button = (Button) findViewById(R.id.denglu);
        mysql=this.openOrCreateDatabase("student.db", MODE_PRIVATE,null);
        try
        {
            mysql.execSQL("CREATE TABLE login_t(_id INTEGER PRIMARY KEY,username TEXT,name1 TEXT,password TEXT )");
        }
        catch(Exception e){}
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.username);
                String name_test = edit.getText().toString();
                EditText editText = (EditText) findViewById(R.id.passwordtext);
                String pass_text = editText.getText().toString();

                m_dlog = ProgressDialog.show(DengluActivity.this,
                        "请稍等", "正在为你登录", true);
                m_dlog.dismiss();
                Cursor cs = mysql.query(true, "login_t", new String[]{"username", "name1"}, "username" + "='" + name_test + "'", null, null, null, null, null);
                Cursor cursor = mysql.query(true, "login_t", new String[]{"username", "password"}, "password" + "='" + pass_text + "'", null, null, null, null, null);
                if (cs.getCount() > 0 && cursor.getCount() > 0) {
                    m_dlog.dismiss();
                    LayoutInflater fc1 = LayoutInflater.from(DengluActivity.this);
                    final View DialogView1 = fc1.inflate(R.layout.tishi, null);
                    cs.moveToFirst();
                    String str = cs.getString(1);
                    TextView textview1 = (TextView) DialogView1.findViewById(R.id.name1);
                    textview1.setText(str);
                    Intent intent = new Intent();
                    intent.setClass(DengluActivity.this, UserActivity.class);
                    startActivity(intent);
                    DengluActivity.this.finish();
                } else {
                    Dialog dialog1 = new AlertDialog.Builder(DengluActivity.this)
                            .setTitle("信息框")
                            .setMessage("登录失败")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent();
                                    intent.setClass(DengluActivity.this, DengluActivity.class);
                                    startActivity(intent);
                                    DengluActivity.this.finish();
                                }
                            }).create();
                    dialog1.show();
                }
            }
        });

    }


}
