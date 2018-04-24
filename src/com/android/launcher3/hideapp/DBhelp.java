package com.android.launcher3.hideapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelp extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    String DATABASE_CREATE = "create table hidetable(_id INTEGER PRIMARY KEY AUTOINCREMENT,mtitle TEXT,mpackagename TEXT)";
    public DBhelp(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }
    public DBhelp(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
