package com.example.user.ddkd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.ddkd.beam.QOrderInfo;
import com.example.user.ddkd.db.DDkdSQLiteOpenHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-05-11.
 */
public class QOrderDao {
    private DDkdSQLiteOpenHelper dDkdSQLiteOpenHelper;
    public QOrderDao(Context context){
        dDkdSQLiteOpenHelper=new DDkdSQLiteOpenHelper(context);
    }

    public void insert(String time,String order){
        SQLiteDatabase db= dDkdSQLiteOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            ContentValues contentValues=new ContentValues();
            contentValues.put("_time",time);
            contentValues.put("_Order", order);
            if(contentValues.get("_time")!=null&&contentValues.get("_Order")!=null) {
                db.insert("qorder", null, contentValues);
            }
            db.close();
        }
    }
    public void delete(String time){
        SQLiteDatabase db= dDkdSQLiteOpenHelper.getWritableDatabase();
        if(db.isOpen()){
            String WhereClause="_time < ?";
            String[] strings={time};
            db.delete("qorder", WhereClause,strings);
            db.close();
        }
    }
    public List<QOrderInfo> query(String time){
        SQLiteDatabase db= dDkdSQLiteOpenHelper.getReadableDatabase();
        List<QOrderInfo> infoList=new ArrayList<>();
        if(db.isOpen()){
            String[] columns={"_id","_time","_Order"};
            String selection = "_time > ?";
            String[] SelectionArgs = {time};
            Cursor person = db.query("qorder", columns,selection, SelectionArgs, null, null, null);
            if(person!=null&&person.getCount()>0){
                while(person.moveToNext()){
                    Gson gson=new Gson();
                    QOrderInfo qOrderInfo=gson.fromJson(person.getString(2),QOrderInfo.class);
                    infoList.add(qOrderInfo);
                }
            }
        }
        return infoList;
    }
}
