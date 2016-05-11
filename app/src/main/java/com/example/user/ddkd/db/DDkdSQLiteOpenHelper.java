package com.example.user.ddkd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2016-05-11.
 */
public class DDkdSQLiteOpenHelper extends SQLiteOpenHelper{
    public DDkdSQLiteOpenHelper(Context context) {
        super(context, "DDkd.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table qorder ( _id integer primary key, _time varchar, _Order varchar);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
