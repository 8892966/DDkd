package com.example.user.ddkd.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.ddkd.db.DDkdSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 2016-05-11.
 */
public class QOrderDao {
    private DDkdSQLiteOpenHelper dDkdSQLiteOpenHelper;
    public QOrderDao(Context context){
//        dDkdSQLiteOpenHelper=new DDkdSQLiteOpenHelper(context);
    }

    public void insert(@NonNull String id,@NonNull String time,@NonNull String order){
//        SQLiteDatabase db= dDkdSQLiteOpenHelper.getWritableDatabase();
//        if(db.isOpen()){
//            ContentValues contentValues=new ContentValues();
//            contentValues.put("_id",id);
//            contentValues.put("_time",time);
//            contentValues.put("_Order", order);
//            if(contentValues.get("_time")!=null&&contentValues.get("_Order")!=null&&contentValues.get("_id")!=null) {
//                db.insert("qorder", null, contentValues);
//            }
//            db.close();
//        }
    }

    public void delete(String time){
//        SQLiteDatabase db= dDkdSQLiteOpenHelper.getWritableDatabase();
//        if(db.isOpen()){
//            String WhereClause="_time < ?";
//            String[] strings={time};
//            db.delete("qorder", WhereClause, strings);
//            db.close();
//        }
    }

    public void deleteById(String id){
//        SQLiteDatabase db= dDkdSQLiteOpenHelper.getWritableDatabase();
//        if(db.isOpen()){
//            String WhereClause="_id = ?";
//            String[] strings={id};
//            db.delete("qorder", WhereClause, strings);
//            db.close();
//        }
    }

    public List<String> query(String time){
//        SQLiteDatabase db= dDkdSQLiteOpenHelper.getReadableDatabase();
//        if(db.isOpen()){
//            String[] columns={"_id","_time","_Order"};
//            String selection = "_time > ?";
//            String[] SelectionArgs = {time};
//            Cursor person = db.query("qorder", columns,selection, SelectionArgs, null, null, null);
//            if(person!=null&&person.getCount()>0){
//                List<String> list=new ArrayList<>();
//                while(person.moveToNext()){
//                    list.add(person.getString(2));
//                    Log.e("QOrderDao",person.getInt(0)+","+person.getString(1)+","+person.getString(2));
//                }
//                return list;
//            }
//        }
        return null;
    }

    public List<String> query(){
//        SQLiteDatabase db= dDkdSQLiteOpenHelper.getReadableDatabase();
//        if(db.isOpen()){
//            String[] columns={"_id","_time","_Order"};
//            Cursor person = db.query("qorder", columns,null, null, null, null, null);
//            if(person!=null&&person.getCount()>0){
//                List<String> list=new ArrayList<>();
//                while(person.moveToNext()){
//                    list.add(person.getString(2));
////                    Log.e("QOrderDao",person.getInt(0)+","+person.getString(1)+","+person.getString(2));
//                }
//                return list;
//            }
//        }
        return null;
    }
}
