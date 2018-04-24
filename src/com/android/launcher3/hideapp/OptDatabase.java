package com.android.launcher3.hideapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OptDatabase {
    private DBhelp dbHelper;
    private SQLiteDatabase db;
    String table_name="hidetable";
    Cursor cursor;
    private ArrayList<String> packagenameArrayList =new ArrayList<>();
    public OptDatabase() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OptDatabase(Context context) {
        super();
        dbHelper = new DBhelp(context, "DB.db");
    }

    public ArrayList<String> queryDB()
    {
        db = dbHelper.getWritableDatabase();
        cursor=db.query(table_name, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Log.i("zhao","11111111111111111111"+cursor.getString(cursor.getColumnIndex("mpackagename")));
            packagenameArrayList.add(cursor.getString(cursor.getColumnIndex("mpackagename")));
        }
        return packagenameArrayList;
    }

    public Cursor queryDBbyID(String id,String[] arrStrings)
    {
        db = dbHelper.getReadableDatabase();
        return db.query(table_name, null, id, arrStrings, null, null, null);
    }
    

    public void addAppInfoData(String mtitle,String mpackagename) 
    {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mtitle", mtitle);
        values.put("mpackagename", mpackagename);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(table_name, null, values);
        db.close();
    }

    public void deleteDataByPackageName(String packgeName) {
        SQLiteDatabase deletedb = dbHelper.getReadableDatabase();
        deletedb.execSQL("DELETE FROM hidetable WHERE mpackagename = '" + packgeName+"'");
        deletedb.close();
    }

    public void deleteDB(Context context) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        context.deleteDatabase("DB.db");
        db.close();
    }
    public void closeDB() {
        db.close();
    }
}
