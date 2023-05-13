package com.example.spending_tracker;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.example.spending_tracker.sqlite.object;
import com.example.spending_tracker.sqlite.sqliteOperations;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class graphic_display extends Activity {

    sqliteOperations sqlOperations;
    List<object> listAlldata;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphic_display);

        sqlOperations = new sqliteOperations(this);
        sqlOperations.openConnecting();

        listAlldata = sqlOperations.showAllData("");
        String time = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }
}
