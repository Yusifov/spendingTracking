package com.example.spending_tracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//Veri kaynağı
public class sqliteOperations {
    SQLiteDatabase db;
    sqliteProccess myDB;

    public sqliteOperations(Context c){
        myDB = new sqliteProccess(c);
    }

    public void openConnecting(){
        db = myDB.getWritableDatabase();
    }

    public void closeConnecting(){
        myDB.close();
    }

    public String insert(String place, Float price, String time) {
        ContentValues values = new ContentValues();
        values.put("place", place);
        values.put("price", price);
        values.put("time", time);
        try {
            db.insert("object", null, values);
            return "ok";
        }catch (Exception e) {
            Log.d("Error ", e.getMessage());
            return "notOk";
        }
    }

    public ArrayList<object> showAllData(String whereClause) {
        String colume[] = {"place", "price", "time"};
       // String whereClause = "column1 = ? OR column1 = ?";
        ArrayList<object> response = new ArrayList<>();
        Cursor cursor = db.query("object", colume, whereClause, null,
                null, null,     null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            object responseObject = new object(cursor.getString(0), cursor.getFloat(1),
                    cursor.getString(2));
            response.add(responseObject);
            cursor.moveToNext();
        }
        cursor.close();
        return response;
    }

    public void deleteData(object object) {
        db.delete("object",
                "place="+object.getPlace()+" and price="+object.getPrice()+" and time="+object.getTime(), null);
    }
}
