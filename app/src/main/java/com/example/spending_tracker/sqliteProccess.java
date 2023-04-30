package com.example.spending_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//sqlite katmanı
public class sqliteProccess extends SQLiteOpenHelper {

    public sqliteProccess(Context c) {
        super(c, "test", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table object (place VARCHAR(45), price FLOAT, time VARCHAR(45))";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("");
    }
}
