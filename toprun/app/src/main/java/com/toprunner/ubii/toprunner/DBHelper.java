package com.toprunner.ubii.toprunner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ${赵鼎} on 2016/11/10 0010.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final  String NAME="topruner.db";
    private static final  int VERSION=1;
    public DBHelper(Context context) {

        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table person(_id integer primary key autoincrement, name varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
